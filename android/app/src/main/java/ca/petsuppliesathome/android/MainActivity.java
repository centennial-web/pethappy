package ca.petsuppliesathome.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ca.petsuppliesathome.android.models.Product;
import ca.petsuppliesathome.android.task.Task1;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private ApiService apiService;
    private RecyclerView recyclerView;
    private ProductsAdapter productsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiService = new Retrofit.Builder()
                .baseUrl(Consts.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new Task1<Integer, List<Product>>(
                param -> {
                    Response<List<Product>> response = apiService.getProducts().execute();
                    if (response.isSuccessful()) {
                        return response.body();
                    }
                    return new ArrayList<>();
                },
                result -> {
                    productsAdapter = new ProductsAdapter(result, MainActivity.this);
                    recyclerView.setAdapter(productsAdapter);
                },
                error -> Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        ).execute(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_reload:
                new Task1<Integer, List<Product>>(
                        param -> {
                            Response<List<Product>> response = apiService.getProducts().execute();
                            if (response.isSuccessful()) {
                                return response.body();
                            }
                            return new ArrayList<>();
                        },
                        result -> {
                            productsAdapter = new ProductsAdapter(result, MainActivity.this);
                            recyclerView.setAdapter(productsAdapter);
                        },
                        error -> Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
                ).execute(1);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
