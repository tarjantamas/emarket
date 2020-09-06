package com.tim32.emarket.customcomponents.product;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.tim32.emarket.apiclients.config.RestApiUrl;
import com.tim32.emarket.apiclients.dto.Product;
import com.tim32.emarket.service.ImageProvider;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class ProductListAdapter extends BaseAdapter {

    private List<Product> products;

    private final Context context;

    private final ImageProvider imageProvider;

    public ProductListAdapter(Context context, ImageProvider imageProvider) {
        this.context = context;
        this.imageProvider = imageProvider;
        products = new ArrayList<>();
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
        ProductItemView productItemView;
        if (convertView == null) {
            productItemView = ProductItemView_.build(context);
        } else {
            productItemView = (ProductItemView) convertView;
        }
        Product product = (Product) getItem(position);
        productItemView.bind(product, RestApiUrl.productImage + "/" + product.getId());

        return productItemView;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }
}
