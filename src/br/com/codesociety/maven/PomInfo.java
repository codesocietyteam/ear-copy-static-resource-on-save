package br.com.codesociety.maven;

/**
 *
 * @author gabrielhof
 * @since v1.0.0
 */
public class PomInfo {

    private String name;
    private String version;
    private PomPackagingType packaging = PomPackagingType.OTHER;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public PomPackagingType getPackaging() {
        return packaging;
    }

    public void setPackaging(PomPackagingType packaging) {
        this.packaging = packaging;
    }
    
    public boolean isPackaging(PomPackagingType packaging) {
        if (packaging == null) {
            return false;
        }
        
        return this.packaging.equals(packaging);
    }
}
