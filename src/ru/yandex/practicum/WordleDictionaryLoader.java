package ru.yandex.practicum;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {
    private static final File wordsFile = new File("words_ru.txt");

    public WordleDictionary load() throws IOException {
        List<String> words = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(wordsFile), StandardCharsets.UTF_8))) {
            String line;
            int lineCount = 0;
            while ((line = fileReader.readLine()) != null) {
                words.add(line);
                lineCount++;
            }
            Logger.log("В файл словаря загружено " +  lineCount + " строк");
        } catch (FileNotFoundException e) {
            Logger.log("Файл словаря не найден: " + e.getMessage());
            throw new IOException("Файл словаря не найден", e);
        }

        return new WordleDictionary(words);
    }
}
