package app.com.relay.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import app.com.relay.R;
import app.com.relay.adapter.AdapterCartList;
import app.com.relay.adapter.AdapterCheckout;
import app.com.relay.model.Client;
import app.com.relay.model.Product;
import app.com.relay.utils.CurrencyFormat;
import app.com.relay.utils.DatabaseHelper;

public class CheckoutActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterCheckout adapterCheckout;
    private DatabaseHelper databaseHelper;
    private TextView txtTotalPrice, txtAddress, txtPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        initToolbar();
        initComponent();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Checkout");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent(){
        databaseHelper = new DatabaseHelper(this);
        txtTotalPrice = (TextView) findViewById(R.id.txt_price);
        txtAddress = (TextView) findViewById(R.id.txt_address);
        txtPhoneNumber = (TextView) findViewById(R.id.txt_phone_number);

        txtAddress.setText(databaseHelper.getCurrentClient().getAddress());
        txtPhoneNumber.setText(databaseHelper.getCurrentClient().getPhoneNumber());

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        //set data and list adapter
        adapterCheckout = new AdapterCheckout(this, databaseHelper.getProductList());
        recyclerView.setAdapter(adapterCheckout);

        setTotalPrice();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }


    private void setTotalPrice(){
        float total = 0;
        for (Product p : databaseHelper.getProductList()){
            total += Float.valueOf(p.price)* Integer.valueOf(p.quantity);
        }

        txtTotalPrice.setText(CurrencyFormat.format(String.valueOf(total)));
    }
}

