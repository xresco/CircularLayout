package com.example.x.circlelayout;

import android.content.Context;

/**
 * Developer: Abed Almoradi
 * Email: Abd.Almoradi@gmail.com
 * Date 11/11/2014
 *
 */

public abstract class CircleLayoutAdapter {

    protected Context context;
    protected CircleLayout parent;
    public void setContext(Context context) {
        this.context = context;
    }

    public  void setParent(CircleLayout parent)
    {
        this.parent = parent;
    }

    public abstract void add(Object item);


    public abstract CircularLayoutItem get(int index);


    public abstract Integer getRoundedIndex(Integer index);


}
