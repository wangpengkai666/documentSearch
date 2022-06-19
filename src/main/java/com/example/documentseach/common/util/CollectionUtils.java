package com.example.documentseach.common.util;

import java.util.List;

/**
 * @author wangpengkai
 */
public class CollectionUtils {
    public static boolean isEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

    public static boolean isNotEmpty(List<?> list) {
        return list != null && !list.isEmpty();
    }
}
