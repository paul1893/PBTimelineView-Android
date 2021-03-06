package com.thefrenchtouch.pbtimelineview.object;

/**
 * Created by Paul on 09/07/16.
 */
public class PBItem {

    private int section;            // The position in the section of the timelineview
    private int id;                 // His id
    private int width;              // His width
    private int height;             // His height
    private String text;            // His text
    private int backgroundColor;    // His background color
    private int textSize;           // His text size
    private int textColor;          // His text color

    /**
     * Constructor
     * @param  width
     * @param height
     * @param section
     * @param id
     * @param text
     * @param backgroundColor
     * @param textSize
     * @param textColor
     * */
    public PBItem(int width, int height, int section, int id, String text, int backgroundColor, int textSize, int textColor) {
        this.width = width;
        this.height = height;
        this.section = section;
        this.id = id;
        this.text = text;
        this.backgroundColor = backgroundColor;
        this.textSize = textSize;
        this.textColor = textColor;

        setupItemConfiguration();
    }

    /**
     * Configure the item
     * */
    private void setupItemConfiguration() {
        // For now, do nothing ...
    }

    public int getItemWidth() {
        return width;
    }

    public void setItemWidth(int width) {
        this.width = width;
    }

    public int getItemHeight() {
        return height;
    }

    public void setItemHeight(int height) {
        this.height = height;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}