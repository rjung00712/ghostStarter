package com.google.engedu.ghost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import java.util.Random;
import java.util.Comparator;
import java.util.*;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    private Random random = new Random();

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

//    @Override
//    public String getAnyWordStartingWith(String prefix) {
//        // check if prefix is empty
//        if(prefix.isEmpty()) {
//            int randomIndex = random.nextInt(words.size());
//            return words.get(randomIndex);
//        }
//        if(!prefix.isEmpty()){    // binary search
//            int start = 0;  // first index
//            int end = words.size() - 1; // last index
//            int middle;  // middle index;
//
//            while(end >= start) {
//                middle = (start + end) / 2;
//                if (words.get(middle).substring(0, prefix.length()).equals(prefix)) {
//                    return words.get(middle);
//                } else if (prefix.compareTo(words.get(middle).substring(0, prefix.length())) < 0) {
//                    end = middle - 1;
//                } else if (prefix.compareTo(words.get(middle).substring(0, prefix.length())) > 0) {
//                    start = middle + 1;
//                }
//            }
//        }
//        return null;
//    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        if(prefix == null || prefix.trim().equals("")){
            Random random = new Random();
            return words.get(random.nextInt(words.size()));
        }
        else{
            Comparator<String> comparator = new Comparator<String>() {
                @Override
                public int compare(String lhs, String rhs) {
                    return lhs.substring(0, Math.min(rhs.length(),lhs.length())).compareToIgnoreCase(rhs);
                }
            };
            int index = Collections.binarySearch(words,prefix,comparator);
            if(index >= 0){
                return words.get(index);
            }
            else{
                return null;
            }
        }
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        String selected = null;

        return selected;
    }
}
