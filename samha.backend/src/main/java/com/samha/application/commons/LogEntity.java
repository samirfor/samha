package com.samha.application.commons;

import com.samha.commons.UseCase;
import com.samha.persistence.filter.Page;
import com.samha.persistence.filter.PagedList;
import com.samha.persistence.filter.Query;
import com.samha.persistence.generics.IGenericRepository;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.order.AuditOrder;

import javax.inject.Inject;
import java.util.List;
import java.util.regex.Pattern;

public class LogEntity<ENTITY> extends UseCase<PagedList<ENTITY>> {

    private final Class<ENTITY> logTargetClass;
    private final Query entityQuery;

    public LogEntity (Class<ENTITY> logtarget, Query query){
        this.logTargetClass = logtarget;
        this.entityQuery = query;
    }

    @Inject
    private IGenericRepository genericRepository;

    @Override
    protected PagedList<ENTITY> execute() throws Exception {
        AuditOrder order = this.buildOrder();

        AuditReader reader = genericRepository.getReader();
        Page page = entityQuery.getPage();
        List<ENTITY> auditList = reader.createQuery().forRevisionsOfEntity(logTargetClass, true, true)
                .addOrder(order != null ? order : AuditEntity.revisionNumber().desc())
                .setFirstResult(page.getSkip() == null ? page.getSize() * page.getNumber() : page.getSkip())
                .setMaxResults(page.getSize())
                .getResultList();

        PagedList pagedList = new PagedList(auditList);
        pagedList.setPage(page);

        return pagedList;
    }

    private AuditOrder buildOrder() {
        if(!this.entityQuery.getOrders().isEmpty()) {
            boolean descending;
            String order = (String) this.entityQuery.getOrders().get(0);
            String[] parts = order.split(Pattern.quote(" "));
            descending = parts.length > 1 && parts[1].equalsIgnoreCase("desc");
            if (descending) {
                return AuditEntity.property(parts[0]).desc();
            } else {
                return AuditEntity.property(parts[0]).asc();
            }
        }else{
            return null;
        }
    }
}
