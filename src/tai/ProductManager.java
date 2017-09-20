package tai;

import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class ProductManager {
    
    public Map<Integer, Product> getListProduct(HttpServletRequest req, String category) {

        List<Product> listAllProduct = getListProduct(req);
        Map<Integer, Product> productByCategory = getProductByCategory(listAllProduct, category);
        return productByCategory;
    }

    private List<Product> getListProduct(HttpServletRequest req) {
        String productCatalogFilePath = req.getServletContext()
        .getRealPath("resources/data/ProductCatalog.xml");
        File productCatalogFile = new File(productCatalogFilePath);

        return buildListProduct(productCatalogFile);
    }

    private Map<Integer, Product> getProductByCategory(List<Product> listProduct, String category) {
        Map<Integer, Product> result = new LinkedHashMap<>();
        for(Product p: listProduct) {
            if(p.getCategory().equals(category)) {
                result.put(Integer.valueOf(p.getId()), p);
            }
        }

        return result;
    }
    private List<Product> buildListProduct(File xmlFile) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            InputStream xmlInput = new FileInputStream(xmlFile);
            SAXParser saxParser = factory.newSAXParser();
            SaxProductHandler productHandler = new SaxProductHandler();
            saxParser.parse(xmlInput, productHandler);
            return productHandler.listProduct;
        }
        catch(IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return null;
    }
}