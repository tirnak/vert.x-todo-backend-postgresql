package org.kirill.todo;

/**
 * Created by kirill on 21.02.16.
 */
public class PortResolver {
    private static int port;
    public static int getPort() {
        if (port == 0) {
            initPort();
        }
        return port;
    }

    /**
     * if app can't bind to any port - there is no use in it
     * hence - log and exit
     */
    private static void initPort() {
        if ("kirill".equals(System.getenv("USER"))) {
            port = 8080;
        } else if (System.getenv("PORT") != null) {
            port = Integer.parseInt(System.getenv("PORT"));
        } else {
            System.out.println("port can't be defined");
            System.exit(0);
        }
    }
}
