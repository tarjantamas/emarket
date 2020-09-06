package com.tim32.emarket.product;

import android.annotation.SuppressLint;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.tim32.emarket.R;
import com.tim32.emarket.apiclients.clients.ProductSecureRestClient;
import com.tim32.emarket.apiclients.dto.Product;
import com.tim32.emarket.customcomponents.gallery.ImageManagementFragment_;
import com.tim32.emarket.util.BundleUtil;
import com.tim32.emarket.util.FragmentUtil;
import org.androidannotations.annotations.*;
import org.androidannotations.rest.spring.annotations.RestService;


/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_product_management)
public class ProductManagementFragment extends Fragment {

    public static final String PRODUCT = "product";

    @ViewById(R.id.name)
    EditText name;

    @ViewById(R.id.description)
    EditText description;

    @ViewById(R.id.price)
    EditText price;

    @ViewById(R.id.unit)
    EditText unit;

    @RestService
    ProductSecureRestClient productSecureRestClient;

    private Product product;

    @SuppressLint("SetTextI18n")
    @AfterViews
    void afterViews() {
        product = BundleUtil.fromJsonExtraOrNull(getArguments(), PRODUCT, Product.class);
        if (product == null) {
            return;
        }
        name.setText(product.getName());
        description.setText(product.getDescription());
        price.setText(product.getPrice().toString());
        unit.setText(product.getUnit());
    }

    @Click(R.id.manageImages)
    void manageImages() {
        FragmentUtil.setRootFragment(this, ImageManagementFragment_.builder().arg("product_id", product.getId()).build());
    }

    @Click(R.id.saveButton)
    void saveClicked() {
        product.setName(name.getText().toString());
        product.setDescription(description.getText().toString());
        product.setPrice(Double.parseDouble(price.getText().toString()));
        product.setUnit(unit.getText().toString());
        product.setShopIds(null);
        product.setCompanyId(null);
        updateProduct();
    }

    @Background
    void updateProduct() {
        try {
            productSecureRestClient.update(product.getId(), product);
            updateProductSuccess();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @UiThread
    void updateProductSuccess() {
        Toast.makeText(getContext(), "Product saved", Toast.LENGTH_SHORT).show();
        FragmentUtil.performBackAction(getActivity());
    }
}
