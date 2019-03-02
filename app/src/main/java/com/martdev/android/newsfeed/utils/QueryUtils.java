package com.martdev.android.newsfeed.utils;

import android.text.TextUtils;
import android.util.Log;

import com.martdev.android.newsfeed.R;
import com.martdev.android.newsfeed.model.NewsInfo;

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

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<NewsInfo> getNewsData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        return extractJsonData(jsonResponse);
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

        HttpURLConnection connection = null;
        InputStream inputStream = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + connection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving news data.", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
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

    private static List<NewsInfo> extractJsonData(String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }

        List<NewsInfo> newsInfoList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray articleArray = jsonObject.optJSONArray("articles");

            for (int i = 0; i < articleArray.length(); i++) {

                JSONObject object = articleArray.getJSONObject(i);
                JSONObject source = object.getJSONObject("source");

                String newsSource = source.getString("name");

                String newsAuthor = object.getString("author");
                if (newsAuthor.equals("null")) {
                    newsAuthor = "No author";
                }

                String title = object.getString("title");
                String url = object.getString("url");
                String urlImage = object.getString("urlToImage");
                String publishedAt = object.getString("publishedAt");

                NewsInfo newsInfo = new NewsInfo(urlImage, newsAuthor, title, newsSource,
                        publishedAt, url);

                newsInfoList.add(newsInfo);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the JSON results. ", e);
        }

        return newsInfoList;
    }
}

