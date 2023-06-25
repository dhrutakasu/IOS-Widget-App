package com.ios.widget.Model;

public class WidgetModel {
    int Small, Medium, Large;
    String Title;

    public WidgetModel(int small, int medium, int large) {
        Small = small;
        Medium = medium;
        Large = large;
    }


    public WidgetModel(int small, int medium, int large,String title) {
        Small = small;
        Medium = medium;
        Large = large;
        Title = title;
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
}
