package app.com.relay.model;

import org.json.JSONException;
import org.json.JSONObject;

public class ReviewProduct {

    private String id;
    private float rating;
    private String comment;
    private String id_product;
    private Client client;

    public ReviewProduct(float rating, String comment, String id_product) {
        this.rating = rating;
        this.comment = comment;
        this.id_product = id_product;
    }

    public ReviewProduct(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getString("id");
            this.rating = Float.valueOf(jsonObject.getString("rating"));
            this.comment = jsonObject.getString("comment");
            this.id_product = jsonObject.getString("id_product");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getId_product() {
        return id_product;
    }

    public void setId_product(String id_product) {
        this.id_product = id_product;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id='" + id + '\'' +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", id_product='" + id_product + '\'' +
                '}';
    }
}
