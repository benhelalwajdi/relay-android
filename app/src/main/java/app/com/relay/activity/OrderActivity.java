package app.com.relay.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.com.relay.R;
import app.com.relay.adapter.AdapterListBasic;
import app.com.relay.model.Order;
import app.com.relay.model.Product;
import app.com.relay.utils.Constant;
import app.com.relay.utils.DataGenerator;

public class OrderActivity extends AppCompatActivity {

    private View parent_view;
    String id ;

    private RecyclerView recyclerView;
    private AdapterListBasic mAdapter;
    private EditText inputSearch;
    List<Order> items,items2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        parent_view = findViewById(android.R.id.content);
        SharedPreferences prfs = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
        id = prfs.getString("ID_USER", "");
        initToolbar();
        getAllProducts(getApplicationContext());
    }

    public void getAllProducts(Context context){
        String URL = Constant.URL_ORDER_STORE+ id  ;
        System.out.println(URL);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    ArrayList<HashMap<String, String>> list = new ArrayList<>() ;
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                // product = new Product(jsonObject);
                                Log.e("Respence Json: ", String.valueOf(jsonObject));
                                Log.e("Respence Product: ",  jsonObject.getString("reference"));
                                HashMap<String, String> productMap = new HashMap<>();
                                productMap.put("id_product", jsonObject.getString("id_product"));
                                productMap.put("date",jsonObject.getString("date"));
                                productMap.put("state", jsonObject.getString("state"));
                                productMap.put("reference",jsonObject.getString("reference"));
                                productMap.put("quantity",jsonObject.getString("quantity"));
                                list.add(productMap);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        initComponent(list);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", "Oups Something went wrong !");
                    }
                }
        );
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsonArrayRequest);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void initComponent(final ArrayList<HashMap<String, String>> list) {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewOrder);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        items = DataGenerator.getOrder(this,list);
        //set data and list adapter
        mAdapter = new AdapterListBasic(this, items);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterListBasic.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Order obj, int position) {
                Snackbar.make(parent_view, "Item " + obj.name + " clicked", Snackbar.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_order_product, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}