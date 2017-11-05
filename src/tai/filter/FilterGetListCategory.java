package tai.filter;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.List;
import org.xml.sax.SAXException;

import tai.entity.Category;
import tai.model.CategoryManager;
import tai.sax.SaxCategoryHandler;

public class FilterGetListCategory implements Filter {

    private CategoryManager cm = new CategoryManager();

    @Override
    public void init(FilterConfig config) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
    FilterChain chain)
        throws ServletException, IOException {
            List<Category> listCategory = cm.getAvailableCategory();
            request.setAttribute("listCategory", listCategory);
            chain.doFilter(request, response);
    }
    @Override
    public void destroy() {

    }
}