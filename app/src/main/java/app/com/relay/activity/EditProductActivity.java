package app.com.relay.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import app.com.relay.R;
import app.com.relay.model.Product;
import app.com.relay.service.ServiceProduct;
import app.com.relay.utils.Constant;

public class EditProductActivity extends AppCompatActivity {

    private HashMap<String, String> product ;
    Product prod = new Product();
    private Product produit ;
    private ServiceProduct serviceProduct = new ServiceProduct();
    FloatingActionButton imgsel ;
    String path;
    String ImageName ;
    ImageView imageGallery ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        Bundle extras = getIntent().getExtras();
        product = (HashMap<String, String>) extras.get("prod");
        System.out.println(product.get("id"));
        prod.setId(product.get("id"));
        prod.setPrice(product.get("price"));
        prod.setSize(product.get("size"));
        prod.setQuantity(product.get("quantity"));
        prod.setDescription(product.get("description"));
        prod.setImage(product.get("image"));
        prod.setName(product.get("name"));
        prod.setDate(product.get("date"));
        prod.toString();
        imgsel = (FloatingActionButton) findViewById(R.id.imgse2);
        imgsel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fintent = new Intent(Intent.ACTION_GET_CONTENT);
                fintent.setType("image/jpeg");
                try {
                    startActivityForResult(fintent, 100);
                } catch (ActivityNotFoundException e) {
                    e.getMessage();
                }
            }
        });
        setData(prod);
        initToolbar();
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
            AppCompatEditText name = (AppCompatEditText) findViewById(R.id.nameProdEdit);
            AppCompatEditText description = (AppCompatEditText) findViewById(R.id.decrProdEdit);
            AppCompatEditText price = (AppCompatEditText) findViewById(R.id.priceProdEdit);
            Spinner spinner = (Spinner) findViewById(R.id.spinnerEdit);
            AppCompatEditText quantity = (AppCompatEditText) findViewById(R.id.quntityProdEdit);
            String image = prod.getImage();
            // create new product for set in
            Product  product =  new Product();
            product.setId(prod.getId());


            if (quantity.getText().toString().contains(" ")) {
                quantity.setError("No Spaces Allowed");
                Toast.makeText(getApplicationContext(), "No Spaces Allowed", Toast.LENGTH_LONG).show();
            }else{
                try {
                    int num = Integer.parseInt(String.valueOf(quantity.getText()));
                    product.setQuantity(String.valueOf(quantity.getText()));
                } catch (NumberFormatException e) {
                    quantity.setError("Value No Allowed");
                    Toast.makeText(getApplicationContext(), "Value No Allowed", Toast.LENGTH_LONG).show();
                }
            }
            if (price.getText().toString().contains(" ")) {
                price.setError("No Spaces Allowed");
                Toast.makeText(getApplicationContext(), "No Spaces Allowed", Toast.LENGTH_LONG).show();
            }
            else{
                try {
                    int num = Integer.parseInt(String.valueOf(price.getText()));
                    product.setPrice(String.valueOf(price.getText()));
                } catch (NumberFormatException e) {
                    price.setError("Value No Allowed");
                    Toast.makeText(getApplicationContext(), "Value No Allowed", Toast.LENGTH_LONG).show();
                }
            }

            product.setSize(String.valueOf(spinner.getSelectedItem()));
            product.setName(String.valueOf(name.getText()));
            product.setDescription(String.valueOf(name.getText()));
            //product.setPrice(String.valueOf(price.getText()));
            // edit the product
            upload(product);
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


    protected void upload(final Product product){
        if(path != null) {
            File f = new File(path);
            ImageName = f.getName();
            Date currentTime = Calendar.getInstance().getTime();
            System.out.println(f.getParent());
            System.out.println("Nom d image : " + ImageName);
            File to = new File(f.getParent(), ImageName);
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
                                System.out.println("Json Image : " + jobj.getString("image"));
                                product.setImage(jobj.getString("image"));
                                System.out.println("the product in update " + product.toString());
                                System.out.println("the url for update " + Constant.URL_PRODUCTS + "update_product");
                                System.out.println("the id of product for the update " + product.getId());
                                serviceProduct.editProduct(Constant.URL_PRODUCTS + "update_product", product, String.valueOf(product.getId()), getApplicationContext());
                                startActivity(new Intent(EditProductActivity.this, StoreAccountActivity.class));
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
        }else{
            System.out.println("Onclick here with out pic "+ImageName  );
            ImageName = ImageName.replace(Constant.URL_IMAGE,"");
            product.setImage(ImageName);
            serviceProduct.editProduct(Constant.URL_PRODUCTS + "update_product", product, String.valueOf(product.getId()), getApplicationContext());
            startActivity(new Intent(EditProductActivity.this, StoreAccountActivity.class));
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
                    imageGallery.setImageURI(data.getData());
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



    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("General information");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void setData(Object produit){
        Product p = (Product) produit;
        imageGallery = (ImageView) findViewById(R.id.imageProdEdit);
        AppCompatEditText prod_name = (AppCompatEditText) findViewById(R.id.nameProdEdit);
        AppCompatEditText price = (AppCompatEditText) findViewById(R.id.priceProdEdit);
        AppCompatEditText quantity = (AppCompatEditText) findViewById(R.id.quntityProdEdit);
        AppCompatEditText descProd = (AppCompatEditText) findViewById(R.id.decrProdEdit);
        prod_name.setText(p.getName());
        descProd.setText(p.getDescription());
        price.setText(p.getPrice());
        ImageName = p.getImage();
        quantity.setText(p.getQuantity());
        Picasso.with(this).load(p.getImage()).resize(200, 200).into(imageGallery);
    }

}
