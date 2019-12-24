package com.trelloiii.thirdproject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

public class SettingsActivity extends Activity {

    private Button button;
    private SeekBar countBar;
    private TextView counter;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        button=findViewById(R.id.button);
        countBar=findViewById(R.id.countBar);
        counter=findViewById(R.id.counter);

        int height=getIntent().getIntExtra("req",-1);

        counter.setText(Math.round(Math.pow(height,2))+"");
        countBar.setProgress(height-4);
        countBar.setMin(0);
        countBar.setMax(2);
        countBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                counter.setText(String.valueOf(Math.round(Math.pow(progress+4,2))));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        button.setOnClickListener(v->{
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Изменение сетки");
            builder.setMessage("Вы действительно хотите изменить сетку? Текущая игра будет утеряна");
            builder.setNegativeButton("Отменить", (dialog, which) -> {
                dialog.cancel();
            });
            builder.setPositiveButton("Принять", (dialog, which) -> {
                Intent intent=new Intent();
                intent.putExtra("count",countBar.getProgress());
                setResult(11,intent);
                finish();
            });
            AlertDialog alert=builder.create();
            alert.show();
        });
        Toast.makeText(this, "REQ " + getIntent().getIntExtra("req",-1), Toast.LENGTH_SHORT).show();
    }

}
