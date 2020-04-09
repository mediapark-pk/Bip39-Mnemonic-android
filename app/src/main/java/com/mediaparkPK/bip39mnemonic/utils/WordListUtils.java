package com.mediaparkPK.bip39mnemonic.utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

public class WordListUtils {
    public static String getFileContent(String language, Context context) {
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
