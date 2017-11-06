package tai.servlet;

import java.io.*;
import java.util.Map;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.xpath.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import tai.entity.Product;
import tai.entity.Role;
import tai.entity.User;
import tai.model.ProductManager;
import tai.utils.StringUtilities;
import tai.utils.XmlUtilities;

public class ServletDeleteProduct extends HttpServlet {

    private StringUtilities stringUtil = StringUtilities.INSTANCE;
    private XmlUtilities xmlUtil = XmlUtilities.INSTANCE;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("currentUser");
        if(user.getRole() != Role.STORE_MANAGER) {
            res.sendRedirect(req.getContextPath());
        }
        else {
            // process request
            String[] listIdToDelete = req.getParameterValues("product-id");
            if(listIdToDelete == null || listIdToDelete.length == 0) {
                Map<Integer, Product> mapProduct = new ProductManager().getListProduct(req, req.getParameter("category"));
                req.setAttribute("no-product-chosen", "true");
                req.setAttribute("mapProduct", mapProduct);
                req.getRequestDispatcher("/WEB-INF/jsp/product/product_delete.jsp").forward(req, res);
            }
            else {
                // process uploaded items
                deleteProduct(req, listIdToDelete);
            
                req.getSession().setAttribute("command-executed", "product-delete");
                res.sendRedirect(req.getContextPath() + "/success");
            }
            
        }
    }

    private void deleteProduct(HttpServletRequest req, String[] listProductId) {
        String xmlFilePath = req.getServletContext().getRealPath("resources/data/ProductCatalog.xml");
        Document doc = xmlUtil.getXmlDocument(xmlFilePath);
        String category = req.getParameter("category");
        for(String id: listProductId) {
            Element elementToBeDeleted = findProductElement(doc, category, id);
            elementToBeDeleted.getParentNode().removeChild(elementToBeDeleted);
        }
        xmlUtil.writeToXml(doc, xmlFilePath);
        // write back to file
        
    }

    private Element findProductElement(Document doc, String category, String id) {
        XPath xpath =   XPathFactory.newInstance()
                        .newXPath();
        String exprStr =      "/ProductCatalog"
                            + "/category[@id=\'" + category + "\']"
                            + "/product[@id=\'" + id + "\']"
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
}