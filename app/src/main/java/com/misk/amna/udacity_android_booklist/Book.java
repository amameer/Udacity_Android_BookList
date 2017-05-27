package com.misk.amna.udacity_android_booklist;

/**
 * Created by Amna on 4/22/2017 AD.
 */

public class Book {
    String mBookTitle;
    String mBookAuthors;
    String mUrl;

    public Book(String mBookTitle, String mBookAuthors, String mUrl) {
        this.mBookTitle = mBookTitle;
        this.mBookAuthors = mBookAuthors;
        this.mUrl = mUrl;
    }

    public String getmBookTitle() {
        return mBookTitle;
    }


    public String getmBookAuthers() {
        return mBookAuthors;
    }


    public String getmUrl() {
        return mUrl;
    }

    @Override
    public String toString() {
        return "Book{" +
                "mBookTitle='" + mBookTitle + '\'' +
                ", mBookAuthors='" + mBookAuthors + '\'' +
                ", mUrl='" + mUrl + '\'' +
                '}';
    }
}
