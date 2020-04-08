package com.mediaparkPK.bip39mnemonic.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Bip39Utils {

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static int ord(String s) throws UnsupportedEncodingException {
        return s.length() > 0 ? (s.getBytes("UTF-8")[0] & 0xff) : 0;
    }

        public static String hash2bin(String paramString) throws UnsupportedEncodingException {
        if (paramString.length() < 1)
            return null;
        byte[] arrayOfByte = new byte[paramString.length() / 2];
        for (int i = 0; i < paramString.length() / 2; i++) {
            int j = Integer.parseInt(paramString.substring(i * 2, i * 2 + 1), 16);
            int k = Integer.parseInt(paramString.substring(i * 2 + 1, i * 2 + 2), 16);

            arrayOfByte[i] = ((byte) (j * 16 + k));
        }
        return new String(arrayOfByte);
    }
//    public static String hash2bin(String hex) {
//        StringBuilder output = new StringBuilder();
//        for (int i = 0; i < hex.length(); i+=2) {
//            String str = hex.substring(i, i+2);
//            output.append((char)Integer.parseInt(str, 16));
//        }
//        return output.toString();
//    }

    public static String hash(String algo, String hash2bin, boolean shouldBin) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        MessageDigest md = MessageDigest.getInstance(algo);
        md.update(hash2bin.getBytes());

        byte byteData[] = md.digest();
        int x = byteData[0];
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    private static String hexaToBinary(String hex) {
        String bin = "";
        String binFragment = "";
        int iHex;
        hex = hex.trim();
        hex = hex.replaceFirst("0x", "");

        for (int i = 0; i < hex.length(); i++) {
            iHex = Integer.parseInt("" + hex.charAt(i), 16);
            binFragment = Integer.toBinaryString(iHex);

            while (binFragment.length() < 4) {
                binFragment = "0" + binFragment;
            }
            bin += binFragment;
        }
        return bin;
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


    /**************************
     *
     * STR_PAD IMPLEMENTED
     *
     **************************/
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

    /**
     * @param inputValue
     * @param fromBase
     * @param toBase
     * @return
     */
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

    /**
     * @param str
     * @param maxValue
     * @return
     */
    public static ArrayList<String> str_split(String str, int maxValue) {
        ArrayList<String> strings = new ArrayList<String>();
        int index = 0;
        while (index < str.length()) {
            strings.add(str.substring(index, Math.min(index + maxValue, str.length())));
            index += maxValue;
        }
        return strings;

    }


    public static int hexValue(char hex_digit) throws Exception {
        switch (hex_digit) {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return (int) hex_digit - '0';

            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
                return (int) hex_digit - 'A' + 10;

            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
                return (int) hex_digit - 'a' + 10;
        }
        throw new Exception("bad ");
    }

    public static String base16Decode(String input) {
        int len = input.length();
        if (len % 2 == 1) {
            return "";
        }

        String output = "";
        for (int i = 0; i < input.length(); ) {
            try {
                i++;
                int hi = hexValue((char) i);
                i++;
                int lo = hexValue((char) i);
                int j = hi << 4 | lo;
                output = output + j;
            } catch (Exception ex) {
            }
        }
        return output;
    }
    public static String bin2hex(byte[] data) {
        StringBuilder hex = new StringBuilder(data.length * 2);
        for (byte b : data)
            hex.append(String.format("%02x", b & 0xFF));
        return hex.toString();
    }
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }


}
