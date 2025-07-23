package com.samha.controller.common;

import com.samha.application.commons.*;
import com.samha.commons.UseCaseFacade;
import com.samha.domain.BaseLogEntity;
import com.samha.persistence.filter.PagedList;
import com.samha.persistence.filter.Query;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

@RestController
public abstract class BaseController<ENTITY extends BaseLogEntity, LOG_TARGET extends BaseLogEntity, KEY extends Serializable> {

    private final Class<ENTITY> entityClass;
    private final Class<LOG_TARGET> logTargetClass;
    protected final UseCaseFacade facade;

    public BaseController(Class<ENTITY> entityClass, Class<LOG_TARGET> logTargetClass, UseCaseFacade facade){
        this.facade = facade;
        Assert.notNull(entityClass, "EntityClass can not be null");
        Assert.notNull(logTargetClass, "LogTargetClass can not be null");
        this.entityClass = entityClass;
        this.logTargetClass = logTargetClass;
    }


    @PostMapping("query")
    public PagedList query(@RequestBody Query query){
        return this.buildQueryEntities(query);
    }

    @GetMapping("all")
    public List<ENTITY> getAll(){
        return this.facade.execute(new GetAll<>(this.entityClass));
    }

    @GetMapping("{id}")
    public ENTITY get(@PathVariable KEY id){
        return this.facade.execute(new GetEntity<>(this.entityClass, id));
    }

    @PostMapping("insert")
    public ENTITY insert(@RequestBody ENTITY body){
        return this.facade.execute(new InsertEntity<>(body));
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable KEY id){
        this.facade.execute(new DeleteEntity<>(entityClass, id));
    }

    @PatchMapping("{id}")
    public ENTITY update(@PathVariable KEY id, @RequestBody ENTITY entity){
        return this.facade.execute(new UpdateEntity<>(entity));
    }

    @PostMapping("log")
    public PagedList<LOG_TARGET> logEntities(@RequestBody Query query){
        return this.facade.execute(new QueryEntities<>(query, logTargetClass));
    }

    public PagedList buildQueryEntities(Query query){
        return this.facade.execute(new QueryEntities<>(query, this.entityClass));
    }
}
