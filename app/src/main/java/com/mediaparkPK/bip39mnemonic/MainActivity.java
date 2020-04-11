package com.mediaparkPK.bip39mnemonic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.mediaparkpk.bip39android.Bip39;
import com.mediaparkpk.bip39android.Mnemonic;
import com.mediaparkpk.bip39android.WordList;
import com.mediaparkpk.bip39android.exceptions.MnemonicException;
import com.mediaparkpk.bip39android.exceptions.WordListException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    private ArrayList<String> entropies = new ArrayList<>();
    private ArrayList<String> words = new ArrayList<>();
    private ArrayList<String> seeds = new ArrayList<>();
    private String passPhrase="TREZOR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.tv);
        WordList.setContext(this);
        loadJSONFromAsset();
        int i = 0;
        for (String entropy:entropies){

            try {
                Mnemonic mnemonic= Bip39.Entropy(entropy);
                if(TextUtils.join(" ",mnemonic.words).equals(words.get(i))) {
                    if(mnemonic.generateSeed(passPhrase,512).toLowerCase().equals(seeds.get(i))){
                        Log.d("zaryabtest", "\nEntropy : "+entropy + "\n "+words.get(i) + "\n "+seeds.get(i)+"\n verified");
                    }
                    else {
                        Log.d("zaryabtest", "\nEntropy : "+entropy + "\n "+words.get(i) + "\n "+seeds.get(i)+"\n not verified");
                    }
                    i++;
                }
                else{
                    Log.d("zaryabtest", "\nEntropy : "+entropy + "\n "+words.get(i) + "\n not verified");
                    i++;
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (MnemonicException e) {
                e.printStackTrace();
            } catch (WordListException e) {
                e.printStackTrace();
            }
        }


    }

    public void loadJSONFromAsset() {
        try {
            InputStream is = getAssets().open("TestVector.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            JSONObject jsonObject = new JSONObject(new String(buffer, "UTF-8"));
            JSONArray jsonArray = jsonObject.getJSONArray("english");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONArray temp = jsonArray.getJSONArray(i);
                entropies.add((String) temp.get(0));
                words.add((String) temp.get(1));
                seeds.add((String) temp.get(2));

            }
            Log.d("zaryab123", "loadJSONFromAsset: " + jsonObject.toString());
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        }
    }
}
