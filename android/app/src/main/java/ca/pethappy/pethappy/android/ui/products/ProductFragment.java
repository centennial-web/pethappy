package ca.pethappy.pethappy.android.ui.products;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ca.pethappy.pethappy.android.App;
import ca.pethappy.pethappy.android.R;
import ca.pethappy.pethappy.android.models.backend.CartItem;
import ca.pethappy.pethappy.android.models.backend.projections.ProductWithoutDescription;
import ca.pethappy.pethappy.android.models.backend.projections.Recommendation;
import ca.pethappy.pethappy.android.ui.base.fragments.BaseFragment;
import ca.pethappy.pethappy.android.ui.cart.CartListener;
import ca.pethappy.pethappy.android.ui.main.MainActivity;
import ca.pethappy.pethappy.android.utils.task.SimpleTask;

import static android.support.v7.widget.DividerItemDecoration.HORIZONTAL;

public class ProductFragment extends BaseFragment {
    private ProductAdapter productAdapter;
    private RecommendationAdapter recommendationAdapter;
    private CartListener cartListener;
    private SwipeRefreshLayout swipeContainer;
    private ProductFragmentListener productFragmentListener;

    private String lastQuery;

    public ProductFragment() {
        // Required empty public constructor
        lastQuery = "";
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get view root
        final View rootView = inflater.inflate(R.layout.fragment_product, container, false);

        // Swipe to refresh
        swipeContainer = rootView.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this::searchProducts);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // Show recommendations
        showRecommendations();

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

        recommendationAdapter = new RecommendationAdapter((recommendation, v) -> {
            Intent productDetailsIntent = new Intent(getContext(), ProductDetailsActivity.class);
            productDetailsIntent.putExtra(ProductDetailsActivity.EXTRA_PRODUCT_ID, recommendation.id);
            startActivity(productDetailsIntent);
        });

        // Recycler view
        RecyclerView recommendationRecyclerView = rootView.findViewById(R.id.recommendationRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recommendationRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), HORIZONTAL);
        recommendationRecyclerView.addItemDecoration(itemDecor);
        recommendationRecyclerView.setAdapter(recommendationAdapter);

        // Query products
        searchProducts();

        return rootView;
    }

    private void showRecommendations() {
        final App app = getApp();
        new SimpleTask<Void, List<Recommendation>>(
                ignored -> app.productsService.getRecommendations(),
                recommendations -> recommendationAdapter.updateData(recommendations),
                error -> {
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        ).execute((Void) null);
    }

    private void searchProducts() {
        final App app = getApp();
        new SimpleTask<Void, List<ProductWithoutDescription>>(
                ignored -> app.productsService.productsFindAllWithoutDescription(lastQuery),
                payload -> {
                    productAdapter.updateData(payload);
                    swipeContainer.setRefreshing(false);
                },
                error -> {
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
        ).execute((Void) null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Cart Listener
        if (context instanceof CartListener) {
            cartListener = (CartListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }

        // Product fragment listener
        if (context instanceof ProductFragmentListener) {
            productFragmentListener = (ProductFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ProductFragmentListener");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MainActivity mainActivity;
        ActionBar actionBar;
        if ((mainActivity = ((MainActivity) getActivity())) != null && (actionBar = mainActivity.getSupportActionBar()) != null) {
            inflater.inflate(R.menu.fragment_product_menu, menu);
            MenuItem item = menu.findItem(R.id.action_search);
            SearchView searchView = new SearchView(actionBar.getThemedContext());
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
            item.setActionView(searchView);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (productFragmentListener != null) {
                        productFragmentListener.onQueryProductTextSubmit(query);
                    }
                    lastQuery = query;
                    searchProducts();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newQuery) {
                    if (newQuery.equals("") && lastQuery.equals("")) {
                        return false;
                    }

                    lastQuery = newQuery;
                    if (lastQuery.equals("")) {
                        searchProducts();
                    }
                    return false;
                }
            });
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onActivityCreated(savedInstanceState);
    }
}
