package app.com.relay.service;

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

import app.com.relay.activity.DelivererHomeActivity;
import app.com.relay.model.Deliverer;
import app.com.relay.utils.Constant;
import app.com.relay.utils.SharedPreferencesHelper;
import app.com.relay.utils.Tools;

public class DelivererService {

    private Context context;
    private Activity activity;
    private ProgressDialog progressDialog;

    public DelivererService(Context context, Activity activity) {

        this.context = context;
        this.activity = activity;
    }

    public void addNewDeliverer(final Deliverer deliverer) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Registering ...");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Constant.URL_USERS + "create_deliverer";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", response);

                        progressDialog.dismiss();
                        context.startActivity(new Intent(context, DelivererHomeActivity.class));

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
                params.put("first_name", deliverer.getFirstName());
                params.put("last_name", deliverer.getLastName());
                params.put("address", deliverer.getAddress());
                params.put("phone_number", deliverer.getPhoneNumber());
                params.put("mail", deliverer.getMail());
                params.put("password", deliverer.getPassword());
                params.put("vehicle", deliverer.getVehicle());
                params.put("image", deliverer.getImage());
                return params;
            }
        };
        queue.add(postRequest);
    }

}
