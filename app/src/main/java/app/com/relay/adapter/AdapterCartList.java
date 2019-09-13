package app.com.relay.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import app.com.relay.R;
import app.com.relay.model.Product;
import app.com.relay.utils.CurrencyFormat;

public class AdapterCartList  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Product> items = new ArrayList<>();
    private final TypedValue mTypedValue = new TypedValue();


    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Product obj, int position);
    }

    public void setOnItemClickListener(final AdapterCartList.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView quantity;
        public TextView price;
        public TextView total;
        public ImageView image;
        public LinearLayout lyt_parent;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.txt_product_name);
            quantity = (TextView) v.findViewById(R.id.txt_quantity);
            price = (TextView) v.findViewById(R.id.txt_price);
            image = (ImageView) v.findViewById(R.id.image);
            lyt_parent = (LinearLayout) v.findViewById(R.id.lyt_parent);
        }
    }

    public AdapterCartList(Context ctx, List<Product> items) {
        this.ctx = ctx;
        this.items = items;
        ctx.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Product product = items.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.title.setText(product.getName());
        String quantityString = product.getQuantity() + " Items";
        viewHolder.quantity.setText(quantityString);
        String priceString = CurrencyFormat.format(product.getPrice());
        viewHolder.price.setText(priceString);
        /*
        viewHolder.title.setText(p.getName());
        viewHolder.category.setText(p.getCategory());
        viewHolder.total.setText(p.getTotal()+" X");
        viewHolder.price.setText(p.getStrPrice());
       */

       /* Picasso.with(ctx).load(p.getImg())
                .resize(100,100)
                .into(holder.image);
       */
        viewHolder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, items.get(position), position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
