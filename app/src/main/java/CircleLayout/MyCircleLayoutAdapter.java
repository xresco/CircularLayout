package CircleLayout;

import android.graphics.drawable.Drawable;

import com.example.x.circlelayout.CircleLayoutAdapter;
import com.example.x.circlelayout.CircularLayoutItem;
import java.util.LinkedList;

/**
 * Created by mindvalley on 12/17/14.
 */


public class MyCircleLayoutAdapter extends CircleLayoutAdapter {


    private LinkedList<Integer> adapter=new LinkedList<>();
    private int startingIdIndex=0;
    private boolean isStartingIdSetten=false;


    public boolean setStartingIndex(int startingIndex) {
        if(!isStartingIdSetten)
        {
            isStartingIdSetten=true;
            startingIdIndex = startingIndex;

            return true;
        }
        return false;
    }

    @Override
    public void add(Object object)
    {
        Integer model=(Integer)object;
        adapter.add(model);


    }


    @Override
    public Integer getRoundedIndex(Integer index)
    {
        try {
            int new_index=((-1*index)%adapter.size()+adapter.size())%adapter.size();
            return new_index;
        }
        catch (Exception e) {
            return -1;
        }
    }

    public Object getCurrentObject()
    {
        return adapter.get( getRoundedIndex(getRoundedIndex(parent.getCurrent_step())-startingIdIndex));
    }

    @Override
    public CircularLayoutItem get(int index)
    {
        Integer drawable=adapter.get(getRoundedIndex((getRoundedIndex(index)-startingIdIndex)));
        MyCircleLayoutItem civ = new MyCircleLayoutItem(context);
        civ.setBackground(context.getResources().getDrawable(drawable));
        civ.setIndex(index);
        return civ;
    }


    public int getSize()
    {
        return adapter.size();
    }

}