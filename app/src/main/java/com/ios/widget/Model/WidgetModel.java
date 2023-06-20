package com.ios.widget.Model;

public class WidgetModel {
    int Small, Medium, Large;

    public WidgetModel(int small, int medium, int large) {
        Small = small;
        Medium = medium;
        Large = large;
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
}
