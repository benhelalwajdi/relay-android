package app.com.relay.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import java.util.List;

import app.com.relay.R;
import app.com.relay.adapter.AdapterRatedProduct;
import app.com.relay.model.Product;
import app.com.relay.utils.Constant;
import app.com.relay.utils.SpacingItemDecoration;
import app.com.relay.utils.Tools;

public class ProductListActivity extends AppCompatActivity {

    private AdapterRatedProduct adapterRatedProduct;
    private List<Product> topRatedProductsList;
    private RecyclerView recyclerView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        initToolbar();
        initComponent();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Products");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    private void initComponent() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(this, 8), true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        getTopRatedProduct();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_store_account) {
            startActivity(new Intent(ProductListActivity.this, StoreAccountActivity.class));
        } else if (item.getItemId() == R.id.action_shopping_cart) {
            startActivity(new Intent(ProductListActivity.this, CartActivity.class));
        } else if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void getTopRatedProduct() {
        topRatedProductsList = new ArrayList<>();
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
                                topRatedProductsList.add(product);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        System.out.println(topRatedProductsList);
                        adapterRatedProduct = new AdapterRatedProduct(getApplicationContext(),
                                topRatedProductsList);
                        recyclerView.setAdapter(adapterRatedProduct);

                        // on item list clicked
                        adapterRatedProduct.setOnItemClickListener(new AdapterRatedProduct
                                .OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, Product obj, int position) {
                                Toast.makeText(ProductListActivity.this, "Item" +
                                        obj.name + "clicked", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ProductListActivity.this,
                                        ProductDetailsActivity.class);
                                System.out.println();
                                intent.putExtra("id", String.valueOf(obj.id));
                                startActivity(intent);
                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", error.getMessage());
                    }
                }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonArrayRequest);
    }
}
