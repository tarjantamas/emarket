package com.tim32.emarket.product;

import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.tim32.emarket.R;
import com.tim32.emarket.apiclients.clients.ProductRestClient;
import com.tim32.emarket.apiclients.clients.ProductSecureRestClient;
import com.tim32.emarket.apiclients.clients.ShopSecureRestClient;
import com.tim32.emarket.apiclients.dto.Product;
import com.tim32.emarket.customcomponents.product.ProductListItem;
import com.tim32.emarket.util.BundleUtil;
import com.tim32.emarket.util.JsonUtils;
import org.androidannotations.annotations.*;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@EActivity(R.layout.activity_product_list)
public class ProductListActivity extends AppCompatActivity {

    public static final String COMPANY_ID = "company_id";

    private static final int ADD_PRODUCT_REQUEST_CODE = 1;

    public static final int CONFIRM_RESULT_CODE = 1;

    public static final String NEW_PRODUCTS = "new_products";

    @ViewById(R.id.productsListView)
    ListView productListView;

    @RestService
    ProductSecureRestClient productSecureRestClient;

    @RestService
    ProductRestClient productRestClient;

    @RestService
    ShopSecureRestClient shopSecureRestClient;

    private ArrayAdapter<ProductListItem> listAdapter;

    private Long companyId;

    private List<Product> newProducts = new ArrayList<>();

    @AfterViews
    void afterViews() {
        companyId = BundleUtil.getLongOrNull(getIntent().getExtras(), COMPANY_ID);
        initProductListAdapter();
        loadProducts();
    }

    @Click(R.id.confirmButton)
    void confirmClicked() {
        Intent intent = new Intent();
        intent.putExtra(NEW_PRODUCTS, JsonUtils.toJson(newProducts));
        setResult(CONFIRM_RESULT_CODE, intent);
        finish();
    }

    @Background
    void loadProducts() {
        try {
            List<Product> products = productRestClient.findByCompanyId(companyId);
            productsLoaded(products);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @UiThread
    @SuppressWarnings("NewApi")
    void productsLoaded(List<Product> products) {
        listAdapter.clear();
        listAdapter.addAll(products.stream().map(ProductListItem::new).collect(Collectors.toList()));
        listAdapter.notifyDataSetChanged();
    }

    @Click(R.id.addProductButton)
    void addProductButtonClicked() {
        ProductRegistrationActivity_.intent(this).extra(ProductRegistrationActivity.COMPANY_ID, companyId)
                .startForResult(ADD_PRODUCT_REQUEST_CODE);
    }

    @OnActivityResult(ADD_PRODUCT_REQUEST_CODE)
    void productAddFinished(Intent data) {
        if (data == null) {
            return;
        }
        Product product = BundleUtil.fromJsonExtraOrNull(data.getExtras(), ProductRegistrationActivity.NEW_PRODUCT, Product.class);
        newProducts.add(product);
        listAdapter.add(new ProductListItem(product));
        listAdapter.notifyDataSetChanged();
    }

    private void initProductListAdapter() {
        listAdapter = new ArrayAdapter<>(this, R.layout.text_view_list_item, new ArrayList<>());
        productListView.setAdapter(listAdapter);
    }

    @UiThread
    void setProducts(List<Product> products) {
        listAdapter.clear();
        listAdapter.addAll(products.stream().map(ProductListItem::new).collect(Collectors.toList()));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(NEW_PRODUCTS, JsonUtils.toJson(newProducts));
        setResult(CONFIRM_RESULT_CODE, intent);
        finish();
    }
}
