package com.trelloiii.thirdproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.LinearGradient;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    private GridLayout grid;
    private final int MAX_SIZE=2;
    private Map<ImageView,Integer> imgs=new LinkedHashMap<>();
    private ImageView first=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        grid=findViewById(R.id.grid);

        for(int i=1;i<=16;i++) {
            ImageView img = new ImageView(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Resources resources=this.getResources();
                img.setImageDrawable(resources.getDrawable(resources.getIdentifier("img"+i,"drawable",this.getPackageName())));
                img.setId(i-1);
                final int id=i-1;
                img.setOnClickListener(v->{
                    if(first==null) {
                        first = (ImageView) v;
                        Toast.makeText(this, "FIRST " + first.getId(), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if((first.getId()-v.getId())==1||(first.getId()-v.getId())==-1||(first.getId()-v.getId())==4||(first.getId()-v.getId())==-4) {
                            Toast.makeText(this, "SECOND true", Toast.LENGTH_SHORT).show();
                            grid.removeView(first);
                            grid.removeView(v);
                            grid.addView(v,first.getId());
                            grid.addView(first,v.getId());
                            int buf=v.getId();
                            v.setId(first.getId());
                            first.setId(buf);
                            first=null;
                        }
                        else
                            Toast.makeText(this,"SECOND false",Toast.LENGTH_SHORT).show();
                    }

//                    if(imgs.size()<MAX_SIZE) {
//                        imgs.put(img,img.getId());
//                        imgs.ge
//                        grid.removeView(img);
//                    }
//                    else {
//                        Toast.makeText(this,"SOSI "+imgs.size(),Toast.LENGTH_SHORT).show();
//                    }
                });
            }

            grid.addView(img,i-1);

            img.getLayoutParams().height=250;
            img.getLayoutParams().width=250;
        }
    }
}
