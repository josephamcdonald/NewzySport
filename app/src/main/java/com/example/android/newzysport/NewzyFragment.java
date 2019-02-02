package com.example.android.newzysport;

import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.loader.app.LoaderManager;
import androidx.loader.app.LoaderManager.LoaderCallbacks;
import androidx.loader.content.Loader;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

public class NewzyFragment extends Fragment implements LoaderCallbacks<List<Newzy>> {

    // Newzy Loader ID constant.
    private static final int NEWZY_LOADER_ID = 0;

    // Declare the progress indicators.
    private ProgressBar progressCircle;
    private ImageView progressIcon;

    // Declare the newzy recycler view.
    private RecyclerView newzyRecyclerView;

    // Declare the empty state view.
    private TextView emptyStateView;

    // Declare the swipe refresh layout.
    private SwipeRefreshLayout swipeLayout;

    // Declare the newzy adapter.
    private NewzyAdapter newzyAdapter;

    public NewzyFragment() {
        // Required empty constructor.
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Find the in-progress views.
        progressCircle = Objects.requireNonNull(getActivity()).findViewById(R.id.progress_circle);
        progressIcon = getActivity().findViewById(R.id.progress_icon);

        // Find the empty state view.
        emptyStateView = getActivity().findViewById(R.id.empty_view);

        // Inflate the root newzy item view.
        final View rootView = inflater.inflate(R.layout.newzy_item_list, container, false);

        // Set the root newzy item view to the newzy recycler view.
        newzyRecyclerView = rootView.findViewById(R.id.newzy_recycler_view);

        // For performance, tell newzyRecyclerView its size is fixed.
        newzyRecyclerView.setHasFixedSize(true);

        // Declare and set the recycler view layout manager.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        newzyRecyclerView.setLayoutManager(layoutManager);

        // Find the swipe refresh layout and set color scheme.
        swipeLayout = rootView.findViewById(R.id.swipe_layout);
        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        // Declare and set the OnRefreshListener to the swipe refresh layout.
        SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // Populate the Newzy list.
                getTheNewzys();
            }
        };
        swipeLayout.setOnRefreshListener(onRefreshListener);

        // Show the progress indicators.
        progressCircle.setVisibility(View.VISIBLE);
        progressIcon.setVisibility(View.VISIBLE);

        // Populate the Newzy list.
        getTheNewzys();

        return rootView;
    }

    private void getTheNewzys() {

        // Check Internet connectivity.
        ConnectivityManager cm = (ConnectivityManager) Objects.requireNonNull(getActivity()).
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        // For easy network connectivity checking.
        boolean weHaveConnectivity = activeNetwork != null && activeNetwork.isConnected();

        if (weHaveConnectivity) {
            // Initialize the loader. Pass in the newzy ID constant defined above and pass in null
            // bundle. Pass in this fragment for the LoaderCallbacks parameter (which is valid
            // because this fragment implements the LoaderCallbacks interface).
            LoaderManager.getInstance(this).restartLoader(NEWZY_LOADER_ID, null, this);

        } else {
            // Hide the progress indicators.
            progressCircle.setVisibility(View.GONE);
            progressIcon.setVisibility(View.GONE);

            // Hide the Newzy list view.
            newzyRecyclerView.setVisibility(View.GONE);

            // Set "No internet" notification/instruction and show the empty state view.
            emptyStateView.setText(R.string.no_internet);
            emptyStateView.setVisibility(View.VISIBLE);

            // Stop the swipe refresh animation.
            swipeLayout.setRefreshing(false);
        }
    }

    @NonNull
    @Override
    public Loader<List<Newzy>> onCreateLoader(int i, Bundle bundle) {

        // Hide the empty state view.
        emptyStateView.setVisibility(View.GONE);

        final String DEFAULT_REQUEST_URL = getString(R.string.default_request_url);

        // Create and return the new loader for the given request URL.
        return new NewzyLoader(getContext(), buildRequestUrl(DEFAULT_REQUEST_URL));
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Newzy>> loader) {

        // Clear the newzy adapter.
        newzyAdapter = null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Newzy>> loader, List<Newzy> newzyData) {

        // Hide the progress indicators.
        progressCircle.setVisibility(View.GONE);
        progressIcon.setVisibility(View.GONE);

        // Clear the newzy adapter.
        newzyAdapter = null;

        // If there is a valid list of newzyData returned, then add it to the adapter's data set.
        if (newzyData != null && !newzyData.isEmpty()) {

            // Hide the empty state view.
            emptyStateView.setVisibility(View.GONE);

            // Assign the data to the adapter and RecyclerView.
            newzyAdapter = new NewzyAdapter(getActivity(), newzyData);
            newzyRecyclerView.setAdapter(newzyAdapter);
            newzyAdapter.notifyDataSetChanged();

            // Show the Newzy RecyclerView.
            newzyRecyclerView.setVisibility(View.VISIBLE);

        } else {
            // Hide the Newzy recycler view.
            newzyRecyclerView.setVisibility(View.GONE);

            // Set "No Newzys" notification/instruction and show the empty state view.
            emptyStateView.setText(R.string.no_newzys);
            emptyStateView.setVisibility(View.VISIBLE);
        }
        // Stop the swipe refresh animation.
        swipeLayout.setRefreshing(false);
    }

    private String buildRequestUrl(String baseUrl) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // Preferences string variables.
        String newzysSortBy = sp.getString(
                getString(R.string.settings_sort_newzys_by_key),
                getString(R.string.settings_sort_newzys_by_default));

        String newzysToRetrieve = sp.getString(
                getString(R.string.settings_newzys_to_retrieve_key),
                getString(R.string.settings_newzys_to_retrieve_default));

        String newzysFromDate = sp.getString(
                getString(R.string.settings_newzys_from_date_key),
                getString(R.string.settings_newzys_from_date_default));

        String newzysToDate = sp.getString(
                getString(R.string.settings_newzys_to_date_key),
                getString(R.string.settings_newzys_to_date_default));

        // Break apart the URI string that's passed into its parameter.
        Uri baseUri = Uri.parse(baseUrl);

        // Prepare the baseUri that we just parsed so we can add query parameters to it.
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameters.
        uriBuilder.appendQueryParameter("q", MainActivity.newzysQuery);

        // If NOT empty, append FROM DATE parameter and its value.
        if (!newzysFromDate.isEmpty()) {
            uriBuilder.appendQueryParameter("from", newzysFromDate);
        }

        // If NOT empty, append TO DATE parameter and its value.
        if (!newzysToDate.isEmpty()) {
            uriBuilder.appendQueryParameter("to", newzysToDate);
        }

        uriBuilder.appendQueryParameter("sortBy", newzysSortBy);
        uriBuilder.appendQueryParameter("pageSize", newzysToRetrieve);
        uriBuilder.appendQueryParameter("apiKey", BuildConfig.NEWS_API_KEY);

        return uriBuilder.toString();
    }
}