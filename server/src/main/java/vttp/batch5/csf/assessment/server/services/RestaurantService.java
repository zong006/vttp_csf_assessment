package vttp.batch5.csf.assessment.server.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.batch5.csf.assessment.server.repositories.OrdersRepository;
import vttp.batch5.csf.assessment.server.repositories.RestaurantRepository;

@Service
public class RestaurantService {

  @Autowired
  private RestaurantRepository restaurantRepository;

  @Autowired
  private OrdersRepository ordersRepository;
  // TODO: Task 2.2
  // You may change the method's signature
  public JsonArray getMenu() {
    List<Document> results = ordersRepository.getMenu();

    JsonArrayBuilder array = Json.createArrayBuilder();
    results.forEach(x -> {
      JsonObject job = Json.createObjectBuilder().add("id", x.getString("_id"))
                                                  .add("name", x.getString("name"))
                                                  .add("description", x.getString("description"))
                                                  .add("price", x.getDouble("price"))
                                                  .build();
      array.add(job);
    });
    return array.build();
  }
  
  // TODO: Task 4
  public boolean verifyCredentials(String username, String password){
    return restaurantRepository.correctCredentials(username);
  }

  public JsonObject makePayment(String username, JsonObject payload){

    JsonArray ja = payload.getJsonArray("items");
    double total = 0;

    for (int i=0 ; i< ja.size() ; i++){
      JsonObject jo = ja.getJsonObject(i);
      total += jo.getJsonNumber("price").doubleValue() * jo.getInt("quantity");
    }

    JsonObject job = Json.createObjectBuilder().add("order_id", UUID.randomUUID().toString().substring(0,8))
                                                .add("payer", username)
                                                .add("payee", "ONG ZHI CONG")
                                                .add("payment", total)
                                                .build();

    RestTemplate restTemplate = new RestTemplate();

    String url = "https://payment-service-production-a75a.up.railway.app/";

    RequestEntity<String> requestEntity = RequestEntity.post(url + "api/payment")
                                                        .header("X-Authenticate", username)
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .body(job.toString(), String.class);
		ResponseEntity<String> responseEntity;
    JsonObject jsonResponse;

    try {
      responseEntity = restTemplate.exchange(requestEntity, String.class);
      jsonResponse = generateRespnseJson(responseEntity);
      
      // System.out.println("ok: " + jsonResponse);
      // status code of 202
      restaurantRepository.savePaymentDetails(username, jsonResponse);
      ordersRepository.insertOrderToMongo(ja, jsonResponse, username);
      
      return jsonResponse;
    } catch (RestClientException e) {
      
      // System.out.println(e.getMessage());
      jsonResponse = Json.createObjectBuilder().add("message", e.getMessage()).build();

      return jsonResponse;
    }
  }


  private JsonObject generateRespnseJson(ResponseEntity<String> responseEntity){
        
        String respBody = responseEntity.getBody();
        InputStream is = new ByteArrayInputStream(respBody.getBytes());
        JsonReader jsonReader = Json.createReader(is);
        JsonObject jsonData = jsonReader.readObject();
        jsonReader.close();

        return jsonData;
	}
}
