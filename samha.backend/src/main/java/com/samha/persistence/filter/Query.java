package com.samha.persistence.filter;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Query<T> implements Serializable {
    private List<Object> projections = new ArrayList<>();
    private Map<String, Object> predicates = new HashMap<>();
    private List<Object> orders = new ArrayList<>();;
    private List<Object> groups = new ArrayList<>();
    Page page;
}
