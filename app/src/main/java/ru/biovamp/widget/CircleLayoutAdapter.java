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


    private LinkedList<CustomImageView> adapter=new LinkedList<CustomImageView>();
    private Context context;

    public CircleLayoutAdapter(Context c)
    {
        context=c;
        int  dp=(int)convertDpToPixel(110,context);
        this.add(R.drawable.a1,dp);
        this.add(R.drawable.a2,dp);
        this.add(R.drawable.a3,dp);
        this.add(R.drawable.a4,dp);
        this.add(R.drawable.a5,dp);
        this.add(R.drawable.a6,dp);
        this.add(R.drawable.a7,dp);
        this.add(R.drawable.a8,dp);
        this.add(R.drawable.a9,dp);
        this.add(R.drawable.a10,dp);
        this.add(R.drawable.a11,dp);
        this.add(R.drawable.a12,dp);

    }
    public void add(Integer image,int size)
    {
        add(image,size,"");
    }
    public void add(Integer image,int size,String text)
    {
        CircleLayout.LayoutParams lp=new CircleLayout.LayoutParams(size,size);// layout paras
        CustomImageView civ = new CustomImageView(context);
        civ.setLayoutParams(lp);
        civ.setBackgroundResource(image);
        civ.setText(text);
        adapter.add(civ);
    }

    public CustomImageView get(int index)
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
