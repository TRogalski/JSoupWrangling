package main;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class WebWordCounter {

    private Map<String, Integer> wordCounts = new HashMap<>();

    public static void main(String[] args) {
        WebWordCounter wordCounter = new WebWordCounter();
        wordCounter.countWords();
        wordCounter.displayCounts();
    }

    public void countWords() {
        Connection connect = Jsoup.connect("http://www.onet.pl/");
        try {
            Document document = connect.get();
            Elements lines = document.select("span.title");
            for (Element line : lines) {
                List<String> lineWords = formatLine(line.text());
                feedNewWords(lineWords);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void feedNewWords(List<String> words) {
        for (String word : words) {
            if (wordCounts.keySet().contains(word.replaceAll(" ",""))) {
                wordCounts.put(word, wordCounts.get(word) + 1);
            } else {
                wordCounts.put(word, 1);
            }
        }
    }

    private List<String> formatLine(String line) {
        List<String> wordList = Arrays.asList(line.replaceAll("[,.?!\"\'0-9\\-–:\\]\\[]", "")
                .toLowerCase().trim().split(" "));
        return wordList;
    }

    private void displayCounts() {
        wordCounts.entrySet().stream()
                .filter(x->x.getKey().length()>3)
                .sorted(Collections.reverseOrder(Map.Entry.<String, Integer>comparingByValue()))
                .forEach(System.out::println);
    }

    private void saveToFile(){
        //TODO
    }
}
