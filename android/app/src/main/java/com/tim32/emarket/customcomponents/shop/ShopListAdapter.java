package com.tim32.emarket.customcomponents.shop;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import com.tim32.emarket.apiclients.dto.Shop;
import com.tim32.emarket.customcomponents.ImageListItemView;
import com.tim32.emarket.customcomponents.ImageListItemView_;
import com.tim32.emarket.service.ImageService;

import java.util.ArrayList;
import java.util.List;

public class ShopListAdapter extends BaseAdapter implements Filterable {

    private List<Shop> shops;
    private List<Shop> shopsFiltered;
    private ImageService imageService;

    private final Context context;

    public ShopListAdapter(Context context, ImageService imageService) {
        this.context = context;
        this.imageService = imageService;
        shops = new ArrayList<>();
        shopsFiltered = new ArrayList<>(shops);
    }

    @Override
    public int getCount() {
        return shops.size();
    }

    @Override
    public Object getItem(int position) {
        return shops.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ((Shop) getItem(position)).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageListItemView imageListItemView;
        if (convertView == null) {
            imageListItemView = ImageListItemView_.build(context);
        } else {
            imageListItemView = (ImageListItemView) convertView;
        }

        Shop shop = (Shop) getItem(position);
        imageListItemView.bind(shop.getName(), imageService.getShopImageUrl(shop.getId()), shop.getId());
        return imageListItemView;
    }

    public void setItems(List<Shop> shops) {
        this.shops = shops;
        this.shopsFiltered = new ArrayList<>(this.shops);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<Shop> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(shopsFiltered);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Shop item : shopsFiltered) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            shops.clear();
            shops.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
