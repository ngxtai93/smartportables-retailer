package tai;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import org.xml.sax.SAXException;

import tai.Category;
import tai.SaxCategoryHandler;

public class FilterGetListCategory implements Filter {

    @Override
    public void init(FilterConfig config) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
    FilterChain chain)
        throws ServletException, IOException {
            // PrintWriter out = response.getWriter();
            String productCatalogFilePath = request.getServletContext()
                .getRealPath("resources/data/ProductCatalog.xml");
            File productCatalogFile = new File(productCatalogFilePath);
            ArrayList<Category> listCategory = getAvailableCategory(productCatalogFile);
            // for(Category category: listCategory) {
            //     out.println(category.getId() + "\t" + category.getName() + "\t" + category.getImageName());
            // }
            // out.println();
            request.setAttribute("listCategory", listCategory);
            chain.doFilter(request, response);
    }
    @Override
    public void destroy() {

    }
    private ArrayList<Category> getAvailableCategory(File xmlFile) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            
            InputStream xmlInput = new FileInputStream(xmlFile);
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