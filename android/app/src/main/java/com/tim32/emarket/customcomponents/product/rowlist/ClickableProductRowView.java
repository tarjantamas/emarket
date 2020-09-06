package com.tim32.emarket.customcomponents.product.rowlist;

import android.content.Context;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tim32.emarket.R;
import com.tim32.emarket.apiclients.config.GlideApp;
import com.tim32.emarket.apiclients.dto.Product;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.view_clickable_product_row)
public class ClickableProductRowView extends ConstraintLayout {

    @ViewById(R.id.productName)
    TextView productName;

    @ViewById(R.id.productDescription)
    TextView productDescription;

    @ViewById(R.id.productPrice)
    TextView productPrice;

    @ViewById(R.id.productImage)
    RoundedImageView productImage;

    private Product product;

    private ClickableProductRowAdapter productRowAdapter;

    public ClickableProductRowView(Context context, ClickableProductRowAdapter productRowAdapter) {
        super(context);
        this.productRowAdapter = productRowAdapter;
    }

    public void bind(Product product, String imageUrl) {
        this.product = product;
        productName.setText(product.getName());
        productDescription.setText(product.getDescription());
        productPrice.setText(getHumanReadablePrice(product.getPrice()));
        GlideApp.with(this).load(imageUrl).into(productImage);
    }

    private String getHumanReadablePrice(Double price) {
        return "€" + price + "/kg";
    }

    @Click(R.id.removeButton)
    void removeClicked() {
        productRowAdapter.removeItem(product);
    }
}
