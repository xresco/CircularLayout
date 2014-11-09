/*
 * Copyright dmitry.zaicew@gmail.com Dmitry Zaitsev
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
 
package ru.biovamp.widget;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import ru.biovamp.circlelayoutexample.MainActivity;

import ru.biovamp.circlelayoutexample.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Region.Op;
import android.graphics.Xfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class CircleLayout extends ViewGroup {
	//my Parameters

	int radius_parameter=250;
	int center_height_parameter=0;
    int center_width_parameter=0;
    CircleLayoutAdapter adapter=new CircleLayoutAdapter(getContext());
    //GestureDetector gestureDetector = null;





    int child_count=2;


	 //----------
    Activity parentActivity;
    private float shiftAngle=0;
    private int current_step=0;
    private float oldX=0;
    private  Timer balancing_timer; // to schedule rotation balancing task
    private int  dp=(int)convertDpToPixel(110,getContext());// Icon Size
    private  CircleLayout.LayoutParams lp=new CircleLayout.LayoutParams(dp,dp);// layout paras
    private boolean isRotateRight=false;
    //----------
//	public static final int LAYOUT_NORMAL = 1;
//	public static final int LAYOUT_PIE = 2;

//	private int mLayoutMode = 1;
	
	private Drawable mInnerCircle;
	
	private float mAngleOffset;
	private float mAngleRange;
	
	private float mDividerWidth;
	private int mInnerRadius;
	
	private Paint mDividerPaint;
	private Paint mCirclePaint;
	
	private RectF mBounds = new RectF();
	
	private Bitmap mDst;
	private Bitmap mSrc;
	private Canvas mSrcCanvas;
	private Canvas mDstCanvas;
	private Xfermode mXfer;
	private Paint mXferPaint;
	
	private View mMotionTarget;
	
	private Bitmap mDrawingCache;
	private Canvas mCachedCanvas;
	private Set<View> mDirtyViews = new HashSet<View>();
	private boolean mCached = false;


//    @Override
//    public int getChildCount() {
//
//        return child_count;
//    }
//
//    @Override
//    public View getChildAt(int index) {
//        return adapter.get(index);
//    }
//
//    @Override
//    public void addView(View child) {
//         adapter.add((CustomeImageView)child);
//        super.addView(child);
//    }
//    @Override
//    public void addView(View child,int index) {
//        adapter.add((CustomeImageView)child);
//        super.addView(child,index);
//    }
    public void init()
    {

//        CustomeImageView civ=new CustomeImageView(getContext());
//        CircleLayout.LayoutParams lp=new CircleLayout.LayoutParams(200,200);
//        //  ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
//        //   p.setMargins(90,90, 90, 90);
//        civ.requestLayout();
//        civ.setLayoutParams(lp);
//        this.addView(civ);

        //int childCount=14;

        for(int i=0;i<child_count/2;i++)
        {
            int index=-1*i;
            CustomImageView civ = new CustomImageView(getContext());
            civ.setLayoutParams(lp);
            civ.setBackgroundResource(adapter.get( index));
            civ.setText("Test" + ( index));

            this.addView(civ);

        }
        for(int i=child_count/2;i>0;i--)
        {
            int index=i;
            CustomImageView civ = new CustomImageView(getContext());
            civ.setLayoutParams(lp);
            civ.setBackgroundResource(adapter.get(  index));
            civ.setText("Test" + ( index));

            this.addView(civ);

        }
        final int childs = getChildCount();

        float totalWeight = 0f;

        for(int i=0; i<childs; i++) {
            final View child = getChildAt(i);

            LayoutParams lp = layoutParams(child);

            totalWeight += lp.weight;
        }
        //    Toast.makeText(getContext(),childs,Toast.LENGTH_SHORT).show();

        shiftAngle= mAngleRange/totalWeight;

    }

    public CircleLayout(Context context) {
		this(context, null);
        init();
	}
	
	
	@SuppressLint("NewApi")
	public CircleLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
        init();
		//gestureDetector = new GestureDetector(getContext(),new MyGestureDetector());
		mDividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleLayout, 0, 0);
		
		try {
			int dividerColor = a.getColor(R.styleable.CircleLayout_sliceDivider, android.R.color.darker_gray);
			mInnerCircle = a.getDrawable(R.styleable.CircleLayout_innerCircle);
			
			if(mInnerCircle instanceof ColorDrawable) {
				int innerColor = a.getColor(R.styleable.CircleLayout_innerCircle, android.R.color.white);
				mCirclePaint.setColor(innerColor);
			}
			
			mDividerPaint.setColor(dividerColor);
			
			mAngleOffset = a.getFloat(R.styleable.CircleLayout_angleOffset, 90f);
			//currentAngle=mAngleOffset;
			mAngleRange = a.getFloat(R.styleable.CircleLayout_angleRange, 360f);
			mDividerWidth = a.getDimensionPixelSize(R.styleable.CircleLayout_dividerWidth, 1);
			mInnerRadius = a.getDimensionPixelSize(R.styleable.CircleLayout_innerRadius, 0);
			
		//	mLayoutMode = a.getColor(R.styleable.CircleLayout_layoutMode, LAYOUT_NORMAL);
		} finally {
			a.recycle();
		}
		
		mDividerPaint.setStrokeWidth(mDividerWidth);
		
		mXfer = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
		mXferPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		//Turn off hardware acceleration if possible
		if(Build.VERSION.SDK_INT >= 11) {
			setLayerType(LAYER_TYPE_SOFTWARE, null);
		}

	}
	
	public void setParentActivity(MainActivity parentActivity) {
		this.parentActivity = parentActivity;
	}
//	public void setLayoutMode(int mode) {
//		mLayoutMode = mode;
//		requestLayout();
//		invalidate();
//	}
//
//	public int getLayoutMode() {
//		return mLayoutMode;
//	}
//
	public int getRadius() {
		final int width = getWidth();
		final int height = getHeight();
		
		final float minDimen = width > height ? height : width;
		
		float radius = (minDimen - mInnerRadius)/2f;
		//radius=2*width;
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
	
	public float getAngleOffset() {
		return mAngleOffset;
	}
	
	public void setInnerRadius(int radius) {
		mInnerRadius = radius;
		requestLayout();
		invalidate();
	}
	
	public int getInnerRadius() {
		return mInnerRadius;
	}
	
	public void setInnerCircle(Drawable d) {
		mInnerCircle = d;
		requestLayout();
		invalidate();
	}
	
	public void setInnerCircle(int res) {
		mInnerCircle = getContext().getResources().getDrawable(res);
		requestLayout();
		invalidate();
	}
	
	public void setInnerCircleColor(int color) {
		mInnerCircle = new ColorDrawable(color);
		requestLayout();
		invalidate();
	}
	
	public Drawable getInnerCircle() {
		return mInnerCircle;
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
		maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
		maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
		
		int width = resolveSize(maxWidth, widthMeasureSpec);
		int height = resolveSize(maxHeight, heightMeasureSpec);
		
		setMeasuredDimension(width, height);
		
		if(mSrc != null && (mSrc.getWidth() != width || mSrc.getHeight() != height)) {
			mDst.recycle();
			mSrc.recycle();
			mDrawingCache.recycle();
			
			mDst = null;
			mSrc = null;
			mDrawingCache = null;
		}
		
		if(mSrc == null) {
			mSrc = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			mDst = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			mDrawingCache = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			
			mSrcCanvas = new Canvas(mSrc);
			mDstCanvas = new Canvas(mDst);
			mCachedCanvas = new Canvas(mDrawingCache);
		}
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
		 float radius = (minDimen - mInnerRadius)/2f;
		 
		radius=(float) (width/2)+radius_parameter;
		int x =  width/2+center_width_parameter;
		int y =  height+(int)radius/2+center_height_parameter;
		Paint paint=new Paint();
		paint.setStrokeWidth(15f);
		paint.setColor(Color.GRAY);
		paint.setAlpha(60);
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawCircle(x, y, radius, paint);
//		for(int i=0;i<getChildCount();i++)
//            drawChild(canvas,i,layoutParams(getChildAt(i)));
	
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
		float radius = (minDimen - mInnerRadius)/2f;
		 
		radius=(float) (width/2)+radius_parameter;
		mBounds.set(width/2 - minDimen/2, height/2 - minDimen/2, width/2 + minDimen/2, height/2 + minDimen/2);
		
		float startAngle = mAngleOffset+270;
		
		for(int i=0; i<childs; i++) {
			final View child = getChildAt(i);
			
			final LayoutParams lp = layoutParams(child);
			
			final float angle = mAngleRange/totalWeight * lp.weight;
			
			
			final float centerAngle = startAngle ;
			final int x;
			final int y;
			if(child instanceof CustomImageView)
			{
				CustomImageView civ=(CustomImageView)child;
				civ.setRotationParameters((int)(centerAngle)+90);
			}
			if(childs > 1) {
				x = (int) (radius * Math.cos(Math.toRadians(centerAngle))) + width/2+center_width_parameter;
				y = (int) (radius * Math.sin(Math.toRadians(centerAngle))) + height+(int)radius/2+center_height_parameter;
				
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
			
			if(left != child.getLeft() || top != child.getTop()
					|| right != child.getRight() || bottom != child.getBottom()
					|| lp.startAngle != startAngle
					|| lp.endAngle != startAngle + angle) {
				mCached = false;
			}
			
			lp.startAngle = startAngle;
			
			startAngle += angle;
			
			lp.endAngle = startAngle;
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
		}
		if (action == MotionEvent.ACTION_MOVE) {
		  float newX=event.getX();			  
		  rotate((newX-this.oldX)/5);
	      this.oldX=newX;
		}
		if (action == MotionEvent.ACTION_UP) {
		  balanceRotate();

		}
		  return true;
	}
	public void smoothRotate(float angle)
	{
       // if(balancing_timer.)
        try {
            balancing_timer.cancel();
            balancing_timer.purge();
        }
       catch (Exception e)
       {

       }
        balancing_timer=new Timer();
		RotationTimerTask timerTask=new RotationTimerTask(angle);
	   // timer.cancel();
        balancing_timer.schedule(timerTask, 0, 5);
	}
	public void rotate(float distance) {

		this.setAngleOffset((getAngleOffset()+distance));
	      for(int i=0; i<getChildCount(); i++) {
				final View child = getChildAt(i);	
			    if(child instanceof CustomImageView)
				{
					CustomImageView civ=(CustomImageView)child;
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
            updateChilds();

        }

	}
	
	public void rotateToAngle(float angle) {

		this.setAngleOffset(angle);
	      for(int i=0; i<getChildCount(); i++) {
				final View child = getChildAt(i);	
			    if(child instanceof CustomImageView)
				{
					CustomImageView civ=(CustomImageView)child;
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
        CustomImageView civ=new CustomImageView(getContext());
        civ.setLayoutParams(lp);
        civ.setBackgroundResource(adapter.get(current_step+index));
        civ.setText("Test"+(current_step+index));
        civ.setIndex(current_step+index);
        View v1=this.getChildAt(replacment_index);
        this.removeView(v1);
        this.addView(civ,replacment_index);
    }
	public void updateChilds()
    {

        if(!isRotateRight)
            updateChildAtIndex(Math.round(getChildCount()/4));
        else
            updateChildAtIndex(-1*Math.round(getChildCount()/4));
//        CustomeImageView civ=new CustomeImageView(getContext());
//        int replacment_index=((-1*(current_step+2))%6+6)%6;
//        int  dp=(int)convertDpToPixel(110,getContext());
//        CircleLayout.LayoutParams lp=new CircleLayout.LayoutParams(dp,dp);
//        civ.setLayoutParams(lp);
//        civ.setBackgroundResource(adapter.get(current_step+2));
//        civ.setText("Test"+(current_step+2));
//        View v1=this.getChildAt(replacment_index);
//        this.removeView(v1);
//        this.addView(civ,replacment_index);
//
//         civ=new CustomeImageView(getContext());
//         replacment_index=((-1*(current_step-2))%6+6)%6;
//        //  dp=(int)convertDpToPixel(110,getContext());
//       //  lp=new CircleLayout.LayoutParams(dp,dp);
//        civ.setLayoutParams(lp);
//        civ.setBackgroundResource(adapter.get(current_step-2));
//        civ.setText("Test"+(current_step-2));
//         v1=this.getChildAt(replacment_index);
//        this.removeView(v1);
//        this.addView(civ,replacment_index);





       // Toast.makeText(getContext(),current_step+"_"+replacment_index,Toast.LENGTH_LONG).show();
    }
	public void balanceRotate()
	{

		float ratio=getAngleOffset()/shiftAngle;
		int roundedRatio=Math.round(ratio);
		int angle=(int) (roundedRatio*shiftAngle);

		smoothRotate(angle);
        int prev_step=current_step;
        current_step=(int)(angle/shiftAngle);
        if(prev_step!=current_step)
            updateChilds();
	}
//	@Override
//	public boolean dispatchTouchEvent(MotionEvent ev) {
//		
//		if(mLayoutMode == LAYOUT_NORMAL) {
//			return super.dispatchTouchEvent(ev);
//		}
//		
//		final int action = ev.getAction();
//		final float x = ev.getX() - getWidth()/2f;
//		final float y = ev.getY() - getHeight()/2f;
//		
//		if(action == MotionEvent.ACTION_DOWN) {
//			
//			if(mMotionTarget != null) {
//				MotionEvent cancelEvent = MotionEvent.obtain(ev);
//				cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
//				
//				cancelEvent.offsetLocation(-mMotionTarget.getLeft(), -mMotionTarget.getTop());
//				
//				mMotionTarget.dispatchTouchEvent(cancelEvent);
//				cancelEvent.recycle();
//				
//				mMotionTarget = null;
//			}
//			
//			final float radius = (float) Math.sqrt(x*x + y*y);
//			
//			if(radius < mInnerRadius || radius > getWidth()/2f || radius > getHeight()/2f) {
//				return false;
//			}
//			
//			float angle = (float) Math.toDegrees(Math.atan2(y, x));
//			
//			if(angle < 0) angle += mAngleRange;
//			
//			final int childs = getChildCount();
//			
//			for(int i=0; i<childs; i++) {
//				final View child = getChildAt(i);
//				final LayoutParams lp = layoutParams(child);
//				
//				float startAngle = lp.startAngle % mAngleRange;
//				float endAngle = lp.endAngle % mAngleRange;
//				float touchAngle = angle;
//				
//				if(startAngle > endAngle) {
//					if(touchAngle < startAngle && touchAngle < endAngle) {
//						touchAngle += mAngleRange;
//					}
//						
//					endAngle += mAngleRange;
//				}
//				
//				if(startAngle <= touchAngle && endAngle >= touchAngle) {
//					ev.offsetLocation(-child.getLeft(), -child.getTop());
//					
//					boolean dispatched = child.dispatchTouchEvent(ev);
//					
//					if(dispatched) {
//						mMotionTarget = child;
//						
//						return true;
//					} else {
//						ev.setLocation(0f, 0f);
//						
//						return onTouchEvent(ev);
//					}
//				}
//			}
//		} else if(mMotionTarget != null) {
//			ev.offsetLocation(-mMotionTarget.getLeft(), -mMotionTarget.getTop());
//			
//			mMotionTarget.dispatchTouchEvent(ev);
//			
//			if(action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
//				mMotionTarget = null;
//			}
//		}
//		
//		return onTouchEvent(ev);
//	}
//

//	class MyGestureDetector extends SimpleOnGestureListener {
////		@Override
////		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
////				float distanceY) {
////			// TODO Auto-generated method stub
////		//	Toast.makeText(getApplicationContext(), "ddd", Toast.LENGTH_SHORT);
////			return super.onScroll(e1, e2, distanceX, distanceY);
////		}
//	    @Override
//	    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//	            float velocityY) {
////	        if (e1.getX() < e2.getX()) {
////	           // currPosition = getVisibleViews("left");
////	        } else {
////	           // currPosition = getVisibleViews("right");
////	        }
////
//	  //  	balanceRotate();
//	       // horizontalScrollView.smoothScrollTo(layouts.get(currPosition) .getLeft(), 0);
//	        return true;
//	    }
//	}
//
	private void drawChild(Canvas canvas, int childIndex, LayoutParams lp) {
        View child=getChildAt(childIndex);
        drawChild(canvas,child,lp);
	}

    private void drawChild(Canvas canvas, View child, LayoutParams lp) {
        mSrcCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        mDstCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        mSrcCanvas.save();

        int childLeft = child.getLeft();
        int childTop = child.getTop();
        int childRight = child.getRight();
        int childBottom = child.getBottom();

        mSrcCanvas.clipRect(childLeft, childTop, childRight, childBottom, Op.REPLACE);
        mSrcCanvas.translate(childLeft, childTop);
        //mSrcCanvas.rotate(60f);

        child.draw(mSrcCanvas);

        mSrcCanvas.restore();

        mXferPaint.setXfermode(null);
        mXferPaint.setColor(Color.BLACK);

        float sweepAngle = (lp.endAngle - lp.startAngle) % 361;

        mDstCanvas.drawArc(mBounds, lp.startAngle, sweepAngle, true, mXferPaint);
        mXferPaint.setXfermode(mXfer);
        mDstCanvas.drawBitmap(mSrc, 0f, 0f, mXferPaint);

        canvas.drawBitmap(mDst, 0f, 0f, null);
    }
	
	private void redrawDirty(Canvas canvas) {
        int counter=0;
		for(View child : mDirtyViews) {
			drawChild(canvas, counter++, layoutParams(child));
		}
		
//		if(mMotionTarget != null) {
//			drawChild(canvas, mMotionTarget, layoutParams(mMotionTarget));
//		}
	}
	
	private void drawDividers(Canvas canvas, float halfWidth, float halfHeight, float radius) {
		final int childs = getChildCount();
		
		if(childs < 2) {
			return;
		}
		
		for(int i=0; i<childs; i++) {
			final View child = getChildAt(i);			
			LayoutParams lp = layoutParams(child);			
			
			canvas.drawLine(halfWidth, halfHeight,
					radius * (float) Math.cos(Math.toRadians(lp.startAngle)) + halfWidth,
					radius * (float) Math.sin(Math.toRadians(lp.startAngle)) + halfHeight,
					mDividerPaint);
			
			if(i == childs-1) {
				canvas.drawLine(halfWidth, halfHeight,
						radius * (float) Math.cos(Math.toRadians(lp.endAngle)) + halfWidth,
						radius * (float) Math.sin(Math.toRadians(lp.endAngle)) + halfHeight,
						mDividerPaint);
			}
		}
	}
	
	private void drawInnerCircle(Canvas canvas, float halfWidth, float halfHeight) {
		if(mInnerCircle != null) {
			if(!(mInnerCircle instanceof ColorDrawable)) {
				mInnerCircle.setBounds(
						(int) halfWidth - mInnerRadius,
						(int) halfHeight - mInnerRadius,
						(int) halfWidth + mInnerRadius,
						(int) halfHeight + mInnerRadius);
				
				mInnerCircle.draw(canvas);
			} else {
				canvas.drawCircle(halfWidth, halfHeight, mInnerRadius, mCirclePaint);
			}
		}
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
//		if(mLayoutMode == LAYOUT_NORMAL) {
//
//		}
        super.dispatchDraw(canvas);
        return;

//		if(mSrc == null || mDst == null || mSrc.isRecycled() || mDst.isRecycled()) {
//			return;
//		}
//
//		final int childs = getChildCount();
//
//		final float halfWidth = getWidth()/2f;
//		final float halfHeight = getHeight()/2f;
//
//		final float radius = halfWidth > halfHeight ? halfHeight : halfWidth;
//
//		if(mCached && mDrawingCache != null && !mDrawingCache.isRecycled() && mDirtyViews.size() < childs/2) {
//			canvas.drawBitmap(mDrawingCache, 0f, 0f, null);
//
//			redrawDirty(canvas);
//
//			drawDividers(canvas, halfWidth, halfHeight, radius);
//
//			drawInnerCircle(canvas, halfWidth, halfHeight);
//
//			return;
//		} else {
//			mCached = false;
//		}
//
//		Canvas sCanvas = null;
//
//		if(mCachedCanvas != null) {
//			sCanvas = canvas;
//			canvas = mCachedCanvas;
//		}
//
//		Drawable bkg = getBackground();
//		if(bkg != null) {
//			bkg.draw(canvas);
//		}
//
//		for(int i=0; i<childs; i++) {
//			final View child = getChildAt(i);
//			LayoutParams lp = layoutParams(child);
//
//			drawChild(canvas, i, lp);
//		}
//
//		drawDividers(canvas, halfWidth, halfHeight, radius);
//
//		drawInnerCircle(canvas, halfWidth, halfHeight);
//
//		if(mCachedCanvas != null) {
//			sCanvas.drawBitmap(mDrawingCache, 0f, 0f, null);
//			mDirtyViews.clear();
//			mCached = true;
//		}
	}
	
	public static class LayoutParams extends ViewGroup.LayoutParams {

		private float startAngle;
		private float endAngle;
		
		public float weight = 1f;
		
		public LayoutParams(int width, int height) {
			super(width, height);
		}
		
		public LayoutParams(Context context, AttributeSet attrs) {
			super(context, attrs);
		}
	}
	
	class RotationTimerTask extends TimerTask {

		float desiredAngle=0;
	    float  currentAngle=0;
		public RotationTimerTask(float angle) {
			desiredAngle=angle;
			currentAngle=CircleLayout.this.getAngleOffset();
		//	Toast.makeText(getContext(), desiredAngle+"_"+currentAngle, Toast.LENGTH_SHORT).show();
		}
		  @Override
		  public void run() {
			  CircleLayout.this.parentActivity.runOnUiThread(new Runnable(){
				    @Override
				    public void run() {

				    	if(currentAngle>=desiredAngle+1)
				    		CircleLayout.this.rotateToAngle(currentAngle-=1);
				    	else if(currentAngle<=desiredAngle-1)
				    		CircleLayout.this.rotateToAngle(currentAngle+=1);
				    	else
				    	{
				    		CircleLayout.this.balancing_timer.cancel();
                            balancing_timer.purge();
				    	}
				    }});
			 }
		  
		 }

}
