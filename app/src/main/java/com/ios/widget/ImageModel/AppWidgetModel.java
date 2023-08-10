package com.ios.widget.ImageModel;

public class AppWidgetModel {
    int Small, Medium, Large,Position;
    String Title;

    public AppWidgetModel(int small, int medium, int large) {
        Small = small;
        Medium = medium;
        Large = large;
    }

    public AppWidgetModel(int small, int medium, int large, String title, int position) {
        Small = small;
        Medium = medium;
        Large = large;
        Title = title;
        Position = position;
    }

    public int getSmall() {
        return Small;
    }

    public void setSmall(int small) {
        Small = small;
    }

    public int getMedium() {
        return Medium;
    }

    public void setMedium(int medium) {
        Medium = medium;
    }

    public int getLarge() {
        return Large;
    }

    public void setLarge(int large) {
        Large = large;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getPosition() {
        return Position;
    }

    public void setPosition(int position) {
        Position = position;
    }
}
