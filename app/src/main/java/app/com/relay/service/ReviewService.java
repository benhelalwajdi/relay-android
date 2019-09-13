package app.com.relay.service;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

import app.com.relay.adapter.AdapterReview;
import app.com.relay.model.ReviewProduct;
import app.com.relay.utils.Constant;

public class ReviewService {

    private Context context;


    public ReviewService(Context context) {
        this.context = context;
    }

    public void addNewReviewOnProduct(final ReviewProduct reviewProduct, final AdapterReview adapterReview){
        System.out.println(reviewProduct + " " + reviewProduct.getClient().getId());
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Constant.URL_ADD_PRODUCT_REVIEW;
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", response);
                       //TODO: Do something on response !
                        adapterReview.notifyDataSetChanged();

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR","error => "+error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                params.put("idClient", reviewProduct.getClient().getId());
                params.put("idProduct", reviewProduct.getId_product());
                params.put("rating", String.valueOf(reviewProduct.getRating()));
                params.put("comment", reviewProduct.getComment());
                return params;
            }
        };
        queue.add(postRequest);

    }
}
