package com.mediaparkPK.bip39mnemonic.secureRandomStrengthener;

import java.nio.ByteBuffer;


public interface EntropySource {
    ByteBuffer provideEntropy();
}