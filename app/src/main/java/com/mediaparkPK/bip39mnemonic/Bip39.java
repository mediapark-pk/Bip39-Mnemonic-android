package com.mediaparkPK.bip39mnemonic;

import android.text.TextUtils;
import android.util.Log;

import com.mediaparkPK.bip39mnemonic.utils.SecureRandomStrengthener;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mediaparkPK.bip39mnemonic.utils.Bip39Utils.base_convert;
import static com.mediaparkPK.bip39mnemonic.utils.Bip39Utils.bytesToHex;
import static com.mediaparkPK.bip39mnemonic.utils.Bip39Utils.hash;
import static com.mediaparkPK.bip39mnemonic.utils.Bip39Utils.hash2bin;
import static com.mediaparkPK.bip39mnemonic.utils.Bip39Utils.str_pad;
import static com.mediaparkPK.bip39mnemonic.utils.Bip39Utils.str_split;

public class Bip39 {
    private final String TAG = "Bip39";
    public final String version = "0.1.2";
    private static int wordsCount;
    private static int overallBits;
    private static int checksumBits;
    private static int entropyBits;
    private static String entropy;
    private static String checksum;
    private static ArrayList<String> rawBinaryChunks;
    private static ArrayList<String> words;
    public static WordList wordList;
    private static ArrayList<Integer> entropyBitsValidator = new ArrayList<>(Arrays.asList(128, 160, 192, 224, 256));

    public Bip39(int wordCount) {
        if (wordCount < 12 || wordCount > 24) {
            Log.d(TAG, " Mnemonic words count must be between 12-24");
        } else if (wordCount % 3 != 0) {
            Log.d(TAG, " Words count must be generated in multiples of 3");
        }
        // Actual words count
        this.wordsCount = wordCount;
        // Overall entropy bits (ENT+CS)
        this.overallBits = this.wordsCount * 11;
        // Checksum Bits are 1 bit per 3 words, starting from 12 words with 4 CS bits
        this.checksumBits = ((this.wordsCount - 12) / 3) + 4;
        // Entropy Bits (ENT)
        this.entropyBits = this.overallBits - this.checksumBits;
    }

    public static Mnemonic Entropy(String entropy) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        validateEntropy(entropy);
        entropyBits = entropy.length() * 4;
        checksumBits = ((entropyBits - 128) / 32) + 4;
        wordsCount = (entropyBits + checksumBits) / 11;
        return (new Bip39(wordsCount))
                .useEntropy(entropy)
                .wordList(WordList.English())
                .mnemonic();
    }

    public static Mnemonic Generate(int wordCount) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return (new Bip39(wordCount))
                .generateSecureEntropy()
                .wordList(WordList.English())
                .mnemonic();
    }

    /**
     * Todo Add the Wordlist and Bool VerifyChecksum in it
     * @param words
     * @return
     */
    public static Mnemonic Words(String words) {

        String[] wordslist = words.split(" ");
        int wordCount = wordslist.length;
        return (new Bip39(wordCount))
                .wordList(WordList.English())
                .reverse(Arrays.asList(wordslist));
    }

    public Bip39 useEntropy(String entropy) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        validateEntropy(entropy);
        this.entropy = entropy;
        this.checksum = this.checksum(entropy, this.checksumBits);
        this.rawBinaryChunks = str_split(hex2bits(this.entropy) + this.checksum, 11);
        return this;
    }

    public Bip39 generateSecureEntropy() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        this.useEntropy(bytesToHex(SecureRandomStrengthener.getInstance().generateAndSeedRandomNumberGenerator().generateSeed(this.entropyBits / 8)));
        return this;
    }

    public Mnemonic mnemonic() {

        Mnemonic mnemonic = new Mnemonic(this.entropy);
        for (final String bit : rawBinaryChunks) {
            int index = Integer.parseInt(base_convert(bit, 2, 10));
            mnemonic.wordsIndex.add(index);
            mnemonic.words.add(this.wordList.getWord(index));
            mnemonic.rawBinaryChunks.add(bit);
            mnemonic.wordsCount++;
        }
        return mnemonic;
    }

    public Bip39 wordList(WordList wordList) {
        this.wordList = wordList;
        return this;
    }

    public Mnemonic reverse(List<String> words) {


        Mnemonic mnemonic = new Mnemonic();
        int pos = 0;
        for (String word : words) {
            pos++;
            int index = wordList.findIndex(word);

            mnemonic.words.add(word);
            mnemonic.wordsIndex.add(index);
            mnemonic.wordsCount++;
            mnemonic.rawBinaryChunks.add(str_pad(base_convert(String.valueOf(index), 10, 2), 11, "0", "STR_PAD_LEFT"));
        }

        String rawBinary = TextUtils.join("", mnemonic.rawBinaryChunks);
        String strentropyBits = rawBinary.substring(0, this.entropyBits);
        //String strchecksumBits = rawBinary.substring(this.entropyBits, this.checksumBits);

        mnemonic.entropy = this.bits2hex(strentropyBits);
        return mnemonic;
    }

    private String hex2bits(String hex) {
        String bits = "";
        for (int i = 0; i < hex.length(); i++) {
            bits = bits + str_pad(base_convert(String.valueOf(hex.charAt(i)), 16, 2), 4, "0", "STR_PAD_LEFT");
        }
        return bits;
    }


    private String bits2hex(String bits) {
        String hex = "";
        for (String chunk : str_split(bits,4)) {
            hex = hex + base_convert(chunk, 2, 16);
        }
        return hex;
    }

    private String checksum(String entropy, int bits) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String  str = hash("SHA-256", hash2bin(entropy), true);
        str=str.substring(0,2);
        int checksumChar= (char)Integer.parseInt(str, 16);
        checksum = "";
        for (int i = 0; i < bits; i++) {
            int j = checksumChar >> (7 - i) & 1;
            checksum = checksum + j;
        }
        return checksum;
    }

    private static void validateEntropy(String entropy) {
        int entropyBits = entropy.length() * 4;
        if (entropyBitsValidator.contains(entropyBits)) {
        }
    }

}
