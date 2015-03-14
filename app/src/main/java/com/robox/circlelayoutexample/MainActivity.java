package com.robox.circlelayoutexample;



import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import com.example.x.circlelayout.CircleLayout;
import com.example.x.circlelayout.CircleLayoutAdapter;
import CircleLayout.*;


public class MainActivity extends Activity implements SeekBar.OnSeekBarChangeListener{
    int progressChanged = 0;
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        progressChanged = i;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        if(seekBar.getId()==R.id.skRad) {
            circularLayout.setRadius(progressChanged);
            circularLayout.init();
        }
        if(seekBar.getId()==R.id.skChildCount) {
            circularLayout.setChildrenCount(progressChanged);
            circularLayout.init();
            circularLayout.balanceRotate();
        }
        if(seekBar.getId()==R.id.skOffsetX) {
            circularLayout.setOffsetX(progressChanged-90);
            circularLayout.init();
        }
        if(seekBar.getId()==R.id.skOffsetY) {
            circularLayout.setOffsetY(progressChanged-60);
            circularLayout.init();
        }
    }

    CircleLayout circularLayout ;

    public void tglClick(View v)
    {
        ToggleButton tg=(ToggleButton)findViewById(R.id.tglBtn);
        circularLayout.setChildrenPinned(tg.isChecked());
        circularLayout.init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SeekBar skRad = (SeekBar) findViewById(R.id.skRad);
        skRad.setOnSeekBarChangeListener(this);

        SeekBar skOffsetX = (SeekBar) findViewById(R.id.skOffsetX);
        skOffsetX.setOnSeekBarChangeListener(this);

        SeekBar skOffsetY = (SeekBar) findViewById(R.id.skOffsetY);
        skOffsetY.setOnSeekBarChangeListener(this);

        SeekBar skChildCount = (SeekBar) findViewById(R.id.skChildCount);
        skChildCount.setOnSeekBarChangeListener(this);



        circularLayout =(CircleLayout) findViewById(R.id.circularLayout);

        MyCircleLayoutAdapter ad=new MyCircleLayoutAdapter();
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
        circularLayout.setAdapter(ad);
        circularLayout.setChildrenCount(10);
        circularLayout.setRadius(120);
        //circularLayout.setChildrenPinned(true);


    }


}
