package com.trelloiii.thirdproject;

import androidx.appcompat.app.AlertDialog;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
    private List<Integer> positions=new ArrayList<>(16);
    private ImageView first=null;
    private Button changeButton;
    private boolean change=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        grid=findViewById(R.id.grid);
        changeButton=findViewById(R.id.change);

        for(int i=1;i<=16;i++) {
            ImageView img = new ImageView(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Resources resources=this.getResources();
                img.setImageDrawable(resources.getDrawable(resources.getIdentifier("img"+i,"drawable",this.getPackageName())));
                img.setId(i-1);
                final int id=i-1;
                img.setOnClickListener(v->{
                    if(change) {
                        if (first == null) {
                            first = (ImageView) v;
                            //Toast.makeText(this, "FIRST " + imgs.get(first), Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(this, "SECOND " + imgs.get(v), Toast.LENGTH_SHORT).show();
                            int firstId = imgs.get(first);
                            int secId = imgs.get(v);
                            if ((firstId - secId) == 1 || (firstId - secId) == -1 || (firstId - secId) == 4 || (firstId - secId) == -4) {
                                if(first.getId()==Integer.valueOf(15)||v.getId()==Integer.valueOf(15)) {
                                    grid.removeAllViews();
                                    imgs.remove(v);
                                    imgs.remove(first);
                                    imgs.put((ImageView) v, firstId);
                                    imgs.put(first, secId);
                                    imgs = MapUtil.sortByValue(imgs);
                                    for (Map.Entry<ImageView, Integer> entry : imgs.entrySet()) {
                                        grid.addView(entry.getKey(), entry.getValue());
                                        System.out.println(entry.getKey().getId());
                                    }
                                   // alert("debug",Arrays.toString(MapUtil.checkWin(imgs)));
                                    //Toast.makeText(this, "CHECK WIN " + Arrays.toString(MapUtil.checkWin(imgs)), Toast.LENGTH_LONG).show();
                                    MapUtil.checkWin(imgs);
                                    first = null;
                                }
                                else{
                                    first=null;
                                }

                            }
                            else {
                                first = null;
                            }
                        }
                    }
                    else{
                        Toast.makeText(this, "FIRST " + imgs.get(v), Toast.LENGTH_SHORT).show();
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
            imgs.put(img,i-1);
            positions.add(i-1);
            grid.addView(img,i-1);

            img.getLayoutParams().height=250;
            img.getLayoutParams().width=250;
        }
        for(Map.Entry<ImageView,Integer> entry:imgs.entrySet())
            System.out.println(entry.getKey()+":"+entry.getValue());
        changeButton.setOnClickListener(v->{
            this.change=!this.change;
        });

    }

    public void alert(String title,String message){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        AlertDialog alert=builder.create();
        alert.show();
    }
}
class MapUtil {
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            list.sort(Map.Entry.comparingByValue());
        }

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    public static int[] checkWin(Map<ImageView,Integer> map){
        int[] arr=new int[16];
        int i=0;
        for(Map.Entry<ImageView,Integer> entry:map.entrySet()){
            arr[i]=entry.getKey().getId();
            i++;
        }
        int counter=arr.length;
        for(int j=0;j<arr.length-1;j++){
            System.out.println(arr[j]);
            if((arr[j+1]-arr[j])==1)
                counter--;
        }
        return arr;
    }
}
