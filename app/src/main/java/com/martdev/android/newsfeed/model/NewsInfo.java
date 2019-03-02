package com.martdev.android.newsfeed.model;

public class NewsInfo {
    private String mImageURL;
    private String mAuthor;
    private String mTitle;
    private String mSource;
    private String mPublishedDate;
    private String mURL;

    public NewsInfo(String imageURL, String author, String title, String source, String publishedDate, String URL) {
        mImageURL = imageURL;
        mAuthor = author;
        mTitle = title;
        mSource = source;
        mPublishedDate = publishedDate;
        mURL = URL;
    }

    public String getImageURL() {
        return mImageURL;
    }

    public String getAuthor() {
        return mAuthor;
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
