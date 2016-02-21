package org.kirill.todo;

/**
 * Created by kirill on 21.02.16.
 */
public class PortResolver {
    static final int port;
    static {
        if (System.getenv("USER").equals("kirill")) {
            port = 8080;
        } else {
            port = Integer.parseInt(System.getenv("PORT"));
        }
    }
}
