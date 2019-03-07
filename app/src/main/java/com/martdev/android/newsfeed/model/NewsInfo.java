package com.martdev.android.newsfeed.model;

public class NewsInfo {
    private String mImageURL;
    private String mTitle;
    private String mSource;
    private String mPublishedDate;
    private String mURL;

    public NewsInfo(String imageURL, String title, String source, String publishedDate, String URL) {
        mImageURL = imageURL;
        mTitle = title;
        mSource = source;
        mPublishedDate = publishedDate;
        mURL = URL;
    }

    public String getImageURL() {
        return mImageURL;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSource() {
        return mSource;
    }

    public String getPublishedDate() {
        return mPublishedDate;
    }

    public String getURL() {
        return mURL;
    }
}
