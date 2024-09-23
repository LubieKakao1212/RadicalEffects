package com.lubiekakao1212.util;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class CollectionUtil {

    public static <V> int indexOfFirst(List<V> list, Predicate<V> predicate) {
        for (int i=0; i<list.size(); i++) {
            if(predicate.test(list.get(i))) {
                return i;
            }
        }
        return -1;
    }

}
