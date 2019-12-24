package com.trelloiii.thirdproject;

import androidx.annotation.Nullable;
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
import android.widget.TextView;
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
    private Map<Button,Integer> imgs;
    private List<Integer> positions=new ArrayList<>(16);
    private Button first=null;
    private Button changeButton;
    private ImageView restart,settings;
    private TextView textCount;
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
        textCount=findViewById(R.id.textCount);
        initGrid();

        changeButton.setOnClickListener(v->{
           // this.change=!this.change;
            randomMove();


        });
        settings.setOnClickListener(v->{
            Intent intent=new Intent(this,SettingsActivity.class);
            intent.putExtra("req",HEIGHT);
            startActivityForResult(intent,HEIGHT);
        });
        restart.setOnClickListener(v->{
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Новая игра");
            builder.setMessage("Начать заново? Текущий прогресс будет утерян");
            builder.setNegativeButton("Отменить", (dialog, which) -> {
                dialog.cancel();
            });
            builder.setPositiveButton("Принять", (dialog, which) -> {
                remakeGrid(HEIGHT);
            });
            AlertDialog alert=builder.create();
            alert.show();
        });


    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        assert data != null;
      //  HEIGHT=
       // grid.setColumnCount(HEIGHT);
        remakeGrid(data.getIntExtra("count",-1)+4);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void remakeGrid(int a){
        switch (a){
            case 4:
                HEIGHT=4;
                grid.removeAllViews();
                grid.setColumnCount(4);
                initGrid();
                for(int i=0;i<HEIGHT*HEIGHT;i++) {
                    grid.getChildAt(i).getLayoutParams().height = 245;
                    grid.getChildAt(i).getLayoutParams().width = 245;
                }
                break;
            case 5:
                HEIGHT=5;
                grid.removeAllViews();
                grid.setColumnCount(5);
                initGrid();
                for(int i=0;i<HEIGHT*HEIGHT;i++) {
                    grid.getChildAt(i).getLayoutParams().height = 195;
                    grid.getChildAt(i).getLayoutParams().width = 195;
                }
                break;
            case 6:
                HEIGHT=6;
                grid.removeAllViews();
                grid.setColumnCount(6);
                initGrid();
                for(int i=0;i<HEIGHT*HEIGHT;i++) {
                    grid.getChildAt(i).getLayoutParams().height = 165;
                    grid.getChildAt(i).getLayoutParams().width = 165;
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void initGrid(){
        imgs=new LinkedHashMap<>();
        for(int i=1;i<=HEIGHT*HEIGHT;i++) {
            Button img = new Button(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Resources resources=this.getResources();
                //img.setImageDrawable(resources.getDrawable(resources.getIdentifier("img"+i,"drawable",this.getPackageName())));
                if(i<HEIGHT*HEIGHT){
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
                            setFocused(first,true);
                        } else {
                            int firstId = imgs.get(first);
                            int secId = imgs.get(v);
                            if(checkBotCorner(firstId)){
                                if ((firstId - secId) == -1 || (firstId - secId) == HEIGHT){
                                    swap(firstId,secId,v,false);
                                }
                                else {
                                    setFocused(first,false);
                                    first = null;
                                }
                            }
                            else if(checkTopCorner(firstId)){
                                if ((firstId - secId) == 1 || (firstId - secId) == -HEIGHT){
                                    swap(firstId,secId,v,false);
                                }
                                else {
                                    setFocused(first,false);
                                    first = null;
                                }
                            }
                            else if(checkLeftSide(firstId)){
                                if ( (firstId - secId) == -1 || (firstId - secId) == HEIGHT || (firstId - secId) == -HEIGHT){
                                    swap(firstId,secId,v,false);
                                }
                                else {
                                    setFocused(first,false);
                                    first = null;
                                }
                            }
                            else if(checkRightSide(firstId)){
                                if ((firstId - secId) == 1 || (firstId - secId) == -HEIGHT || (firstId - secId) == HEIGHT){
                                    swap(firstId,secId,v,false);
                                }
                                else {
                                    setFocused(first,false);
                                    first = null;
                                }
                            }
                            else {
                                if ((firstId - secId) == 1 || (firstId - secId) == -1 || (firstId - secId) == HEIGHT || (firstId - secId) == -HEIGHT) {
                                    swap(firstId,secId,v,false);
                                }
                                else {
                                    setFocused(first,false);
                                    first = null;
                                }
                            }
//                            if ((firstId - secId) == 1 || (firstId - secId) == -1 || (firstId - secId) == 4 || (firstId - secId) == -4) {
//
//
//                            }

                        }
                    }
                });
            }
            imgs.put(img,i-1);
            positions.add(i-1);
            grid.addView(img,i-1);

            img.getLayoutParams().height=245;
            img.getLayoutParams().width=245;

        }
        for(int i=0;i<50;i++) {
            randomMove();
        }
        moveCounter=0;
        textCount.setText(String.valueOf(moveCounter));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setFocused(View v,boolean focus){
        if(v.getId()!=Integer.valueOf(HEIGHT*HEIGHT-1)) {
            if (focus)
                v.setBackground(getDrawable(R.drawable.button0_15_focused));
            else
                v.setBackground(getDrawable(R.drawable.button0_15));
        }
        else{
            if (focus)
                v.setBackground(getDrawable(R.drawable.button16_focused));
            else
                v.setBackground(getDrawable(R.drawable.button16));
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void randomMove(){
        Plate black=getBlack(imgs);
        List<Plate> nears=getNearBlack(black,imgs);
        Random r=new Random();
        Plate random=nears.get(r.nextInt(nears.size()));
        first=black.getImg();
        swap(black.getIndex(),random.getIndex(),random.getImg(),true);
    }
    public void alert(String title,String message){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        AlertDialog alert=builder.create();
        alert.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void swap(int firstId, int secId, View v,boolean isRandom){
        if(first.getId()==Integer.valueOf(HEIGHT*HEIGHT-1)||v.getId()==Integer.valueOf(HEIGHT*HEIGHT-1)) {
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
            if(!isRandom) {
                moveCounter++;
                textCount.setText(String.valueOf(moveCounter));
                if (MapUtil.checkWin(imgs) == 1)
                    alert("Победа!", "Вы закончили игру за " + moveCounter + " шагов");
            }
            setFocused(first,false);
            first = null;
        }
        else{
            setFocused(first,false);
            first=null;
        }
    }

    public Plate getBlack(Map<Button,Integer> map){
        Button black=null;
        int blackIndex=0;
        for(Map.Entry<Button,Integer> entry:map.entrySet()){
            if(entry.getKey().getId()==Integer.valueOf(HEIGHT*HEIGHT-1)){
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
                if((entry.getValue()-black.getIndex())==-1|| (entry.getValue()-black.getIndex())==HEIGHT){
                    resultList.add(new Plate(entry.getKey(),entry.getValue()));
                }
            }
            else if(checkTopCorner(entry.getValue())){
                if((entry.getValue()-black.getIndex())==1|| (entry.getValue()-black.getIndex())==-HEIGHT){
                    resultList.add(new Plate(entry.getKey(),entry.getValue()));
                }
            }
            else if(checkLeftSide(entry.getValue())){
                if((entry.getValue()-black.getIndex())==-1||(entry.getValue()-black.getIndex())==HEIGHT||
                        (entry.getValue()-black.getIndex())==-HEIGHT){
                    resultList.add(new Plate(entry.getKey(),entry.getValue()));
                }
            }
            else if(checkRightSide(entry.getValue())){
                if((entry.getValue()-black.getIndex())==1|| (entry.getValue()-black.getIndex())==-HEIGHT||
                        (entry.getValue()-black.getIndex())==HEIGHT){
                    resultList.add(new Plate(entry.getKey(),entry.getValue()));
                }
            }
            else {
                if ((entry.getValue() - black.getIndex()) == 1 || (entry.getValue() - black.getIndex()) == -1 ||
                        (entry.getValue() - black.getIndex()) == HEIGHT || (entry.getValue() - black.getIndex()) == -HEIGHT) {
                    resultList.add(new Plate(entry.getKey(), entry.getValue()));
                }
            }
        }
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
