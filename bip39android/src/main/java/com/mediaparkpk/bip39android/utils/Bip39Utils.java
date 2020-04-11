package com.mediaparkpk.bip39android.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class Bip39Utils {

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static byte[] hex2bin(String hex) throws NumberFormatException {
        if (hex.length() % 2 > 0) {
            throw new NumberFormatException("Hexadecimal input string must have an even length.");
        }
        byte[] r = new byte[hex.length() / 2];
        for (int i = hex.length(); i > 0; ) {
            r[i / 2 - 1] = (byte) (digit(hex.charAt(--i)) | (digit(hex.charAt(--i)) << 4));
        }
        return r;
    }

    public static String hash(String algo, byte[] hash2bin, boolean shouldBin) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        MessageDigest md = MessageDigest.getInstance(algo);
        md.update(hash2bin);

        byte byteData[] = md.digest();
        int x = byteData[0];
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public static final String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String str_pad(String input, int length, String pad, String sense) {
        int resto_pad = length - input.length();
        String padded = "";

        if (resto_pad <= 0) {
            return input;
        }

        if (sense.equals("STR_PAD_RIGHT")) {
            padded = input;
            padded += _fill_string(pad, resto_pad);
        } else if (sense.equals("STR_PAD_LEFT")) {
            padded = _fill_string(pad, resto_pad);
            padded += input;
        } else // STR_PAD_BOTH
        {
            int pad_left = (int) Math.ceil(resto_pad / 2);
            int pad_right = resto_pad - pad_left;

            padded = _fill_string(pad, pad_left);
            padded += input;
            padded += _fill_string(pad, pad_right);
        }
        return padded;
    }

    private static String _fill_string(String pad, int resto) {
        boolean first = true;
        String padded = "";

        if (resto >= pad.length()) {
            for (int i = resto; i >= 0; i = i - pad.length()) {
                if (i >= pad.length()) {
                    if (first) {
                        padded = pad;
                    } else {
                        padded += pad;
                    }
                } else {
                    if (first) {
                        padded = pad.substring(0, i);
                    } else {
                        padded += pad.substring(0, i);
                    }
                }
                first = false;
            }
        } else {
            padded = pad.substring(0, resto);
        }
        return padded;
    }

    public static String base_convert(final String inputValue, final int fromBase, final int toBase) {
        if (fromBase < 2 || fromBase > 36 || toBase < 2 || toBase > 36) {
            return null;
        }
        String ret = null;
        try {
            ret = Integer.toString(Integer.parseInt(inputValue, fromBase), toBase);
        } catch (Exception ex) {
        }
        ;
        return ret;
    }

    public static ArrayList<String> str_split(String str, int maxValue) {
        ArrayList<String> strings = new ArrayList<String>();
        int index = 0;
        while (index < str.length()) {
            strings.add(str.substring(index, Math.min(index + maxValue, str.length())));
            index += maxValue;
        }
        return strings;

    }

    private static int digit(char ch) {
        int r = Character.digit(ch, 16);
        if (r < 0) {
            throw new NumberFormatException("Invalid hexadecimal string: " + ch);
        }
        return r;
    }


}
