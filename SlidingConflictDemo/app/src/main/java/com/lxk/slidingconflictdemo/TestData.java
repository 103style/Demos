package com.lxk.slidingconflictdemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author https://github.com/103style
 * @date 2019/12/11 22:02
 */
public class TestData {

    public static final List<ItemBean> ITEMS = new ArrayList<>();

    public static final Map<String, ItemBean> ITEM_MAP = new HashMap<>();

    private static final int COUNT = 25;

    static {
        for (int i = 1; i <= COUNT; i++) {
            addItem(createItem(i));
        }
    }

    private static void addItem(ItemBean item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static ItemBean createItem(int position) {
        return new ItemBean(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    public static class ItemBean {
        public String id;
        public String content;
        public String details;

        public ItemBean(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }
    }
}
