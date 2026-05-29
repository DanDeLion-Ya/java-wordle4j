package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Wordle {

    static Scanner scanner = new Scanner(System.in);
    private static final String GAME_LOG = "log.txt";

    public static void main(String[] args) {
        PrintWriter log = null;
        try {
            //создание лог-файла
            log = new PrintWriter(new FileWriter(GAME_LOG));
            WordleDictionaryLoader dictionaryLoader = new WordleDictionaryLoader(log);
            //создаём словарь для игры, которые подходят по критериям ТЗ
            WordleDictionary dictionary = new WordleDictionary(dictionaryLoader.loadListWords("words_ru.txt"), log);
            //Создаём объект с игровой логикой
            WordleGame game = new WordleGame(dictionary, log);
            int countStep = game.getSteps();
            boolean isWin = false;
            int attempt = 0;
            log.println("Начало игры!");
                while (countStep > 0) {
                    attempt += 1;
                    System.out.println("Введите ваш ответ: ");
                    String userWord = scanner.nextLine();
                    userWord = userWord.toLowerCase();
                    if (userWord.isEmpty()) {
                        log.println("Игрок запросил подсказку");
                        String computerWord = game.getHintFromComputer();
                        String computerHint = game.getHint(computerWord);
                        if (!computerWord.equals("Слова для подсказок закончились.")) {
                            System.out.println(computerWord);
                            System.out.println(computerHint);
                            game.updateExactLetters(computerWord, computerHint);
                            game.updateExcludedLetters(computerWord, computerHint);
                            game.updateDifferentPlaceLetters(computerWord, computerHint);
                        } else {
                            System.out.println("Подсказок больше нет! Яндекс в помощь.");
                        }
                        continue;
                    } if (userWord.length() != game.getAnswer().length()) {
                        System.out.println("Слово должно состоять строго из " + game.getAnswer().length() + " букв!");
                        log.println("Некорректная длина слова: " + userWord.length());
                        continue;
                    } if (dictionary.containsWords(userWord)) {
                            String hint = game.getHint(userWord);
                            countStep -= 1;
                            game.addUsedWord(userWord);
                            log.println("Осталось попыток: " + countStep + " игрок ввёл слово: " + userWord);
                            if (game.checkCorrectAnswer(userWord)) {
                                if (attempt == 1) {
                                    System.out.println(userWord);
                                    System.out.println(hint);
                                    System.out.println("Оооооо...Ничёси! Да таких ГЕНИЕВ(как ты) ещё СВЕТ не видовал!");
                                    log.println("Игрок угадал слово с 1-й попытки!");
                                    isWin = true;
                                    break;
                                } else {
                                    System.out.println(userWord);
                                    System.out.println(hint);
                                    System.out.println("Молодец, наверное. Почти красавчик/вица!");
                                    log.println("Игрок угадал слово!");
                                    isWin = true;
                                    break;
                                }
                            } else {
                                System.out.println(userWord);
                                System.out.println(hint);
                                System.out.println("Подумай получше! У тебя осталось: " + countStep + " попыток.");
                                log.println("Игрок ответил неправильно.");
                            }
                        } else {
                            log.println("Игрок ввёл слово, которого нет в словаре.");
                            throw new WordNotFoundInDictionary("Слово не найдено в словаре!");
                        }
                    }
                if (!isWin) {
                    System.out.println("Увы... не получилось, не фортануло! Загаданное слово: " + game.getAnswer());
                    log.println("Игрок проиграл.");
                }
        } catch (WordNotFoundInDictionary wnfid) {
            System.out.println(wnfid.getMessage());
            log.println("Введённое игроком слово отсутствует в словаре!");
        } catch (IOException e) {
            System.out.println("Ошибка при работе с лог-файлом.");
        } finally {
            if (log != null) {
                log.close();
            }
        }
    }
}