package com.example.x.circlelayout;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Developer: Abed Almoradi
 * Email: Abd.Almoradi@gmail.com
 * Date 11/11/2014
 */


public abstract class CircularLayoutItem extends LinearLayout {
	private int rotation_angle=0;
    private float onMotionDown_X,onMotionDown_Y;// coordinates when motion down event
	private boolean isPressed=false;
    private int index;
    protected CircleLayout parent;
    private ImageView background;
    private ImageView forground;
    private OnClickListener clickListener;
    private OnFocusListener focusListener;

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setRotationParameters(int angle)// set the rotation angle of the object
	{
        rotation_angle=angle;
        rotate();
        this.invalidate();
	}


    public void setParent(CircleLayout parent) {
        this.parent = parent;
    }

    public CircularLayoutItem(Context context) {
        super(context);
        init();
    }

    public CircularLayoutItem(Context context, CircleLayout cl) {
		super(context);
        parent=cl;
		init();
	}
	public CircularLayoutItem(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }
    public CircularLayoutItem(Context context, AttributeSet attrs, int defStyle)
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

    public  void onClick()
    {
        clickListener.onClick();
    }
    public  void onFocus()
    {
        focusListener.onFocus();
    }
    public  void onUnFocus()
    {
        focusListener.onUnFocus();
    }


    public void init() //Initialize the object
    {
        setWillNotDraw(false);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_image_view_layout, this);
        background = (ImageView) findViewById(R.id.background);
        forground = (ImageView) findViewById(R.id.forground);

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
        Animation an = new RotateAnimation(0, -1*parent.get_pinnded_childs_rotation_angle(), this.getHeight() / 2, this.getWidth() / 2);
        an.setDuration(300);               // duration in ms
        an.setRepeatCount(0);                // -1 = infinite repeated
        an.setRepeatMode(Animation.REVERSE); // reverses each repeat
        an.setFillAfter(true);
        this.setAnimation(an);
    }


    private void rotate()
    {
       if(background!=null)
       if(!parent.get_is_pinned_childs())
           background.setRotation(rotation_angle%360);

       else
           background.setRotation(parent.get_pinnded_childs_rotation_angle());

        if(forground!=null)
            if(!parent.get_is_pinned_childs())
                forground.setRotation(rotation_angle%360);

            else
                forground.setRotation(parent.get_pinnded_childs_rotation_angle());

    }

    @Override
    public void setBackground(Drawable d)
    {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            background.setBackgroundDrawable(d);
        } else {
            background.setBackground(d);
        }
        background.setScaleType(ImageView.ScaleType.FIT_XY);
    }


    public void setForground(Drawable d)
    {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            forground.setBackgroundDrawable(d);
        } else {
            forground.setBackground(d);
        }
    }

    public interface OnClickListener {
        public void onClick();
    }
    public interface OnFocusListener {
        public void onFocus();
        public void onUnFocus();
    }

    public void setOnClickListener(OnClickListener o)
    {
        clickListener=o;
    }
    public void setOnFocusListener(OnFocusListener o)
    {
        focusListener=o;
    }

}
