package com.ios.widget.ImageModel;

public class AppWidgetImages {

    String imageId;
    String uri;
    int widgetId;

    public AppWidgetImages(String str, String str2, int i) {
        this.imageId = str;
        this.uri = str2;
        this.widgetId = i;
    }

    public String getImageId() {
        return this.imageId;
    }

    public void setImageId(String str) {
        this.imageId = str;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String str) {
        this.uri = str;
    }

    public int getWidgetId() {
        return this.widgetId;
    }

    public void setWidgetId(int i) {
        this.widgetId = i;
    }
}
