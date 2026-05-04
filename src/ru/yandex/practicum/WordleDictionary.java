package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {

    private final List<String> words;

    public WordleDictionary(List<String> words) {
        this.words = words;
    }

    public ArrayList<String> getWords() {
        return new ArrayList<>(words);
    }

    public String getAnswer() throws DictionaryException {
        if  (words == null || words.isEmpty()) {
            throw new DictionaryException("Словарь пуст. Невозможно загадать слово");
        }

        Random random = new Random();
        String answer = words.get(random.nextInt(words.size()));

        Logger.log("В игре участвует слово: " + answer);
        return answer;
    }

    public WordleDictionary getDictionary() {
        ArrayList<String> preparedWords = new ArrayList<>();

        for (String word : words) {
            if (word.length() == 5) {
                preparedWords.add(word.toLowerCase().replace("ё", "е"));
            }
        }

        Logger.log("Словарь отфильтрован согласно критериям игры");
        return new WordleDictionary(preparedWords);
    }
}
