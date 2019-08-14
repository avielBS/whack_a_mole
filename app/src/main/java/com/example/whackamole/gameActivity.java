package com.example.whackamole;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class gameActivity extends AppCompatActivity {

    private final int ROWS = 3;
    private final int COLUMS = 3;
    private final int GAME_DURACTION = 30;
    private final int WIN_SCORE = 30;
    private final int MAX_MISS = 3;

    private long timeLeft;
    private CountDownTimer countDownTimer;
    private TextView textViewCountDown;
    private TextView textViewScore;
    private TextView textViewMiss;
    private Timer timer;


    private String name;
    private int score;
    private int miss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle extars = getIntent().getExtras();
        name = extars.getString("name");

        textViewCountDown = findViewById(R.id.time_txt);
        textViewScore = findViewById(R.id.score_txt);
        textViewMiss = findViewById(R.id.miss_txt);
        RelativeLayout game_laLayout = findViewById(R.id.game__layout);

        GridLayout gridLayout = createGridLayout(ROWS, COLUMS);
        final Button buttons[] = new Button[ROWS * COLUMS];

        for (int i = 0; i < ROWS * COLUMS; i++) {
            Button b = new Button(this);
            b.setBackgroundColor(Color.GREEN);
            b.setAlpha(0);
 //           b.setBackgroundResource(R.drawable.mole);
            b.setText("X");

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.getAlpha() > 0.3)
                        score++;
                    else
                        miss++;

                    textViewMiss.setText("" + miss);
                    textViewScore.setText("" + score);
                    checkGameStatus();

                    Log.d("score", "score: " + score);

                }
            });
            buttons[i] = b;
            gridLayout.addView(buttons[i]);
        }


        game_laLayout.addView(gridLayout);


        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                final int index = (int) (Math.random() * (ROWS * COLUMS));
                buttons[index].post(new Runnable() {
                    @Override
                    public void run() {
                        showRandomButtons(buttons, index);
                    }
                });
            }
        }, 200, 800);


        startCountDown();

    }

    private void showRandomButtons(Button[] buttons, int index) {


        ObjectAnimator rotation = ObjectAnimator.ofFloat(buttons[index], "rotation", 0f, 360f);
        rotation.setDuration(1000);
        ObjectAnimator show = ObjectAnimator.ofFloat(buttons[index], "alpha", 0f, 1f, 1f, 0f);
        show.setDuration(2000);

        //setAnimator
        AnimatorSet set = new AnimatorSet();
        set.play(show).with(rotation);
        set.start();




    }


    private GridLayout createGridLayout(int rows, int colums) {
        GridLayout gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(colums);
        gridLayout.setRowCount(rows);
        return gridLayout;
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(GAME_DURACTION * 1000, 1000) {
            @Override
            public void onTick(long l) {
                timeLeft = l;
                updateTextCountDown();
            }

            @Override
            public void onFinish() {
                timeLeft = 0;
                updateTextCountDown();
                checkGameStatus();
            }
        }.start();

    }

    private void checkGameStatus() {
        if (score >= WIN_SCORE || timeLeft == 0 || miss == MAX_MISS) {
            timer.cancel();
            showStatusMessage();
            countDownTimer.cancel();
        }
    }

    private void showStatusMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String message = "";
        if (this.score >= WIN_SCORE)
            message = this.name + " Win !";
        else if (this.score < WIN_SCORE || this.miss == 3)
            message = this.name + " Lose !";

        builder.setTitle("Status").setMessage(message).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        }).show();
    }

    private void updateTextCountDown() {
        int minutes = (int) (timeLeft / 1000) / 60;
        int seconds = (int) (timeLeft / 1000) % 60;

        textViewCountDown.setText(String.format(Locale.getDefault(), "%2d:%2d", minutes, seconds));
        if (timeLeft <= 10 * 1000)
            textViewCountDown.setTextColor(Color.RED);

    }
}
