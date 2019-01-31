package com.example.android.newzysport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class NewzyUtils {

    private NewzyUtils() {
        // Required empty constructor.
    }

    // Tag for the log messages.
    private static final String LOG_TAG = NewzyUtils.class.getSimpleName();

    public static List<Newzy> fetchNewzys(String requestUrl) {

        // Assign the URL object.
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back.
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        // Extract results from the JSON response and return a list of Newzy objects.
        return extractResultsFromJson(jsonResponse);
    }

    // Returns new URL object from the given URL string.
    private static URL createUrl(String stringUrl) {
        URL url = null;

        try {
            url = new URL(stringUrl);

        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    // Make an HTTP request to the given URL and return a String as the response.
    private static String makeHttpRequest(URL url) throws IOException {

        // Declare connectivity constants.
        final int TIMEOUT_10_SECONDS = 10000;
        final int TIMEOUT_15_SECONDS = 15000;

        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }
        // Initialize URL connection and input stream.
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(TIMEOUT_10_SECONDS);
            urlConnection.setConnectTimeout(TIMEOUT_15_SECONDS);
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);

            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {

            Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
        } finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    // Convert the InputStream into a String which contains the whole JSON response from the server.
    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();

            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }

    // Return a list of Newzy objects that has been built up from parsing a JSON response.
    private static List<Newzy> extractResultsFromJson(String responseJSON) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(responseJSON)) {
            return null;
        }
        // Create an empty Newsy ArrayList that so we can start adding Newzy items.
        List<Newzy> newzys = new ArrayList<>();

        try {
            // Get JSON Object.
            JSONObject jsonResponseObj = new JSONObject(responseJSON);

            // Get the JSON "articles" array.
            JSONArray articlesArray = jsonResponseObj.getJSONArray("articles");

            for (int i = 0; i < articlesArray.length(); ++i) {

                // The current result in the "articles" array.
                JSONObject currentArticle = articlesArray.getJSONObject(i);

                JSONObject source = currentArticle.getJSONObject("source");
                String section = source.optString("name");

                String date = currentArticle.optString("publishedAt");
                String title = currentArticle.optString("title");
                String url = currentArticle.optString("url");
                String author = currentArticle.optString("author");
                String image = currentArticle.optString("urlToImage");

                newzys.add(new Newzy(section, date, title, url, author, image));
            }

        } catch (JSONException e) {

            Log.e("NewzyUtils", "Problem parsing the JSON results", e);
        }

        // Return the list of Newzys.
        return newzys;
    }
}