package br.org.igen.netbeans.plugin.server;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author gabrielhof
 * @since v1.0.0
 */
public enum SupportedApplicationServer implements ApplicationServer {

    WILDFLY(new WildflyApplicationServer()),
    ;
    private final ApplicationServer server;
    
    private SupportedApplicationServer(ApplicationServer server) {
        this.server = server;
    }

    @Override
    public String getName() {
        return server.getName();
    }

    @Override
    public String toString() {
        return server.getName();
    }

    public static List<SupportedApplicationServer> all() {
        return Arrays.asList(values());
    }

}
