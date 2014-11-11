package com.robox.circlelayoutexample;


import com.robox.widget.CircleLayout;
import com.robox.widget.CircleLayoutAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity {
    CircleLayout normalWithRange ;
    public void btnClick(View v)
    {
        EditText childCount=(EditText)findViewById(R.id.txtChildCount);
        EditText offsetX=(EditText)findViewById(R.id.txtOffsetX);
        EditText offsetY=(EditText)findViewById(R.id.txtOffsetY);
        EditText radius=(EditText)findViewById(R.id.txtRad);
        try {
            if (radius.getText() != null) {
                normalWithRange.setRadius((Integer.parseInt(radius.getText().toString())));
                normalWithRange.init();
            }
        }
        catch (Exception e){Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT);}
        try {
            if (childCount.getText() != null) {
                normalWithRange.setChildrenCount((Integer.parseInt(childCount.getText().toString())));
                normalWithRange.init();
            }
        }
        catch (Exception e){Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT);}
        try {
            if (offsetX.getText() != null) {
                normalWithRange.setOffsetX((Integer.parseInt(offsetX.getText().toString())));
                normalWithRange.init();
            }
        }
        catch (Exception e){Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT);}
        try {
            if (offsetY.getText() != null) {
                normalWithRange.setOffsetY((Integer.parseInt(offsetY.getText().toString())));
                normalWithRange.init();
            }
        }
        catch (Exception e){Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT);}

    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        normalWithRange =(CircleLayout) findViewById(R.id.normalWithRange);

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
