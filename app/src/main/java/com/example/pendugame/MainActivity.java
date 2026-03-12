package com.example.pendugame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String[] HANGMAN_STAGES = {
        "  +---+\n  |   |\n      |\n      |\n      |\n      |\n=========",
        "  +---+\n  |   |\n  O   |\n      |\n      |\n      |\n=========",
        "  +---+\n  |   |\n  O   |\n  |   |\n      |\n      |\n=========",
        "  +---+\n  |   |\n  O   |\n /|   |\n      |\n      |\n=========",
        "  +---+\n  |   |\n  O   |\n /|\\  |\n      |\n      |\n=========",
        "  +---+\n  |   |\n  O   |\n /|\\  |\n /    |\n      |\n=========",
        "  +---+\n  |   |\n  O   |\n /|\\  |\n / \\  |\n      |\n========="
    };

    private Game game;
    private Game.Difficulty difficulty;

    private TextView tvHangman;
    private TextView tvWordDisplay;
    private TextView tvErrors;
    private TextView tvStatus;
    private GridLayout glKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ── Wire up views ──────────────────────────────────────────────────────
        tvHangman     = findViewById(R.id.tv_hangman);
        tvWordDisplay = findViewById(R.id.tv_word_display);
        tvErrors      = findViewById(R.id.tv_errors);
        tvStatus      = findViewById(R.id.tv_status);
        glKeyboard    = findViewById(R.id.gl_keyboard);

        Button btnNewGame = findViewById(R.id.btn_new_game);
        btnNewGame.setOnClickListener(v -> resetGame());

        // ── Read difficulty from intent ────────────────────────────────────────
        String diff = getIntent().getStringExtra("difficultes");
        if (diff == null) diff = "MOYEN";
        switch (diff) {
            case "FACILE": difficulty = Game.Difficulty.FACILE;   break;
            case "DIFFICILE": difficulty = Game.Difficulty.DIFFICILE;   break;
            default:     difficulty = Game.Difficulty.MOYEN; break;
        }

        startNewGame();
    }

    // ── Game lifecycle ─────────────────────────────────────────────────────────

    private void startNewGame() {
        game = new Game(difficulty);
        buildKeyboard();
        updateUI();
    }

    private void resetGame() {
        startNewGame();
    }

    // ── Keyboard builder ───────────────────────────────────────────────────────

    private void buildKeyboard() {
        glKeyboard.removeAllViews();

        for (char c = 'A'; c <= 'Z'; c++) {
            final char letter = c;
            Button btn = new Button(this);
            btn.setText(String.valueOf(letter));
            btn.setTag(letter);
            btn.setTextSize(13f);
            btn.setTextColor(Color.WHITE);
            btn.setAllCaps(true);
            btn.setPadding(0, 4, 0, 4);
            btn.setBackgroundColor(Color.parseColor("#16213E"));

            // Equal-width columns inside the GridLayout
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams(
                    GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f),
                    GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
            );
            lp.width  = 0;
            lp.height = GridLayout.LayoutParams.WRAP_CONTENT;
            lp.setMargins(3, 3, 3, 3);
            btn.setLayoutParams(lp);

            btn.setOnClickListener(v -> onLetterClicked(btn, letter));
            glKeyboard.addView(btn);
        }
    }

    private void onLetterClicked(Button btn, char letter) {
        if (game.isOver()) return;

        boolean correct = game.guess(letter);

        // Disable & color the pressed key
        btn.setEnabled(false);
        btn.setAlpha(0.35f);
        btn.setBackgroundColor(correct
                ? Color.parseColor("#00FF00")
                : Color.parseColor("#FF0000"));

        updateUI();

        if (game.isOver()) {
            disableAllKeys();
            saveScore();
            navigateToEnd();
        }
    }

    private void updateUI() {
        // Hangman drawing
        tvHangman.setText(HANGMAN_STAGES[game.getErrors()]);

        // Word display
        tvWordDisplay.setText(game.getDisplayWord());

        // Error counter
        tvErrors.setText(game.getErrors() + " / " + Game.MAX_ERRORS + " erreurs");

        // Status label
        if (game.isWon()) {
            tvStatus.setText("Tu as gagné !");
            tvStatus.setTextColor(Color.parseColor("#00FF00"));
        } else if (game.isLost()) {
            tvStatus.setText("PERDU LOOSER  –  le mot était: " + game.getWord());
            tvStatus.setTextColor(Color.parseColor("#FF0000"));
        } else {
            tvStatus.setText("");
        }
    }

    private void disableAllKeys() {
        for (int i = 0; i < glKeyboard.getChildCount(); i++) {
            View v = glKeyboard.getChildAt(i);
            v.setEnabled(false);
            if (v.isEnabled()) v.setAlpha(0.35f);
        }
    }

    private void saveScore() {
        SharedPreferences prefs = getSharedPreferences("scores", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if (game.isWon()) {
            editor.putInt("gagner",   prefs.getInt("gagner",   0) + 1);
        } else {
            editor.putInt("perdu", prefs.getInt("perdu", 0) + 1);
        }
        editor.apply();
    }

    private void navigateToEnd() {
        // Short delay so the player can see the final state before leaving
        tvWordDisplay.postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, EndGame.class);
            intent.putExtra("gagner",  game.isWon());
            intent.putExtra("mot", game.getWord());
            startActivity(intent);
        }, 1200);
    }
}
