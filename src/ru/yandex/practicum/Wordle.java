package ru.yandex.practicum;

import java.io.IOException;
import java.util.Scanner;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            Logger.log("Приложение запущено");
            WordleDictionaryLoader wordleDictionaryLoader = new WordleDictionaryLoader();
            WordleDictionary dictionary = wordleDictionaryLoader.load().getDictionary();

            WordleGame game = new WordleGame(dictionary.getAnswer(), 6, dictionary);
            Logger.log("Запуск игры...");

            System.out.println("Введите слово, не более 5 букв (Enter для подсказки): ");

            while (game.canMakeStep()) {
                String answer = scanner.nextLine();

                try {
                    if (answer.isEmpty()) {
                        try {
                            answer = game.getSuggestion();
                            System.out.println(answer);
                        } catch (NoHintException e) {
                            System.out.println(e.getMessage());
                            Logger.log("Сообщение помощника: " + e.getMessage());
                            continue;
                        }
                    }

                    game.validateWord(answer);

                    if (game.checkAnswer(answer)) {
                        System.out.println("Игрок выиграл");
                        Logger.log("Завершение программы по событию выигрыша игрока");
                        return;
                    }

                    System.out.println(game.getHint(answer));
                    game.reduceSteps();

                } catch (UserInputException e) {
                    System.out.println("Ошибка пользователя: " + e.getMessage());
                    Logger.log("Ошибка пользователя: " + e.getMessage());
                }

                if (!game.canMakeStep()) {
                    System.out.println("Завершение игры. Загаданное слово: " + game.getAnswer());
                    Logger.log("Завершение игры");
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка загрузки словаря: " + e.getMessage());
            Logger.log("Ошибка загрузки словаря: " + e.getMessage());
        } catch (GameException e) {
            System.out.println("Ошибка: " + e.getMessage());
            Logger.log("Ошибка: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Неожиданная ошибка: " + e.getMessage());
            Logger.log("Неожиданная ошибка: " + e.getMessage());
        }
    }
}