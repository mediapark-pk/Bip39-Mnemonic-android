package com.mediaparkpk.bip39android;

import android.text.TextUtils;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import static com.mediaparkpk.bip39android.utils.Bip39Utils.bytesToHex;
import static com.mediaparkpk.bip39android.utils.MnemonicUtils.pbkdf2;

public class Mnemonic {

    public String entropy;
    public int wordsCount;
    public ArrayList<Integer> wordsIndex;
    public ArrayList<String> words;
    public ArrayList<String> rawBinaryChunks;


    public Mnemonic(){
        this.entropy = "";
        this.wordsCount = 0;
        this.wordsIndex= new ArrayList<>();
        this.words=new ArrayList<>();
        this.rawBinaryChunks= new ArrayList<>();
    }


    public  Mnemonic(String entropy) {
        this.entropy = entropy;
        this.wordsCount = 0;
        this.wordsIndex= new ArrayList<>();
        this.words=new ArrayList<>();
        this.rawBinaryChunks= new ArrayList<>();
    }
    public String generateSeed(String passphrase, int bytes) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return bytesToHex(pbkdf2(
                TextUtils.join(" ",words).toCharArray(),
                ("mnemonic"+passphrase).getBytes(),
                2048,
                bytes
        ));
    }



}
