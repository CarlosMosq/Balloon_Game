package com.company.balloonburstgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class Game_Over extends AppCompatActivity {
    TextView scoreResult, highScore, congrats;
    Button playAgain, quitGame;
    int score;
    SharedPreferences sharing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        congrats = findViewById(R.id.congrats);
        scoreResult = findViewById(R.id.scoreResult);
        highScore = findViewById(R.id.highScore);
        playAgain = findViewById(R.id.playAgain);
        quitGame = findViewById(R.id.quitGame);

        score = getIntent().getIntExtra("scoreNbr", 0);
        scoreResult.setText(String.format("%s %s", getString(R.string.yourScore), score));

        sharing = this.getSharedPreferences("Score", Context.MODE_PRIVATE);
        int highestScore = sharing.getInt("highScore", 0);

        if (score > highestScore) {
            sharing.edit().putInt("highScore", score).apply();
            congrats.setText(getString(R.string.highScore));
            highScore.setText(String.format("%s %s", getString(R.string.yourHighScore), score));
        }
        else {
            congrats.setText(getString(R.string.congrats));
            highScore.setText(String.format("%s %s", getString(R.string.yourHighScore), highestScore));
        }


        playAgain.setOnClickListener(v -> {
            Intent i = new Intent(Game_Over.this, MainActivity.class);
            startActivity(i);
            finish();
        });

        quitGame.setOnClickListener(v -> {
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        });

    }
}