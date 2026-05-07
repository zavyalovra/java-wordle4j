package ru.yandex.practicum;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {

    private final String answer;
    private int steps;
    private final WordleDictionary dictionary;
    private final KnowledgeBase knowledge = new KnowledgeBase();

    public WordleGame(String answer, int steps, WordleDictionary dictionary) {
        this.answer = answer;
        this.steps = steps;
        this.dictionary = dictionary;
    }

    public boolean canMakeStep() {
        return steps > 0;
    }

    public void reduceSteps() {
        steps--;
    }

    public void validateWord(String answer) throws UserInputException {
        String normalizedAnswer = answer.toLowerCase().replace("ё", "е");

        if (normalizedAnswer.length() != 5) {
            throw new UserInputException("Слово должно состоять строго из пяти букв");
        }

        for (int i = 0; i < normalizedAnswer.length(); i++) {
            char symbol = normalizedAnswer.charAt(i);
            boolean isCyrillic = symbol >= 'а' && symbol <= 'я';
            if (!isCyrillic) throw new UserInputException("Слово должно состоять только из символов кириллицы");
        }

        boolean isExistWord = false;
        for (String word : dictionary.getWords()) {
            if (normalizedAnswer.equals(word)) {
                isExistWord = true;
                break;
            }
        }

        if (!isExistWord) {
            throw new UserInputException("Этого слова нет в словаре");
        }
    }

    public boolean checkAnswer(String answer) {
        String normalizedAnswer = answer.toLowerCase().replace("ё", "е");
        return normalizedAnswer.equals(this.answer);
    }

    public String getHint(String input) {
        String normalizedInput = input.toLowerCase().replace("ё", "е");

        StringBuilder hint = new StringBuilder("-----");
        StringBuilder usedChars = new StringBuilder(this.answer);

        for (int i = 0; i < 5; i++) {
            if (normalizedInput.charAt(i) == this.answer.charAt(i)) {
                hint.setCharAt(i, '+');
                usedChars.setCharAt(i, '*');
            }
        }

        for (int i = 0; i < 5; i++) {
            if (hint.charAt(i) != '-') continue;

            char currentChar = normalizedInput.charAt(i);
            int indexInAnswer = usedChars.indexOf(String.valueOf(currentChar));

            if (indexInAnswer != -1) {
                hint.setCharAt(i, '^');
                usedChars.setCharAt(indexInAnswer, '*');
            }
        }

        String result = hint.toString();
        knowledge.update(normalizedInput, result);

        return result;
    }

    public String getSuggestion() throws NoHintException {
        return knowledge.suggest(dictionary.getWords());
    }

    public String getAnswer() {
        return answer;
    }
}
