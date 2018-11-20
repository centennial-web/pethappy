package ca.pethappy.pethappy.android.ui.products;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import ca.pethappy.pethappy.android.R;

public class ProductDetails extends AppCompatActivity {
    public static TextView data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details2);

        data = (TextView)findViewById(R.id.detailsTextView);
        FetchData fetchData = new FetchData();
        fetchData.execute();
    }
}
