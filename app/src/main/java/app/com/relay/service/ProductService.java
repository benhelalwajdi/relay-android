package app.com.relay.service;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import app.com.relay.adapter.AdapterGridShopProductCard;
import app.com.relay.model.Product;
import app.com.relay.utils.Constant;

public class ProductService {

    private List<Product> list;
    private Product product;
    private AdapterGridShopProductCard adapter;



    public List<Product> getAllProducts(final Context context) {
        list = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                Constant.URL_PRODUCTS,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Product product = new Product(jsonObject);
                                list.add(product);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", error.getMessage());
                    }
                }
        );
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsonArrayRequest);
        return list;
    }

    public Product getProductById(String id, Context context) {
        String URL = Constant.URL_PRODUCTS + "/product/" + id;
        this.product = new Product();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Product p = new Product(response);
                        initiateProduct(p);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", error.getMessage());
                    }
                }
        );
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsonObjectRequest);
        System.out.println(product.toString());
        return this.product;
    }

    private void initiateProduct(Product product) {
        this.product = product;
        System.out.println(product.toString());
        System.out.println(this.product.toString());
    }
}
