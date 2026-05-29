package ru.yandex.practicum;


public class WordNotFoundInDictionary extends Exception {
    public WordNotFoundInDictionary(final String message) {
        super(message);
    }
}
