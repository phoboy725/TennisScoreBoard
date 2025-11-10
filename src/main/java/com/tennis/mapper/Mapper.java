package com.tennis.mapper;

public interface Mapper<F, T> {
    T mapFrom(F object);
}
