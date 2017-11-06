package tai.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.*;

import tai.entity.Category;
import tai.model.CategoryManager;

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