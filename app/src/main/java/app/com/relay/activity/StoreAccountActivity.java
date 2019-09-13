package app.com.relay.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.relay.R;
import app.com.relay.adapter.AdapterListBasic;
import app.com.relay.adapter.AdapterListProduct;
import app.com.relay.model.Order;
import app.com.relay.model.Product;
import app.com.relay.service.NotificationService;
import app.com.relay.service.ServiceProduct;
import app.com.relay.utils.Constant;
import app.com.relay.utils.DataGenerator;
import app.com.relay.utils.HttpHandler;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;
import static java.lang.Thread.sleep;

public class StoreAccountActivity extends AppCompatActivity {

    private String idStore;
    private Intent intent;
    private String Image;
    private JSONArray jsonArray;
    private int number;
    View parent_view;
    private static int numbreOfOrder;
    int sizeList;
    private RequestQueue queuee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_account);
        intent = getIntent();
        parent_view = findViewById(android.R.id.content);
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setDataStore(getApplicationContext());
                pullToRefresh.setRefreshing(false);
            }
        });
        SharedPreferences _sharedPrefs;
        SharedPreferences.Editor _prefsEditor;

        SharedPreferences prfs = getSharedPreferences("AUTHENTICATION_FILE_NAME",
                Context.MODE_PRIVATE);
        _prefsEditor = prfs.edit();
        _prefsEditor.putString("USER_TYPE", "STORE");
        _prefsEditor.apply();


        String id = "54"; //prfs.getString("ID_USER", "");
        idStore = id;
        System.out.println(idStore);
        InitToolbar();
        ActionButton();
        setDataStore(getApplicationContext());
        // HashMap<String, String> storeMap = new HashMap<>();
        // getAllProducts(getApplicationContext(),storeMap);
        //  threadNotif();
        /**/
    }

    /* JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    URL,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Product p = new Product(response);
                            initiateProduct(p);
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
            System.out.println(product.toString());*/
    public void setDataStore(Context context) {
        String URL = Constant.URL_STORES + "54";
        System.out.println(URL);
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    HashMap<String, String> storeMap = new HashMap<>();

                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<HashMap<String, String>> list2 = new ArrayList<>();
                        System.out.println(response + "line 145");
                        // product = new Product(jsonObject);Log.e("Respence Json: ", String.valueOf(jsonObject));
                        try {
                            Log.e("Respence Store: ", response.getString("store_name"));

                            storeMap.put("store_name", response.getString("store_name"));
                            storeMap.put("phone_number", response.getString("phone_number"));
                            storeMap.put("address", response.getString("address"));
                            storeMap.put("mail", response.getString("mail"));
                            storeMap.put("image", Constant.URL_IMAGE + response.getString("image"));
                            storeMap.put("user_type", response.getString("user_type"));
                            storeMap.put("store_type", response.getString("store_type"));
                            storeMap.put("id", String.valueOf(response.getString("id")));
                            //System.out.println("Size of list is  : "+ list.size());
                            Image = response.getString("image");
                            list2.add(storeMap);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        accountData(storeMap);
                        getAllProducts(getApplicationContext(), storeMap);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", "Ligne 1 Oups Something went wrong !");
                    }
                }
        );
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsonArrayRequest);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //threadNotif();
    }

    private void addImagesToThegallery(ArrayList<HashMap<String, String>> list) {
        LinearLayout imageGallery = (LinearLayout) findViewById(R.id.rootContainer);
        if (((LinearLayout) imageGallery).getChildCount() > (list.size() + 1)) {
            for (int i = 1; i < ((LinearLayout) imageGallery).getChildCount(); i++) {
                (
                        (LinearLayout) imageGallery).removeViewAt(i);
                System.out.println(i);
            }
        }
        TextView productNumber = (TextView) findViewById(R.id.prod_number);
        productNumber.setText(String.valueOf(list.size()));

        for (HashMap<String, String> product : list) {
            System.out.println("image " + product.get("image"));
            imageGallery.addView(getImageView(product));
        }
    }

    private View getImageView(final HashMap<String, String> Thisproduct) {
        ImageView imageView = new ImageView(getApplicationContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 10, 0);
        imageView.setLayoutParams(lp);
        Picasso.with(this).load(Thisproduct.get("image")).resize(200, 200).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("this is pic", Thisproduct.get("image"));
                System.out.println("this on click " + Thisproduct);
                Intent i = new Intent(StoreAccountActivity.this, StoreProductActivity.class);
                i.putExtra("prod", Thisproduct);
                startActivity(i);
            }
        });
        return imageView;
    }


    //Action Button
    private void ActionButton() {
        //The View Element Used getById
        FloatingActionButton AddProduct = findViewById(R.id.AddProductStore);
        //OnClick Add Product we directed to the AddProductActivity
        AddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StoreAccountActivity.this, AddProductActivity.class);
                startActivity(i);
            }
        });
    }

    //toolbar
    private void InitToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_store, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.IconProfilStorePrameter:
                Intent intent = new Intent(this, EditProfile.class);
                this.startActivity(intent);
                break;
            case R.id.IconOrderStorePrameter:
                Intent intentOrder = new Intent(this, OrderActivity.class);
                this.startActivity(intentOrder);
                break;
            case R.id.Iconliv:
                Intent intentLiv = new Intent(this, DelivererHomePageActivity.class);
                this.startActivity(intentLiv);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    public Product product;

    public void getAllProducts(Context context, final HashMap<String, String> storeMap) {
        String URL = Constant.URL_PRODUCTS + "store/" + idStore;
        System.out.println(URL);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    HashMap<String, String> productMap;

                    @Override
                    public void onResponse(JSONArray response) {

                        ArrayList<HashMap<String, String>> list = new ArrayList<>();
                        if (list.size() != 0) {
                            System.out.println("size list is before the response " + list.size());
                        }
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject jsonObject = response.getJSONObject(i);
                                // product = new Product(jsonObject);
                                Log.e("Respence Json: ", String.valueOf(jsonObject));
                                Log.e("Respence Product: ", jsonObject.getString("name"));
                                System.out.println("size list in line 192 : " + list.size());
                                HashMap<String, String> productMap = new HashMap<>();
                                productMap.put("name", jsonObject.getString("name"));
                                productMap.put("description", jsonObject.getString("description"));
                                productMap.put("quantity", jsonObject.getString("quantity"));
                                productMap.put("size", jsonObject.getString("size"));
                                productMap.put("image", Constant.URL_IMAGE + jsonObject.getString("image"));
                                productMap.put("price", jsonObject.getString("price"));
                                // productMap.put("date", changedate(String.valueOf(jsonObject.get("date"))));
                                productMap.put("id", String.valueOf(jsonObject.getString("id")));
                                list.add(productMap);
                                //lProd = getlist();
                                System.out.println("Size of list is  : " + list.size());

                                System.out.println("list line 207: " + list.toString());
                                initComponent(list, storeMap);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", " Oups Something went wrong !");
                    }
                }
        );

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsonArrayRequest);

    }

    String changedate(String date) {
        date = date.replace("-", ":");
        date = date.replace("T", " ");
        date = date.replace(".000Z", " ");
        System.out.println("date line 229 : " + date);
        return date;
    }


    private void accountData(HashMap<String, String> storeMap) {
        CircularImageView imageView = (CircularImageView) findViewById(R.id.imageAcountStore);
        TextView adress = (TextView) findViewById(R.id.addresseStore);
        TextView name = (TextView) findViewById(R.id.nameStoreAcount);
        System.out.println("Image Account store " + storeMap.get("image"));
        String url_image = storeMap.get("image");
        System.out.println("Image of Store" + url_image);
        Picasso.with(this).load(url_image).resize(200, 200).into(imageView);
        System.out.println(storeMap.get("address"));
        adress.setText(storeMap.get("address"));
        name.setText(storeMap.get("store_name"));
    }

    private void initComponent(ArrayList<HashMap<String, String>> list, HashMap<String, String> storeMap) {
        System.out.println("list line 286: " + list.toString());
        RecyclerView precyclerView;
        AdapterListProduct pAdapter;
        precyclerView = (RecyclerView) findViewById(R.id.recyclerViewStore);
        precyclerView.setLayoutManager(new LinearLayoutManager(this));
        precyclerView.setHasFixedSize(true);
        List<Product> items = DataGenerator.getProd(this, list, storeMap);
        System.out.println("items line 291: " + items.toString());

        //set data and list adapter
        pAdapter = new AdapterListProduct(getApplicationContext(), items, Image);
        precyclerView.setAdapter(pAdapter);
        accountElement(list);
        //threadNotif();
    }

    public void accountElement(ArrayList<HashMap<String, String>> list) {
        System.out.println("size of list is : " + list.size());
        System.out.println("list of hash product : " + list);
        addImagesToThegallery(list);
    }

    @Override
    protected void onStop() {
        super.onStop();
     //   startService(new Intent(this, NotificationService.class));
    }

    public void threadNotif() {
        final Handler handler = new Handler();
        final NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext());
        final Runnable r = new Runnable() {
            public void run() {
                int num = number();
                System.out.println("hello thread " + numbreOfOrder);
                System.out.println("hello thread 2 " + number);
                if ((numbreOfOrder - number) != 0) {
                    Intent intent = new Intent(getApplication(), OrderActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplication(), 0, intent, 0);
                    mBuilder.setContentIntent(pendingIntent);
                    mBuilder.setSmallIcon(R.drawable.about_icon_store_activity);
                    mBuilder.setContentTitle(numbreOfOrder + " new order ");
                    mBuilder.setContentText("You have new order check it please with references ");
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(001, mBuilder.build());
                }
                numbreOfOrder = number;
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(r, 1000);
    }

    private int number() {
        final int[] i = {0};
        String URL = Constant.URL_ORDER_STORE + idStore;
        System.out.println(URL);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    HashMap<String, String> storeMap = new HashMap<>();

                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<HashMap<String, String>> list2 = new ArrayList<>();
                        number = response.length();
                        System.out.println("nombre" + number);
                        System.out.println("nombreoforder " + numbreOfOrder);
                        if (numbreOfOrder < number) {
                            numbreOfOrder = number - numbreOfOrder;
                        }
                        if (numbreOfOrder != 0) {
                            jsonArray = response;
                        }
                        System.out.println("nombre of order " + numbreOfOrder);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", "Oups Something went wrong !");
                    }
                }
        );
        if (queuee == null) {
            queuee = Volley.newRequestQueue(getApplicationContext());
        }
        queuee.add(jsonArrayRequest);
        return number;

    }
}

