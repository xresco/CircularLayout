package com.robox.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.LinkedList;

/**
 * Created by x on 11/2/2014.
 */
public class CircleLayoutAdapter {


    private LinkedList<Integer> adapter=new LinkedList<Integer>();
    private Context context;
    private CircleLayout parent;
    public void setContext(Context context) {
        this.context = context;
    }

    public void setParent(CircleLayout parent) {
        this.parent = parent;
    }

    public void add(Integer image)
    {
        adapter.add(image);
        if (parent!=null)
         parent.init();

    }

    public Integer get(int index)
    {
        int new_index=((-1*index)%adapter.size()+adapter.size())%adapter.size();
        return adapter.get(new_index);
    }
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }
}
