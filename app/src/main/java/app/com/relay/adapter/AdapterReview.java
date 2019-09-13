package app.com.relay.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.com.relay.R;
import app.com.relay.model.ReviewProduct;

public class AdapterReview extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ReviewProduct> items = new ArrayList<>();


    private Context ctx;

    public AdapterReview(Context ctx, List<ReviewProduct> items) {
        this.ctx = ctx;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review,
                parent, false);
        return new AdapterReview.ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ReviewProduct reviewProduct = items.get(position);
        AdapterReview.ViewHolder viewHolder = (AdapterReview.ViewHolder) holder;

        String fullName = reviewProduct.getClient().getFirstName() + " " +
                reviewProduct.getClient().getLastName();
        viewHolder.txtClientName.setText(fullName);
        viewHolder.txtComment.setText(reviewProduct.getComment());
        viewHolder.ratingBar.setRating(reviewProduct.getRating());

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
        public TextView txtClientName, txtComment;
        public RatingBar ratingBar;

        public ViewHolder(View v) {
            super(v);
            txtClientName = (TextView) v.findViewById(R.id.txt_client_name);
            txtComment = (TextView) v.findViewById(R.id.txt_comment);
            ratingBar = (RatingBar) v.findViewById(R.id.rating_bar);
        }
    }
}
