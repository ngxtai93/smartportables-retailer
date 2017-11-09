package tai.servlet;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import tai.utils.AjaxUtility;
import tai.utils.StringUtilities;

public class ServletSearch extends HttpServlet {
	
	StringUtilities stringUtil = StringUtilities.INSTANCE;
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
		String queryString = stringUtil.unfilter(req.getQueryString());
		String[] querySplit = queryString.split("&");
		
		if(!validateQueryString(querySplit)) {
			res.sendRedirect("/");
			return;
		};
		
		String type = querySplit[0].split("=")[1];
		String value = querySplit[1].split("=")[1];
		
		switch(type.toString()) {
			case "autocomplete":
				processAutoComplete(req, res, value);
				break;
		}
    }

	private void processAutoComplete(HttpServletRequest req, HttpServletResponse res, String value) 
		throws ServletException, IOException {
		AjaxUtility ajaxUtil = new AjaxUtility();
		
		String innerHTMLResult = ajaxUtil.getSearchResult(req.getContextPath(), value);
		if(innerHTMLResult == null) {
			res.getWriter().print("");
			return;
		}
		else {
			res.getWriter().print(innerHTMLResult);
		}
	}

	/**
	 * Validate query string of form [type=?&value=?]. 
	 */
	private boolean validateQueryString(String[] queryStringSplit) {
		if(queryStringSplit.length != 2) {
			return false;
		}
		String[] typeSplit = queryStringSplit[0].split("=");
		String[] valueSplit = queryStringSplit[1].split("=");
		if(typeSplit[0].equals("type") && valueSplit[0].equals("value")) {
			return false;
		}
		return true;
	}
}
