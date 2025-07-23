package com.samha.persistence.filter;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class PagedList<T> implements Serializable {
    Page page = null;
    List<T> list;
    List<Map<String, Object>> listMap;

    public PagedList(List<T> list){
        this.list = list;
    }

    public PagedList(){}

    public PagedList(List<T> list, Page page){
        this.list = list;
        this.page = page;
    }
}
