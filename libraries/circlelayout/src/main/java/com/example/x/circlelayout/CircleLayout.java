/**
 * Developer: Abed Almoradi
 * Email: Abd.Almoradi@gmail.com
 * Date 11/11/2014
 * This class represents a circular layout where you can add items, control the radius of the circle and the density of the items
 * on the circle
 */

package com.example.x.circlelayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;



import java.util.Timer;
import java.util.TimerTask;


public class CircleLayout extends ViewGroup {
    private int radius_parameter=250; // Radius offset
    private int center_height_parameter=-10;// height offset
    private int center_width_parameter=0; //width offset
    private CircleLayoutAdapter adapter;
    private int child_count=10;

    private  boolean childs_isPinned=false; // true if the children is pinned to the cricle so they spin around a pivot
    private  int pinnded_childs_rotation_angle=0; // the rotation angle of the children when they are pinned
    private Activity parentActivity= (Activity) this.getContext();
    private float shiftAngle=0;
    private int current_step=0;
    private int old_step=0;
    private float oldX=0;
    private  Timer balancing_timer; // to schedule rotation balancing task
//    private int  dp=(int)convertDpToPixel(150,getContext());// Icon Size
//    private  CircleLayout.LayoutParams lp=new CircleLayout.LayoutParams(dp,dp);// layout paras
    private int  dp=0;
    private  CircleLayout.LayoutParams lp=new CircleLayout.LayoutParams(dp,dp);
    private boolean isRotateRight=false;
    private float mAngleOffset;
    private float mAngleRange;
    private boolean isRotationHappening = false;
    private boolean isCenteredImageVisible=false;
    private boolean isInitialized=false;

    public int getCurrent_step()
    {
        return current_step;
    }

    public void init()
    {
        pinnded_childs_rotation_angle=0;
        this.removeAllViews();
        this.invalidate();
        try {


            if(!isInitialized) {
                isInitialized=true;
                dp = getHeight() - 30;
                dp = Math.min(dp, 120);
                dp=(int)convertDpToPixel(dp,getContext());
                lp = new CircleLayout.LayoutParams(dp, dp);// layout paras
            }

            for (int i = 0; i < child_count / 2; i++) {
                int index = -1 * i;

                CircularLayoutItem civ =adapter.get(index);
                civ.setLayoutParams(lp);
                civ.setParent(this);
                this.addView(civ);
            }
            for (int i = child_count / 2; i > 0; i--) {
                int index = i;
                CircularLayoutItem civ =adapter.get(index);
                civ.setLayoutParams(lp);
                civ.setParent(this);
                this.addView(civ);
            }

            final int childs = getChildCount();
            float totalWeight = 0f;
            for (int i = 0; i < childs; i++) {
                final View child = getChildAt(i);
                LayoutParams lp = layoutParams(child);
                totalWeight += lp.weight;
            }
            shiftAngle = mAngleRange / totalWeight;
        }
        catch (Exception e){}
    }

    public CircleLayout(Context context) {
        this(context, null);
//        init();
    }


    @SuppressLint("NewApi")
    public CircleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
//        init();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleLayout, 0, 0);

        try {
            mAngleOffset = a.getFloat(R.styleable.CircleLayout_angleOffset, 90f);
            mAngleRange = a.getFloat(R.styleable.CircleLayout_angleRange, 360f);
        } finally {
            a.recycle();
        }


    }

    public boolean isRotationHappening() {
        return isRotationHappening;
    }

    public int get_pinnded_childs_rotation_angle() {
        return pinnded_childs_rotation_angle;
    }

    public boolean get_is_pinned_childs()
    {
        return childs_isPinned;
    }

    public  void setChildrenPinned(boolean pinned)
    {
        childs_isPinned=pinned;
        final int childs = getChildCount();
        for (int i = 0; i < childs; i++) {
            final CircularLayoutItem child = (CircularLayoutItem) getChildAt(i);
            child.invalidate();
        }
    }
    public int getRadius() {
        final int width = getWidth();
        final int height = getHeight();

        final float minDimen = width > height ? height : width;

        float radius = (minDimen )/2f;
        return (int) radius;
    }

    public void getCenter(PointF p) {
        p.set(getWidth()/2f, getHeight()/2);
    }

    public void setAngleOffset(float offset) {
        mAngleOffset = offset;
        requestLayout();
        invalidate();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        init();
    }

    public float getAngleOffset() {
        return mAngleOffset;
    }

    public void setAdapter(CircleLayoutAdapter adapter) {
        adapter.setContext(getContext());
        this.adapter = adapter;
        this.adapter.setParent(this);

    }
    public void setOffsetY(int y)
    {
        center_height_parameter=(int)convertDpToPixel(y,getContext());
    }

    public void setOffsetX(int X)
    {
        center_width_parameter=(int)convertDpToPixel(X,getContext());
    }

    public void setRadius(int r)
    {
        radius_parameter=(int)convertDpToPixel(r,getContext());
    }

    public void setChildrenCount(int c)
    {
        child_count=c;
//        init();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int count = getChildCount();

        int maxHeight = 0;
        int maxWidth = 0;

        // Find rightmost and bottommost child
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                maxWidth = Math.max(maxWidth, child.getMeasuredWidth());
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
            }
        }

        // Check against our minimum height and width
        maxHeight = Math.max(maxHeight/2, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth/2, getSuggestedMinimumWidth());

        int width = resolveSize(maxWidth, widthMeasureSpec);
        int height = resolveSize(maxHeight, heightMeasureSpec);


        setMeasuredDimension(width, height);

    }

    private LayoutParams layoutParams(View child) {
        return (LayoutParams) child.getLayoutParams();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub

        final int width = getWidth();
        final int height = getHeight();


        final float minDimen = width > height ? height : width;
        float radius = (minDimen )/2f;

        radius=(float) (width/2)+radius_parameter;
        int x =  width/2+center_width_parameter;
        int y =  height/3+(int)radius+center_height_parameter;
        Paint paint=new Paint();
        paint.setStrokeWidth(dp/10f);
        paint.setColor(Color.GRAY);
        paint.setAlpha(60);
        paint.setStyle(Paint.Style.STROKE);


//        float ratio=getAngleOffset()/shiftAngle;
//        int roundedRatio=Math.round(ratio);
//        int angle=(int) (roundedRatio*shiftAngle);

        canvas.drawCircle(x, y - radius, dp / 2 + 2, paint);
        super.onDraw(canvas);
    }
    @Override
    @SuppressWarnings("deprecation")
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int childs = getChildCount();

        float totalWeight = 0f;

        for(int i=0; i<childs; i++) {
            final View child = getChildAt(i);
            LayoutParams lp = layoutParams(child);
            totalWeight += lp.weight;
        }

        shiftAngle= mAngleRange/totalWeight;


        final int width = getWidth();
        final int height = getHeight();

        final float minDimen = width > height ? height : width;
        float radius = (minDimen )/2f;

        radius=(float) (width/2)+radius_parameter;

        float startAngle = mAngleOffset+270;

        for(int i=0; i<childs; i++) {
            final View child = getChildAt(i);

            final LayoutParams lp = layoutParams(child);

            final float angle = mAngleRange/totalWeight * lp.weight;


            final float centerAngle = startAngle ;
            final int x;
            final int y;
            if(child instanceof CircularLayoutItem)
            {
                CircularLayoutItem civ=(CircularLayoutItem)child;
                civ.setRotationParameters((int)(centerAngle)+90);
            }
            if(childs > 1) {
                x = (int) (radius * Math.cos(Math.toRadians(centerAngle))) + width/2+center_width_parameter;
                y = (int) (radius * Math.sin(Math.toRadians(centerAngle))) + height/3+(int)radius+center_height_parameter;

            } else {
                x = width/2;
                y = height/2;
            }

            final int halfChildWidth = child.getMeasuredWidth()/2;
            final int halfChildHeight = child.getMeasuredHeight()/2;

            final int left = lp.width != LayoutParams.FILL_PARENT ? x - halfChildWidth : 0;
            final int top = lp.height != LayoutParams.FILL_PARENT ? y - halfChildHeight : 0;
            final int right = lp.width != LayoutParams.FILL_PARENT ? x + halfChildWidth : width;
            final int bottom = lp.height != LayoutParams.FILL_PARENT ? y + halfChildHeight : height;

            child.layout(left, top, right, bottom);
            startAngle += angle;
        }

        invalidate();
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        LayoutParams lp = new LayoutParams(p.width, p.height);

        if(p instanceof LinearLayout.LayoutParams) {
            lp.weight = ((LinearLayout.LayoutParams) p).weight;
        }

        return lp;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action=event.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN) {
            this.oldX=event.getX();
            isRotationHappening=true;
            old_step=current_step;
        }
        if (action == MotionEvent.ACTION_MOVE) {
            float newX=event.getX();
            rotate((newX-this.oldX)/12);
            this.oldX=newX;
        }
        if (action == MotionEvent.ACTION_UP) {
            balanceRotate();
            if(old_step!=current_step) {
//                adapter.get(current_step).onFocus();
                adapter.get(old_step).onUnFocus();
            }
        }
        return true;
    }
    public void smoothRotate(float angle)
    {
        if(!isRotationHappening()) {

            try {
                balancing_timer.cancel();
                balancing_timer.purge();
            } catch (Exception e) {
//            e.printStackTrace();
            }
            balancing_timer = new Timer();
            RotationTimerTask timerTask = new RotationTimerTask(angle);
            balancing_timer.schedule(timerTask, 0, 15);
        }
    }
    public void rotate(float distance) {

        this.setAngleOffset((getAngleOffset()+distance));
        for(int i=0; i<getChildCount(); i++) {
            final View child = getChildAt(i);
            if(child instanceof CircularLayoutItem)
            {
                CircularLayoutItem civ=(CircularLayoutItem)child;
                civ.setRotationParameters((int)(getAngleOffset()));

            }
        }
        float ratio=getAngleOffset()/shiftAngle;
        int roundedRatio=Math.round(ratio);
        int angle1=(int) (roundedRatio*shiftAngle);

        int prev_step=current_step;
        current_step=(int)(angle1/shiftAngle);
        if(prev_step!=current_step)
        {
            isRotateRight=current_step>prev_step?false:true;
            if(isRotateRight)
                pinnded_childs_rotation_angle=20;
            else
                pinnded_childs_rotation_angle=-20;
            updateChilds();

        }

    }

    public void rotateToAngle(float angle) {

        this.setAngleOffset(angle);
        for(int i=0; i<getChildCount(); i++) {
            final View child = getChildAt(i);
            if(child instanceof CircularLayoutItem)
            {
                CircularLayoutItem civ=(CircularLayoutItem)child;
                civ.setRotationParameters((int) (getAngleOffset()));

            }
        }
        float ratio=getAngleOffset()/shiftAngle;
        int roundedRatio=Math.round(ratio);
        int angle1=(int) (roundedRatio*shiftAngle);

        int prev_step=current_step;
        current_step=(int)(angle1/shiftAngle);
        if(prev_step!=current_step)
        {
            isRotateRight=current_step>prev_step?false:true;
            if(isRotateRight)
                pinnded_childs_rotation_angle=20;
            else
                pinnded_childs_rotation_angle=-20;
            updateChilds();

        }
    }
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public void  updateChildAtIndex(int index)
    {
        int replacment_index=((-1*(current_step+index))%getChildCount()+getChildCount())%getChildCount();

        View v1=this.getChildAt(replacment_index);
        this.removeView(v1);

        CircularLayoutItem civ =adapter.get(current_step+index);
        civ.setLayoutParams(lp);
        civ.setParent(this);
        this.addView(civ,replacment_index);


    }
    public void updateChilds()
    {

        if(!isRotateRight)
            updateChildAtIndex(Math.round(getChildCount()/4));
        else
            updateChildAtIndex(-1*Math.round(getChildCount()/4));
    }
    public void balanceRotate()
    {
        if(childs_isPinned) {
            final int childs = getChildCount();
            for (int i = 0; i < childs; i++) {
                final CircularLayoutItem child = (CircularLayoutItem) getChildAt(i);
                child.balance();
            }
        }
        isRotationHappening=false;
        float ratio=getAngleOffset()/shiftAngle;
        int roundedRatio=Math.round(ratio);
        int angle=(int) (roundedRatio*shiftAngle);
        smoothRotate(angle);
        int prev_step=current_step;
        current_step=(int)(angle/shiftAngle);
        if(prev_step!=current_step)
            updateChilds();
    }
    public void rotateStep(int i)
    {
        if(!isRotationHappening) {
            adapter.get(current_step).onUnFocus();
            float ratio = getAngleOffset() / shiftAngle;
            int roundedRatio = Math.round(ratio);

            int angle = (int) (roundedRatio * shiftAngle + i * shiftAngle);
            smoothRotate(angle);
            int prev_step = current_step;
            current_step = (int) (angle / shiftAngle);
            if (prev_step != current_step)
                updateChilds();
//        balanceRotate();
//        adapter.get(current_step).onFocus();
        }
    }

    public void showCenteredImage()
    {
        isCenteredImageVisible=true;
        invalidate();
    }

    public void hideCenteredImage()
    {
        isCenteredImageVisible=false;
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        super.dispatchDraw(canvas);
        if(isCenteredImageVisible) {
            final int width = getWidth();
            final int height = getHeight();
//            dp=(int)convertDpToPixel(height/2,getContext());// Icon Size

            final float minDimen = width > height ? height : width;
            float radius = (minDimen) / 2f;

            radius = (float) (width / 2) + radius_parameter;
            int x = width / 2 + center_width_parameter;
            int y = height / 3 + (int) radius + center_height_parameter;
            Paint paint = new Paint();
            paint.setStrokeWidth(25f);
            paint.setColor(Color.rgb(10,10,10));
            paint.setAlpha(150);
            canvas.drawCircle(x, y - radius, dp / 2 + 2, paint);
            paint = new Paint();
            Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.mute);
            canvas.drawBitmap(b, x - b.getWidth() / 2, y - b.getHeight() / 2 - radius, paint);
        }
        return;

    }

    public static class LayoutParams extends ViewGroup.LayoutParams {

        public float weight = 1f;

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
        }
    }

    class RotationTimerTask extends TimerTask {

        int desiredAngle=0;
        float f_currentAngle=0;
        int  currentAngle=0;
        public RotationTimerTask(float angle) {
            isRotationHappening=true;
            desiredAngle=(int)angle;
            f_currentAngle=CircleLayout.this.getAngleOffset();
            currentAngle=(int)f_currentAngle;
            float difference=f_currentAngle-angle;
            if(difference>0 && difference<1)
                currentAngle=desiredAngle+1;
            if(difference>-1 && difference<0)
                currentAngle=desiredAngle-1;
        }
        @Override
        public void run() {
            CircleLayout.this.parentActivity.runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    if(currentAngle>desiredAngle)
                        CircleLayout.this.rotateToAngle(currentAngle-=1);
                    else if(currentAngle<desiredAngle)
                        CircleLayout.this.rotateToAngle(currentAngle+=1);
                    else
                    {
                        CircleLayout.this.balancing_timer.cancel();
                        balancing_timer.purge();
                        adapter.get(current_step).onFocus();
                        isRotationHappening=false;
                    }
                }});
        }

    }

}
