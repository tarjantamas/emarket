package com.tim32.emarket.customcomponents.product;

import android.content.Context;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tim32.emarket.R;
import com.tim32.emarket.apiclients.config.GlideApp;
import com.tim32.emarket.apiclients.dto.Product;
import com.tim32.emarket.util.ProductUtil;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.view_product_item)
public class ProductItemView extends ConstraintLayout {

    @ViewById(R.id.productImage)
    RoundedImageView productImage;

    @ViewById(R.id.productName)
    TextView productName;

    @ViewById(R.id.productPrice)
    TextView productPrice;

    @AfterViews
    void afterViews() {
    }

    private Product product;

    public ProductItemView(Context context) {
        super(context);
    }

    public void bind(Product product, String imageUrl) {
        this.product = product;
        productName.setText(product.getName());
        productPrice.setText(ProductUtil.getPriceString(getContext(), product));
        GlideApp.with(this).load(imageUrl).error(R.drawable.placeholder).into(productImage);
    }
}
