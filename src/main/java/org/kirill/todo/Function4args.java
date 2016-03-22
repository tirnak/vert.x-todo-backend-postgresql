package org.kirill.todo;

/**
 * Created by kise0116 on 22.03.2016.
 *
 * Function of 4 arguments. Why not? =)
 */
@FunctionalInterface
public interface Function4args<T,R> {
    R apply(T one, T two, T three, T four);
}
