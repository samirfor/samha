package com.samha.persistence.generics;

import com.samha.domain.BaseLogEntity;
import com.samha.domain.log.AuditCompositeKey;
import com.samha.domain.log.RevInfo;
import com.samha.domain.log.RevInfo_;
import com.samha.persistence.filter.Page;
import com.samha.persistence.filter.PagedList;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GenericRepository implements IGenericRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericRepository.class);

    @PersistenceContext
    private EntityManager entityManager;

    public GenericRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public <ENTITY extends BaseLogEntity> List<ENTITY> findAll(Class<ENTITY> entityClass){
        String queryBase = "from " + entityClass.getName();
        TypedQuery<ENTITY> query = entityManager.createQuery(queryBase, entityClass);
        return query.getResultList();
    }

    @Override
    public <ENTITY extends BaseLogEntity, KEY extends Serializable> ENTITY get(Class<ENTITY> entityClass, KEY id) {
        return entityManager.find(entityClass, id);
    }

    @Override
    @Transactional
    public <ENTITY extends BaseLogEntity> ENTITY save(ENTITY entityClass) {
        entityManager.persist(entityClass);
        return entityClass;
    }

    @Override
    @Transactional
    public <ENTITY extends BaseLogEntity> void delete(ENTITY entityClass) {
        this.persistDeleteLog(entityClass);
        entityManager.remove(entityClass);
    }

    @Override
    @Transactional
    public <ENTITY extends BaseLogEntity> ENTITY update(ENTITY entity) {
        return entityManager.merge(entity);
    }

    @Override
    public <ENTITY extends BaseLogEntity> List<ENTITY> find(Class<ENTITY> entityClass, IQueryBuilder<ENTITY, ENTITY> queryBuilder) {
        return find(entityClass, entityClass, queryBuilder);
    }

    @Override
    public <ENTITY extends BaseLogEntity, TARGET> List<TARGET> find(Class<ENTITY> entityClass, Class<TARGET> targetClass, IQueryBuilder<ENTITY, TARGET> queryBuilder) {
        CriteriaQuery<TARGET> query = this.createCriteriaQuery(entityClass, targetClass, queryBuilder);
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public <ENTITY extends BaseLogEntity, TARGET> PagedList<TARGET> find(Class<ENTITY> entityClass, Class<TARGET> targetClass, Page page, IQueryBuilder<ENTITY, TARGET> queryBuilder) {
        if (page == null)
            return new PagedList<>(this.find(entityClass, targetClass, queryBuilder));

        // Execute Base Criteria Query
        CriteriaQuery<TARGET> criteriaQuery = this.createCriteriaQuery(entityClass, targetClass, queryBuilder);

        page.setTotalItems(PersistenceHelper.count(this.entityManager, criteriaQuery).intValue());

        if (page.getTotalItems() == 0) return new PagedList<>(new ArrayList<TARGET>(), page);

        List<TARGET> list = entityManager.createQuery(criteriaQuery)
                .setFirstResult(page.getSkip() == null ? page.getSize() * page.getNumber() : page.getSkip())
                .setMaxResults(page.getSize())
                .getResultList();

        return new PagedList<>(list, page);
    }

    @Override
    public <ENTITY extends BaseLogEntity> ENTITY findSingle(Class<ENTITY> entityClass, IQueryBuilder<ENTITY, ENTITY> queryBuilder) {
        return findsingle(entityClass, entityClass, queryBuilder);
    }

    @Override
    public <ENTITY extends BaseLogEntity, TARGET> TARGET findsingle(Class<ENTITY> entityClass, Class<TARGET> targetClass, IQueryBuilder<ENTITY, TARGET> queryBuilder) {
        CriteriaQuery<TARGET> query = this.createCriteriaQuery(entityClass, targetClass, queryBuilder);
        List<TARGET> resultList = entityManager.createQuery(query).setMaxResults(1).getResultList();
        if(resultList.isEmpty()) return null;
        return resultList.get(0);
    }

    @Override
    public void flush() {
        entityManager.flush();
    }

    @Override
    public AuditReader getReader() {
        return AuditReaderFactory.get(this.entityManager);
    }

    private <ENTITY extends BaseLogEntity, TARGET> CriteriaQuery<TARGET> createCriteriaQuery(Class<ENTITY> entityClass, Class<TARGET> targetClass, IQueryBuilder queryBuilder){
        return queryBuilder.build(new QueryHelper<>(entityManager, entityClass, targetClass)).getCriteriaQuery();
    }


    //Necessário para o tratamento específico de exclusão de registros
    private <ENTITY extends BaseLogEntity> void persistDeleteLog(ENTITY entityClass) {
        try {
            //Recupero a classe de log respectiva da classe informada
            Object logInstance = entityClass.getLogEntity().getDeclaredConstructor().newInstance();
            //Pego os campos da classe
            Field[] fields = entityClass.getClass().getDeclaredFields();
            for (var field : fields) {
                //necessário para tornar o campo acessível (private para public)
                field.setAccessible(true);
                Object value = field.get(entityClass);
                try {
                    //pego o campo pelo nome
                    Field logField = logInstance.getClass().getDeclaredField(field.getName());
                    logField.setAccessible(true);
                    logField.set(logInstance, value);
                    //como a classe de log não possui campo id, irá jogar uma exceção, basta ignorar.
                }catch (NoSuchFieldException e) {}
            }
            Field revTypeField = logInstance.getClass().getDeclaredField("revtype");
            Field pkField = logInstance.getClass().getDeclaredField("pk");
            Field entityIdField = entityClass.getClass().getDeclaredField("id");
            entityIdField.setAccessible(true);
            revTypeField.setAccessible(true);
            pkField.setAccessible(true);
            AuditCompositeKey pk = new AuditCompositeKey();
            Long entityId = (Long) entityIdField.get(entityClass);
            pk.setId(entityId);
            //HibernateEnvers utiliza a tabela RevInfo para gravas os últimos revs, sem isso, não é possível persistir na tabela de log.
            RevInfo lastRev = createRev();
            pk.setRev(lastRev.getRev());
            pkField.set(logInstance, pk);
            revTypeField.set(logInstance, 2);
            entityManager.persist(logInstance);
        } catch (Exception e) {
            //Não interromper a execução do fluxo se houver erro ao criar o log.
            LOGGER.error(e.getMessage());
        }
    }

    private <ENTITY extends BaseLogEntity> Long getCreatedDate(ENTITY entityClass) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(entityClass.getLogEntity());
        Root root = query.from(entityClass.getLogEntity());

        query.select(builder.greatest(root.get("createdDate")));
        return (Long) entityManager.createQuery(query).getSingleResult();
    }

    private <ENTITY extends BaseLogEntity> String getCreatedBy(ENTITY entityClass) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(entityClass.getLogEntity());
        Root root = query.from(entityClass.getLogEntity());

        query.select(builder.greatest(root.get("createdBy")));
        return (String) entityManager.createQuery(query).getSingleResult();
    }

    private RevInfo createRev() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(RevInfo.class);
        Root<RevInfo> revInfoRoot = query.from(RevInfo.class);
        query.select(builder.greatest(revInfoRoot.get(RevInfo_.rev)));
        Long lastRev = (Long) entityManager.createQuery(query).getSingleResult();

        RevInfo newRev = new RevInfo();
        newRev.setRev(lastRev + 1);
        newRev.setRevtstmp(LocalDateTime.now().atZone(ZoneOffset.UTC).toInstant().toEpochMilli());
        entityManager.persist(newRev);
        return newRev;
    }
}
