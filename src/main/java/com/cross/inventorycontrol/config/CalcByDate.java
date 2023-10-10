package com.cross.inventorycontrol.config;

import com.cross.inventorycontrol.domain.model.History;
import org.apache.logging.log4j.util.PropertySource;

import java.util.Comparator;

public class CalcByDate implements Comparator<History> {
    @Override
    public int compare(History o1, History o2) {
        return o1.getDate().compareTo(o2.getDate());
    }
}
