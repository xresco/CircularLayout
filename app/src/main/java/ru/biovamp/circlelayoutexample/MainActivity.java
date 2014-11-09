package ru.biovamp.circlelayoutexample;


import ru.biovamp.widget.CircleLayout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;


public class MainActivity extends Activity {

	private char mState = 0;
	
	public boolean click(View v)
	{
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
		

	}
	

}
