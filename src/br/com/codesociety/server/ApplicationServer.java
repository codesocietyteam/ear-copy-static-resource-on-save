package br.com.codesociety.server;

/**
 *
 * @author gabrielhof
 * @since v1.0.0
 */
public interface ApplicationServer {

    public String getName();
    
    public void createDirectory(FileContext context);
    
    public void createFile(FileContext context);
    
    public void updateFile(FileContext context);
    
    public void deleteFile(FileContext context);
    
}
