package vttp.batch5.csf.assessment.server.repositories;



import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;


@Repository
public class OrdersRepository {

  @Autowired
  private MongoTemplate template;

  private final String COLLECTION_RESTAURANT = "menus";
  private final String COLLECTION_ORDERS = "orders";

  // TODO: Task 2.2
  // You may change the method's signature
  // Write the native MongoDB query in the comment below
  //
  //  Native MongoDB query here
    
  /*
    db.getCollection("menus").find({}).sort({"name":1})
  */
  public List<Document> getMenu() {
    
    Query query = new Query();
    query.with(Sort.by(Sort.Direction.ASC, "name"));

    List<Document> results = template.find(query, Document.class, COLLECTION_RESTAURANT);
    return results;
  }

  // TODO: Task 4
  // Write the native MongoDB query for your access methods in the comment below
  //
  //  Native MongoDB query here

  public void insertOrderToMongo(JsonArray items, JsonObject fromChuk, String username){
    // Document toInsert = Document.parse(fromChuk.toString());
    
    Date timestamp = new Date(fromChuk.getJsonNumber("timestamp").longValue());
    Document toInsert = new Document();

    toInsert.put("timestamp", timestamp);
    toInsert.put("payment_id", fromChuk.getString("payment_id"));
    toInsert.put("order_id", fromChuk.getString("order_id"));
    toInsert.put("total", fromChuk.getJsonNumber("total").doubleValue());

    toInsert.put("username", username);
    toInsert.put("_id", fromChuk.getString("order_id"));

    List<Document> dlist = new LinkedList<>();
    for (int i=0 ; i< items.size() ; i++){
      Document x = Document.parse(items.getJsonObject(i).toString());
      dlist.add(x);
    }
    toInsert.put("items", dlist);

    template.insert(toInsert, COLLECTION_ORDERS);
  }
  
}
