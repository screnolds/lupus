package com.test.mylupusproject.ui.data;

import android.util.Log;

public class ExpandHelper {
    private int position = 0;
    private boolean expand = false;
    public ExpandHelper() {
    }

    public void setExpand(int position) {
        this.position = position;
        this.expand = true;
        Log.d("ExpandHelper", "Expand value set for Document Position: " + position);
    }

    public int getPosition() {
        return this.position;
    }

    public boolean getExpandValue() {
        return this.expand;
    }

    public void reset() {
        this.expand = false;
    }
}
