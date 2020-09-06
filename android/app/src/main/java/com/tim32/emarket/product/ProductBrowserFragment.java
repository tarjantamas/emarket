package com.tim32.emarket.product;

import android.annotation.SuppressLint;
import android.location.Location;
import android.widget.EditText;
import android.widget.GridView;
import androidx.fragment.app.Fragment;
import com.tim32.emarket.R;
import com.tim32.emarket.apiclients.clients.ProductSecureRestClient;
import com.tim32.emarket.apiclients.dto.Product;
import com.tim32.emarket.customcomponents.product.ProductListAdapter;
import com.tim32.emarket.data.entity.Setting;
import com.tim32.emarket.service.ImageProvider;
import com.tim32.emarket.service.LocationService;
import com.tim32.emarket.service.ProductService;
import com.tim32.emarket.service.SettingService;
import com.tim32.emarket.util.BundleUtil;
import com.tim32.emarket.util.FragmentUtil;
import org.androidannotations.annotations.*;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.List;

@EFragment(R.layout.fragment_product_browser)
public class ProductBrowserFragment extends Fragment {

    @ViewById(R.id.productGrid)
    GridView productGrid;

    @RestService
    ProductSecureRestClient productSecureRestClient;

    @Bean
    ProductService productService;

    @Bean
    ImageProvider imageProvider;

    @Bean
    SettingService settingService;

    @Bean
    LocationService locationService;

    @ViewById(R.id.searchTerm)
    EditText searchTerm;

    private ProductListAdapter productListAdapter;

    @AfterViews
    void afterViews() {
        getActivity().setTitle(R.string.products);
        productListAdapter = new ProductListAdapter(getContext(), imageProvider);
        productGrid.setAdapter(productListAdapter);
        loadProducts(null);
    }

    @SuppressLint("LogNotTimber")
    @TextChange(R.id.searchTerm)
    void searchTermChanged() {
        loadProducts(searchTerm.getText().toString());
    }

    @Background
    void loadProducts(String searchTerm) {
        try {
            Setting settingsDto = settingService.getSetting();
            if (locationService.isLocationAvailable()) {
                Location userLocation = locationService.getCurrentLocation();
                loadProducts(searchTerm, userLocation.getLatitude(), userLocation.getLongitude(), settingsDto.getSearchRadius());
            } else {
                loadProducts(searchTerm, null, null, null);
            }
        } catch (Exception e) {
            FragmentUtil.performBackAction(getActivity());
            e.printStackTrace();
        }
    }

    void loadProducts(String searchTerm, Double lat, Double lng, Double radius) {
        try {
            Long shopId = BundleUtil.getLongOrNull(getArguments(), "shop_id");
            List<Product> products;
            if (shopId == null) {
                products = productService.search(searchTerm, lng, lat, radius);
            } else {
                products = productService.searchByShopId(shopId, searchTerm);
            }
            setProducts(products);
        } catch (Exception e) {
            FragmentUtil.performBackAction(getActivity());
            e.printStackTrace();
        }
    }

    @UiThread
    void setProducts(List<Product> products) {
        productListAdapter.setProducts(products);
    }

    @ItemClick(R.id.productGrid)
    void productClicked(Product product) {
        FragmentUtil.setRootFragment(this, ProductDetailFragment_.builder().arg(ProductDetailFragment.PRODUCT_ID, product.getId().toString()).build());
    }
}
