package app.com.relay.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import app.com.relay.R;
import app.com.relay.model.Product;
import app.com.relay.utils.CurrencyFormat;

public class AdapterCheckout extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Product> items = new ArrayList<>();
    private Context ctx;

    public AdapterCheckout(Context ctx, List<Product> items) {
        this.ctx = ctx;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkout_item, parent, false);
        return new AdapterCheckout.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Product product = items.get(position);
        AdapterCheckout.ViewHolder viewHolder = (AdapterCheckout.ViewHolder) holder;

        viewHolder.productName.setText(product.getName());
        String quantityString = "QTY : " + product.getQuantity();
        viewHolder.quantity.setText(quantityString);
        double totalPrice = Double.valueOf(product.getPrice()) *
                Integer.valueOf(product.getQuantity());
        String priceString = CurrencyFormat.format(String.valueOf(totalPrice));
        viewHolder.price.setText(priceString);


       /* Picasso.with(ctx).load(p.getImg())
                .resize(100,100)
                .into(holder.image);
       */
           }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView productName;
        public TextView quantity;
        public TextView price;
        public ImageView image;
        public LinearLayout lyt_parent;

        public ViewHolder(View v) {
            super(v);
            productName = (TextView) v.findViewById(R.id.txt_product_name);
            quantity = (TextView) v.findViewById(R.id.txt_quantity);
            price = (TextView) v.findViewById(R.id.txt_price);
            image = (ImageView) v.findViewById(R.id.image);
            lyt_parent = (LinearLayout) v.findViewById(R.id.lyt_parent);
        }
    }
}


