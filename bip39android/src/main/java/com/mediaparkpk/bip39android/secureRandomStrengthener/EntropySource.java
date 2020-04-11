package com.mediaparkpk.bip39android.secureRandomStrengthener;

import java.nio.ByteBuffer;


public interface EntropySource {
    ByteBuffer provideEntropy();
}