package app.com.relay.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.com.relay.R;
import app.com.relay.activity.StoreProductActivity;
import app.com.relay.model.Product;
import app.com.relay.model.Review;

public class AdapterListReview extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Review> items = new ArrayList<>();

    private Context ctx;
    private String image;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Product obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterListReview(Context context, List<Review> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView comment;
        public AppCompatRatingBar rating;


        @SuppressLint("WrongViewCast")
        public OriginalViewHolder(View v) {
            super(v);
            rating = (AppCompatRatingBar) v.findViewById(R.id.rating);
            comment = (TextView) v.findViewById(R.id.comment);
           }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_review, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final HashMap<String, String> reviewMap = new HashMap<>();
            OriginalViewHolder view = (OriginalViewHolder) holder;
            Review p = items.get(position);
            reviewMap.put("id", p.getId());
            reviewMap.put("comment",p.getComment());
            reviewMap.put("rating", p.getRating());
            System.out.println("line 75 "+p.toString());
            view.comment.setText(p.getComment());
            view.rating.setRating(Float.parseFloat(p.getRating()));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}