package com.example.applab7;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.applab7.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{
    //建立兩個數值,用於計算兔子與烏龜的進度
    private int progressRabbit = 0;
    private int progressTurtle = 0;
    private Button btn_start;
    private SeekBar sb_rabbit, sb_turtle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_start = findViewById(R.id.btn_start);
        sb_rabbit = findViewById(R.id.sb_rabbit);
        sb_turtle = findViewById(R.id.sb_turtle);
//開始按鈕監聽器
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                btn_start.setEnabled(false);
                progressRabbit = 0;
                progressTurtle = 0;
                sb_rabbit.setProgress(0);
                sb_turtle.setProgress(0);
                runRabbit();
                runTurtle();
            }
        });
    }
    private final Handler handler = new Handler (Looper.myLooper (), new Handler.Callback() {
        @Override
        public boolean handleMessage (@NonNull Message msg) {
//判斷編號,並更新SeekBar的進度
            if (msg.what == 1)
                sb_rabbit.setProgress(progressRabbit);
            else if (msg.what == 2)
                sb_turtle.setProgress(progressTurtle);
//判斷該抵達終點
            if (progressRabbit >= 100 && progressTurtle < 100) {
                Toast.makeText(MainActivity.this,
                        "兔子贏", Toast.LENGTH_SHORT).show();
                btn_start.setEnabled(true);

            } else if (progressTurtle >= 100 && progressRabbit < 100) {
                Toast.makeText(MainActivity.this,
                        "烏龜贏", Toast.LENGTH_SHORT).show();
                btn_start.setEnabled(true);
            }
            return false;
        }
    });
        //模擬兔子移動
    private void runRabbit() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//兔子有三分之二的機率會偷懶
                boolean[] sleepProbability = {true, true, false};
                while (progressRabbit <= 100 && progressTurtle < 100) {
                    try {
                        Thread.sleep(100);
                        if (sleepProbability[(int) (Math.random() * 3)])
                            Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progressRabbit += 3;
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }
    private void runTurtle()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressTurtle <= 100 && progressRabbit < 100)
                {
                    try {
                        Thread.sleep(100);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    progressTurtle += 1;

                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }
}