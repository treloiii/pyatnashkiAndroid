package com.trelloiii.thirdproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private GridLayout grid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        grid=findViewById(R.id.grid);

        for(int i=1;i<16;i++) {
            ImageView img = new ImageView(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Resources resources=this.getResources();
                img.setImageDrawable(resources.getDrawable(resources.getIdentifier("img"+i,"drawable",this.getPackageName())));
                img.setId(i);
                img.setOnClickListener(v->{
                    Toast.makeText(this,img.getId()+"",Toast.LENGTH_SHORT).show();
                    grid.removeView(img);
                });
            }

            grid.addView(img);

            img.getLayoutParams().height=250;
            img.getLayoutParams().width=250;
        }
    }
}
