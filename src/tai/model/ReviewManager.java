package tai.model;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import tai.entity.Review;

public class ReviewManager {
    public Review buildReview(HttpServletRequest req) {
        Review review = new Review();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/uuuu");

        review.setProductName		(req.getParameter("product-name"));
		review.setProductCategory	(req.getParameter("product-category"));

		String productPrice = req.getParameter("product-price");
		if(productPrice != null && productPrice.length() > 0) {
			review.setProductPrice(Double.valueOf(productPrice));
		}

		review.setRetailerName		(req.getParameter("retailer-name"));
		String retailerZip = req.getParameter("retailer-zip");
		if(retailerZip != null && retailerZip.length() > 0) {
			review.setRetailerZip(Integer.valueOf(retailerZip));
		}
        review.setRetailerCity		(req.getParameter("retailer-city"));
		review.setRetailerState		(req.getParameter("retailer-state"));
		review.setProductOnSale			
			(req.getParameter("product-on-sale") != null ? Boolean.TRUE : Boolean.FALSE);
        review.setManufacturerName	(req.getParameter("manufacturer-name"));
		review.setManufacturerRebate	
			(req.getParameter("manufacturer-rebate") != null ? Boolean.TRUE : Boolean.FALSE);
		review.setUsername			(req.getParameter("user-id"));
		String age = req.getParameter("user-age");
		if(age != null && age.length() > 0) {
			review.setUserAge(Integer.valueOf(age));
		}
        review.setGender			(req.getParameter("gender"));
		review.setOccupation		(req.getParameter("occupation"));
		String rating = req.getParameter("review-rating");
		if(rating != null && rating.length() > 0) {
			review.setRating(Double.valueOf(rating));
		}
        review.setReviewDate		(req.getParameter("review-date") == null ? null : LocalDate.parse(req.getParameter("review-date"), dtf));
		review.setReviewText		(req.getParameter("review-text"));
		
        return review;
    }
}