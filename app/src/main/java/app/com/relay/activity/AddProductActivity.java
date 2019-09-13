package app.com.relay.activity;

import android.content.ActivityNotFoundException;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.koushikdutta.async.future.FutureCallback;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import app.com.relay.R;
import app.com.relay.utils.Constant;

public class AddProductActivity extends AppCompatActivity {

    FloatingActionButton imgsel ;
    String path;
    String ImageName ;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        img = (ImageView)findViewById(R.id.img);
        imgsel = (FloatingActionButton) findViewById(R.id.selimg);
        imgsel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fintent = new Intent(Intent.ACTION_GET_CONTENT);
                fintent.setType("image/jpeg");
                try {
                    startActivityForResult(fintent, 100);
                   } catch (ActivityNotFoundException e) {

                }
            }
        });
        initToolbar();
    }

    protected void upload(){
        if(path!= null){
        File f = new File(path);
        ImageName = f.getName();
        Date currentTime = Calendar.getInstance().getTime();
        System.out.println(f.getParent());
        System.out.println("Nom d image : "+ImageName);
        File to = new File(f.getParent(),ImageName);
        f.renameTo(to);
            com.koushikdutta.async.future.Future uploading = com.koushikdutta.ion.Ion.with(getApplicationContext())
                    .load(Constant.URL_UPLOAD_IMAGE)
                    .setMultipartFile("profile", f)
                    .asString()
                    .withResponse()
                    .setCallback(new FutureCallback<com.koushikdutta.ion.Response<String>>() {
                        @Override
                        public void onCompleted(Exception e, com.koushikdutta.ion.Response<String> result) {
                            try {
                                JSONObject jobj = new JSONObject(result.getResult());
                                System.out.println("Json Image : "+     jobj.getString("image"));
                                addProduct(Constant.URL_PRODUCTS+"create_product",jobj.getString("image"));
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
        }else{
            startActivity(new Intent(AddProductActivity.this, StoreAccountActivity.class));
            Toast.makeText(getApplicationContext(),"The product with image is the perfect image",Toast.LENGTH_LONG).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;
        switch (requestCode) {
            case 100:
                if (resultCode == RESULT_OK) {
                    path = getPathFromURI(data.getData());
                    System.out.println("path : "+path);
                    img.setImageURI(data.getData());
                }
        }
    }
    private String getPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.action_done) {
            AppCompatEditText name = (AppCompatEditText) findViewById(R.id.nameProdAdd);
            AppCompatEditText description = (AppCompatEditText) findViewById(R.id.decrProdAdd);
            AppCompatEditText price = (AppCompatEditText) findViewById(R.id.priceProdAdd);
            Spinner spinner = (Spinner) findViewById(R.id.spinner);
            AppCompatEditText quantity = (AppCompatEditText) findViewById(R.id.quntityProdAdd);
            upload();
            startActivity(new Intent(AddProductActivity.this, StoreAccountActivity.class));
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("General information");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void addProduct(String url, final String image){
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
                AppCompatEditText name = (AppCompatEditText) findViewById(R.id.nameProdAdd);
                AppCompatEditText description = (AppCompatEditText) findViewById(R.id.decrProdAdd);
                AppCompatEditText price = (AppCompatEditText) findViewById(R.id.priceProdAdd);
                Spinner spinner = (Spinner) findViewById(R.id.spinner);
                AppCompatEditText quantity = (AppCompatEditText) findViewById(R.id.quntityProdAdd);
                params.put("name", String.valueOf(name.getText()));
                params.put("description", String.valueOf(description.getText()));
                params.put("price", String.valueOf(price.getText()));
                params.put("quantity", String.valueOf(quantity.getText()));
                params.put("image", image);
                params.put("size", String.valueOf(spinner.getSelectedItem()));
                params.put("store_id", "54");
                return params;
            }
        };
        queue.add(postRequest);
    }

}
