package com.example.pendugame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class EndGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        boolean won  = getIntent().getBooleanExtra("gagner",  false);
        String  word = getIntent().getStringExtra("mot");
        if (word == null) word = "???";

        TextView tvResult  = findViewById(R.id.tv_result);
        TextView tvWord    = findViewById(R.id.tv_word);
        TextView tvScore   = findViewById(R.id.tv_score);
        Button   btnAgain  = findViewById(R.id.btn_play_again);

        if (won) {
            tvResult.setText(R.string.result_victory);
            tvResult.setTextColor(Color.parseColor("#00FF00"));
            tvWord  .setText(getString(R.string.you_guessed_it, word));
        } else {
            tvResult.setText(R.string.result_defeat);
            tvResult.setTextColor(Color.parseColor("#FF0000"));
            tvWord  .setText(getString(R.string.word_was, word));
        }

        SharedPreferences prefs  = getSharedPreferences("scores", MODE_PRIVATE);
        int wins   = prefs.getInt("gagner",   0);
        int losses = prefs.getInt("perdu", 0);
        tvScore.setText(getString(R.string.overall_score, wins, losses));

        btnAgain.setOnClickListener(v -> {
            Intent intent = new Intent(this, StartGame.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }
}