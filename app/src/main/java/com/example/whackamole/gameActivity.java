package com.example.whackamole;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class gameActivity extends AppCompatActivity {

    private final int ROWS = 3;
    private final int COLUMNS = 3;
    private final int GAME_DURACTION = 30;
    private final int WIN_SCORE = 30;
    private final int MAX_MISS = 3;

    private long timeLeft;
    private CountDownTimer countDownTimer;
    private TextView textViewCountDown;
    private TextView textViewScore;
    private TextView textViewMiss;
    private Timer timer;

    private LinearLayout wholeAndMole[];
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

        timer = new Timer();
        GridLayout gridLayout = createGridLayout(ROWS, COLUMNS);
        wholeAndMole = new LinearLayout[ROWS * COLUMNS];


        for (int i = 0; i < ROWS * COLUMNS; i++) {

            wholeAndMole[i] = new LinearLayout(this);
            ImageView whole = new ImageView(this);
            ImageView mole = new ImageView(this);

            whole.setImageResource(R.drawable.small_whole);
            mole.setImageResource(R.drawable.small_mole);
            mole.setAlpha(0f);

            mole.setTranslationY(50f);

            mole.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.getAlpha() > 0.3)
                        score++;
                    else
                        miss++;

                    textViewMiss.setText("" + miss);
                    textViewScore.setText("" + score);
                    checkGameStatus();

                }
            });


            wholeAndMole[i].setOrientation(LinearLayout.VERTICAL);
            wholeAndMole[i].addView(mole);
            wholeAndMole[i].addView(whole);

            gridLayout.addView(wholeAndMole[i]);
        }


        game_laLayout.addView(gridLayout);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                final int index = (int) (Math.random() * (ROWS * COLUMNS));
                wholeAndMole[index].post(new Runnable() { //return to the main thread
                    @Override
                    public void run() {
                        showRandomButtons(wholeAndMole, index);
                    }
                });
            }
        }, 200, 800);


        startCountDown();

    }



    private void showRandomButtons(LinearLayout[] wholeAndMole, int index) {

        float buttom=50,top=-10;
        ObjectAnimator jump = ObjectAnimator.ofFloat(wholeAndMole[index].getChildAt(0), "translationY",   buttom,top,top,buttom);
        jump.setDuration(2000);

        ObjectAnimator show = ObjectAnimator.ofFloat(wholeAndMole[index].getChildAt(0), "alpha", 0f, 1f, 1f, 0f);
        show.setDuration(2000);

        //setAnimator
        AnimatorSet set = new AnimatorSet();
        set.play(show).with(jump);
        set.start();

    }

    private GridLayout createGridLayout(int rows, int columns) {
        GridLayout gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(columns);
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

        builder.setTitle("Status").setMessage(message);
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        builder.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        builder.show();
    }

    private void updateTextCountDown() {
        int minutes = (int) (timeLeft / 1000) / 60;
        int seconds = (int) (timeLeft / 1000) % 60;

        if(timeLeft >10*1000)
        textViewCountDown.setText(String.format(Locale.getDefault(), "%2d:%2d", minutes, seconds));

        else {
            textViewCountDown.setText(String.format(Locale.getDefault(), "%2d", seconds));
            textViewCountDown.setTextColor(Color.RED);

            ObjectAnimator scaleX = ObjectAnimator.ofFloat(this.textViewCountDown,"scaleX",0f,1f);
            scaleX.setDuration(800);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(this.textViewCountDown,"scaleY",0f,1f);
            scaleY.setDuration(800);

            AnimatorSet set = new AnimatorSet();
            set.playTogether(scaleX,scaleY);
            set.start();
        }
    }


}
