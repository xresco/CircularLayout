package ru.biovamp.circlelayoutexample;


import java.util.Timer;
import java.util.TimerTask;

import ru.biovamp.widget.CircleLayout;
import ru.biovamp.widget.CustomeImageView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;


public class MainActivity extends Activity {

	private char mState = 0;
	
	public boolean click(View v)
	{

        CircleLayout cl=(CircleLayout)findViewById(R.id.normalWithRange);
        CustomeImageView civ=new CustomeImageView(getApplicationContext());
        ViewGroup.LayoutParams lp=new ViewGroup.LayoutParams(200,200);
        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        p.setMargins(90,90, 90, 90);
        civ.requestLayout();
        civ.setLayoutParams(lp);
        cl.addView(civ);
        cl.balanceRotate();

        return false;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/*
		 * All code below is NOT required. I've added it just for demonstration
		 * of different layout modes
		 */
		
		final View pie = findViewById(R.id.pie);
		final View normal = findViewById(R.id.normal);
		final CircleLayout normalWithRange =(CircleLayout) findViewById(R.id.normalWithRange);
		normalWithRange.setParentActivity(this);
		final SeekBar sk=(SeekBar) findViewById(R.id.seekBar);     
	    sk.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {       
		    @Override       
		    public void onStopTrackingTouch(SeekBar seekBar) {      
		        // TODO Auto-generated method stub      
		    }       
		    @Override       
		    public void onStartTrackingTouch(SeekBar seekBar) {     
		        // TODO Auto-generated method stub      
		    }       
		    @Override       
		    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {     
		        // TODO Auto-generated method stub 
		    	CircleLayout cl=(CircleLayout)normalWithRange;
		    	cl.setAngleOffset(Float.valueOf(String.valueOf(progress)));
	
		    }       
		});           
		
		final Button switchBtn = (Button) findViewById(R.id.switchBtn);
		switchBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				switch (mState) {
				case 0:
					normalWithRange.setVisibility(View.GONE);
					pie.setVisibility(View.GONE);
					normal.setVisibility(View.VISIBLE);
					
					switchBtn.setText(R.string.normalWidthRange);
					mState ++;
					break;
				case 1:
					normalWithRange.setVisibility(View.VISIBLE);
					
					pie.setVisibility(View.GONE);
					normal.setVisibility(View.GONE);
					
					switchBtn.setText(R.string.pie);
					mState++;
					break;
				case 2:				
					normalWithRange.setVisibility(View.GONE);
					pie.setVisibility(View.VISIBLE);
					normal.setVisibility(View.GONE);

					
					switchBtn.setText(R.string.normal);
					mState = 0;
					break;
					
				default:
					break;
				}
			}
		});
	}
	

}
