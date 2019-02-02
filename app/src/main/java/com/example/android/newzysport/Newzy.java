package com.example.android.newzysport;

class Newzy {

    /**
     * Newzy section.
     */
    private final String newzySection;

    /**
     * Newzy publication date.
     */
    private final String newzyDate;

    /**
     * Newzy title.
     */
    private final String newzyTitle;

    /**
     * Newzy web URL.
     */
    private final String newzyUrl;

    /**
     * Newzy author.
     */
    private final String newzyAuthor;

    /**
     * Newzy image URL.
     */
    private final String newzyImage;

    /**
     * Create a Newzy object without time.
     *
     * @param section newzy section.
     * @param date newzy publication date.
     * @param title newzy publication time.
     * @param url newzy title.
     * @param author newzy web URL.
     * @param image newzy image URL.
     */
    public Newzy(String section, String date, String title, String url, String author, String image) {
        newzySection = section;
        newzyDate = date;
        newzyTitle = title;
        newzyUrl = url;
        newzyAuthor = author;
        newzyImage = image;
    }

    /**
     * Get the Newzy section.
     */
    public String getNewzySection() {
        return newzySection;
    }

    /**
     * Get the Newzy publication date.
     */
    public String getNewzyDate() {
        return newzyDate;
    }

    /**
     * Get the Newzy title.
     */
    public String getNewzyTitle() {
        return newzyTitle;
    }

    /**
     * Get the Newzy web URL.
     */
    public String getNewzyUrl() {
        return newzyUrl;
    }

    /**
     * Get the Newzy author.
     */
    public String getNewzyAuthor() {
        return newzyAuthor;
    }

    /**
     * Get the Newzy image URL.
     */
    public String getNewzyImage() {
        return newzyImage;
    }
}