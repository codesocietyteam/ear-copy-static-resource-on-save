package br.org.igen.netbeans.plugin.maven;

import java.io.InputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author gabrielhof
 * @since v1.0.0
 */
public class PomInfoParser {

    public static PomInfo parse(FileObject pomFileObject) {
        try (InputStream pomInputStream = pomFileObject.getInputStream()) {
            Document pomDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(pomInputStream);

            Element root = (Element) pomDocument.getFirstChild();

            PomInfo pomInfo = new PomInfo();
            pomInfo.setName(getText(root.getElementsByTagName("name")));
            pomInfo.setVersion(getText(root.getElementsByTagName("version")));
            pomInfo.setPackaging(PomPackagingType.getFor(getText(root.getElementsByTagName("packaging"))));

            return pomInfo;
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
        
        return null;
    }
    
    private static String getText(NodeList element) {
        if (element.getLength() <= 0) {
            return null;
        }

        String value = element.item(0).getTextContent();

        if (value == null) {
            return null;
        }

        return value.trim();
    }
}
