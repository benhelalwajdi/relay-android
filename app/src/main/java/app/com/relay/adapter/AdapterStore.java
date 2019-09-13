package app.com.relay.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import app.com.relay.R;
import app.com.relay.model.Store;

public class AdapterStore extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Store> items = new ArrayList<>();


    private Context ctx;
    private AdapterStore.OnItemClickListener mOnItemClickListener;

    public AdapterStore(Context ctx, List<Store> items) {
        this.ctx = ctx;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommended_item,
                parent, false);
        return new AdapterStore.ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Store store = items.get(position);
        AdapterStore.ViewHolder viewHolder = (AdapterStore.ViewHolder) holder;

        viewHolder.title.setText(store.getStoreName());


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

    public interface OnItemClickListener {
        void onItemClick(View view, Store obj, int position);
    }

    public void setOnItemClickListener(final AdapterStore.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;
        public LinearLayout lyt_parent;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.txt_store_name);
            image = (ImageView) v.findViewById(R.id.store_image);
            lyt_parent = (LinearLayout) v.findViewById(R.id.lyt_parent);
        }
    }
}
