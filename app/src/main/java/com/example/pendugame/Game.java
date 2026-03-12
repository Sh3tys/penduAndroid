package com.example.pendugame;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Game {

    public static final int MAX_ERRORS = 6;

    public enum Difficulty { FACILE, MOYEN, DIFFICILE }

    private final String word;
    private final Set<Character> guessedLetters;
    private int errors;

    private static final List<String> EASY_WORDS = Arrays.asList(
            "OUI", "NON", "SALUT", "CHAT", "CHIEN", "EAU", "FEU"
    );
    private static final List<String> MEDIUM_WORDS = Arrays.asList(
            "JARDIN", "REACT", "ANGULAR", "NODEJS", "HTML",
            "CSS"
    );
    private static final List<String> HARD_WORDS = Arrays.asList(
            "ANDROID", "JAVASCRIPT", "TYPESCRIPT", "DEVELOPPEMENT",
            "ENTREPRISE", "ANTICONSTITUTIONELLEMENT", "PROGRAMMATION"
    );

    public Game(Difficulty difficulty) {
        List<String> wordList;
        switch (difficulty) {
            case FACILE:  wordList = EASY_WORDS;   break;
            case DIFFICILE:  wordList = HARD_WORDS;   break;
            default:    wordList = MEDIUM_WORDS; break;
        }
        this.word = wordList.get(new Random().nextInt(wordList.size()));
        this.guessedLetters = new HashSet<>();
        this.errors = 0;
    }

    public boolean guess(char letter) {
        letter = Character.toUpperCase(letter);
        guessedLetters.add(letter);
        if (word.indexOf(letter) >= 0) {
            return true;
        } else {
            errors++;
            return false;
        }
    }

    public boolean isLetterGuessed(char letter) {
        return guessedLetters.contains(Character.toUpperCase(letter));
    }
    public String getDisplayWord() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (guessedLetters.contains(c)) {
                sb.append(c);
            } else {
                sb.append('_');
            }
            if (i < word.length() - 1) sb.append(' ');
        }
        return sb.toString();
    }

    public boolean isWon() {
        for (char c : word.toCharArray()) {
            if (!guessedLetters.contains(c)) return false;
        }
        return true;
    }

    public boolean isLost()  { return errors >= MAX_ERRORS; }
    public boolean isOver()  { return isWon() || isLost(); }
    public int     getErrors() { return errors; }
    public String  getWord()   { return word; }
}
