package com.company.petrinet;

import java.util.*;

final class Util {

    private Util() {}

    static void require(boolean condition, String error) {
        if (!condition) {
            throw new IllegalArgumentException(error);
        }
    }

    static <T> List<T> findDuplicates(List<T> collection) {
        List<T> duplicates = new LinkedList<>();
        Set<T> seen = new HashSet<>();
        collection.forEach(elem -> {
            if (seen.contains(elem)) {
                duplicates.add(elem);
            } else {
                seen.add(elem);
            }
        });
        return duplicates;
    }

}
