package com.tim32.emarket.customcomponents.product;

import android.content.Intent;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.tim32.emarket.R;
import com.tim32.emarket.apiclients.dto.Product;
import com.tim32.emarket.customcomponents.product.rowlist.ClickableProductRowAdapter;
import com.tim32.emarket.service.ImageProvider;
import com.tim32.emarket.util.JsonUtils;
import org.androidannotations.annotations.*;

import java.util.List;

@EActivity(R.layout.activity_product_picker)
public class ProductPicker extends AppCompatActivity {

    public final static String PRODUCT_LIST = "product_list";

    public final static String SELECTED_PRODUCT = "selected_product";

    public final static int PRODUCT_SELECTED = 1;

    public final static int SELECTION_CANCELED = 2;

    @ViewById(R.id.productList)
    ListView productList;

    @Bean
    ImageProvider imageProvider;

    private ClickableProductRowAdapter productRowAdapter;

    @AfterViews
    void afterViews() {
        Intent intent = getIntent();
        String productsJson = intent.getStringExtra(PRODUCT_LIST);

        List<Product> products = JsonUtils.fromJson(productsJson, JsonUtils.PRODUCT_LIST_TYPE);
        productRowAdapter = new ClickableProductRowAdapter(this, imageProvider);
        productList.setAdapter(productRowAdapter);
        productRowAdapter.setProducts(products);
        productList.setDivider(null);
        productList.setDividerHeight(10);
    }

    @ItemClick(R.id.productList)
    void listViewItemClick(Product product) {
        Intent resultData = new Intent();
        resultData.putExtra(SELECTED_PRODUCT, JsonUtils.toJson(product));
        setResult(PRODUCT_SELECTED, resultData);
        finish();
    }

    @Click(R.id.cancelButton)
    void cancelClicked() {
        setResult(SELECTION_CANCELED);
        finish();
    }
}
