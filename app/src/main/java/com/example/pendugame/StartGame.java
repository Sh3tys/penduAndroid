package com.example.pendugame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StartGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        Button   btnEasy   = findViewById(R.id.btn_easy);
        Button   btnMedium = findViewById(R.id.btn_medium);
        Button   btnHard   = findViewById(R.id.btn_hard);
        TextView tvScore   = findViewById(R.id.tv_score);

        updateScore(tvScore);

        btnEasy  .setOnClickListener(v -> startGame("FACILE"));
        btnMedium.setOnClickListener(v -> startGame("MOYEN"));
        btnHard  .setOnClickListener(v -> startGame("DIFFICILE"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView tvScore = findViewById(R.id.tv_score);
        updateScore(tvScore);
    }

    private void updateScore(TextView tvScore) {
        SharedPreferences prefs = getSharedPreferences("scores", MODE_PRIVATE);
        int wins   = prefs.getInt("gagner",   0);
        int losses = prefs.getInt("perdu", 0);
        tvScore.setText(getString(R.string.score_display, wins, losses));
    }

    private void startGame(String difficulty) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("difficultes", difficulty);
        startActivity(intent);
    }
}
