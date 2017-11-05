package tai.utils;

import com.mongodb.MongoClient;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import com.mongodb.Block;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
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
				result.put(document.getString("_id"), 
				document.getDouble("average-rating"));
			}
		};
		reviewCollection.aggregate(
			Arrays.asList(
				Aggregates.group("$product-name", Accumulators.avg("average-rating", "$review-rating"))
				, Aggregates.limit(limit)
				, Aggregates.sort(Sorts.descending("average-rating"))
			)
		).forEach(toResult);
		return result;
	}

	public Map<Integer, Integer> selectTopZipByProductCount(int limit) {
		MongoCollection<Document> reviewCollection = mongoDatabase.getCollection("review");
		Map<Integer, Integer> result = new LinkedHashMap<>(limit);
		Block<Document> toResult = new Block<Document>() {
			@Override
			public void apply(final Document document) {
				result.put(document.getInteger("_id"), 
				document.getInteger("product-count"));
				
			}
		};
		reviewCollection.aggregate(
			Arrays.asList(
				  new Document("$group", 
					new Document("_id", 
						new Document("zip", "$retailer-zip")
						.append("product", "$product-name")
					)
				  )
				, new Document("$group",
					new Document("_id", "$_id.zip")
					.append("product-count", new Document("$sum", 1))
				  )
				, new Document("$limit", limit)
				, new Document("$sort", new Document("count", -1))
			)
		)
		.forEach(toResult);

		return result;
	}

	public Map<String, Integer> selectTopProductByAmount(int limit) {
		MongoCollection<Document> reviewCollection = mongoDatabase.getCollection("review");
		Map<String, Integer> result = new LinkedHashMap<>(limit);
		Block<Document> toResult = new Block<Document>() {
			@Override
			public void apply(final Document document) {
				System.out.println(document.toJson());
				result.put(document.getString("_id"), 
				document.getInteger("product-count"));
			}
		};

		reviewCollection.aggregate(
			Arrays.asList(
				Aggregates.group("$product-name", Accumulators.sum("product-count", 1))
				, Aggregates.limit(limit)
				, Aggregates.sort(Sorts.descending("product-count"))
			)
		)
		.forEach(toResult);

		return result;
	}

	public void closeMongoDbConnection() {
		if(mongoClient != null) {
			mongoClient.close();
			System.out.println("MongoDB connection closed.");
		}
	}

	public void initMongoDbConnection() {
		mongoClient = new MongoClient("localhost", 27017);
		mongoDatabase = mongoClient.getDatabase("smart_portables");
	}
}