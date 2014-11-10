package ru.biovamp.circlelayoutexample;


import ru.biovamp.widget.CircleLayout;
import ru.biovamp.widget.CircleLayoutAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		final CircleLayout normalWithRange =(CircleLayout) findViewById(R.id.normalWithRange);
        CircleLayoutAdapter ad=new CircleLayoutAdapter();
        ad.add(R.drawable.a1);
        ad.add(R.drawable.a2);
        ad.add(R.drawable.a3);
        ad.add(R.drawable.a4);
        ad.add(R.drawable.a5);
        ad.add(R.drawable.a6);
        ad.add(R.drawable.a7);
        ad.add(R.drawable.a8);
        ad.add(R.drawable.a9);
        ad.add(R.drawable.a10);
        ad.add(R.drawable.a11);
        ad.add(R.drawable.a12);
        normalWithRange.setAdapter(ad);
        normalWithRange.setChildrenCount(10);
        normalWithRange.setRadius(120);
        //normalWithRange.setChildrenPinned(true);


	}
	

}
