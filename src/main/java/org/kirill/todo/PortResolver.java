package org.kirill.todo;

/**
 * Created by kirill on 21.02.16.
 *
 * For local start - 8080 is good enough.
 * However, Heroku has it's own ideas about default port
 * and tells us port number by System.getenv("PORT")
 */
public class PortResolver {
    private static int port;
    public static int getPort() {
        if (port == 0) {
            initPort();
        }
        return port;
    }

    private static void initPort() {
        String herokuPort = System.getenv("PORT");
        if (herokuPort != null) {
            port = Integer.parseInt(herokuPort);
        } else {
            port = 8080;
        }
    }
}
