package br.com.codesociety.server;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author gabrielhof
 * @since v1.0.0
 */
public enum SupportedApplicationServer {

    WILDFLY(new WildflyApplicationServer()),
    ;
    
    private final ApplicationServer server;
    
    private SupportedApplicationServer(ApplicationServer server) {
        this.server = server;
    }

    public String getName() {
        return server.getName();
    }
    
    @Override
    public String toString() {
        return getName();
    }
    
    public ApplicationServer getApplicationServer() {
        return server;
    }

    public static List<SupportedApplicationServer> all() {
        return Arrays.asList(values());
    }

}
