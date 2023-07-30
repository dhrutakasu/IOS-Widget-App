package com.ios.widget.Model;

public class WidgetData {
    int id, Type, Position, Number,Temp;
    String City;

    public WidgetData(int id, int type, int position, int number, String city, int temp) {
        this.id = id;
        Type = type;
        Position = position;
        Number = number;
        City = city;
        Temp = temp;
    }

    public WidgetData(int type, int position, int number, String city, int temp) {
        Type = type;
        Position = position;
        Number = number;
        City = city;
        Temp = temp;
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

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public int getTemp() {
        return Temp;
    }

    public void setTemp(int temp) {
        Temp = temp;
    }
}
