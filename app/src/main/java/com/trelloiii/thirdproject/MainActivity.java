package com.trelloiii.thirdproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.Intent;
import android.content.res.ColorStateList;
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
import java.util.Random;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    private GridLayout grid;
    public static int HEIGHT=4;
    private Map<Button,Integer> imgs=new LinkedHashMap<>();
    private List<Integer> positions=new ArrayList<>(16);
    private Button first=null;
    private Button changeButton;
    private ImageView restart,settings;
    private boolean change=true;
    private int moveCounter=0;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        grid=findViewById(R.id.grid);
        changeButton=findViewById(R.id.change);
        restart=findViewById(R.id.restart);
        settings=findViewById(R.id.settings);

        for(int i=1;i<=HEIGHT*HEIGHT;i++) {
            Button img = new Button(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Resources resources=this.getResources();
                //img.setImageDrawable(resources.getDrawable(resources.getIdentifier("img"+i,"drawable",this.getPackageName())));
                if(i<16){
                    img.setBackground(getDrawable(R.drawable.button0_15));
                    img.setText(i+"");
                }
                else{
                    img.setBackground(getDrawable(R.drawable.button16));
                }
                img.setTextColor(getColor(R.color.button0_15_text));
                img.setId(i-1);
                final int id=i-1;
                img.setOnClickListener(v->{
                    if(change) {
                        if (first == null) {
                            first = (Button) v;
                            //Toast.makeText(this, "FIRST " + imgs.get(first), Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(this, "SECOND " + imgs.get(v), Toast.LENGTH_SHORT).show();
                            int firstId = imgs.get(first);
                            int secId = imgs.get(v);
                            if(checkBotCorner(firstId)){
                                if ((firstId - secId) == -1 || (firstId - secId) == 4){
                                    swap(firstId,secId,v);
                                }
                                else {
                                    first = null;
                                }
                            }
                            else if(checkTopCorner(firstId)){
                                if ((firstId - secId) == 1 || (firstId - secId) == -4){
                                    swap(firstId,secId,v);
                                }
                                else {
                                    first = null;
                                }
                            }
                            else if(checkLeftSide(firstId)){
                                if ( (firstId - secId) == -1 || (firstId - secId) == 4 || (firstId - secId) == -4){
                                    swap(firstId,secId,v);
                                }
                                else {
                                    first = null;
                                }
                            }
                            else if(checkRightSide(firstId)){
                                if ((firstId - secId) == 1 || (firstId - secId) == -4 || (firstId - secId) == 4){
                                    swap(firstId,secId,v);
                                }
                                else {
                                    first = null;
                                }
                            }
                            else {
                                if ((firstId - secId) == 1 || (firstId - secId) == -1 || (firstId - secId) == 4 || (firstId - secId) == -4) {
                                    swap(firstId,secId,v);
                                }
                                else {
                                    first = null;
                                }
                            }
//                            if ((firstId - secId) == 1 || (firstId - secId) == -1 || (firstId - secId) == 4 || (firstId - secId) == -4) {
//
//
//                            }

                        }
                    }
                    else{
                        Toast.makeText(this, "FIRST " + imgs.get(v), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            imgs.put(img,i-1);
            positions.add(i-1);
            grid.addView(img,i-1);

            img.getLayoutParams().height=245;
            img.getLayoutParams().width=245;
        }
        changeButton.setOnClickListener(v->{
           // this.change=!this.change;
            randomMove();


        });
        for(int i=0;i<50;i++) {
            randomMove();
        }

    }

    public void randomMove(){
        Plate black=getBlack(imgs);
        List<Plate> nears=getNearBlack(black,imgs);
        Random r=new Random();
        Plate random=nears.get(r.nextInt(nears.size()));
        first=black.getImg();
        swap(black.getIndex(),random.getIndex(),random.getImg());
    }
    public void alert(String title,String message){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        AlertDialog alert=builder.create();
        alert.show();
    }

    public void swap(int firstId,int secId,View v){
        if(first.getId()==Integer.valueOf(15)||v.getId()==Integer.valueOf(15)) {
            grid.removeAllViews();
            imgs.remove(v);
            imgs.remove(first);
            imgs.put((Button) v, firstId);
            imgs.put(first, secId);
            imgs = MapUtil.sortByValue(imgs);
            for (Map.Entry<Button, Integer> entry : imgs.entrySet()) {
                grid.addView(entry.getKey(), entry.getValue());
                System.out.println(entry.getKey().getId());
            }
            moveCounter++;
//            if(MapUtil.checkWin(imgs)==1)
//                alert("Победа!","Вы закончили игру за "+moveCounter+" шагов");
            first = null;
        }
        else{
            first=null;
        }
    }

    public Plate getBlack(Map<Button,Integer> map){
        Button black=null;
        int blackIndex=0;
        for(Map.Entry<Button,Integer> entry:map.entrySet()){
            if(entry.getKey().getId()==Integer.valueOf(15)){
                black=entry.getKey();
                blackIndex=entry.getValue();
                break;
            }
        }
        return new Plate(black,blackIndex);
    }

    /*
         1 - Right
        -1 - Left
         4 - Top
        -4 - Bot
     */
    public List<Plate> getNearBlack(Plate black,Map<Button,Integer> map){
       // alert("debug",checkBotCorner(black.getIndex())+":"+String.valueOf(black.getIndex()%4));
        List<Plate> resultList=new ArrayList<>();
        for(Map.Entry<Button,Integer> entry:map.entrySet()){
            if(checkBotCorner(entry.getValue())){
                if((entry.getValue()-black.getIndex())==-1|| (entry.getValue()-black.getIndex())==4){
                    resultList.add(new Plate(entry.getKey(),entry.getValue()));
                }
            }
            else if(checkTopCorner(entry.getValue())){
                if((entry.getValue()-black.getIndex())==1|| (entry.getValue()-black.getIndex())==-4){
                    resultList.add(new Plate(entry.getKey(),entry.getValue()));
                }
            }
            else if(checkLeftSide(entry.getValue())){
                if((entry.getValue()-black.getIndex())==-1||(entry.getValue()-black.getIndex())==4||
                        (entry.getValue()-black.getIndex())==-4){
                    resultList.add(new Plate(entry.getKey(),entry.getValue()));
                }
            }
            else if(checkRightSide(entry.getValue())){
                if((entry.getValue()-black.getIndex())==1|| (entry.getValue()-black.getIndex())==-4||
                        (entry.getValue()-black.getIndex())==4){
                    resultList.add(new Plate(entry.getKey(),entry.getValue()));
                }
            }
            else {
                if ((entry.getValue() - black.getIndex()) == 1 || (entry.getValue() - black.getIndex()) == -1 ||
                        (entry.getValue() - black.getIndex()) == 4 || (entry.getValue() - black.getIndex()) == -4) {
                    resultList.add(new Plate(entry.getKey(), entry.getValue()));
                }
            }
        }
        Toast.makeText(this,resultList.size()+"",Toast.LENGTH_LONG).show();
        return resultList;
    }

    public boolean checkTopCorner(int index){
        return checkBotCorner(index*HEIGHT);
    }
    public boolean checkBotCorner(int index){
        return (HEIGHT==(index/HEIGHT+1))&&(index%HEIGHT==0);
    }
    public boolean checkLeftSide(int index){
        if(index!=0){
            return index%HEIGHT==0;
        }
        else {
            return false;
        }
    }
    public boolean checkRightSide(int index){
        boolean a=false;
        for(int i=1;i<=HEIGHT-2;i++){
            a=checkTopCorner(index-HEIGHT*i);
            if(a)
                break;
        }
        return a;
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

    public static int checkWin(Map<Button,Integer> map){
        int[] arr=new int[MainActivity.HEIGHT*MainActivity.HEIGHT];
        int i=0;
        for(Map.Entry<Button,Integer> entry:map.entrySet()){
            arr[i]=entry.getKey().getId();
            i++;
        }
        int counter=arr.length;
        for(int j=0;j<arr.length-1;j++){
            System.out.println(arr[j]);
            if((arr[j+1]-arr[j])==1)
                counter--;
        }
        return counter;
    }
}
