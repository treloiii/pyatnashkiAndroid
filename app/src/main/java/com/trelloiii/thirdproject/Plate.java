package com.trelloiii.thirdproject;

import android.widget.Button;
import android.widget.ImageView;

public class Plate {
    private Button img;
    private int index;
    private int id;

    public Plate(Button img, int index) {
        this.img = img;
        this.index = index;
        this.id = img.getId();
    }

    public void setImg(Button img) {
        this.img = img;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Button getImg() {
        return img;
    }

    public int getIndex() {
        return index;
    }

    public int getId() {
        return id;
    }
}
