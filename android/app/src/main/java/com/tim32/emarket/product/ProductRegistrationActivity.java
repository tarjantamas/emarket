package com.tim32.emarket.product;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.tim32.emarket.R;
import com.tim32.emarket.apiclients.clients.ProductSecureRestClient;
import com.tim32.emarket.apiclients.dto.Product;
import com.tim32.emarket.util.BundleUtil;
import com.tim32.emarket.util.DataValidatingToaster;
import com.tim32.emarket.util.JsonUtils;
import org.androidannotations.annotations.*;
import org.androidannotations.rest.spring.annotations.RestService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;


/**
 * A simple {@link Fragment} subclass.
 */
@EActivity(R.layout.activity_product_registration)
public class ProductRegistrationActivity extends AppCompatActivity {

    public static final String TAG = ProductRegistrationActivity.class.getName();

    public static final String COMPANY_ID = "company_id";

    public static final String NEW_PRODUCT = "new_product";

    public static final int RESULT_CODE_CREATED = 1;

    @ViewById(R.id.nameField)
    EditText nameField;

    @ViewById(R.id.descriptionField)
    EditText descriptionField;

    @ViewById(R.id.priceField)
    EditText priceField;

    @ViewById(R.id.unitField)
    EditText unitField;

    @ViewById(R.id.saveButton)
    Button saveButton;

    @RestService
    ProductSecureRestClient productSecureRestClient;

    private Long companyId;

    @AfterViews()
    void afterViews() {
        companyId = BundleUtil.getLongOrNull(getIntent().getExtras(), COMPANY_ID);
    }

    @Click(R.id.saveButton)
    void saveButtonClicked() {
        new DataValidatingToaster(this)
                .rule(() -> StringUtils.isNotEmpty(nameField.getText().toString()), "Please provide a name")
                .rule(() -> StringUtils.isNotEmpty(descriptionField.getText().toString()), "Please provide a description")
                .rule(() -> NumberUtils.isCreatable(priceField.getText().toString()), "Please provide a price")
                .rule(() -> StringUtils.isNotEmpty(unitField.getText().toString()), "Please provide a unit")
                .validateAllRules();
        createProduct(nameField.getText().toString(), descriptionField.getText().toString(), Double.parseDouble(priceField.getText().toString()), unitField.getText().toString());
    }

    @Background
    void createProduct(String name, String description, double price, String unit) {
        try {
            Product product = productSecureRestClient.create(Product.builder()
                    .name(name)
                    .description(description)
                    .price(price)
                    .companyId(companyId)
                    .unit(unit)
                    .build());
            productCreationSucceeded(product);
        } catch (Exception e) {
            e.printStackTrace();
            productCreationFailed();
        }
    }

    @UiThread
    void productCreationSucceeded(Product product) {
        Intent result = new Intent();
        result.putExtra(NEW_PRODUCT, JsonUtils.toJson(product));
        setResult(RESULT_CODE_CREATED, result);
        finish();
    }

    @UiThread
    void productCreationFailed() {
        Toast.makeText(this, "Failed to create product", Toast.LENGTH_SHORT).show();
        finish();
    }
}
