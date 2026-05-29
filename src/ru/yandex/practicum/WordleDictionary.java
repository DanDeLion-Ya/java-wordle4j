package ru.yandex.practicum;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.io.PrintWriter;

public class WordleDictionary {

    private List<String> words;

    private PrintWriter log;

    WordleDictionary (List<String> words, PrintWriter log) {
        this.words = words;
        this.log = log;
    }

    public List<String> getWords() {
        return words;
    }
    //Проверка, содержится ли переданное слово(введённое пользователем) в списке
    public boolean containsWords(String userWord) {
        if (words.contains(userWord)) {
            return true;
        } else {
            return false;
        }
    }
    // Получение случайного слова!
    public String getRandomWord() throws NoSuchElementException {
        Random random = new Random();
        if (!words.isEmpty()) {
            int randomIndex = random.nextInt(words.size());
            String randomWord = words.get(randomIndex);
            return randomWord;
        } else {
            throw new NoSuchElementException("Список пуст!");
        }
    }
}
