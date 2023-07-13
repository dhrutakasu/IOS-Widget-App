package com.ios.widget.Model;

public class WidgetData {
    int id, Type, Position, Number;
    int Small, Medium, Large;

    public WidgetData(int id, int type, int small, int medium, int large, int position, int number) {
        this.id = id;
        Small = small;
        Medium = medium;
        Large = large;
        Type = type;
        Position = position;
        Number = number;
    }

    public WidgetData(int type, int position, int number) {
        Type = type;
        Position = position;
        Number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public int getPosition() {
        return Position;
    }

    public void setPosition(int position) {
        Position = position;
    }

    public int getNumber() {
        return Number;
    }

    public void setNumber(int number) {
        Number = number;
    }
}
