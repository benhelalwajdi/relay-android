package app.com.relay.service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import app.com.relay.activity.HomeActivity;
import app.com.relay.model.Client;
import app.com.relay.utils.Constant;
import app.com.relay.utils.SharedPreferencesHelper;
import app.com.relay.utils.Tools;

public class ClientService {

    private Context context;
    private Activity activity;
    private ProgressDialog progressDialog;

    public ClientService(Context context, Activity activity) {

        this.context = context;
        this.activity = activity;
    }
    public void addNewClient(final Client client){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Registering ...");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Constant.URL_USERS + "create_client";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", response);

                        progressDialog.dismiss();
                        context.startActivity(new Intent(context, HomeActivity.class));

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.d("ERROR","error => "+error.toString());
                        Tools.showToast("Please try again.", activity);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                params.put("first_name", client.getFirstName());
                params.put("last_name", client.getLastName());
                params.put("address", client.getAddress());
                params.put("phone_number", client.getPhoneNumber());
                params.put("mail", client.getMail());
                params.put("password", client.getPassword());
                params.put("image", client.getImage());
                return params;
            }
        };
        queue.add(postRequest);
    }



}
