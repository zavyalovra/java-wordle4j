package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {
    private WordleGame game;
    WordleDictionary dict;

    @BeforeAll
    static void initLog() {
        Logger.setWriter(new PrintWriter(System.out, true));
    }

    @BeforeEach
    void initGame() {
        dict = new WordleDictionary(Arrays.asList("фляга", "абзац", "автор", "рыбка", "грязь"));
        game = new WordleGame("фляга", 6, dict);
    }

    @Test
    void shouldReturnTrueForCorrectAnswer() {
        assertTrue(game.checkAnswer("фляга"));
    }

    @Test
    void shouldReturnFalseForIncorrectAnswer() {
        assertFalse(game.checkAnswer("абзац"));
    }

    @Test
    void shouldReturnCorrectHintString() {
        assertEquals("^-+--", game.getHint("грязь"));
    }

    @Test
    void shouldNotThrowExceptionForValidWord() {
        String validWord = "абзац";
        try {
            game.validateWord(validWord);
            Logger.log(validWord + ": validateWord работает исправно");
        } catch (Exception e) {
            Logger.log(e.getMessage());
            fail();
        }
    }

    @Test
    void shouldThrowExceptionForInvalidWord() {
        String inValidWord = "дом";
        try {
            game.validateWord(inValidWord);
            fail();
        } catch (Exception e) {
            Logger.log(inValidWord + ": " + e.getMessage());
            assertEquals("Слово должно состоять строго из пяти букв", e.getMessage());
        }
    }

    @Test
    void shouldThrowExceptionForNoCyrillicWord() {
        String inValidWord = "жулиk";
        try {
            game.validateWord(inValidWord);
            fail();
        } catch (Exception e) {
            Logger.log(inValidWord + ": " + e.getMessage());
            assertEquals("Слово должно состоять только из символов кириллицы", e.getMessage());
        }
    }

    @Test
    void shouldThrowExceptionForNoCharWord() {
        String inValidWord = "жу123";
        try {
            game.validateWord(inValidWord);
            fail();
        } catch (Exception e) {
            Logger.log(inValidWord + ": " + e.getMessage());
            assertEquals("Слово должно состоять только из символов кириллицы", e.getMessage());
        }
    }

    @Test
    void filterAfterHint() {
        KnowledgeBase kb = new KnowledgeBase();
        kb.update("абзац", "+----");

        List<String> words = Arrays.asList("автор", "рыбка", "грязь");
        List<String> result = kb.filter(words);

        assertTrue(result.contains("автор"));
        assertFalse(result.contains("рыбка"));
        assertFalse(result.contains("грязь"));
    }

    @Test
    void suggestIsNotEmpty() {
        KnowledgeBase kb = new KnowledgeBase();

        List<String> words = Arrays.asList("автор", "рыбка", "грязь");

        try {
            String suggestion = kb.suggest(words);
            assertNotNull(suggestion);
            assertTrue(words.contains(suggestion));
        } catch (NoHintException e) {
            Logger.log(e.getMessage());
            assertNotNull(e.getMessage());
        }
    }

    @Test
    void shouldReturnAnswerGame() {
        WordleDictionary testDictionary = new WordleDictionary(List.of("автор"));
        try {
            String answer = testDictionary.getAnswer();
            assertEquals("автор", answer);
        } catch (DictionaryException e) {
            Logger.log(e.getMessage());
            fail();
        }
    }

    @Test
    void shouldThrowExceptionForEmptyDictionary() {
        WordleDictionary testDictionary = new WordleDictionary(List.of());
        try {
            testDictionary.getAnswer();
            fail();
        } catch (DictionaryException e) {
            Logger.log(e.getMessage());
            assertEquals("Словарь пуст. Невозможно загадать слово", e.getMessage());
        }
    }

    @Test
    void shouldReturnSizeOfFilteredList() {
        WordleDictionary testDictionary = new WordleDictionary(Arrays.asList("кот", "слово", "первопутье"));
        WordleDictionary filtered = testDictionary.getDictionary();
        assertEquals(1, filtered.getWords().size());
    }

    @Test
    void shouldThrowNoHintExceptionWhenNoHintPossible() {
        // Создаём пустой словарь
        WordleDictionary emptyDict = new WordleDictionary(List.of());
        WordleGame gameWithEmptyDict = new WordleGame("фляга", 6, emptyDict);

        try {
            gameWithEmptyDict.getSuggestion();
            fail();
        } catch (NoHintException e) {
            Logger.log(e.getMessage());
            assertEquals("Нет подходящих слов", e.getMessage());
        }
    }
}
