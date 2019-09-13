package app.com.relay.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

import app.com.relay.adapter.AdapterGridShopProductCard;
import app.com.relay.adapter.AdapterImageSlider;
import app.com.relay.adapter.AdapterRatedProduct;
import app.com.relay.adapter.AdapterStore;
import app.com.relay.model.Image;
import app.com.relay.model.Product;
import app.com.relay.model.Store;
import app.com.relay.service.ProductService;
import app.com.relay.utils.Constant;
import app.com.relay.utils.DataGenerator;
import app.com.relay.utils.DatabaseHelper;
import app.com.relay.utils.SharedPreferencesHelper;
import app.com.relay.utils.SpacingItemDecoration;
import app.com.relay.utils.Tools;
import app.com.relay.R;

public class HomeActivity extends AppCompatActivity {


    private RelativeLayout topLyt;
    private RecyclerView recyclerView, recyclerViewRecommended, recyclerViewTopRatedProducts;
    private AdapterGridShopProductCard mAdapter;
    private AdapterStore adapterStore;
    private AdapterRatedProduct adapterRatedProduct;
    private List<Product> list;
    private List<Store> recommendedStoresList;
    private List<Product> topRatedProductsList;
    private DatabaseHelper databaseHelper;


    /* Slider */
    private ViewPager viewPager;
    private LinearLayout layout_dots;
    private AdapterImageSlider adapterImageSlider;
    private Runnable runnable = null;
    private Handler handler = new Handler();


    private static int[] array_image_place = {
            R.drawable.image_2,
            R.drawable.image_18,
            R.drawable.image_19,
            R.drawable.image_20,
            R.drawable.image_24,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initComponent();
        initToolbar(databaseHelper.getClientFullName());
    }


    private void initToolbar(String name) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Hi, " + name);
        Tools.setSystemBarColor(this, R.color.colorPrimary);

    }

    private void initComponent() {

        /* SLIDER */
        layout_dots = (LinearLayout) findViewById(R.id.layout_dots);
        viewPager = (ViewPager) findViewById(R.id.pager);
        adapterImageSlider = new AdapterImageSlider(this, new ArrayList<Image>());

        final List<Image> items = new ArrayList<>();
        for (int i = 0; i < array_image_place.length; i++) {
            Image obj = new Image();
            obj.image = array_image_place[i];
            obj.imageDrw = getResources().getDrawable(obj.image);

            items.add(obj);
        }

        adapterImageSlider.setItems(items);
        viewPager.setAdapter(adapterImageSlider);

        // displaying selected image first
        viewPager.setCurrentItem(0);
        addBottomDots(layout_dots, adapterImageSlider.getCount(), 0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int pos, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int pos) {
                addBottomDots(layout_dots, adapterImageSlider.getCount(), pos);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        startAutoSlider(adapterImageSlider.getCount());

        databaseHelper = new DatabaseHelper(this);
        System.out.println(databaseHelper.getClientFullName());
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(this, 8), true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        recyclerViewRecommended = (RecyclerView) findViewById(R.id.recyclerViewRecommended);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(
                HomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewRecommended.setLayoutManager(horizontalLayoutManager);

        recyclerViewTopRatedProducts = (RecyclerView) findViewById(R.id.recyclerViewTopRated);
        recyclerViewTopRatedProducts.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewTopRatedProducts.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(this, 8), true));
        recyclerViewTopRatedProducts.setHasFixedSize(true);
        recyclerViewTopRatedProducts.setNestedScrollingEnabled(false);


        /*set data and list adapter*/
        getProducts();
        getRecommendedStores();
        getTopRatedProduct();


        topLyt = (RelativeLayout) findViewById(R.id.top_lyt);
        NestedScrollView nested_content = (NestedScrollView) findViewById(R.id.nested_scroll_view);
        nested_content.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY < oldScrollY) { // up
                    animateSearchBar(false);
                }
                if (scrollY > oldScrollY) { // down
                    animateSearchBar(true);
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_store_account) {
            {
                startActivity(new Intent(HomeActivity.this, StoreAccountActivity.class));
            }
        } else if (item.getItemId() == R.id.action_shopping_cart) {
            startActivity(new Intent(HomeActivity.this, CartActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }

    public void clickAction(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_more_new_releases:
                startActivity(new Intent(HomeActivity.this, ProductListActivity.class));
                break;
            case R.id.btn_more_top_rated:
                startActivity(new Intent(HomeActivity.this, ProductListActivity.class));
                break;
        }
    }


    boolean isTopLytHide = false;

    private void animateSearchBar(final boolean hide) {
        if (isTopLytHide && hide || !isTopLytHide && !hide) return;
        isTopLytHide = hide;
        int moveY = hide ? -(2 * topLyt.getHeight()) : 0;
        topLyt.animate().translationY(moveY).setStartDelay(100).setDuration(300).start();
    }

    private void getProducts() {
        list = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                Constant.URL_PRODUCTS,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < 4; i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Product product = new Product(jsonObject);
                                list.add(product);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        System.out.println(list);
                        mAdapter = new AdapterGridShopProductCard(getApplicationContext(), list);
                        recyclerView.setAdapter(mAdapter);

                        // on item list clicked
                        mAdapter.setOnItemClickListener(new AdapterGridShopProductCard
                                .OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, Product obj, int position) {
                                Toast.makeText(HomeActivity.this, "Item" +
                                        obj.name + "clicked", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(HomeActivity.this,
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

    private void getRecommendedStores() {
        recommendedStoresList = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                Constant.URL_STORES,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < 10; i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Store store = new Store(jsonObject);
                                recommendedStoresList.add(store);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        System.out.println(recommendedStoresList);
                        adapterStore = new AdapterStore(getApplicationContext(),
                                recommendedStoresList);
                        recyclerViewRecommended.setAdapter(adapterStore);

                        // on item list clicked
                        adapterStore.setOnItemClickListener(new AdapterStore.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, Store obj, int position) {
                                Toast.makeText(HomeActivity.this, "Item" +
                                        obj.getStoreName() + "clicked", Toast.LENGTH_SHORT).show();
                               /* Intent intent = new Intent(HomeActivity.this,
                                        ProductDetailsActivity.class);
                                System.out.println();
                                intent.putExtra("id", String.valueOf(obj.id));
                                startActivity(intent);*/
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

    private void getTopRatedProduct() {
        topRatedProductsList = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                Constant.URL_PRODUCTS,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < 4; i++) {
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
                        recyclerViewTopRatedProducts.setAdapter(adapterRatedProduct);

                        // on item list clicked
                        adapterRatedProduct.setOnItemClickListener(new AdapterRatedProduct
                                .OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, Product obj, int position) {
                                Toast.makeText(HomeActivity.this, "Item" +
                                        obj.name + "clicked", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(HomeActivity.this,
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

    private void addBottomDots(LinearLayout layout_dots, int size, int current) {
        ImageView[] dots = new ImageView[size];

        layout_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle_outline);
            layout_dots.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current].setImageResource(R.drawable.shape_circle);
        }
    }


    private void startAutoSlider(final int count) {
        runnable = new Runnable() {
            @Override
            public void run() {
                int pos = viewPager.getCurrentItem();
                pos = pos + 1;
                if (pos >= count) pos = 0;
                viewPager.setCurrentItem(pos);
                handler.postDelayed(runnable, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);
    }

}
