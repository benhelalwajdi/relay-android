package app.com.relay.model;

import android.graphics.drawable.Drawable;

import org.json.JSONException;
import org.json.JSONObject;

public class Product {


    public String id ;
    public String name ;
    public String description ;
    public String price ;
    public String quantity ;
    public String size;
    public String image;
    public String date;
    public String idStore;

   public Product(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getString("id");
            this.name = jsonObject.getString("name");
            this.price = jsonObject.getString("price");
            this.description = jsonObject.getString("description");
            this.size = jsonObject.getString("size");
            this.idStore = jsonObject.getString("store_id");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public Product(String id, String name, String description, String price, String image, String size,
                   String quantity, String idStore) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.price = price;
        this.description = description;
        this.size = size;
        this.quantity = quantity;
        this.idStore = idStore;
    }
    public Product() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getIdStore() {
        return idStore;
    }

    public void setIdStore(String idStore) {
        this.idStore = idStore;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                ", quantity='" + quantity + '\'' +
                ", size='" + size + '\'' +
                ", image='" + image + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
