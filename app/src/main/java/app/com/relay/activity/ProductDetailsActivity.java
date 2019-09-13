package app.com.relay.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import app.com.relay.R;
import app.com.relay.adapter.AdapterReview;
import app.com.relay.model.Client;
import app.com.relay.model.Product;
import app.com.relay.model.ReviewProduct;
import app.com.relay.service.ReviewService;
import app.com.relay.utils.Constant;
import app.com.relay.utils.CurrencyFormat;
import app.com.relay.utils.DatabaseHelper;
import app.com.relay.utils.Tools;
import app.com.relay.utils.ViewAnimation;

public class ProductDetailsActivity extends AppCompatActivity {

    private View parent_view;
    private ImageButton bt_toggle_reviews, bt_toggle_description;
    private View lyt_expand_reviews, lyt_expand_description;
    private NestedScrollView nested_scroll_view;
    private TextView txtProductName, txtProductPrice, txtProductDescription, txtSumRating,
            txtStoreName, txt_no_item;
    private Intent intent;
    private List<ReviewProduct> reviewList;
    private RatingBar ratingBar;
    private AdapterReview adapterReview;
    private RecyclerView recyclerViewRatings;
    private Product product;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        parent_view = findViewById(R.id.parent_view);

        initToolbar();
        initComponent();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Fashion");
    }

    private void initComponent() {
        intent = getIntent();
        databaseHelper = new DatabaseHelper(this);


        txtProductName = (TextView) findViewById(R.id.txt_product_name);
        txtProductPrice = (TextView) findViewById(R.id.txt_product_price);
        txtProductDescription = (TextView) findViewById(R.id.txt_product_description);
        txtStoreName = (TextView) findViewById(R.id.txt_store_name);
        txtSumRating = (TextView) findViewById(R.id.txt_sum_rating);
        txt_no_item = (TextView) findViewById(R.id.txt_no_item);
        ratingBar = (RatingBar) findViewById(R.id.rating_bar);

        recyclerViewRatings = (RecyclerView) findViewById(R.id.recyclerViewReviews);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                ProductDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerViewRatings.setLayoutManager(linearLayoutManager);

        getProductById(intent.getStringExtra("id"), this);
        getProductReviews(intent.getStringExtra("id"));


        // nested scrollview
        nested_scroll_view = (NestedScrollView) findViewById(R.id.nested_scroll_view);

        // section reviews
        bt_toggle_reviews = (ImageButton) findViewById(R.id.bt_toggle_reviews);
        lyt_expand_reviews = (View) findViewById(R.id.lyt_expand_reviews);
        bt_toggle_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSection(view, lyt_expand_reviews);
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

    }


    private void toggleSection(View bt, final View lyt) {
        boolean show = toggleArrow(bt);
        if (show) {
            ViewAnimation.expand(lyt, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    Tools.nestedScrollTo(nested_scroll_view, lyt);
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

    public void getProductById(String id, Context context) {
        String URL = Constant.URL_PRODUCTS + id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        product = new Product(response);
                        txtProductName.setText(product.getName());
                        txtProductPrice.setText(CurrencyFormat.format(product.getPrice()));
                        txtProductDescription.setText(product.getDescription());
                        getStoreName(product.getIdStore());
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
    }

    private void getProductReviews(String id) {
        reviewList = new ArrayList<>();
        String URL = Constant.URL_GET_PRODUCT_REVIEWS + id;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                ReviewProduct reviewProduct = new ReviewProduct(jsonObject);
                                reviewProduct.setClient(databaseHelper.getCurrentClient());
                                reviewList.add(reviewProduct);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        updateRatingBar();
                        updateRatingSum();
                        adapterReview = new AdapterReview(getApplicationContext(),
                                reviewList);
                        recyclerViewRatings.setAdapter(adapterReview);
                        if (adapterReview.getItemCount() > 0) {
                            txt_no_item.setVisibility(View.GONE);
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
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonArrayRequest);
    }

    private void getStoreName(String id) {
        String URL = Constant.URL_STORES + id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            txtStoreName.setText(response.getString("store_name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"product",Toast.LENGTH_LONG).show();
                    }
                }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

   /* private void getClientName(String id, final ReviewProduct reviewProduct) {
        String URL = Constant.URL_CLIENTS + id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Client client = new Client(response);
                        reviewProduct.setClient(client);
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
        queue.add(jsonObjectRequest);
    }
*/
    private void showCustomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_review);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final EditText txtComment = (EditText) dialog.findViewById(R.id.txt_comment);
        final TextView txtUserName = (TextView) dialog.findViewById(R.id.txt_user_name);
        txtUserName.setText(databaseHelper.getClientFullName());
        final AppCompatRatingBar ratingBar = (AppCompatRatingBar)
                dialog.findViewById(R.id.rating_bar);
        ((AppCompatButton) dialog.findViewById(R.id.bt_cancel))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

        ((AppCompatButton) dialog.findViewById(R.id.bt_submit))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String comment = txtComment.getText().toString().trim();
                        if (comment.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Ecrivez quelque chose !",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            ReviewService reviewService = new ReviewService(getApplicationContext());
                            ReviewProduct reviewProduct = new ReviewProduct(
                                    ratingBar.getRating(),
                                    comment,
                                    intent.getStringExtra("id"));
                            Client client = databaseHelper.getCurrentClient();
                            reviewProduct.setClient(client);
                            reviewService.addNewReviewOnProduct(reviewProduct, adapterReview);
                            reviewList.add(reviewProduct);
                            adapterReview.notifyDataSetChanged();
                            updateRatingBar();
                            updateRatingSum();
                            checkIfReviewListIsEmpty();

                        }
                        dialog.dismiss();
                    }
                });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void clickAction(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.fab_add:
                showCustomDialog();
                break;
            case R.id.fab_add_to_cart:
                DatabaseHelper databaseHelper = new DatabaseHelper(this);
                if (databaseHelper.getProductById(intent.getStringExtra("id")) == null) {
                    if (databaseHelper.addProductToCart(product)) {
                        Snackbar.make(parent_view, "Add to Cart", Snackbar.LENGTH_SHORT).show();

                    } else {
                        Snackbar.make(parent_view, "Something went wrong. Please try again !",
                                Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(parent_view, "This product is already in your cart.",
                            Snackbar.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void checkIfReviewListIsEmpty() {
        if (adapterReview.getItemCount() == 0) {
            txt_no_item.setVisibility(View.VISIBLE);
        } else {
            txt_no_item.setVisibility(View.GONE);
        }
    }


    private void updateRatingSum() {
        txtSumRating.setText(String.valueOf(reviewList.size()));
    }


    private void updateRatingBar() {
        float rating = 0;
        for (ReviewProduct reviewProduct : reviewList) {
            rating += reviewProduct.getRating();
        }
        ratingBar.setRating(rating / reviewList.size());

    }
}
