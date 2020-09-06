package com.tim32.emarket.product;

import android.content.Intent;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.tim32.emarket.R;
import com.tim32.emarket.apiclients.clients.ProductRestClient;
import com.tim32.emarket.apiclients.dto.Product;
import com.tim32.emarket.apiclients.dto.Shop;
import com.tim32.emarket.customcomponents.product.rowlist.ClickableProductRowAdapter;
import com.tim32.emarket.service.ImageProvider;
import com.tim32.emarket.util.BundleUtil;
import com.tim32.emarket.util.JsonUtils;
import org.androidannotations.annotations.*;
import org.androidannotations.rest.spring.annotations.RestService;
import org.apache.commons.collections4.SetUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@EActivity(R.layout.activity_manage_shop_products)
public class ManageShopProductsActivity extends AppCompatActivity {

    public static final int RESULT_CODE_SELECTED = 1;

    public static final int RESULT_CODE_CANCELED = 2;

    public static final int RESULT_CODE_MANAGE_PRODUCTS = 3;

    public static final String SHOP = "SHOP";

    public static final String SELECTED_PRODUCT_IDS = "selected_product_ids";

    @ViewById(R.id.shopProducts)
    ListView shopProductsView;

    @ViewById(R.id.companyProducts)
    ListView companyProductsView;

    @RestService
    ProductRestClient productRestClient;

    @Bean
    ImageProvider imageProvider;

    private ClickableProductRowAdapter shopProductsAdapter;

    private ClickableProductRowAdapter companyProductsAdapter;

    private List<Product> shopProducts;

    private List<Product> companyProducts;

    private Shop shop;

    @AfterViews
    void afterViews() {
        shopProductsView.setDivider(null);
        companyProductsView.setDivider(null);
        shop = BundleUtil.fromJsonExtraOrNull(getIntent().getExtras(), SHOP, Shop.class);
        if (shop == null) {
            return;
        }
        loadShopProducts(shop.getId());
        loadCompanyProducts(shop.getCompanyId());
    }

    @Click(R.id.addNewProductButton)
    void addNewProductClicked() {
        ProductListActivity_.intent(this).extra(ProductListActivity.COMPANY_ID, shop.getCompanyId()).startForResult(RESULT_CODE_MANAGE_PRODUCTS);
    }

    @OnActivityResult(RESULT_CODE_MANAGE_PRODUCTS)
    @SuppressWarnings("NewApi")
    void onManageProductsResult(Intent data) {
        if (data == null) {
            return;
        }
        List<Product> newProducts = BundleUtil.fromJsonExtraOrNull(data.getExtras(), ProductListActivity.NEW_PRODUCTS, JsonUtils.PRODUCT_LIST_TYPE);
        companyProductsAdapter.addProducts(newProducts);
    }

    @Background
    void loadCompanyProducts(Long companyId) {
        try {
            productsLoaded(null, productRestClient.findByCompanyId(companyId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Background
    void loadShopProducts(Long shopId) {
        try {
            productsLoaded(productRestClient.findProductsByShopId(shopId), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @UiThread
    void productsLoaded(List<Product> shopProducts, List<Product> companyProducts) {
        if (shopProducts != null) {
            this.shopProducts = shopProducts;
        } else if (companyProducts != null) {
            this.companyProducts = companyProducts;
        }

        if (this.shopProducts == null || this.companyProducts == null) {
            return;
        }

        this.companyProducts = new ArrayList<>(SetUtils.difference(new HashSet<>(this.companyProducts), new HashSet<>(this.shopProducts)));

        initShopProductsView();
        initCompanyProductsView();
    }

    private void initCompanyProductsView() {
        shopProductsAdapter = new ClickableProductRowAdapter(this, imageProvider);
        shopProductsView.setAdapter(shopProductsAdapter);
        shopProductsAdapter.setProducts(shopProducts);
    }

    private void initShopProductsView() {
        companyProductsAdapter = new ClickableProductRowAdapter(this, imageProvider);
        companyProductsView.setAdapter(companyProductsAdapter);
        companyProductsAdapter.setProducts(companyProducts);
    }

    @ItemClick(R.id.shopProducts)
    void shopProductClicked(Product product) {
        shopProductsAdapter.removeItem(product);
        companyProductsAdapter.addProduct(product);
    }

    @ItemClick(R.id.companyProducts)
    void companyProductClicked(Product product) {
        companyProductsAdapter.removeItem(product);
        shopProductsAdapter.addProduct(product);
    }

    @Click(R.id.confirmButton)
    @SuppressWarnings("NewApi")
    void confirmClicked() {
        List<Long> selectedProductIds = shopProducts.stream().map(Product::getId)
                .collect(Collectors.toList());
        Intent result = new Intent();
        result.putExtra(SELECTED_PRODUCT_IDS, JsonUtils.toJson(selectedProductIds));
        setResult(RESULT_CODE_SELECTED, result);
        finish();
    }

    @Click(R.id.cancelButton)
    void cancelClicked() {
        setResult(RESULT_CODE_CANCELED);
        finish();
    }
}
