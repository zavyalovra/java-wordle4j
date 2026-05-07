package ru.yandex.practicum;

import java.util.*;

public class KnowledgeBase {

    private final Set<Character> requiredLetters = new HashSet<>();
    private final Set<Character> excludedLetters = new HashSet<>();
    private final Character[] correctPositions = new Character[5];
    private final Map<Integer, Set<Character>> wrongPositions = new HashMap<>();

    public KnowledgeBase() {
        for (int i = 0; i < 5; i++) {
            wrongPositions.put(i, new HashSet<>());
        }
    }

    public void update(String guess, String hint) {
        for (int i = 0; i < 5; i++) {
            char c = guess.charAt(i);
            char h = hint.charAt(i);

            if (h == '+') {
                correctPositions[i] = c;
                requiredLetters.add(c);
            } else if (h == '^') {
                requiredLetters.add(c);
                wrongPositions.get(i).add(c);
            } else { // '-'
                if (!requiredLetters.contains(c)) {
                    excludedLetters.add(c);
                }
            }
        }
    }

    public boolean isCandidate(String word) {

        // проверка на исключенные буквы
        for (char c : word.toCharArray()) {
            if (excludedLetters.contains(c)) return false;
        }

        // проверка на наличие правильных букв
        for (char c : requiredLetters) {
            if (!word.contains(String.valueOf(c))) return false;
        }

        // правильные позиции
        for (int i = 0; i < 5; i++) {
            if (correctPositions[i] != null &&
                    word.charAt(i) != correctPositions[i]) {
                return false;
            }
        }

        // неправильные позиции
        for (int i = 0; i < 5; i++) {
            if (wrongPositions.get(i).contains(word.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public List<String> filter(List<String> words) {
        List<String> result = new ArrayList<>();

        for (String word : words) {
            if (isCandidate(word)) {
                result.add(word);
            }
        }

        return result;
    }

    public String suggest(List<String> words) throws NoHintException {
        Random random = new Random();
        List<String> candidates = filter(words);

        if (candidates.isEmpty()) {
            throw new NoHintException("Нет подходящих слов");
        }

        return candidates.get(random.nextInt(candidates.size()));
    }
}
