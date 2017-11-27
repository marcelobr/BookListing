package com.example.android.booklisting;

/**
 * Created by marcelo on 20/11/17.
 */

public class Book {

    private String title;
    private String smallThumbnail;
    private String[] authors;
    private String infoLink;

    public Book(String title, String smallThumbnail, String[] authors, String infoLink) {
        this.title = title;
        this.smallThumbnail = smallThumbnail;
        this.authors = authors;
        this.infoLink = infoLink;
    }

    public String getTitle() {
        return title;
    }

    public String getSmallThumbnail() {
        return smallThumbnail;
    }

    public String[] getAuthors() {
        return authors;
    }

    public String getInfoLink() {
        return infoLink;
    }
}
