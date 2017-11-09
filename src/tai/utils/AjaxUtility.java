package tai.utils;

import java.util.List;

import tai.entity.Product;

public class AjaxUtility {

	private MySQLDataStoreUtilities mySqlUtil = MySQLDataStoreUtilities.INSTANCE;
	private StringUtilities stringUtil = StringUtilities.INSTANCE;
	
	/**
	 * Return search result in the form of a DOM object
	 * <a href="$product-link" style="background:#48baea; height:5px; padding:10px 20px 25px;">
	 * <span>$product-name</span>
	 * </a> 
	 */
	public String getSearchResult(String rootPath, String value) {
		
		final int MAX_SEARCH_RESULT = 10;
		final int MAX_NAME_LENGTH = 50;
		List<Product> listProduct = mySqlUtil.selectProductContains(value, MAX_SEARCH_RESULT);
		
		StringBuilder innerHtmlBuilder = new StringBuilder();
		if(listProduct == null) {
			return null;
		}
		
		for(Product p: listProduct) {
			String pName = p.getName();
			if(pName.length() > MAX_NAME_LENGTH) {
				pName = pName.substring(0, MAX_NAME_LENGTH) + "...";
			}
			
			innerHtmlBuilder
				.append("<a href=\"" + rootPath + "/product/" + p.getCategory() + "/" + p.getId() + "\"")
				.append(" style=\"background:#48baea; height:5px; padding:10px 20px 25px;\">")
				.append("<span>" + stringUtil.filter(pName) + "</span>")
				.append("</a>")
				.append("\n")
			;
		}
		return innerHtmlBuilder.toString();
	}
}
