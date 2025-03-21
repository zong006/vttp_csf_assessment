package vttp.batch5.csf.assessment.server.controllers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import vttp.batch5.csf.assessment.server.services.RestaurantService;

@RestController
@RequestMapping("/api")
public class RestaurantController {

  @Autowired
  private RestaurantService restaurantService;
  // TODO: Task 2.2
  // You may change the method's signature
  @GetMapping("/menu")
  public ResponseEntity<String> getMenus() {

    return ResponseEntity.ok().body(restaurantService.getMenu().toString());
  }

  // TODO: Task 4
  // Do not change the method's signature
  @PostMapping("/food_order")
  public ResponseEntity<String> postFoodOrder(@RequestBody String payload) {

    InputStream is = new ByteArrayInputStream(payload.getBytes());
    JsonReader jr = Json.createReader(is);
    JsonObject job = jr.readObject();

    String username = job.getString("username");
    String password = job.getString("password");

    boolean verified = restaurantService.verifyCredentials(username, password);

    // System.out.println(job);

    JsonObjectBuilder builder = Json.createObjectBuilder();
    if (!verified){

      builder.add("message", "Invalid username and/or password.");  
      return ResponseEntity.status(401).body(builder.build().toString());
    }

    JsonObject response = restaurantService.makePayment(username, job);

    if (response.containsKey("message")){ // <-- fails
      return ResponseEntity.status(500).body(response.toString());
    }
    
    return ResponseEntity.status(200).body(response.toString());
    
  }
}
