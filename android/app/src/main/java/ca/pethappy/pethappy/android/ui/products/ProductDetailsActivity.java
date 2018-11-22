package ca.pethappy.pethappy.android.ui.products;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import ca.pethappy.pethappy.android.R;
import ca.pethappy.pethappy.android.consts.Consts;
import ca.pethappy.pethappy.android.models.backend.Product;
import ca.pethappy.pethappy.android.ui.base.BaseActivity;
import ca.pethappy.pethappy.android.utils.formatters.NumberFormatter;
import ca.pethappy.pethappy.android.utils.glide.GlideApp;
import ca.pethappy.pethappy.android.utils.task.SimpleTask;
import retrofit2.Response;

public class ProductDetailsActivity extends BaseActivity {
    // Components
    private ImageView pictureImv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // Toolbar
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Fab
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            // TODO: 22/11/18 Add ptoduct to the cart
            Snackbar.make(view, "Product added to the cart", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        });

        // Components
        pictureImv = findViewById(R.id.pictureImv);
        pictureImv.setImageResource(R.drawable.placeholder);

        // Query products
        new SimpleTask<Long, Product>(
                productId -> {
                    Response<Product> response = getApp().noSecEndpoints.productsFindById(productId).execute();
                    if (response.isSuccessful()) {
                        return response.body();
                    }
                    return null;
                },
                product -> {
                    if (product != null) {
                        // Get photo
                        GlideApp.with(ProductDetailsActivity.this)
                                .load(Consts.AWS_S3_URL + "/" + product.imageUrl)
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .apply(RequestOptions
                                        .diskCacheStrategyOf(DiskCacheStrategy.ALL)
                                        .placeholder(R.drawable.placeholder))
                                .into(pictureImv);

                        ((TextView) findViewById(R.id.nameTxt)).setText(product.name);
                        ((TextView) findViewById(R.id.manufacturerTxt)).setText(product.manufacturer.name);
                        ((TextView) findViewById(R.id.categoryTxt)).setText(product.category.name);
                        ((TextView) findViewById(R.id.descriptionTxt)).setText(product.description);
                        ((TextView) findViewById(R.id.ingredientTxt)).setText(product.ingredient.name);
                        ((TextView) findViewById(R.id.weightKgTxt)).setText(NumberFormatter.getInstance().formatNumber2(product.weightKg) + " Kg");
                        ((TextView) findViewById(R.id.priceTxt)).setText("CDN$ " + NumberFormatter.getInstance().formatNumber2(product.price));
                    }
                },
                error -> {
                    Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    finish();
                }
        ).execute(getIntent().getLongExtra("productId", 0L));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
