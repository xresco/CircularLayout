package ru.biovamp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class CustomeImageView extends Button {
	int angle=0;

	boolean bool=false;
	public void setRotationParameters(int r)
	{
		angle=r;
	}
	public CustomeImageView(Context context) {
		super(context);
		init();
	}
	public CustomeImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CustomeImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
        
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            //if move event occurs
            long downTime = SystemClock.uptimeMillis();
            long eventTime = SystemClock.uptimeMillis() + 100;
            float x = ev.getRawX();
            float y = ev.getRawY();
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
            //I store a reference to listview in this layout
            ((CircleLayout)this.getParent()).dispatchTouchEvent(motionEvent); //send event to listview
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }
    
    public void init()
    {
    	this.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action=event.getAction();
				if(action==MotionEvent.ACTION_DOWN)
				{
					bool=true;
			//		Toast.makeText(getContext(), "llll", Toast.LENGTH_SHORT).show();
					return true;
				}
				
				if(action==MotionEvent.ACTION_UP)
				{
					Toast.makeText(getContext(), "fffff", Toast.LENGTH_SHORT).show();
					if(bool)
					{
						bool=false;
						
					}
				
				}
				return false;
				
				
			}
		});
    }
	
//	@Override
//	protected void onDraw(Canvas canvas) {
//		// TODO Auto-generated method stub
//		canvas.rotate(60, 10, 10);
//
//		//super.onDraw(canvas);
//
//		canvas.restore();
//	}
	
	@Override
	public void draw(Canvas canvas) {
	    canvas.save();
	    canvas.rotate(angle,this.getHeight()/2,this.getWidth()/2);
	    super.draw(canvas);
	    canvas.restore();
	}
}
