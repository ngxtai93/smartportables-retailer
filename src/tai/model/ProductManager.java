package tai.model;

import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import tai.entity.Product;
import tai.sax.SaxProductHandler;

public class ProductManager {
    
    public Map<Integer, Product> getListProduct(HttpServletRequest req, String category) {

        List<Product> listAllProduct = getListProduct(req.getServletContext());
        Map<Integer, Product> productByCategory = getProductByCategory(listAllProduct, category);
        return productByCategory;
    }

    public Map<Integer, Product> getProductAccessories(HttpServletRequest req, Product product) {
        ArrayList<Integer> listAccessoryId = product.getListAccessoryId();
        if(listAccessoryId == null) {
            return null;
        }

        List<Product> listAllProduct = getListProduct(req.getServletContext());
        Map<Integer, Product> productByCategory = getProductByCategory(listAllProduct, "accessory");

        Map<Integer, Product> mapAccessory = new LinkedHashMap<>();
        for(Integer id: listAccessoryId) {
            mapAccessory.put(id, productByCategory.get(id));
        }

        return mapAccessory;
    }
    public List<Product> getListProduct(ServletContext sc) {
        String productCatalogFilePath = sc.getRealPath("resources/data/ProductCatalog.xml");
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