package com.mediaparkPK.bip39mnemonic.utils;

import java.nio.ByteBuffer;


public interface EntropySource {
    ByteBuffer provideEntropy();
}