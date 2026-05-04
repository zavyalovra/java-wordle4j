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
        try {
            game.validateWord("абзац");
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void shouldThrowExceptionForInvalidWord() {
        try {
            game.validateWord("дом");
        } catch (Exception e) {
            assertFalse(e.getMessage().isEmpty());
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
        String suggestion = kb.suggest(words);

        assertNotNull(suggestion);
        assertTrue(words.contains(suggestion));
    }

    @Test
    void shouldReturnAnswerGame() {
        WordleDictionary testDictionary = new WordleDictionary(Arrays.asList("автор"));
        try {
            String answer = testDictionary.getAnswer();
            assertEquals("автор", answer);
        } catch (DictionaryException e) {
            fail();
        }
    }

    @Test
    void shouldThrowExceptionForEmptyDictionary() {
        WordleDictionary testDictionary = new WordleDictionary(Arrays.asList());
        try {
            testDictionary.getAnswer();
        } catch (DictionaryException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    void shouldReturnSizeOfFilteredList() {
        WordleDictionary testDictionary = new WordleDictionary(Arrays.asList("кот", "слово", "первопутье"));
        WordleDictionary filtered = testDictionary.getDictionary();
        assertEquals(1, filtered.getWords().size());
    }
}
