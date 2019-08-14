package com.example.whackamole;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Locale;

public class gameActivity extends AppCompatActivity {

    private final int ROWS = 3;
    private final int COLUMS = 3;
    private final int GAME_DURACTION = 15;
    private long timeLeft;
    private CountDownTimer countDownTimer;
    private TextView textViewCountDown;
    private TextView textViewScore;
    private TextView textViewMiss;

    private String name;
    private int score;
    private int miss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle extars = getIntent().getExtras();
        name = extars.getString("name");

        TextView textView = findViewById(R.id.name_txt_view);
        textViewCountDown = findViewById(R.id.time_txt);
        textViewScore = findViewById(R.id.score_txt);
        textViewMiss = findViewById(R.id.miss_txt);
        GridLayout game_laLayout = findViewById(R.id.game__layout);
        GridLayout gridLayout = createGridLayout(ROWS,COLUMS);

        for (int i = 0; i < ROWS*COLUMS; i++) {
            Button v = new Button(this);
            v.setBackgroundColor(Color.BLUE);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ObjectAnimator animator = ObjectAnimator.ofFloat(view,"rotation",0f,360f);
                    animator.setDuration(1000);
                    animator.start();
                }
            });
            gridLayout.addView(v);
        }

        game_laLayout.addView(gridLayout);
        textView.setText(name);
        startCountDown();

    }

    private GridLayout createGridLayout(int rows, int colums) {
        GridLayout gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(colums);
        gridLayout.setRowCount(rows);
        return gridLayout;
    }

    private void startCountDown() {
    CountDownTimer countDownTimer = new CountDownTimer(GAME_DURACTION*1000,1000) {
        @Override
        public void onTick(long l) {
            timeLeft = l;
            updateTextCountDown();
        }

        @Override
        public void onFinish() {
            timeLeft=0;
            updateTextCountDown();
            showStatusMessage(name);
        }
    }.start();
    }

    private void showStatusMessage(String name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Status").setMessage(name+" win").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        }).show();
    }

    private void updateTextCountDown() {
        int minutes = (int)(timeLeft/1000)/60 ;
        int seconds = (int)(timeLeft/1000)%60;

        textViewCountDown.setText(String.format(Locale.getDefault(),"%2d:%2d",minutes,seconds));
        if(timeLeft<=10*1000)
            textViewCountDown.setTextColor(Color.RED);

    }
}
