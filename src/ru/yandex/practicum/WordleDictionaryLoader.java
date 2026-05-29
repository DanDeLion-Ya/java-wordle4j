package ru.yandex.practicum;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {
    private PrintWriter log;

    public WordleDictionaryLoader (PrintWriter log) {
        this.log = log;
    }

    //Загрузка списка слов из файла, с заменой буквы "ё" на "е" и перевод слов в нижний регистр
    protected List<String> loadListWords (String fileName) throws IOException {
        List<String> listWords = new ArrayList<>();
        try (FileInputStream byteStreamToFile = new FileInputStream(fileName);
            InputStreamReader symbolTransformWord = new InputStreamReader(byteStreamToFile, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(symbolTransformWord)) {
            while (br.ready()) {
                String word = br.readLine().toLowerCase();
                if (word.contains("ё")) {
                    word = word.replace("ё", "е");
                        } if (word.length() == 5) {
                            listWords.add(word);
                }
            } log.println("В словарь загружено: " + listWords.size() + " слов.");
        }
        return listWords;
    }
}
