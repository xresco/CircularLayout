package ru.biovamp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CustomImageView extends Button {
	private int rotation_angle=0;
    private float onMotionDown_X,onMotionDown_Y;// coordinates when motion down event
	private boolean isPressed=false;

	public void setRotationParameters(int angle)// set the rotation angle of the object
	{
        rotation_angle=angle;
	}
	public CustomImageView(Context context) {
		super(context);
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
          //  ev.get
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
        Toast.makeText(getContext(), "fffff", Toast.LENGTH_SHORT).show();
    }
    public void init() //Initialize the object
    {
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
	
	@Override
	public void draw(Canvas canvas) {
	    canvas.save();
	    canvas.rotate(rotation_angle,this.getHeight()/2,this.getWidth()/2);
	    super.draw(canvas);
	    canvas.restore();
	}
}
