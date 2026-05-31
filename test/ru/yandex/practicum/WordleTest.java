package ru.yandex.practicum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class WordleTest {
    private WordleGame game;
    private PrintWriter log = new PrintWriter(System.out);

    @BeforeEach
    void origin () {
        List<String> words = new ArrayList<>();
        words.add("котик");
        WordleDictionary dictionary = new WordleDictionary(words, log);
        game = new WordleGame(dictionary, log);
    }

    @Test
    public void testCheckCorrectAnswerTrue() {
        boolean result = game.checkCorrectAnswer("котик");
        Assertions.assertTrue(result);
    }

    @Test
    public void testCheckCorrectAnswerFalse() {
        boolean result = game.checkCorrectAnswer("вагон");
        Assertions.assertFalse(result);
    }

    @Test
    public void testAllCharactersCorrect() {
        Assertions.assertEquals("+++++" , game.getHint("котик"));
    }

    @Test
    public void testNotAllCharactersCorrect() {
        Assertions.assertEquals("+--^^" , game.getHint("капот"));
    }

    @Test
    public void testAllCharactersIncorrect() {
        Assertions.assertEquals("-----" , game.getHint("абзац"));
    }

    @Test
    public void testUpdateExactLetters() {
        Assertions.assertEquals("     " , game.getExactLetters().toString());
        String userWord = "квант";
        String hint = game.getHint(userWord);
        game.updateExactLetters(userWord, hint);
        String exacted = "к    ";
        Assertions.assertEquals(exacted, game.getExactLetters().toString());
    }

    @Test
    public void testUpdateExcludedLetters() {
        assertTrue(game.getExcludedLetters().isEmpty());
        String userWord = "квант";
        String hint = game.getHint(userWord);
        game.updateExcludedLetters(userWord, hint);

        Assertions.assertTrue(game.getExcludedLetters().contains('в'));
        Assertions.assertTrue(game.getExcludedLetters().contains('а'));
        Assertions.assertTrue(game.getExcludedLetters().contains('н'));
    }

    @Test
    public void testUpdateDifferentPlaceLetters() {
        assertTrue(game.getDifferentPlaceLetters().isEmpty());
        String userWord = "квант";
        String hint = game.getHint(userWord);
        game.updateDifferentPlaceLetters(userWord, hint);

        Assertions.assertTrue(game.getDifferentPlaceLetters().containsKey('т'));
        Assertions.assertTrue(game.getDifferentPlaceLetters().get('т').contains(4));
    }

    @Test
    public void testAddUsedWord() {
        game.addUsedWord("котик");
    }

    @Test
    public void testGetHintFromComputer() {
        String hintWord = game.getHintFromComputer();
        Assertions.assertNotEquals(game.getAnswer(), hintWord);
    }

    @Test
    public void testGetRandomWord() {
        List<String> words = new ArrayList<>();
        words.add("котик");
        WordleDictionary dictionary = new WordleDictionary(words, log);
        Assertions.assertEquals("котик", dictionary.getRandomWord());
    }

    @Test
    public void testGetRandomWordReturnsWordFromDictionary() {
        List<String> words = new ArrayList<>();
        words.add("котик");
        words.add("армия");
        words.add("вагон");
        WordleDictionary dictionary = new WordleDictionary(words, log);
        Assertions.assertTrue(words.contains(dictionary.getRandomWord()));
    }
}
