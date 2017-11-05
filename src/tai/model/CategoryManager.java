package tai.model;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import org.xml.sax.SAXException;

import tai.entity.Category;
import tai.sax.SaxCategoryHandler;

public class CategoryManager {
    private final String PRODUCT_CATALOG_FILE_PATH = "resources/data/ProductCatalog.xml";

    /**
     * Get category list from XML file
     */
    public ArrayList<Category> getAvailableCategory(ServletContext sc) {
        String productCatalogFilePath = sc.getRealPath(PRODUCT_CATALOG_FILE_PATH);
        File productCatalogFile = new File(productCatalogFilePath);

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            
            InputStream xmlInput = new FileInputStream(productCatalogFile);
            SAXParser saxParser = factory.newSAXParser();
            SaxCategoryHandler categoryHandler = new SaxCategoryHandler();
            saxParser.parse(xmlInput, categoryHandler);
            return categoryHandler.listCategory;
        }
        catch(IOException|ParserConfigurationException|SAXException e) {
            e.printStackTrace();
        }
        return null;
    }
}