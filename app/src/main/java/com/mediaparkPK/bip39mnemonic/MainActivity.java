package com.mediaparkPK.bip39mnemonic;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    private ArrayList<String> entropies = new ArrayList<>();
    private ArrayList<String> words = new ArrayList<>();
    private ArrayList<String> seeds = new ArrayList<>();
    private ArrayList<String> spinnerOptions = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private String passPhrase = "TREZOR";
    private TextView output, seed, outputTv;
    private Spinner options;
    private EditText input, inputpasspharse;
    private Button tests, alltests, generateSeed;
    Mnemonic mnemonic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input = (EditText) findViewById(R.id.input);
        options = (Spinner) findViewById(R.id.options);
        output = (TextView) findViewById(R.id.output);
        seed = (TextView) findViewById(R.id.seed);
        tests = (Button) findViewById(R.id.test);
        alltests = (Button) findViewById(R.id.alltest);
        outputTv = (TextView) findViewById(R.id.textoutput);
        inputpasspharse = (EditText) findViewById(R.id.passpharse);
        generateSeed = (Button) findViewById(R.id.generateSeed);
        spinnerOptions.add("Entropy to Words");
        spinnerOptions.add("Words to Entropy");
        adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, spinnerOptions);
        options.setAdapter(adapter);
        WordList.setContext(this);
        loadJSONFromAsset();
        alltests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Please Check Application Logs", Toast.LENGTH_SHORT).show();
                inputpasspharse.setVisibility(View.GONE);
                generateSeed.setVisibility(View.GONE);
                runAllTests();
            }
        });
        tests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    runTest();
                } catch (WordListException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                } catch (NoSuchAlgorithmException e) {
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (MnemonicException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
        generateSeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    GenerateSeed();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void GenerateSeed() throws InvalidKeySpecException, NoSuchAlgorithmException {
        if(mnemonic!=null) {
            if (!inputpasspharse.getText().toString().isEmpty()) {
                seed.setText(mnemonic.generateSeed(passPhrase,512));


            } else {
                Toast.makeText(this, "Please Enter Passphrase", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Mnemnonic is Null", Toast.LENGTH_SHORT).show();
        }
    }

    private void runTest() throws WordListException, NoSuchAlgorithmException, MnemonicException, UnsupportedEncodingException, InvalidKeySpecException {
        if (options.getSelectedItem().toString().equals("Entropy to Words")) {
            if (!input.getText().toString().isEmpty()) {
                mnemonic = Bip39.Entropy(input.getText().toString());
                if (mnemonic != null) {
                    outputTv.setText("Words");
                    output.setText(TextUtils.join(" ", mnemonic.words));
                    inputpasspharse.setVisibility(View.VISIBLE);
                    generateSeed.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(this, "Please Enter the Entropy", Toast.LENGTH_SHORT).show();
            }

        } else {
            if (!input.getText().toString().isEmpty()) {
                mnemonic = Bip39.Words(input.getText().toString());
                if (mnemonic != null) {
                    outputTv.setText("Entropy");
                    output.setText(mnemonic.entropy);
                    inputpasspharse.setVisibility(View.VISIBLE);
                    generateSeed.setVisibility(View.VISIBLE);
                }
            }
            else{
                Toast.makeText(this, "Please Enter the Words", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void loadJSONFromAsset() {
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

    private void runAllTests() {
        int i = 0;
        for (String entropy : entropies) {

            try {
                Mnemonic mnemonic = Bip39.Entropy(entropy);
                if (TextUtils.join(" ", mnemonic.words).equals(words.get(i))) {
                    if (mnemonic.generateSeed(passPhrase, 512).toLowerCase().equals(seeds.get(i))) {
                        Log.d("MediaparkPk", "\nEntropy : " + entropy + "\n " + words.get(i) + "\n " + seeds.get(i) + "\n verified");
                    } else {
                        Log.d("MediaparkPk", "\nEntropy : " + entropy + "\n " + words.get(i) + "\n " + seeds.get(i) + "\n not verified");
                    }
                    i++;
                } else {
                    Log.d("MediaparkPk", "\nEntropy : " + entropy + "\n " + words.get(i) + "\n not verified");
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


   




}
