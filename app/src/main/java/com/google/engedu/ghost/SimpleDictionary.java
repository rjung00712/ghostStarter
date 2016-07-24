package com.google.engedu.ghost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import android.util.Log;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        if (prefix == null || prefix.trim().equals("")) {
            Random random = new Random();
            return words.get(random.nextInt(words.size()));
        } else {
            int start = 0;  // first index
            int end = words.size() - 1; // last index
            int middle;  // middle index;

            while (end >= start) {
                middle = (start + end) / 2;
                String middleWord = words.get(middle);

                Log.i("current word: ", words.get(middle));

                if (isPrefix(prefix, middleWord)) {
                    return middleWord;
                } else if (prefix.compareTo(words.get(middle)) < 0) {
                    end = middle - 1;
                } else if (prefix.compareTo(words.get(middle)) > 0) {
                    start = middle + 1;
                }
            }

        }
        return null;
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        String selected = null;

        // lists to separate even length words and odd length words with prefix
        ArrayList<String> evenWords = new ArrayList<>();
        ArrayList<String> oddWords = new ArrayList<>();

        if(prefix == null || prefix.trim().equals("")) {
            Random random = new Random();
            return words.get(random.nextInt(words.size()));
        }
        else {
            int start = 0;  // first index
            int end = words.size() - 1; // last index
            int middle;  // middle index;

            while(end >= start) {
                middle = (start + end) / 2;

                String middleWord = words.get(middle);

                // find all words in range with prefix and sort them according to even or odd
                if(isPrefix(prefix, middleWord)) {
                    int i = middle;
                    String it = words.get(i);  // cursor to iterate through list
                    while(isPrefix(prefix, it)) {
                        Log.i("this is added: ", it);
                        if(it.length() % 2 == 0) {
                            evenWords.add(it);
                        } else {
                            oddWords.add(it);
                        }
                        i--;
                        it = words.get(i);
                    }

                    i = middle;
                    it = words.get(i);
                    while(isPrefix(prefix, it)) {
                        Log.i("this is added: ", it);
                        if(it.length() % 2 == 0) {
                            evenWords.add(it);
                        } else {
                            oddWords.add(it);
                        }
                        i++;
                        it = words.get(i);
                    }

                    // randomly select a word from appropriate set
                    // depending on who went first
                    Random random = new Random();
                    GhostActivity activity = new GhostActivity();
                    if(activity.userFirst()) {  // if user went first
                        if(activity.isUserTurn()) {
                            return oddWords.get(random.nextInt(oddWords.size()));
                        } else {
                            return evenWords.get(random.nextInt(evenWords.size()));
                        }
                    } else {    // if computer went first
                        if(!activity.isUserTurn()) {    // if currently computer's turn
                            return oddWords.get(random.nextInt(oddWords.size()));
                        } else {
                            return evenWords.get(random.nextInt(evenWords.size()));
                        }
                    }
                } else if (prefix.compareTo(words.get(middle)) < 0) {
                    end = middle - 1;
                } else if (prefix.compareTo(words.get(middle)) > 0) {
                    start = middle + 1;
                }
            }

        }
        return null;
    }

    public boolean isPrefix(String prefix, String word) {
        return (word.startsWith(prefix) && !word.equals(prefix));
    }
}
