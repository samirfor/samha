package com.samha.persistence.filter;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Page {
    private int size = 10;
    private int number = 0;
    private int totalItems;
    private Integer skip = null;

    public Page(int size) {
        this.size = size;
    }

    public Page(int size, int number) {
        this.size = size;
        this.number = number;
    }
}
