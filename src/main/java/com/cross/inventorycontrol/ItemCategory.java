package com.cross.inventorycontrol;

import java.util.HashMap;
import java.util.Map;

public class ItemCategory {
    public static Map<Integer,String> item = new HashMap<>();
    static {
        item.put(0,"食べ物");
        item.put(1,"調味料");
        item.put(2,"お菓子");
        item.put(3,"生活用品");
        item.put(4,"防災グッズ");
        item.put(5,"その他");
    }
}
