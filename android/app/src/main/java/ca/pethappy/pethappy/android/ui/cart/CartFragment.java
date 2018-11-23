package ca.pethappy.pethappy.android.ui.cart;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import ca.pethappy.pethappy.android.ui.base.fragments.OnFragmentInteractionListener;
import ca.pethappy.pethappy.android.ui.products.ProductDetailsActivity;
import ca.pethappy.pethappy.android.utils.formatters.NumberFormatter;
import ca.pethappy.pethappy.android.utils.task.SimpleTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private CartBadgeListener cartBadgeListener;
    private CartListener cartListener;

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
                // Add item
                new SimpleTask<Void, List<CartItem>>(
                        ignored -> getApp().cartServices.addItemToCart(cartItem.product.id)
                                ? getApp().cartServices.listItems()
                                : new ArrayList<>(),
                        cartItems -> {
                            cartAdapter.updateData(cartItems);
                            updateTotal(cartItems);
                        },
                        error -> {
                            Toast.makeText(getApp().getApplicationContext(), "Something went wrong. "
                                    + error.getMessage(), Toast.LENGTH_SHORT).show();
                            System.out.println(error.getMessage());
                        }
                ).execute((Void) null);
            }

            @Override
            public void onMinusClick(CartItem cartItem, CartAdapter.ViewHolder viewHolder) {
                // Remove item
                new SimpleTask<Void, List<CartItem>>(
                        ignored -> getApp().cartServices.removeItemFromCart(cartItem.product.id)
                                ? getApp().cartServices.listItems()
                                : new ArrayList<>(),
                        cartItems -> {
                            cartAdapter.updateData(cartItems);
                            updateTotal(cartItems);
                        },
                        error -> {
                            Toast.makeText(getApp().getApplicationContext(), "Something went wrong. "
                                    + error.getMessage(), Toast.LENGTH_SHORT).show();
                            System.out.println(error.getMessage());
                        }
                ).execute((Void) null);
            }
        });

        // Recycler view
        recyclerView = rootView.findViewById(R.id.recyclerView);
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
        int quantity = 0;
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems) {
            quantity += cartItem.quantity;
            total = total.add(cartItem.product.price.multiply(BigDecimal.valueOf(cartItem.quantity)));
        }
        ((TextView) getView().findViewById(R.id.totalTxt))
                .setText(quantity + " / CDN$ " + NumberFormatter.getInstance().formatNumber2(total));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }

        if (context instanceof CartBadgeListener) {
            cartBadgeListener = (CartBadgeListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement CartBadgeListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
