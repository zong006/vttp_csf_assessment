package vttp.batch5.csf.assessment.server.repositories;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import jakarta.json.JsonObject;

// Use the following class for MySQL database

@Repository
public class RestaurantRepository {

    @Autowired
    private JdbcTemplate template;

    public boolean correctCredentials(String username){
        String SQL_VERIFY_USER = """
                select count(*) as count from customers
                where username = ? and password = sha2(?, 224)
                ;
                """;
        SqlRowSet rs = template.queryForRowSet(SQL_VERIFY_USER, username, username);
        int count = 0;
        while (rs.next()){
            count = rs.getInt("count");
            break;
        }
        // System.out.println("count: " + count);
        return count == 1;
    }

    public int savePaymentDetails(String username, JsonObject chukPayload){
        String SQL_SAVE_DETAILS = """
                insert into place_orders
                (order_id, payment_id, order_date, total, username)
                values
                (?,?,?,?,?)
                """;

        Date orderDate = new Date( chukPayload.getJsonNumber("timestamp").longValue());
        
        int updated = template.update(SQL_SAVE_DETAILS, chukPayload.getString("order_id"),
                                            chukPayload.getString("payment_id"),    
                                            orderDate,
                                            chukPayload.getJsonNumber("total").doubleValue(),
                                            username
                                            );
        return updated;
    }
}
