package com.example.bettertrialbook.dal;

public interface Callback<T> {
    void execute(T t);
}
