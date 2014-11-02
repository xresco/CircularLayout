package ru.biovamp.widget;

import android.content.Context;
import android.view.ViewGroup;

import java.util.LinkedList;

/**
 * Created by x on 11/2/2014.
 */
public class CircleLayoutAdapter {


    private LinkedList<CustomeImageView> adapter=new LinkedList<CustomeImageView>();


    public void add(CustomeImageView v)
    {
        adapter.add(v);
    }

    public CustomeImageView get(int index)
    {
        return adapter.get(index%adapter.size());
    }

}
