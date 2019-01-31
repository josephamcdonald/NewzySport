package com.example.android.newzysport;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

public class NewzyLoader extends AsyncTaskLoader<List<Newzy>> {

    /** Declare the newzy request URL */
    private String newzyUrl;

    NewzyLoader(Context context, String url) {
        super(context);

        // Assign the newzy URl.
        newzyUrl = url;
    }

    @Override
    protected void onStartLoading() {

        forceLoad();
    }

    @Override
    public List<Newzy> loadInBackground() {

        // Don't perform the request if there are no URLs, or the first URL is null.
        if (newzyUrl == null) {
            return null;
        }
        return NewzyUtils.fetchNewzys(newzyUrl);
    }
}