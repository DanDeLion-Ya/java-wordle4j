package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.*;

public class WordleGame {
    // Ответ
    private String answer;
    //Количество шагов
    private int steps = 6;
    //Словарь
    private WordleDictionary dictionary;

    private PrintWriter log;
    //Использованные слова
    private LinkedHashMap<String, Integer> usedWords;
    //Точные буквы
    private StringBuilder exactLetters;
    //Хранение исключенных букв
    private Set<Character> excludedLetters;
    //Хранение букв находящихся в слове, но на другом месте
    private Map<Character, Set<Integer>> differentPlaceLetters;

    public WordleGame(WordleDictionary dictionary, PrintWriter log) {
        this.answer = dictionary.getRandomWord();
        log.println("Загаданное слово: " + answer);
        this.dictionary = dictionary;
        this.log = log;
        this.usedWords = new LinkedHashMap<>();
        this.exactLetters = new StringBuilder("     ");
        this.excludedLetters = new HashSet<>();
        this.differentPlaceLetters = new HashMap<>();
    }

    protected String getAnswer() {
        return answer;
    }

    protected int getSteps() {
        return steps;
    }

    protected WordleDictionary getDictionary() {
        return dictionary;
    }

    protected StringBuilder getExactLetters() {
        return exactLetters;
    }

    protected Set<Character> getExcludedLetters() {
        return excludedLetters;
    }

    protected Map<Character, Set<Integer>> getDifferentPlaceLetters() {
        return differentPlaceLetters;
    }

    //проверка на совпадение загаданного слова и слова введённого пользователем
    protected boolean checkCorrectAnswer(String userWord) {
        return answer.equals(userWord);
    }
    //Получение символьных подсказок
    protected String getHint(String userWord) {
        StringBuilder hintSymbols = new StringBuilder();
        if (checkCorrectAnswer(userWord)) {
            hintSymbols.append("+++++");
        } else {
            for (int i = 0; i < answer.length(); i++) {
                if (userWord.charAt(i) == answer.charAt(i)) {
                    hintSymbols.append("+");
                } else if (answer.indexOf(userWord.charAt(i)) != -1) {
                    hintSymbols.append("^");
                } else {
                    hintSymbols.append("-");
                }
            }
        } return hintSymbols.toString();
    }
    //Добавление слова в список использованных слов
    protected void addUsedWord(String userWord){
        if (!usedWords.containsKey(userWord)) {
            usedWords.put(userWord, 1);
            log.println("Слово " + userWord + " добавлено в список использованных слов (usedWords)");
        } else {
            int quantity = usedWords.get(userWord);
            usedWords.put(userWord, quantity += 1);
            log.println("Слово " + userWord + " вводится повторно!");
        }
    }
    //Получение подсказки от компьютера
    protected String getHintFromComputer() {
        Random random = new Random();
        List<String> candidatesOnAnswer = new ArrayList<>();
        for (String word : dictionary.getWords()) {
            boolean isSuitable = true;
            if (word.equals(answer)) {
                isSuitable = false;
            }
            if (usedWords.containsKey(word)) {
                isSuitable = false;
            }
            for (int i = 0; i < exactLetters.length(); i++) {
                if (exactLetters.charAt(i) != ' ' && word.charAt(i) != exactLetters.charAt(i)) {
                    isSuitable = false;
                    break;
                }
            }
            for (char symbol : excludedLetters) {
                if (word.indexOf(symbol) != -1) {
                    isSuitable = false;
                    break;
                }
            }
            for (Map.Entry<Character, Set<Integer>> entry : differentPlaceLetters.entrySet()) {
                char letter = entry.getKey();
                Set<Integer> anotherPosition = entry.getValue();
                //Буква находится в слове
                if (word.indexOf(letter) == -1) {
                    isSuitable = false;
                    break;
                }
                // Буква не на другой позиции
                for (int position : anotherPosition) {
                    if (word.charAt(position) == letter) {
                        isSuitable = false;
                        break;
                    }
                }
            }
            if (isSuitable) {
                candidatesOnAnswer.add(word);
            }
        }
        if (candidatesOnAnswer.isEmpty()) {
            log.println("Слова-подсказки закончились.");
            return "Слова для подсказок закончились.";
        } else {
            int randomIndex = random.nextInt(candidatesOnAnswer.size());
            String hintFromComputer = candidatesOnAnswer.get(randomIndex);
            addUsedWord(hintFromComputer);
            log.println("Компьютер подсказал слово: " + hintFromComputer);
            return hintFromComputer;
        }
    }
    //Запоминаем буквы на правильных местах
    protected void updateExactLetters(String userWord, String hint) {
        for(int i = 0; i < exactLetters.length(); i++) {
            if (hint.charAt(i) == '+') {
                exactLetters.setCharAt(i, userWord.charAt(i));
            }
        }
    }
    //Запоминаем буквы, которых нет в загаданном слове
    protected void updateExcludedLetters(String userWord, String hint) {
        for(int i = 0; i < hint.length(); i++) {
            if (hint.charAt(i) == '-') {
                excludedLetters.add(userWord.charAt(i));
            }
        }
    }
    //Запоминаем буквы которые есть в загаданном слове, но на другой позиции
    protected void updateDifferentPlaceLetters(String userWord, String hint) {
        for(int i = 0; i < hint.length(); i++) {
            if (hint.charAt(i) == '^') {
                char letter = userWord.charAt(i);
                Set<Integer> position = differentPlaceLetters.get(letter);
                if (position == null) {
                    position = new HashSet<>();
                }
                position.add(i);
                differentPlaceLetters.put(letter, position);
            }
        }
    }
}