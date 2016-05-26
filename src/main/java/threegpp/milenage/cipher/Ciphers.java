/*
 * Copyright (c) 2016 Constantin Roganov 
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software 
 * and associated documentation files (the "Software"), to deal in the Software without restriction, 
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, 
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is 
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or 
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING 
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package threegpp.milenage.cipher;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * <h1>Ciphers</h1>
 * <p>
 * Helper class for creating ciphers.
 *
 * @author Constantin Roganov {@literal <rccbox @ gmail . com>}
 * @version 1.0.0
 * @since 14.05.16
 */
public class Ciphers {

    /**
     * Create Cipher from algorithm parameters and key bytes.
     *
     * @param cipherId  {@link String} representation of cipher ID for {@link Cipher#getInstance(String)}.
     * @param algoId  {@link String} representation of algorithm ID for {@link SecretKeySpec#SecretKeySpec(byte[], String)}.
     * @param key  byte array representing a ciphering key.
     * @return  {@link Cipher} object.
     */
    public static Cipher createCipher(String cipherId, String algoId, byte [] key) {
        try {
            Key cipheringKey = new SecretKeySpec(key, algoId);

            Cipher cipher = Cipher.getInstance(cipherId);
            cipher.init(Cipher.ENCRYPT_MODE, cipheringKey);

            return cipher;

        } catch (NoSuchAlgorithmException
                | NoSuchPaddingException
                | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create Rijndael (a.k.a. AES) cipher.
     *
     * @param key  byte array representing a ciphering key.
     * @return  {@link Cipher} object.
     */
    public static Cipher createRijndaelCipher(byte [] key) {
        return createCipher("AES/ECB/NoPadding", "AES", key);
    }
}
