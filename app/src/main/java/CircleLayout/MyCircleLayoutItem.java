package CircleLayout;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.widget.Toast;

import com.example.x.circlelayout.CircleLayout;
import com.example.x.circlelayout.CircularLayoutItem;


public class MyCircleLayoutItem extends CircularLayoutItem {



    public MyCircleLayoutItem(Context context) {
        super(context);initialize();
    }

    public MyCircleLayoutItem(Context context, CircleLayout cl) {
        super(context, cl);
        initialize();
    }

    public MyCircleLayoutItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public MyCircleLayoutItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    public void initialize()
    {


        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick() {
                Toast.makeText(getContext(),"Item number: "+getIndex(),Toast.LENGTH_SHORT).show();
            }
        });

        this.setOnFocusListener(new OnFocusListener()
        {
            @Override
            public void onFocus() {
//                Toast.makeText(getContext(),"Item number: "+getIndex()+ " is now on focus ",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUnFocus() {

            }
        });
    }



}
