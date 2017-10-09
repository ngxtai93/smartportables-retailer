package tai.utils;

import com.mongodb.MongoClient;
import com.mongodb.client.*;
import org.bson.Document;
import java.util.Map;

public enum MongoDBDataStoreUtilities {
    INSTANCE;

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    private MongoDBDataStoreUtilities() {
        mongoClient = new MongoClient("localhost", 27017);
        mongoDatabase = mongoClient.getDatabase("smart_portables");
    }

    public void insertReview(Map<String, String[]> paramMap) {
        MongoCollection<Document> reviewCollection = mongoDatabase.getCollection("review");
        Document doc = new Document();
        for(Map.Entry<String, String[]> entry: paramMap.entrySet()) {
            String[] value = entry.getValue();
            if(value != null && value.length > 0) {
                if(!value[0].equals("")) {
                    doc.append(entry.getKey(), value[0]);
                }
            }
        }
        reviewCollection.insertOne(doc);
    }



}