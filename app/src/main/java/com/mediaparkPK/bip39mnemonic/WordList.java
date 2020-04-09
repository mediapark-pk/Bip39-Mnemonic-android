package com.mediaparkPK.bip39mnemonic;

import android.content.Context;

import com.mediaparkPK.bip39mnemonic.exceptions.WordListException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.mediaparkPK.bip39mnemonic.utils.WordListUtils.getFileContent;

public class WordList {

    private static Map<String, WordList> instances;
    private String language;
    private static Context context;
    private ArrayList<String> words;
    private int count;
    private String[] wordsliststr;

    public WordList(String language, Context context) throws WordListException {
        this.language = language.trim();
        this.words = new ArrayList<>(12);
        this.count = 0;
        this.context = context;
        String wordlist = getFileContent(this.language,context);
        if (wordlist != null) {
            wordsliststr = wordlist.split("\n");
            for (String word : wordsliststr) {
                words.add(word.trim());
                this.count++;
            }
        }
        if (this.count != 2048) {
            throw new WordListException("BIP39 words list file must have precise 2048 entries");
        }

    }
    public static WordList English() throws WordListException {
        return getLanguage("english");
    }

    public static WordList Spanish() throws WordListException {
        return getLanguage("spanish");
    }

    public static WordList Italian() throws WordListException {
        return getLanguage("italian");
    }

    public static WordList French() throws WordListException {
        return getLanguage("french");
    }


    public static WordList getLanguage(String language) throws WordListException {
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



}
