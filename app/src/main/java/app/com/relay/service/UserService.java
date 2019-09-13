package app.com.relay.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import app.com.relay.activity.DelivererHomeActivity;
import app.com.relay.activity.HomeActivity;
import app.com.relay.activity.StoreAccountActivity;
import app.com.relay.model.Client;
import app.com.relay.utils.DatabaseHelper;
import app.com.relay.utils.SharedPreferencesHelper;

public class UserService {
    private Context ctx;


    public UserService(Context context) {
        this.ctx = context;

    }

    public void tryToLogin(String URL) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.has("user")) {
                            Toast.makeText(ctx, "Check email/password and try again.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                if (response.getString("user_type").equals("CLIENT")) {
                                    DatabaseHelper databaseHelper = new DatabaseHelper(ctx);
                                    databaseHelper.addClient(new Client(response));
                                    checkUserTypeAndRedirect(ctx);
                                } else {
                                    if (response.getString("user_type").equals("STORE")) {
                                        SharedPreferences _sharedPrefs;
                                        SharedPreferences.Editor _prefsEditor;
                                        SharedPreferences prfs = ctx.getSharedPreferences("AUTHENTICATION_FILE_NAME",
                                                Context.MODE_PRIVATE);
                                        _prefsEditor = prfs.edit();
                                        _prefsEditor.putString("id", response.getString("id"));
                                        _prefsEditor.apply();
                                        ctx.startActivity(new Intent(ctx, StoreAccountActivity.class));
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ctx, "Check email/password and try again.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(ctx);
        queue.add(jsonObjectRequest);
    }


    private void checkUserTypeAndRedirect(Context ctx) {
        DatabaseHelper databaseHelper = new DatabaseHelper(ctx);
        if (databaseHelper.getUserType().equals("CLIENT")) {
            ctx.startActivity(new Intent(ctx, HomeActivity.class));
        }
        if (databaseHelper.getUserType().equals("STORE")) {
            ctx.startActivity(new Intent(ctx, StoreAccountActivity.class));
        }
        if (databaseHelper.getUserType().equals("DELIVERER")) {
            ctx.startActivity(new Intent(ctx, DelivererHomeActivity.class));
        }
    }
}
