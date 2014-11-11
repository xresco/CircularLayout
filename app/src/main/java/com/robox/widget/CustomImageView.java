package com.robox.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.Toast;

import com.robox.widget.CircleLayout;

public class CustomImageView extends Button {
	private int rotation_angle=0;
    private float onMotionDown_X,onMotionDown_Y;// coordinates when motion down event
	private boolean isPressed=false;
    private int index;
    private CircleLayout parent;

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setRotationParameters(int angle)// set the rotation angle of the object
	{
        rotation_angle=angle;
        this.invalidate();
	}

    public CustomImageView(Context context, CircleLayout cl) {
		super(context);
        parent=cl;
		init();
	}
	public CustomImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }
    public CustomImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)  // to pass the touch event to the parent
    {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            onMotionDown_X = ev.getRawX();
            onMotionDown_Y = ev.getRawY();
        }
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            //if move event occurs
            long downTime = SystemClock.uptimeMillis();
            long eventTime = SystemClock.uptimeMillis() + 100;
            float x = ev.getRawX();
            float y = ev.getRawY();
            if((Math.abs(onMotionDown_X-x)>10) || (Math.abs(onMotionDown_Y-y)>10)) {
                int metaState = 0;
                //create new motion event
                MotionEvent motionEvent = MotionEvent.obtain(
                        downTime,
                        eventTime,
                        MotionEvent.ACTION_DOWN,
                        x,
                        y,
                        metaState
                );
                ((CircleLayout) this.getParent()).dispatchTouchEvent(motionEvent); //send event to listview
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    public void onClick() //the function get activated whenever click event occurs
    {
       // setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        Toast.makeText(getContext(), index+"", Toast.LENGTH_SHORT).show();
    }
    public void init() //Initialize the object
    {
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
    	this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action=event.getAction();
				if(action==MotionEvent.ACTION_DOWN)
                    isPressed=true;
				else if(action==MotionEvent.ACTION_UP)
				{
                    onClick();
					if(isPressed)
					{
                        isPressed=false;
						return false;
					}
				}
				return true;
			}
		});
    }
	public void balance()
    {
        CircleLayout cl=(CircleLayout)this.getParent();
        Animation an = new RotateAnimation(0, -1*parent.get_pinnded_childs_rotation_angle(), this.getHeight() / 2, this.getWidth() / 2);
        an.setDuration(300);               // duration in ms
        an.setRepeatCount(0);                // -1 = infinite repeated
        an.setRepeatMode(Animation.REVERSE); // reverses each repeat
        an.setFillAfter(true);
        this.setAnimation(an);
    }
	@Override
	public void draw(Canvas canvas) {
	   canvas.save();
       CircleLayout cl=(CircleLayout)this.getParent();
       if(!parent.get_is_pinned_childs())
	        canvas.rotate(rotation_angle%360,this.getHeight()/2,this.getWidth()/2);
       else
            canvas.rotate(parent.get_pinnded_childs_rotation_angle(), this.getHeight() / 2, this.getWidth() / 2);

	    super.draw(canvas);
	    canvas.restore();
	}
}
