package org.burbokop.exp_craft.utils;

import com.google.common.base.Predicate;

public interface PredicateProvider<T> {
    Predicate<T> predicate();
}
