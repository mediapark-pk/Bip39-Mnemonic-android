# BIP39 Mnemonic Android
BIP39 Android implementation.

[![](https://jitpack.io/v/mediapark-pk/Bip39-Mnemonic-android.svg)](https://jitpack.io/#mediapark-pk/Bip39-Mnemonic-android)

PHP implementation [BIP39 Mnemonic PHP](https://github.com/furqansiddiqui/bip39-mnemonic-php).

Cpp implementation [BIP39 Mnemonic Cpp](https://github.com/mediapark-pk/bip39-cxx).

![BIP39 Mnemonic Android](https://github.com/mediapark-pk/Bip39-Mnemonic-android/blob/master/entropytoWords.png)
![BIP39 Mnemonic Android](https://github.com/mediapark-pk/Bip39-Mnemonic-android/blob/master/wordstoEntropy.png)

Gradle
------
**Step 1**. Add the JitPack repository to your build file

```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
**Step 2**. Add the dependency
```
dependencies {
    ...
    implementation 'com.github.mediapark-pk:Bip39-Mnemonic-android:0.1'
}
```

Usage
-----
**Pass Context to WordList File Before Using The BIP39 Methods**
```
   WordList.setContext(this);
```

**Entropy To Words Generation.**

**Seed Generation with Passphrase "TREZOR" and 512 bytes.**
```Java
        try {
            Mnemonic mnemonic = Bip39.Entropy("00000000000000000000000000000000");
            ArrayList<String> words = mnemonic.words;
            String seed =mnemonic.generateSeed("TREZOR",512);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (MnemonicException e) {
            e.printStackTrace();
        } catch (WordListException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }
```
**Words To Entropy Generation.**

**Seed Generation with Passphrase "TREZOR" and 512 bytes.**

```
        try {
            Mnemonic mnemonic = Bip39.Words("letter advice cage absurd amount doctor acoustic avoid letter advice cage above");
            String entropy = mnemonic.entropy;
            String seed = mnemonic.generateSeed("TREZOR",512);
        } catch (MnemonicException e) {
            e.printStackTrace();
        } catch (WordListException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

```
* **Test Vectors File is Placed in Code**
[Test Vectors](https://github.com/mediapark-pk/Bip39-Mnemonic-android/blob/master/app/src/main/assets/TestVector.json)


**Releases**
* **0.1**
    * Initial release

License
-------
```
MIT License

Copyright (c) 2020 MediaPark-Pk <admin@mediapark.pk>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
