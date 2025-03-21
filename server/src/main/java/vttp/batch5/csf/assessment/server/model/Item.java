package vttp.batch5.csf.assessment.server.model;

public class Item {
    private String id;
    private int quantity;
    private float price;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public float getPrice() {
        return price;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    @Override
    public String toString() {
        return "Item [id=" + id + ", quantity=" + quantity + ", price=" + price + "]";
    }
}
