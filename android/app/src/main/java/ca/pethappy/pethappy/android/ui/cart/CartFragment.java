package ca.pethappy.pethappy.android.ui.cart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ca.pethappy.pethappy.android.R;
import ca.pethappy.pethappy.android.models.backend.CartItem;
import ca.pethappy.pethappy.android.ui.base.fragments.BaseFragment;
import ca.pethappy.pethappy.android.ui.products.ProductDetailsActivity;
import ca.pethappy.pethappy.android.utils.formatters.NumberFormatter;
import ca.pethappy.pethappy.android.utils.task.SimpleTask;

public class CartFragment extends BaseFragment {
    private CartListener cartListener;
    private CartAdapter cartAdapter;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get view root
        final View rootView = inflater.inflate(R.layout.fragment_cart, container, false);

        // Adapter
        cartAdapter = new CartAdapter(getApp(), new CartAdapter.CartAdapterEventsListener() {
            @Override
            public void onItemClick(CartItem cartItem, CartAdapter.ViewHolder viewHolder) {
                Intent productDetailsIntent = new Intent(getContext(), ProductDetailsActivity.class);
                productDetailsIntent.putExtra(ProductDetailsActivity.EXTRA_PRODUCT_ID, cartItem.product.id);
                startActivity(productDetailsIntent);
            }

            @Override
            public void onPlusClick(CartItem cartItem, CartAdapter.ViewHolder viewHolder) {
                viewHolder.minusBtn.setEnabled(false);

                // Add item
                new SimpleTask<Void, List<CartItem>>(
                        ignored -> getApp().cartServices.addItemToCart(cartItem.product.id)
                                ? getApp().cartServices.listItems()
                                : new ArrayList<>(),
                        cartItems -> {
                            // Update data adapter
                            cartAdapter.updateData(cartItems);

                            // Update totals
                            updateTotal(cartItems);

                            // Spread the word
                            if (cartListener != null) {
                                cartListener.addItemToCart(cartItem.product.id, cartItems);
                            }

                            viewHolder.minusBtn.setEnabled(true);
                        },
                        error -> {
                            Toast.makeText(getApp().getApplicationContext(), "Something went wrong. "
                                    + error.getMessage(), Toast.LENGTH_SHORT).show();
                            viewHolder.minusBtn.setEnabled(true);
                        }
                ).execute((Void) null);
            }

            @Override
            public void onMinusClick(CartItem cartItem, CartAdapter.ViewHolder viewHolder) {
                viewHolder.minusBtn.setEnabled(false);

                // Remove item
                new SimpleTask<Void, List<CartItem>>(
                        ignored -> getApp().cartServices.removeItemFromCart(cartItem.product.id)
                                ? getApp().cartServices.listItems()
                                : new ArrayList<>(),
                        cartItems -> {
                            // Update data adapter
                            cartAdapter.updateData(cartItems);

                            // Update totals
                            updateTotal(cartItems);

                            // Spread the word
                            if (cartListener != null) {
                                cartListener.removeItemFromCart(cartItem.product.id, cartItems);
                            }

                            viewHolder.minusBtn.setEnabled(true);
                        },
                        error -> {
                            Toast.makeText(getApp().getApplicationContext(), "Something went wrong. "
                                    + error.getMessage(), Toast.LENGTH_SHORT).show();
                            viewHolder.minusBtn.setEnabled(true);
                        }
                ).execute((Void) null);
            }
        });

        // Recycler view
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(cartAdapter);

        // Query products
        new SimpleTask<Void, List<CartItem>>(
                none -> getApp().cartServices.listItems(),
                cartItems -> {
                    // Feed the recycler view
                    cartAdapter.updateData(cartItems);

                    // Update total (bottom)
                    updateTotal(cartItems);
                },
                error -> Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show()
        ).execute((Void) null);

        return rootView;
    }

    /**
     * Count and Sum the cartAdapter.getCartItems and
     * update the total label.
     */
    private void updateTotal(final List<CartItem> cartItems) {
        // Get totalTxt reference
        final TextView totalTxt;
        if (getView() == null || (totalTxt = getView().findViewById(R.id.totalTxt)) == null) {
            return;
        }

        // Sum
        int quantity = 0;
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems) {
            quantity += cartItem.quantity;
            total = total.add(cartItem.product.price.multiply(BigDecimal.valueOf(cartItem.quantity)));
        }

        // Show the result
        totalTxt.setText(quantity + " / CDN$ " + NumberFormatter.getInstance().formatNumber2(total));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof CartListener) {
            cartListener = (CartListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        cartListener = null;
    }
}
