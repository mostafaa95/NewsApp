package com.example.android.newsapp;

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

public final class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    private static List<News> extractFeatureFromJson(String newsJSON) {

        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        List<News> newses = new ArrayList<>();


        try {
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            JSONObject BaseObject = baseJsonResponse.getJSONObject("response");
            JSONArray newsArray = BaseObject.getJSONArray("results");

            for (int i = 0; i < newsArray.length(); i++) {

                JSONObject currentNews = newsArray.getJSONObject(i);
                String title = currentNews.getString("sectionName");
                String information = currentNews.getString("webTitle");
                String date = currentNews.getString("webPublicationDate");
                // Extract the value for the key called "url"
                String url = currentNews.getString("webUrl");

                String authorName = "";
                JSONArray jsonTagsArray = currentNews.optJSONArray("tags");
                if (!jsonTagsArray.isNull(0)) {
                    JSONObject jsonTagsObject = jsonTagsArray.getJSONObject(0);
                    if (!jsonTagsObject.isNull("webTitle"))
                        authorName = jsonTagsObject.getString("webTitle");
                }

                News news = new News(title, information, authorName, date, url);
                newses.add(news);
            }
        } catch (JSONException e) {

            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        return newses;
    }


    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
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


    public static List<News> fetchNewsData(String requestUrl) {

        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<News> news = extractFeatureFromJson(jsonResponse);
        return news;
    }
}
