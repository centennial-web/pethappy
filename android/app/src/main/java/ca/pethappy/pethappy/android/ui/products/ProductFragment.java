package ca.pethappy.pethappy.android.ui.products;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ca.pethappy.pethappy.android.App;
import ca.pethappy.pethappy.android.R;
import ca.pethappy.pethappy.android.api.page.Page;
import ca.pethappy.pethappy.android.models.backend.CartItem;
import ca.pethappy.pethappy.android.models.backend.projections.ProductWithoutDescription;
import ca.pethappy.pethappy.android.ui.base.fragments.BaseFragment;
import ca.pethappy.pethappy.android.ui.cart.CartListener;
import ca.pethappy.pethappy.android.utils.task.SimpleTask;
import retrofit2.Response;

public class ProductFragment extends BaseFragment {
    private ProductAdapter productAdapter;
    private CartListener cartListener;

    public ProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get view root
        final View rootView = inflater.inflate(R.layout.fragment_product, container, false);

        // AdaptergetPackageName
        productAdapter = new ProductAdapter(new ProductAdapter.ProductAdapterEventsListener() {
            @Override
            public void onItemClick(ProductWithoutDescription product, View productCardView) {
                Intent productDetailsIntent = new Intent(getContext(), ProductDetailsActivity.class);
                productDetailsIntent.putExtra(ProductDetailsActivity.EXTRA_PRODUCT_ID, product.id);
                startActivity(productDetailsIntent);
            }

            @Override
            public void onPlusItemClick(ProductWithoutDescription product, View plusBtnView) {
                plusBtnView.setEnabled(false);
                new SimpleTask<Void, List<CartItem>>(
                        ignored -> getApp().cartServices.addItemToCart(product.id)
                                ? getApp().cartServices.listItems()
                                : new ArrayList<>(),
                        cartItems -> {
                            if (cartItems.size() > 0) {
                                // Spread the word
                                if (cartListener != null) {
                                    cartListener.addItemToCart(product.id, cartItems);
                                }
                            }
                            plusBtnView.setEnabled(true);
                        },
                        error -> {
                            Toast.makeText(getActivity(), "Something went wrong. " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            plusBtnView.setEnabled(true);
                        }
                ).execute((Void) null);
            }
        });

        // Recycler view
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(productAdapter);

        final App app = getApp();

        // Query products
        new SimpleTask<Void, Page<ProductWithoutDescription>>(
                none -> {
                    Response<Page<ProductWithoutDescription>> response = app.noSecEndpoints.productsFindAllWithoutDescription().execute();
                    return (response.isSuccessful()) ? response.body() : new Page<>();
                },
                payload -> productAdapter.updateData(payload.content),
                error -> Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show()
        ).execute((Void) null);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof CartListener) {
            cartListener = (CartListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
}
