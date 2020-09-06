package com.tim32.emarket.customcomponents.product.rowlist;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.tim32.emarket.apiclients.dto.Product;
import com.tim32.emarket.service.ImageProvider;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class ProductRowAdapter extends BaseAdapter {

    private final Context context;

    private final ImageProvider imageProvider;

    private List<Product> products;

    public ProductRowAdapter(Context context, ImageProvider imageProvider) {
        this.context = context;
        this.imageProvider = imageProvider;
    }

    @Override
    public int getCount() {
        if (CollectionUtils.isEmpty(products)) {
            return 0;
        }
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Product product = products.get(position);
        if (convertView instanceof ProductRowView) {
            ((ProductRowView) convertView).bind(product, imageProvider.getProductImage(product.getId()));
            return convertView;
        }
        ProductRowView productRowView = ProductRowView_.build(context, this);
        productRowView.bind(product, imageProvider.getProductImage(product.getId()));
        return productRowView;
    }

    public void removeItem(Product product) {
        products.remove(product);
        notifyDataSetChanged();
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    public void addProduct(Product product) {
        products.add(product);
        notifyDataSetChanged();
    }
}
