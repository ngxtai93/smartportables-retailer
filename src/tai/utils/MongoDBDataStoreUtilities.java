package tai.utils;

import com.mongodb.MongoClient;
import com.mongodb.client.*;
import com.mongodb.client.model.Sorts;
import com.mongodb.Block;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bson.Document;
import tai.entity.Review;
import java.util.Map;
import java.util.LinkedHashMap;

public enum MongoDBDataStoreUtilities {
	INSTANCE;

	private MongoClient mongoClient;
	private MongoDatabase mongoDatabase;

	private MongoDBDataStoreUtilities() {
		mongoClient = new MongoClient("localhost", 27017);
		mongoDatabase = mongoClient.getDatabase("smart_portables");
	}

	public void insertReview(Review review) {
		MongoCollection<Document> reviewCollection = mongoDatabase.getCollection("review");
		Document doc = new Document();
		doc	.append("product-name", review.getProductName())
			.append("product-category", review.getProductCategory())
			.append("product-price", review.getProductPrice())
			.append("retailer-name", review.getRetailerName())
			.append("retailer-zip", review.getRetailerZip())
			.append("retailer-city", review.getRetailerCity())
			.append("retailer-state", review.getRetailerState())
			.append("product-on-sale", review.getProductOnSale())
			.append("manufacturer-name", review.getManufacturerName())
			.append("manufacturer-rebate", review.getManufacturerRebate())
			.append("user-id", review.getUsername())
			.append("age", review.getUserAge())
			.append("gender", review.getGender())
			.append("occupation", review.getOccupation())
			.append("review-rating", review.getRating())
		;
		Date reviewDate = Date.from(review.getReviewDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
		doc	.append("review-date", reviewDate)
			.append("review-text", review.getReviewText())
		;

		reviewCollection.insertOne(doc);
	}

	public Map<String, Double> selectTopProductByRating(int limit) {
		MongoCollection<Document> reviewCollection = mongoDatabase.getCollection("review");
		Map<String, Double> result = new LinkedHashMap<>(limit);
		Block<Document> toResult = new Block<Document>() {
			@Override
			public void apply(final Document document) {
				result.put(document.getString("product-name"), 
				Double.valueOf(document.getString("review-rating")));
			}
		};
		reviewCollection.find()
			.sort(Sorts.descending("review-rating"))
			.limit(limit)
			.forEach(toResult)
		;
		return result;
	}

	public void closeMongoDbConnection() {
		if(mongoClient != null) {
			mongoClient.close();
		}
	}
}