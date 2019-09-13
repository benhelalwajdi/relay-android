package app.com.relay.adapter;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.com.relay.R;
import app.com.relay.activity.StoreAccountActivity;
import app.com.relay.activity.StoreProductActivity;
import app.com.relay.model.Product;
import app.com.relay.model.Product;
import app.com.relay.utils.Tools;

public class AdapterListProduct extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Product> items = new ArrayList<>();

    private Context ctx;
    private String image;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Product obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterListProduct(Context context, List<Product> items, String image) {
        this.items = items;
        this.image = image;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView date;
        public ImageView storePic;
        public ImageView prodPic;


        public OriginalViewHolder(View v) {
            super(v);
            storePic = (ImageView) v.findViewById(R.id.storePic);
            prodPic = (ImageView) v.findViewById(R.id.prodPic);
            name = (TextView) v.findViewById(R.id.nameProduct);
            date = (TextView) v.findViewById(R.id.dateProductStore);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_product, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final HashMap<String, String> productMap = new HashMap<>();
            OriginalViewHolder view = (OriginalViewHolder) holder;
            Product p = items.get(position);
            productMap.put("name", p.getName());
            productMap.put("description", p.getDescription());
            productMap.put("quantity", p.getQuantity());
            productMap.put("size", p.getSize());
            productMap.put("image", p.getImage());
            productMap.put("price", p.getPrice());
            productMap.put("date", p.getDate());
            productMap.put("id", String.valueOf(p.getId()));
            System.out.println("line 75 " + p.toString());
            view.name.setText(p.getName());
            view.date.setText(p.getDate());
            Picasso.with(ctx).load(p.getImage()).resize(200, 200).into(view.prodPic);
            try {
                Picasso.with(ctx).load(image).resize(200, 200).into(view.storePic);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                Toast.makeText(this.ctx,"Please add pic to your store", Toast.LENGTH_LONG).show();
            }
            view.prodPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(view.getContext(), StoreProductActivity.class);
                    i.putExtra("prod", productMap);
                    view.getContext().startActivity(i);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}