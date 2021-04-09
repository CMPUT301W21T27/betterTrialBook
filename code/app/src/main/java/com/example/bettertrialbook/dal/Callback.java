package com.example.bettertrialbook.dal;

/**
 * This Callback is needed due to the asynchronous nature of firebase queries. It
 * ensures that the result from firestore is properly returned to the correct function.
 */
public interface Callback<T> {
    void execute(T t);
}
