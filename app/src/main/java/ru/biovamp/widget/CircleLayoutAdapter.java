package ru.biovamp.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.ViewGroup;

import java.util.LinkedList;

import ru.biovamp.circlelayoutexample.R;

/**
 * Created by x on 11/2/2014.
 */
public class CircleLayoutAdapter {


    private LinkedList<Integer> adapter=new LinkedList<Integer>();
    private Context context;

    public CircleLayoutAdapter(Context c)
    {
        context=c;
        this.add(R.drawable.a1);
        this.add(R.drawable.a2);
        this.add(R.drawable.a3);
        this.add(R.drawable.a4);
        this.add(R.drawable.a5);
        this.add(R.drawable.a6);
        this.add(R.drawable.a7);
        this.add(R.drawable.a8);
        this.add(R.drawable.a9);
        this.add(R.drawable.a10);
        this.add(R.drawable.a11);
        this.add(R.drawable.a12);

    }

    public void add(Integer image)
    {
//        CircleLayout.LayoutParams lp=new CircleLayout.LayoutParams(size,size);// layout paras
//        CustomImageView civ = new CustomImageView(context);
//        civ.setLayoutParams(lp);
//        civ.setBackgroundResource(image);
//        civ.setText(text);
        adapter.add(image);
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
