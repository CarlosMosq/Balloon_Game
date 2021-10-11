package com.company.balloonburstgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    TextView countDown, remainingTime, score;
    GridLayout grid;
    ImageView one, two, three, four, five, six, seven, eight, nine;
    CountDownTimer timeToPlay;
    int scoreNbr = 0;
    boolean soundOnOff = true;

    List<ImageView> allItems;
    ArrayList<ImageView> balloonsArr = new ArrayList<>();

    Handler handler;
    Runnable runnable;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countDown = findViewById(R.id.countDown);
        remainingTime = findViewById(R.id.remainingTime);
        score = findViewById(R.id.scoreCount);
        grid = findViewById(R.id.grid);
        one = findViewById(R.id.balloon1);
        two = findViewById(R.id.balloon2);
        three = findViewById(R.id.balloon3);
        four = findViewById(R.id.balloon4);
        five = findViewById(R.id.balloon5);
        six = findViewById(R.id.balloon6);
        seven = findViewById(R.id.balloon7);
        eight = findViewById(R.id.balloon8);
        nine = findViewById(R.id.balloon9);

        allItems = Arrays.asList(one, two, three, four, five, six, seven, eight, nine);
        balloonsArr.addAll(allItems);

        mediaPlayer = MediaPlayer.create(this, R.raw.explosion);

        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countDown.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                balloonsControl();
                timeToPlay.start();
            }
        }.start();

        timeToPlay = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String secsToFinish = String.valueOf(millisUntilFinished / 1000);
                remainingTime.setText(String.format("%s %s", getString(R.string.time), secsToFinish));
            }

            @Override
            public void onFinish() {
                Intent i = new Intent(MainActivity.this, Game_Over.class);
                i.putExtra("scoreNbr", scoreNbr);
                startActivity(i);
                finish();
            }
        };

        one.setOnClickListener(v -> increaseScoreByOne(one));
        two.setOnClickListener(v -> increaseScoreByOne(two));
        three.setOnClickListener(v -> increaseScoreByOne(three));
        four.setOnClickListener(v -> increaseScoreByOne(four));
        five.setOnClickListener(v -> increaseScoreByOne(five));
        six.setOnClickListener(v -> increaseScoreByOne(six));
        seven.setOnClickListener(v -> increaseScoreByOne(seven));
        eight.setOnClickListener(v -> increaseScoreByOne(eight));
        nine.setOnClickListener(v -> increaseScoreByOne(nine));



//end of onCreate
    }

    public void increaseScoreByOne(ImageView imageView) {
        scoreNbr++;
        String newScore = String.valueOf(scoreNbr);
        score.setText(String.format("%s %s", getString(R.string.score), newScore));
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(0);
        }
        mediaPlayer.start();
        imageView.setImageResource(R.drawable.boom_image);
    }

    public void balloonsControl() {
        countDown.setVisibility(View.INVISIBLE);
        remainingTime.setVisibility(View.VISIBLE);
        score.setVisibility(View.VISIBLE);

        handler = new Handler();
        runnable = () -> {
            for (ImageView image : balloonsArr) {
                image.setVisibility(View.INVISIBLE);
                image.setImageResource(R.drawable.balloon);
            }
            grid.setVisibility(View.VISIBLE);

            Random random = new Random();
            int i = random.nextInt(balloonsArr.size());
            balloonsArr.get(i).setVisibility(View.VISIBLE);

            long delay = 2000;
            if (scoreNbr != 0) {
                delay *= 0.8;
            }
            //postDelayed defines how long until the code above should be repeated;
            handler.postDelayed(runnable, delay);
        };

        handler.post(runnable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sound_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.disableSound) {
          if (soundOnOff) {
              mediaPlayer.setVolume(0,0);
              item.setIcon(R.drawable.ic_baseline_volume_off_24);
              soundOnOff = false;
          }
          else {
              mediaPlayer.setVolume(1,1);
              item.setIcon(R.drawable.ic_baseline_volume_up_24);
              soundOnOff = true;
          }
        }

        return super.onOptionsItemSelected(item);
    }

    //end of MainActivity class
}