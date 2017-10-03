package tai.utils;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import java.io.*;

public enum XmlUtilities {
    INSTANCE;

    private XmlUtilities() {

    }

    public Document getXmlDocument(String filePath) {
        Document doc = null;
        try {
            doc =   DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(filePath);
        }
        catch(ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    public void writeToXml(Document doc, String xmlFilePath) {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer =   tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            OutputStream out = new FileOutputStream(new File(xmlFilePath));
            Writer writer = new OutputStreamWriter(out, "UTF-8");
            Result output = new StreamResult(writer);
            Source input = new DOMSource(doc);

            transformer.transform(input, output);

            writer.close();
        }
        catch(TransformerException | IOException e) {
            e.printStackTrace();
        }
    }
}