/*Richard Jung and Michelle Duong collab*/

package com.google.engedu.ghost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import android.util.Log;
import java.util.Random;

public class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        TrieNode cursor = this;
        String key;       // key value each character

        for(int i = 0; i < s.length(); i++) {
            key = s.charAt(i) + "";

            if(cursor.children.containsKey(key)) {
                cursor = cursor.children.get(key);
            } else {
                TrieNode newNode = new TrieNode();
                cursor.children.put(key, newNode);

                cursor = newNode;
            }
        }
//        Log.i("is word", Boolean.toString(cursor.isWord));
        cursor.isWord = true;

//        Log.i("is word now", Boolean.toString(cursor.isWord));
    }

    public boolean isWord(String s) {
        TrieNode cursor = this;
        String key;

        // check to see if s is a word
        for(int i = 0; i < s.length(); i++) {
            key = s.charAt(i) + "";
            // see if the s is part of a word
            if(cursor.children.containsKey(key)) {
                cursor = cursor.children.get(key);
            } else {
                return false;
            }
        }

        if(cursor.isWord) {
            return true;
        } else {
            return false;
        }
    }

    public String getAnyWordStartingWith(String s) {
        TrieNode cursor = this;
        String wordReturned = "";
        String key;

        if (s == null || s.trim().equals("")) {
            // get a random word corresponding to random letters that exist in each keyset
            while (!cursor.isWord) {
                key = "";
                Object[] keys = cursor.children.keySet().toArray();
                key += keys[new Random().nextInt(keys.length)];
//                Log.i("key ", key);
                wordReturned += key;
                cursor = cursor.children.get(key);
            }
        } else {
            for(int i = 0; i < s.length(); i++) {
                key = s.charAt(i) + "";

                // see if the s is part of a word
                if(cursor.children.containsKey(key)) {
                    wordReturned += key;
                    cursor = cursor.children.get(key);
                } else {
                    return null;
                }
            }
            // append the rest of the characters to the s
            while(!cursor.isWord) {
                key = "";
                Object[] keys = cursor.children.keySet().toArray();
                key += keys[new Random().nextInt(keys.length)];
//                Log.i("key ", key);
                wordReturned += key;
                cursor = cursor.children.get(key);
            }
        }

        return wordReturned;
    }

    public String getGoodWordStartingWith(String s) {
        TrieNode cursor = this;
        String wordReturned = "";
        String key;

        if (s == null || s.trim().equals("")) {
            wordReturned += (char)((int)'a' + new Random().nextInt(27));
        } else {
            for(int i = 0; i < s.length(); i++) {
                key = s.charAt(i) + "";

                // see if the s is part of a word
                if(cursor.children.containsKey(key)) {
                    wordReturned += key;
                    cursor = cursor.children.get(key);
                } else {
                    return null;
                }
            }
            if(cursor.isWord) {
                return wordReturned;
            } else {
                key = "";
                Object[] keys = cursor.children.keySet().toArray();
                key += keys[new Random().nextInt(keys.length)];
                while(!cursor.children.containsKey(key)) {
                    key = "";
                    key += keys[new Random().nextInt(keys.length)];
                }

                wordReturned += key;
            }
        }

        return wordReturned;
    }
}
