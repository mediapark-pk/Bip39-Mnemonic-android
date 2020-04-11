package com.mediaparkpk.bip39android.utils;

import android.content.Context;

import com.mediaparkpk.bip39android.exceptions.WordListException;

import java.io.IOException;
import java.io.InputStream;

public class WordListUtils {
    public static String getFileContent(String language, Context context) throws WordListException {
        try {
            InputStream inputStreamReader = context.getResources().openRawResource(context.getResources().getIdentifier(language, "raw",context.getPackageName()));
            int size = inputStreamReader.available();
            byte[] buffer = new byte[size];
            inputStreamReader.read(buffer);
            inputStreamReader.close();
            return new String(buffer);
        } catch (IOException e) {
            throw new WordListException("BIP39 wordlist for  not found or is not readable" + language);
        }
    }
}
