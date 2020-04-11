package com.mediaparkpk.bip39android;

import android.text.TextUtils;

import com.mediaparkpk.bip39android.exceptions.MnemonicException;
import com.mediaparkpk.bip39android.exceptions.WordListException;
import com.mediaparkpk.bip39android.secureRandomStrengthener.SecureRandomStrengthener;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mediaparkpk.bip39android.utils.Bip39Utils.base_convert;
import static com.mediaparkpk.bip39android.utils.Bip39Utils.bytesToHex;
import static com.mediaparkpk.bip39android.utils.Bip39Utils.hash;
import static com.mediaparkpk.bip39android.utils.Bip39Utils.hex2bin;
import static com.mediaparkpk.bip39android.utils.Bip39Utils.str_pad;
import static com.mediaparkpk.bip39android.utils.Bip39Utils.str_split;

public class Bip39 {
    private final String TAG = "Bip39";
    public final String version = "0.1.2";
    private int wordsCount;
    private int overallBits;
    private int checksumBits;
    private int entropyBits;
    private String entropy;
    private String checksum;
    private ArrayList<String> rawBinaryChunks;
    private ArrayList<String> words;
    public WordList wordList;

    public Bip39(int wordCount) throws MnemonicException {
        if (wordCount < 12 || wordCount > 24) {
            throw new MnemonicException("Mnemonic words count must be between 12-24");
        } else if (wordCount % 3 != 0) {
            throw new MnemonicException("Words count must be generated in multiples of 3");
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

    public static Mnemonic Entropy(String entropy) throws UnsupportedEncodingException, NoSuchAlgorithmException, MnemonicException, WordListException {
        validateEntropy(entropy);
        int entropyBits = entropy.length() * 4;
        int checksumBits = ((entropyBits - 128) / 32) + 4;
        int wordsCount = (entropyBits + checksumBits) / 11;
        return (new Bip39(wordsCount))
                .useEntropy(entropy)
                .wordList(WordList.English())
                .mnemonic();
    }

    public static Mnemonic Generate(int wordCount) throws UnsupportedEncodingException, NoSuchAlgorithmException, MnemonicException, WordListException {
        return (new Bip39(wordCount))
                .generateSecureEntropy()
                .wordList(WordList.English())
                .mnemonic();
    }

    /**
     * Todo Add the Wordlist and Bool VerifyChecksum in it
     *
     * @param words
     * @return
     */
    public static Mnemonic Words(String words) throws MnemonicException, WordListException {

        String[] wordslist = words.split(" ");
        int wordCount = wordslist.length;
        return (new Bip39(wordCount))
                .wordList(WordList.English())
                .reverse(Arrays.asList(wordslist));
    }

    public Bip39 useEntropy(String entropy) throws UnsupportedEncodingException, NoSuchAlgorithmException, MnemonicException {
        validateEntropy(entropy);
        this.entropy = entropy;
        this.checksum = this.checksum(entropy, this.checksumBits);
        this.rawBinaryChunks = str_split(hex2bits(this.entropy) + this.checksum, 11);
        return this;
    }

    public Bip39 generateSecureEntropy() throws UnsupportedEncodingException, NoSuchAlgorithmException, MnemonicException {
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
        for (String chunk : str_split(bits, 4)) {
            hex = hex + base_convert(chunk, 2, 16);
        }
        return hex;
    }

    private String checksum(String entropy, int bits) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String str = hash("SHA-256", hex2bin(entropy), true);
        str = str.substring(0, 2);
        int checksumChar = (char) Integer.parseInt(str, 16);
        checksum = "";
        for (int i = 0; i < bits; i++) {
            int j = checksumChar >> (7 - i) & 1;
            checksum = checksum + j;
        }
        return checksum;
    }

    private static void validateEntropy(String entropy) throws MnemonicException {
        if (entropy.matches("/^[a-f0-9]{2,}$/")) {
            throw new MnemonicException("Invalid entropy (requires hexadecimal)");
        }
        ArrayList<Integer> entropyBitsValidator = new ArrayList<>(Arrays.asList(128, 160, 192, 224, 256));
        int entropyBits = entropy.length() * 4;
        if (!entropyBitsValidator.contains(entropyBits)) {
            throw new MnemonicException("Invalid entropy length");
        }
    }

}
