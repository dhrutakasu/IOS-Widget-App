package com.ios.widget.Model;

public class WidgetData {
    String id, Type, Position, Number;

    public WidgetData(String id, String type, String position, String number) {
        this.id = id;
        Type = type;
        Position = position;
        Number = number;
    }

    public WidgetData(String type, String position, String number) {
        Type = type;
        Position = position;
        Number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }
}
