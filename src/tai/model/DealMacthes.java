package tai.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletContext;

import tai.entity.Deal;
import tai.entity.DealType;
import tai.entity.Product;
import tai.utils.MySQLDataStoreUtilities;

public class DealMacthes {
	private final String DEAL_FILE_PATH = "resources/data/DealMatches.txt";
	private MySQLDataStoreUtilities mySqlUtil = MySQLDataStoreUtilities.INSTANCE;

	/**
	 * Parse deal matches, apply deals to current product 
	 */
	public void processDealMatch(ServletContext sc) {
		String dealFullFilePath = sc.getRealPath(DEAL_FILE_PATH);
		File dealFile = new File(dealFullFilePath);
		List<Deal> listDeal = new ArrayList<>();
		
		try(BufferedReader br = new BufferedReader
				(new InputStreamReader
						(new FileInputStream(dealFile), "UTF-8"))
		) {
			String line;
			while((line = br.readLine()) != null) {
				Deal deal = parseDeal(line);
				if(deal != null) {
					listDeal.add(deal);
				}
				
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		if(listDeal.size() == 0) {
			return;
		}
		
		Map<Deal, Product> mapDealListProduct = applyDeal(listDeal);
		if(mapDealListProduct == null || mapDealListProduct.size() == 0) {
			return;
		}
		
		// choose 2 random product
		if(mapDealListProduct.size() > 2) {
			int firstIndex = -1;
			int secondIndex = -1;
			
			listDeal.clear();
			for(Map.Entry<Deal, Product> entry: mapDealListProduct.entrySet()) {
				listDeal.add(entry.getKey());
			}
			
			Random rng = new Random();
			firstIndex = rng.nextInt(listDeal.size());
			secondIndex = rng.nextInt(listDeal.size());
			while(secondIndex == firstIndex) {
				secondIndex = rng.nextInt(listDeal.size());
			}
			
			Map<Deal, Product> mapTwoDealProduct = new HashMap<>();
			Deal firstDeal = listDeal.get(firstIndex);
			Deal secondDeal = listDeal.get(secondIndex);
			mapTwoDealProduct.put(firstDeal, mapDealListProduct.get(firstDeal));
			mapTwoDealProduct.put(secondDeal, mapDealListProduct.get(secondDeal));
			sc.setAttribute("map-deal", mapTwoDealProduct);
		}
		else if(mapDealListProduct.size() > 0){
			sc.setAttribute("map-deal", mapDealListProduct);
		}
	}

	/**
	 * Apply found deal into product, remove all existing discount.
	 * Return map of discounted product 
	 */
	private Map<Deal, Product> applyDeal(List<Deal> listDeal) {
		Map<Deal, Product> result = null;
		for(Deal deal: listDeal) {
			Product p = mySqlUtil.selectProductByName(deal.getProductName());
			if(p != null) {
				if(result == null) {
					result = new HashMap<>();
				}
				// apply deal
				p.setDiscount(0.0);
				switch(deal.getDealType()) {
					case PERCENTAGE:
					{
						double price = p.getPrice().doubleValue();
						double discount = price * deal.getAmount().doubleValue() / 100;
						p.setDiscount(Double.valueOf(discount));
						break;
					}
					case REDUCTION:
					{
						p.setDiscount(deal.getAmount());
						break;
					}
				}
				mySqlUtil.updateProduct(p);
				result.put(deal, p);
			}
		}
		return result;
	}

	/**
	 * Parse DealMatches.txt 
	 */
	private Deal parseDeal(String line) {
		Double amount = null;
		DealType dealType = null;
		String htmlLink = null;
		final String END_TOKEN = "#Deal";
		final String START_TOKEN = "Save";
		final String START_TOKEN_2 = "Get";
		
		String[] lineSplit = line.split(" ");
		
		// Must start with start_token and end with end_tokens
		boolean format = 
				(lineSplit[0].equals(START_TOKEN) || lineSplit[0].equals(START_TOKEN_2))
				&& (lineSplit[lineSplit.length - 1].equals(END_TOKEN) || lineSplit[lineSplit.length - 2].equals(END_TOKEN))
				&& lineSplit.length >= 5
		;
		if(format != true) {
			return null;
		}
		
		// must be a value with $ or %
		String value = lineSplit[1];
		if(value.substring(0,1).equals("$")) {	// $200			
			amount = Double.valueOf(value.substring(1, value.length()));
			dealType = DealType.REDUCTION;
		}
		else if(value.substring(value.length() - 1, value.length()).equals("%")) {
			// xx%
			amount = Double.valueOf(value.substring(0, value.length() - 1));
			dealType = DealType.PERCENTAGE;
		}
		int productIndex = -1;
		
		if(lineSplit[2].equals("on")) {
			if(lineSplit[3].equals("the")
				|| lineSplit[3].equals("a")
				|| lineSplit[3].contains("select")
			) 
			{
				productIndex = 4;
			}
			else {
				productIndex = 3;
			}
		}
		else {
			return null;
		}
		
		StringBuilder productNameBuilder = new StringBuilder();
		for(int i = productIndex; i < lineSplit.length - 1 && !lineSplit[i].equals(END_TOKEN); i++) {
			productNameBuilder.append(lineSplit[i]);
			productNameBuilder.append(" ");
		}
		if(lineSplit[lineSplit.length - 1].contains("http")) {
			htmlLink = lineSplit[lineSplit.length - 1];
		}
		
		Deal deal = new Deal();
		deal.setAmount(amount);
		deal.setDealType(dealType);
		deal.setProductName(productNameBuilder.toString());
		deal.setTweet(line);
		deal.setLink(htmlLink);
		
		return deal;
	}
}
