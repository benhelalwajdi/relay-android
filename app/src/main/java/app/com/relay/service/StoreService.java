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

import app.com.relay.model.Product;
import app.com.relay.model.Store;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import app.com.relay.activity.StoreAccountActivity;
import app.com.relay.model.Store;
import app.com.relay.utils.Constant;
import app.com.relay.utils.Tools;

public class StoreService {

    private Context context;
    private Activity activity;
    private ProgressDialog progressDialog;

    public StoreService(Context context, Activity activity) {

        this.context = context;
        this.activity = activity;
    }

    public void addNewStore(final Store store) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Registering ...");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Constant.URL_USERS + "create_store";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", response);

                        progressDialog.dismiss();
                        context.startActivity(new Intent(context, StoreAccountActivity.class));

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.d("ERROR", "error => " + error.toString());
                        Tools.showToast("Please try again.", activity);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("store_type", store.getStoreType());
                params.put("store_name", store.getStoreName());
                params.put("address", store.getAddress());
                params.put("phone_number", store.getPhoneNumber());
                params.put("mail", store.getMail());
                params.put("password", store.getPassword());
                params.put("image", store.getImage());
                return params;
            }
        };
        queue.add(postRequest);
    }

    public void editStore(String url, final Store store, final String id , Context context){
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
            {/*UPDATE user SET store_name = ? , store_type = ? , mail = ? , password = ? , address = ?," +
        " phone_number = ? WHERE id = ?";*/

                System.out.println("fhhhhf"+store.toString());
                Map<String, String> params = new HashMap<String, String>();
                params.put("store_name", store.getStoreName());
                params.put("store_type", store.getStoreType());
                params.put("phone_number", store.getPhoneNumber());
                params.put("mail", store.getMail());
                params.put("image",store.getImage());
                params.put("address", store.getAddress());
                params.put("password","password");
                params.put("id", id);
                return params;
            }
        };
        queue.add(postRequest);
    }
}
