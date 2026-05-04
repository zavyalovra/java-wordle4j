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

    public WordleDictionary load() {
        List<String> words = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(new FileReader(wordsFile, StandardCharsets.UTF_8))) {
            while (fileReader.ready()) {
                words.add(fileReader.readLine());
            }
            Logger.log("Файл словаря загружен успешно");
        } catch (FileNotFoundException e) {
            Logger.log("Файл словаря не найден: " + e.getMessage());
        } catch (IOException e) {
            Logger.log("Ошибка чтения файла словаря: " + e.getMessage());
        }

        return new WordleDictionary(words);
    }
}
