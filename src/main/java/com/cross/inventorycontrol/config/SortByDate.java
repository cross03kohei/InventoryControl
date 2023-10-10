package com.cross.inventorycontrol.config;

import com.cross.inventorycontrol.domain.model.History;

import java.util.Comparator;

public class SortByDate implements Comparator<History> {
    @Override
    public int compare(History o1, History o2) {
        return o2.getDate().compareTo(o1.getDate());
    }
}
