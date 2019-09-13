package app.com.relay.service;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import app.com.relay.model.Product;

public class ServiceProduct {
    public static List<Product> list = new ArrayList<>();
    public Product product ;

    public void listProduct(Product prod){
        list.add(prod);
        System.out.print(list.size()+" taille de list");
    }

    public List<Product> getlist(){
        System.out.println();
        System.out.println(list.size()+" getlist wooooow");
        return list;

    }
    public void editProduct(String url, final Product product, final String id , Context context){
        //url = "http://localhost:3003/products/update";
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {

                Map<String, String> params = new HashMap<String, String>();
                params.put("name", product.getName());
                params.put("description", product.getDescription());
                params.put("price", product.getPrice());
                params.put("quantity", product.getQuantity());
                params.put("size", product.getSize());
                params.put("image",product.getImage());
                params.put("id", product.getId());
                return params;
            }
        };
        queue.add(postRequest);

    }

}
