package com.demo.zomato.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.demo.zomato.model.HeaderItem;
import com.demo.zomato.model.Item;
import com.demo.zomato.model.ListItem;
import com.demo.zomatosearch.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jyoti on 8/11/2018.
 */

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<ListItem> list = new ArrayList<>();
    private final Context mContext;
    private LayoutInflater mInflater;

    public SearchAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {

            case ListItem.TYPE_HEADER:
                View v1 = mInflater.inflate(R.layout.header_item, parent,
                        false);
                viewHolder = new HeaderViewHolder(v1);
                break;

            case ListItem.TYPE_ITEM:
                View v2 = mInflater.inflate(R.layout.list_item, parent, false);
                viewHolder = new ItemHolder(v2);
                break;
        }

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {

            case ListItem.TYPE_HEADER:
                HeaderItem hItem
                        = (HeaderItem) list.get(position);
                HeaderViewHolder hViewHolder
                        = (HeaderViewHolder) holder;

                hViewHolder.txt_header.setText(hItem.getCuisine());

                break;

            case ListItem.TYPE_ITEM:
                Item item = (Item) list.get(position);
                ItemHolder itemViewHolder = (ItemHolder) holder;
                itemViewHolder.name.setText(item.getRestaurant_().getName());
                itemViewHolder.rating.setText(item.getRestaurant_().getUserRating().getAggregateRating());
                itemViewHolder.avgPrice.setText(mContext.getResources().getString(R.string.price,
                        Integer.toString(item.getRestaurant_().getAverageCostForTwo())));
                Glide.with(mContext).load(item.getRestaurant_().getFeaturedImage()).error(R.mipmap.ic_launcher).into(itemViewHolder.icon);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public void update(List<ListItem> newList) {
        int old_size = list.size();
        list.addAll(newList);
        notifyItemRangeInserted(old_size, list.size());
    }

    static class ItemHolder extends RecyclerView.ViewHolder {
        TextView name, rating, avgPrice;
        ImageView icon;

        public ItemHolder(View viewItem) {
            super(viewItem);
            name = (TextView) itemView.findViewById(R.id.name);
            rating = (TextView) itemView.findViewById(R.id.rating);
            avgPrice = (TextView) itemView.findViewById(R.id.avgPrice);
            icon = (ImageView) itemView.findViewById(R.id.icon);
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView txt_header;

        HeaderViewHolder(View itemView) {
            super(itemView);
            txt_header = (TextView) itemView.findViewById(R.id.cuisineName);
        }
    }
}
