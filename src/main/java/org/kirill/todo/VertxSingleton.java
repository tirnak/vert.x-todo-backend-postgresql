package org.kirill.todo;

import io.vertx.core.Vertx;

/**
 * Created by kirill on 01.04.16.
 *
 * Wrapper for the only Vertx.vertx()
 */
public class VertxSingleton {
    public static final Vertx vertx = Vertx.vertx();
}
