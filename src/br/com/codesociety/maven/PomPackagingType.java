package br.com.codesociety.maven;

/**
 *
 * @author gabrielhof
 * @since v1.0.0
 */
public enum PomPackagingType {

    JAR,
    WAR,
    EAR,
    OTHER;
    
    public static PomPackagingType getFor(String packaging) {
        if (packaging == null) {
            return OTHER;
        }
        
        packaging = packaging.trim();
        
        if (packaging.isEmpty()) {
            return OTHER;
        }
        
        for (PomPackagingType packagingType : values()) {
            if (packagingType.toString().equalsIgnoreCase(packaging)) {
                return packagingType;
            }
        }
        
        return OTHER;
    }
    
}
