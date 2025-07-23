package com.samha.application.commons;

import com.samha.commons.UseCase;
import com.samha.domain.BaseLogEntity;
import com.samha.persistence.generics.IGenericRepository;
import com.samha.persistence.filter.PagedList;
import com.samha.persistence.filter.Query;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.inject.Inject;
import javax.persistence.Tuple;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
public class QueryEntities<ENTITY extends BaseLogEntity> extends UseCase<PagedList> {

    private Class<ENTITY> entityClass;
    protected Query entityQuery;
    public boolean distinct = false;

    @Inject
    protected IGenericRepository genericRepository;

    public QueryEntities(Query query, Class<ENTITY> entityClass){
        this.entityClass = entityClass;
        this.entityQuery = query;
    }

    public QueryEntities(Query query, Class<ENTITY> entityClass, boolean distinct) {
        this.entityClass = entityClass;
        this.entityQuery = query;
        this.distinct = distinct;
    }

    @Override
    protected PagedList execute() throws Exception {
        PagedList pagedList = genericRepository.find(entityClass, Tuple.class, entityQuery.getPage(),
                q -> q.entityQuery(entityQuery).distinct(this.distinct)
        );

        List<Map<String, Object>> listMap = (List<Map<String, Object>>) pagedList.getList().stream().map(elem -> UseCase.convertTupleToMap((Tuple) elem)).collect(Collectors.toList());

        pagedList.setList(null);
        pagedList.setListMap(listMap);

        return pagedList;
    }
}
