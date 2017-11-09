package tai.servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ServletSearch extends HttpServlet {
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
		String queryString = req.getQueryString();
		String[] querySplit = queryString.split("&");
		
		System.out.println(queryString);
    }
}
