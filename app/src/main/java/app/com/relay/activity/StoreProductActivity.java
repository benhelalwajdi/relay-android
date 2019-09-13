package app.com.relay.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.relay.R;
import app.com.relay.adapter.AdapterListProduct;
import app.com.relay.adapter.AdapterListReview;
import app.com.relay.model.Product;
import app.com.relay.model.Review;
import app.com.relay.utils.Constant;
import app.com.relay.utils.DataGenerator;
import app.com.relay.utils.Tools;
import app.com.relay.utils.ViewAnimation;

public class StoreProductActivity extends AppCompatActivity {

    private View parent_view;

    private ImageButton bt_toggle_reviews, bt_toggle_warranty, bt_toggle_description;
    private View lyt_expand_reviews, lyt_expand_warranty, lyt_expand_description;
    private NestedScrollView nested_scroll_view;
    private HashMap<String, String> prod ;
    private boolean rotate = false;
    private String idStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_product);
        parent_view = findViewById(R.id.parent_view);
        Bundle extras = getIntent().getExtras();
        SharedPreferences prfs = getSharedPreferences("AUTHENTICATION_FILE_NAME",
                Context.MODE_PRIVATE);
        String userType = prfs.getString("USER_TYPE", "");
        String id = prfs.getString("ID_USER", "");
        idStore = "54";

        prod = (HashMap<String, String>) extras.get("prod");
        Log.d("The Prod data",prod.get("name"));
        setData(prod);
        initToolbar();
        initComponent();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("store");
    }

    private void initComponent() {
        // nested scrollview
        nested_scroll_view = (NestedScrollView) findViewById(R.id.nested_scroll_view);

        // section reviews
        bt_toggle_reviews = (ImageButton) findViewById(R.id.bt_toggle_reviews);
        lyt_expand_reviews = (View) findViewById(R.id.lyt_expand_reviews);
        bt_toggle_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSection(view, lyt_expand_reviews);
                getAllReview(getApplicationContext());
            }
        });
        // section warranty
        bt_toggle_warranty = (ImageButton) findViewById(R.id.bt_toggle_warranty);
        lyt_expand_warranty = (View) findViewById(R.id.lyt_expand_warranty);
        bt_toggle_warranty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSection(view, lyt_expand_warranty);
            }
        });

        // section description
        bt_toggle_description = (ImageButton) findViewById(R.id.bt_toggle_description);
        lyt_expand_description = (View) findViewById(R.id.lyt_expand_description);
        bt_toggle_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSection(view, lyt_expand_description);
            }
        });

        // expand first description
        toggleArrow(bt_toggle_description);
        lyt_expand_description.setVisibility(View.VISIBLE);

        final FloatingActionButton fab_edit = (FloatingActionButton) findViewById(R.id.fab_edit);
        final FloatingActionButton fab_delete = (FloatingActionButton) findViewById(R.id.fab_delete);
        ViewAnimation.initShowOut(fab_edit);
        ViewAnimation.initShowOut(fab_delete);

        ((FloatingActionButton) findViewById(R.id.fab_add)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotate = ViewAnimation.rotateFab(v, !rotate);
                if (rotate) {
                    ViewAnimation.showIn(fab_edit);
                    ViewAnimation.showIn(fab_delete);
                } else {
                    ViewAnimation.showOut(fab_edit);
                    ViewAnimation.showOut(fab_delete);
                }
            }
        });

        fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(parent_view, "Edit Product", Snackbar.LENGTH_LONG).show();
                Intent i = new Intent(StoreProductActivity.this, EditProductActivity.class);
                i.putExtra("prod", prod);
                startActivity(i);
            }
        });

        fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(parent_view, "Delete Product", Snackbar.LENGTH_LONG).show();
                showCustomDialog(prod);
            }
        });


    }

    private void setAdapter(ArrayList<HashMap<String, String>> list) {
        RecyclerView precyclerView = (RecyclerView) findViewById(R.id.recyclerViewReview);
        precyclerView.setLayoutManager(new LinearLayoutManager(this));
        precyclerView.setHasFixedSize(true);
        List<Review> items = DataGenerator.getReview(this,list);
        System.out.println("items line 291: "+items.toString());
        //set data and list adapter
        AdapterListReview pAdapter = new AdapterListReview(getApplicationContext(), items);
        precyclerView.setAdapter(pAdapter);
    }

    public void getAllReview(Context context){
        String URL = Constant.URL_GET_PRODUCT_REVIEWS+ prod.get("id");
        System.out.println(URL);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    HashMap<String, String> reviewMap ;

                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<HashMap<String, String>> list = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                // product = new Product(jsonObject);
                                Log.e("Respence Json: ", String.valueOf(jsonObject));
                                Log.e("Respence Product: ",  jsonObject.getString("comment"));
                                System.out.println("size list in line 192 : "+ list.size());
                                HashMap<String, String> reviewMap = new HashMap<>();
                                reviewMap.put("id", jsonObject.getString("id"));
                                reviewMap.put("rating",jsonObject.getString("rating"));
                                reviewMap.put("comment", jsonObject.getString("comment"));
                                list.add(reviewMap);
                                //lProd = getlist();
                                System.out.println("Size of list is  : "+ list.size());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        System.out.println("list line 207: "+list.toString());
                        setAdapter(list);
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

    private void showCustomDialog(HashMap<String,String> p) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_dark);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((TextView) dialog.findViewById(R.id.title)).setText(p.get("name"));
        ((TextView) dialog.findViewById(R.id.content)).setText(p.get("description"));
        Picasso.with(this).load(p.get("image")).resize(200, 200).into((ImageView) dialog.findViewById(R.id.image));

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((Button) dialog.findViewById(R.id.bt_no)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Your exit Clicked", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        ((Button) dialog.findViewById(R.id.bt_yes)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Your exit Clicked", Toast.LENGTH_SHORT).show();
                String url =  Constant.URL_PRODUCTS+"delete_product";
                delete(url);
                dialog.dismiss();
                Intent i = new Intent(StoreProductActivity.this,StoreAccountActivity.class);
                startActivity(i);

            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    private void toggleSection(View bt, final View lyt) {
        boolean show = toggleArrow(bt);
        if (show) {
            ViewAnimation.expand(lyt, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                  //  Tools.nestedScrollTo(nested_scroll_view, lyt);
                }
            });
        } else {
            ViewAnimation.collapse(lyt);
        }
    }

    public boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }

    public void setData(HashMap<String, String> produit){
        ImageView imageGallery = (ImageView) findViewById(R.id.image);
        TextView prod_name = (TextView) findViewById(R.id.prod_name);
        TextView price = (TextView) findViewById(R.id.price);
        TextView quantity_sizeProd = (TextView) findViewById(R.id.quantity_sizeProd);
        TextView descProd = (TextView) findViewById(R.id.descProd);
        prod_name.setText(produit.get("name"));
        descProd.setText(produit.get("description"));
        price.setText(produit.get("price")+" Dtn");
        quantity_sizeProd.setText("Quantity : "+ produit.get("quantity")+"\n Size : "+ produit.get("size"));
        Picasso.with(this).load(produit.get("image")).resize(200, 200).into(imageGallery);

    }


    public void delete(String url){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
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
                params.put("id", prod.get("id"));
                return params;
            }
        };
        queue.add(postRequest);
    }






}
