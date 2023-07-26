import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static final AtomicInteger beautifulWordsLength3 = new AtomicInteger(0);
    private static final AtomicInteger beautifulWordsLength4 = new AtomicInteger(0);
    private static final AtomicInteger beautifulWordsLength5 = new AtomicInteger(0);

    public static void main(String[] args) {
        String[] texts = new String[100_000];
        Random random = new Random();

        // Генерируем 100,000 коротких слов
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        // Создаем три потока для проверки красоты слова
        Thread thread1 = new Thread(() -> checkBeautifulWords(texts, 3));
        Thread thread2 = new Thread(() -> checkBeautifulWords(texts, 4));
        Thread thread3 = new Thread(() -> checkBeautifulWords(texts, 5));

        // Запускаем потоки
        thread1.start();
        thread2.start();
        thread3.start();

        try {
            // Ожидаем завершения всех потоков
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Выводим результаты на экран
        System.out.println("Красивых слов с длиной 3: " + beautifulWordsLength3.get() + " шт");
        System.out.println("Красивых слов с длиной 4: " + beautifulWordsLength4.get() + " шт");
        System.out.println("Красивых слов с длиной 5: " + beautifulWordsLength5.get() + " шт");
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void checkBeautifulWords(String[] texts, int length) {
        for (String text : texts) {
            if (isBeautifulWord(text, length)) {
                switch (length) {
                    case 3 -> beautifulWordsLength3.incrementAndGet();
                    case 4 -> beautifulWordsLength4.incrementAndGet();
                    case 5 -> beautifulWordsLength5.incrementAndGet();
                }
            }
        }
    }

    public static boolean isBeautifulWord(String word, int length) {
        // Проверка красоты слова
        if (word.length() != length) {
            return false;
        }

        if (length == 1) {
            // Слово из одной буквы всегда считается "красивым"
            return true;
        }

        // Проверка на палиндром
        for (int i = 0; i < length / 2; i++) {
            if (word.charAt(i) != word.charAt(length - 1 - i)) {
                return false;
            }
        }

        // Проверка на возрастающую последовательность букв
        for (int i = 1; i < length; i++) {
            if (word.charAt(i) < word.charAt(i - 1)) {
                return false;
            }
        }

        return true;
    }
}
