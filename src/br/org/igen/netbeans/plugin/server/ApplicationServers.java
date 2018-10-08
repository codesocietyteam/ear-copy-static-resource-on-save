package br.org.igen.netbeans.plugin.server;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author gabrielhof
 * @since v1.0.0
 */
public class ApplicationServers {

    private static final ApplicationServer SUPPORTED_APPLICATION_SERVERS[] = {
        new WildflyApplicationServer()
    };

    public static List<ApplicationServer> getSupported() {
        return Arrays.asList(SUPPORTED_APPLICATION_SERVERS);
    }
    
    
    
}
