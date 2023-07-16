package com.ios.widget.Model;

public class WidgetData {
    int id, Type, Position, Number;

    public WidgetData(int id, int type, int position, int number) {
        this.id = id;
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
