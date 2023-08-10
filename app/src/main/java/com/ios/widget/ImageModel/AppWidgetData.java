package com.ios.widget.ImageModel;

public class AppWidgetData {
    boolean Sim;
    int id, Type, Position, Number,Temp;
    String City;

    public AppWidgetData(int id, int type, int position, int number, String city, int temp, boolean sim) {
        this.id = id;
        Type = type;
        Position = position;
        Number = number;
        City = city;
        Temp = temp;
        Sim = sim;
    }

    public AppWidgetData(int type, int position, int number, String city, int temp, boolean sim) {
        Type = type;
        Position = position;
        Number = number;
        City = city;
        Temp = temp;
        Sim = sim;
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

    public boolean getSim() {
        return Sim;
    }

    public void setSim(boolean sim) {
        Sim = sim;
    }
}
