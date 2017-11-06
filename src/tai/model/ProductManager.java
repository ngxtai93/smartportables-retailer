package tai.model;

import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import tai.entity.Product;
import tai.sax.SaxProductHandler;
import tai.utils.MySQLDataStoreUtilities;
import tai.utils.XmlUtilities;

public class ProductManager {
    
    private XmlUtilities xmlUtil = XmlUtilities.INSTANCE;
    private MySQLDataStoreUtilities mySqlUtil = MySQLDataStoreUtilities.INSTANCE;

    /**
     * Populate mysql table from xml data
     */
    public void processLoadToMySQL(ServletContext sc) {
        List<Product> listAllProduct = getXmlListProduct(sc);

        // truncate old data
        mySqlUtil.truncateTable("product");
        mySqlUtil.truncateTable("product_accessories");
        
        mySqlUtil.resetAutoIncrement("product");
        mySqlUtil.resetAutoIncrement("product_accessories");

        mySqlUtil.initListProduct(listAllProduct);
    }

    /**
     * Add product to MySQL 
     */
	public void addProduct(Product product) {
		Integer productId = Integer.valueOf(mySqlUtil.getProductCountCategory(product.getCategory()) + 1);
		product.setId(productId);
		
		mySqlUtil.insertProduct(product);
	}
	
	/*
	 * Reflect update change to MySQL
	 */
	public void updateProduct(Product updatedProduct) {
		// TODO Auto-generated method stub
		int seqNo = mySqlUtil.selectProductSeqNo(updatedProduct.getCategory(), updatedProduct.getId().intValue());
		Product dbProduct = mySqlUtil.selectProduct(seqNo);
		
		// copy data to updatedProduct
		if(updatedProduct.getDiscount() == null) {
			updatedProduct.setDiscount(dbProduct.getDiscount());
		}
		if(updatedProduct.getRebate() == null) {
			updatedProduct.setRebate(dbProduct.getRebate());
		}
		if(updatedProduct.getName() == null) {
			updatedProduct.setName(dbProduct.getName());
		}
		if(updatedProduct.getPrice() == null) {
			updatedProduct.setPrice(dbProduct.getPrice());
		}
		if(updatedProduct.getImage() == null) {
			updatedProduct.setImage(dbProduct.getImage());
		}
		if(updatedProduct.getAmount() == null) {
			updatedProduct.setAmount(dbProduct.getAmount());
		}
		
		mySqlUtil.updateProduct(seqNo, updatedProduct);
	}

	/**
	 * Get list of product by <product ID, product> 
	 */
	public Map<Integer, Product> getListProduct(String category) {
		Map<Integer, Product> listProduct = mySqlUtil.selectProduct(category);
		return listProduct;
	}
	
    public Map<Integer, Product> getListProductFromXml(HttpServletRequest req, String category) {

        List<Product> listAllProduct = getListProduct(req.getServletContext());
        Map<Integer, Product> productByCategory = getProductByCategory(listAllProduct, category);
        return productByCategory;
    }

    public Map<Integer, Product> getProductAccessories(HttpServletRequest req, Product product) {
        List<Integer> listAccessoryId = product.getListAccessoryId();
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
    public List<Product> getXmlListProduct(ServletContext sc) {
        String productCatalogFilePath = sc.getRealPath("resources/data/ProductCatalog.xml");
        File productCatalogFile = new File(productCatalogFilePath);

        return buildListProduct(productCatalogFile);
    }

    public List<Product> getListProduct(ServletContext sc) {
        String productCatalogFilePath = sc.getRealPath("resources/data/ProductCatalog.xml");
        File productCatalogFile = new File(productCatalogFilePath);

        return buildListProduct(productCatalogFile);
    }

    public void updateProductToXml(HttpServletRequest req, Product product) {
        String xmlFilePath = req.getServletContext().getRealPath("resources/data/ProductCatalog.xml");
        Document doc = xmlUtil.getXmlDocument(xmlFilePath);
        Element elementToBeUpdated = findProductElement(doc, product);
        updateElement(elementToBeUpdated, product);

        xmlUtil.writeToXml(doc, xmlFilePath);
    }

    private Element findProductElement(Document doc, Product product) {
        XPath xpath =   XPathFactory.newInstance()
                        .newXPath();
        String exprStr =      "/ProductCatalog"
                            + "/category[@id=\'" + product.getCategory() + "\']"
                            + "/product[@id=\'" + product.getId() + "\']"
        ;
        NodeList nl = null;
        try {
            XPathExpression expr = xpath.compile(exprStr);
            nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        }
        catch(XPathExpressionException e) {
            e.printStackTrace();
        }
        
        return (Element) nl.item(0);
    }

    private void updateElement(Element origin, Product product) {
        // update element
        if(product.getImage() != null) {
            Element imageElement = (Element) origin.getElementsByTagName("image").item(0);
            imageElement.setTextContent(product.getImage());
        }
        if(product.getName() != null) {
            Element nameElement = (Element) origin.getElementsByTagName("name").item(0);
            nameElement.setTextContent(product.getName());
        }
        if(product.getPrice() != null) {
            Element priceElement = (Element) origin.getElementsByTagName("price").item(0);
            priceElement.setTextContent(String.valueOf(product.getPrice()));
        }
        if(product.getDiscount() != null) {
            Element discountElement = (Element) origin.getElementsByTagName("discount").item(0);
            discountElement.setTextContent(String.valueOf(product.getDiscount()));
        }
        if(product.getRebate() != null) {
            Element rebateElement = (Element) origin.getElementsByTagName("rebate").item(0);
            rebateElement.setTextContent(String.valueOf(product.getRebate()));
        }
        if(product.getAmount() != null) {
            Element amountElement = (Element) origin.getElementsByTagName("amount").item(0);
            amountElement.setTextContent(String.valueOf(product.getAmount()));
        }
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