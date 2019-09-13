package app.com.relay.activity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import app.com.relay.R;
import app.com.relay.adapter.AdapterCartList;
import app.com.relay.adapter.AdapterGridShopProductCard;
import app.com.relay.model.CartItem;
import app.com.relay.model.Product;
import app.com.relay.utils.CurrencyFormat;
import app.com.relay.utils.DatabaseHelper;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterCartList mAdapter;
    private LinearLayout lyt_notfound;
    private Toolbar toolbar;
    private DatabaseHelper databaseHelper;
    private ArrayList<Product> listData;
    private TextView txtTotalPrice, txtTotalItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initToolbar();
        initComponent();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Shopping Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void initComponent() {
        databaseHelper = new DatabaseHelper(this);
        listData = new ArrayList<>(databaseHelper.getProductList());

        txtTotalPrice = (TextView) findViewById(R.id.txt_total_price);
        txtTotalItems = (TextView) findViewById(R.id.txt_total_item);
        lyt_notfound = (LinearLayout) findViewById(R.id.lyt_notfound);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));



        //set data and list adapter
        mAdapter = new AdapterCartList(this, listData);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new AdapterCartList.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Product obj, int position) {
                dialogCartAction(obj, position);
            }
        });

        checkIfCartIsEmpty();

      /*  if (mAdapter.getItemCount() == 0) {
            lyt_notfound.setVisibility(View.VISIBLE);
        } else {
            lyt_notfound.setVisibility(View.GONE);
        }*/

        setTotalPrice();
        setTotalItems();

    }


    private void dialogCartAction(final Product product, final int position) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_cart_option);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        ((TextView) dialog.findViewById(R.id.title)).setText(product.getName());
        final TextView qty = (TextView) dialog.findViewById(R.id.quantity);
        qty.setText(product.getQuantity());
        final TextView title = (TextView) dialog.findViewById(R.id.title);
        title.setText(product.getName());
        ((ImageView) dialog.findViewById(R.id.img_decrease)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.valueOf(product.getQuantity()) > 1) {
                    qty.setText(String.valueOf(Integer.valueOf(qty.getText().toString()) - 1));
                }
            }
        });
        ((ImageView) dialog.findViewById(R.id.img_increase)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qty.setText(String.valueOf(Integer.valueOf(qty.getText().toString()) + 1));
            }
        });
        ((Button) dialog.findViewById(R.id.bt_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listData.get(listData.indexOf(product)).setQuantity(qty.getText().toString());
                mAdapter.notifyDataSetChanged();
                databaseHelper.updateQuantity(listData.get(listData.indexOf(product)));
                setTotalPrice();
                setTotalItems();
                dialog.dismiss();
            }
        });
        ((Button) dialog.findViewById(R.id.bt_remove)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listData.remove(listData.get(listData.indexOf(product)));
                mAdapter.notifyDataSetChanged();
                checkIfCartIsEmpty();
                databaseHelper.deleteProductFromCart(product.getId());
                setTotalPrice();
                setTotalItems();
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void clickAction(View view){
        int id = view.getId();
        switch(id){
            case R.id.bt_checkout:
                if (!listData.isEmpty()) {
                    startActivity(new Intent(CartActivity.this, CheckoutActivity.class));
                }
                break;
        }
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



    private void setTotalPrice(){
        float total = 0;
        for (Product p : listData){
            total += Float.valueOf(p.price)* Integer.valueOf(p.quantity);
        }

        txtTotalPrice.setText(CurrencyFormat.format(String.valueOf(total)));
    }

    private void setTotalItems(){
        txtTotalItems.setText(listData.size() + " Item(s)");
    }

    private  void checkIfCartIsEmpty(){
        if (mAdapter.getItemCount() == 0) {
            lyt_notfound.setVisibility(View.VISIBLE);
        } else {
            lyt_notfound.setVisibility(View.GONE);
        }
    }
}
