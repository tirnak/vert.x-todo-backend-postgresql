package org.kirill.todo;

/**
 * Created by kirill on 22.03.2016.
 *
 * Function of 4 arguments. Why not? =)
 */
@FunctionalInterface
public interface Function4args<T,R> {
    R apply(T one, T two, T three, T four);
}
