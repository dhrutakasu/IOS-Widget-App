package com.ios.widget.ImageModel;

public class AppWidgetMaster {

    boolean LoadingIndicator;
    int colorCode;
    int column;
    int cornerBorder;
    int cropType;
    boolean customMode;
    boolean flipControl;
    int interval;
    int opacity;
    int rotationType;
    int row;
    int shape;
    int size;
    int spaceBorder;
    int widgetId;
    int Id;

    public int describeContents() {
        return 0;
    }

    public AppWidgetMaster() {
        this.size = 15;
        this.shape = 0;
        this.flipControl = true;
        this.LoadingIndicator = true;
        this.customMode = false;
        this.cropType = 1;
        this.rotationType = 0;
        this.opacity = 255;
        this.cornerBorder = 15;
        this.spaceBorder = 12;
        this.interval = 2;
        this.row = 0;
        this.column = 0;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getWidgetId() {
        return this.widgetId;
    }

    public void setWidgetId(int i) {
        this.widgetId = i;
    }

    public int getColorCode() {
        return this.colorCode;
    }

    public void setColorCode(int i) {
        this.colorCode = i;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int i) {
        this.size = i;
    }

    public boolean isCustomMode() {
        return this.customMode;
    }

    public void setCustomMode(boolean z) {
        this.customMode = z;
    }

    public int getRow() {
        return this.row;
    }

    public void setRow(int i) {
        this.row = i;
    }

    public int getColumn() {
        return this.column;
    }

    public void setColumn(int i) {
        this.column = i;
    }

    public int getShape() {
        return this.shape;
    }

    public void setShape(int i) {
        this.shape = i;
    }

    public boolean isFlipControl() {
        return this.flipControl;
    }

    public void setFlipControl(boolean z) {
        this.flipControl = z;
    }

    public boolean isLoadingIndicator() {
        return this.LoadingIndicator;
    }

    public void setLoadingIndicator(boolean z) {
        this.LoadingIndicator = z;
    }

    public int getSpaceBorder() {
        return this.spaceBorder;
    }

    public void setSpaceBorder(int i) {
        this.spaceBorder = i;
    }

    public int getCropType() {
        return this.cropType;
    }

    public void setCropType(int i) {
        this.cropType = i;
    }

    public int getRotationType() {
        return this.rotationType;
    }

    public void setRotationType(int i) {
        this.rotationType = i;
    }

    public int getOpacity() {
        return this.opacity;
    }

    public void setOpacity(int i) {
        this.opacity = i;
    }

    public int getCornerBorder() {
        return this.cornerBorder;
    }

    public void setCornerBorder(int i) {
        this.cornerBorder = i;
    }

    public int getInterval() {
        return this.interval;
    }

    public void setInterval(int i) {
        this.interval = i;
    }

}
