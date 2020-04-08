package com.mediaparkPK.bip39mnemonic;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WordList {

    private static Map<String, WordList> instances;
    private String language;
    private static Context context;
    private ArrayList<String> words;
    private int count;
    private String[] wordsliststr;

    public WordList(String language, Context context) {
        this.language = language.trim();
        this.words = new ArrayList<>(12);
        this.count = 0;
        this.context = context;
        String wordlist = getFileContent();
        if (wordlist != null) {
            wordsliststr = wordlist.split("\n");
            for (String word : wordsliststr) {
                words.add(word.trim());
                this.count++;
            }
        }
        if (this.count != 2048) {

        }

    }
    public static WordList English() {
        return getLanguage("english");
    }

    public static WordList Spanish() {
        return getLanguage("spanish");
    }

    public static WordList Italian() {
        return getLanguage("italian");
    }

    public static WordList French() {
        return getLanguage("french");
    }


    public static WordList getLanguage(String language) {
        if (instances == null) {
            instances = new HashMap<>();
        }
        if (instances.containsKey(language)) {
            return instances.get(language);
        }
        instances.put(language, new WordList(language, context));
        return instances.get(language);

    }

    public String which() {
        return this.language;
    }

    public String getWord(int index) {
        return this.words.get(index);
    }

    public int findIndex(String search) {
        if (words.contains(search)) {
            return words.indexOf(search);
        }
        return -1;
    }

    private String getFileContent() {
        try {
            InputStream inputStreamReader = context.getAssets().open(language + ".txt");
            int size = inputStreamReader.available();
            byte[] buffer = new byte[size];
            inputStreamReader.read(buffer);
            inputStreamReader.close();
            return new String(buffer);
        } catch (IOException e) {
        }
        return null;
    }

}
