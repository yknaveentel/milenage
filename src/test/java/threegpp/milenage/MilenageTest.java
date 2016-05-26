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
package threegpp.milenage;

import org.junit.Test;
import threegpp.milenage.biginteger.BigIntegerBuffer;
import threegpp.milenage.biginteger.BigIntegerBufferFactory;
import threegpp.milenage.cipher.Ciphers;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertArrayEquals;
import static threegpp.milenage.MilenageTestData.*;

/**
 * <h1>MilenageTest</h1>
 * <p/>
 *
 * @author Constantin Roganov {@literal <rccbox @ gmail . com>}
 * @version 1.0.0
 * @since 05.03.16
 */
public class MilenageTest {

    private static MilenageBufferFactory<BigIntegerBuffer> factory = BigIntegerBufferFactory.getInstance();
    private static Cipher key = Ciphers.createRijndaelCipher(K);

    @Test
    public void opcTest() {
        byte [] opc = Milenage.calculateOPc(OP, key, factory);

        assertArrayEquals(opc, OPC);
    }

    @Test
    public void cipherTest() {
        MilenageBuffer<BigInteger> plain = factory.create(PLAIN);
        assertArrayEquals(CIPHER, plain.encrypt(key).toBytes());
    }

    @Test
    public void etsiTs135207Test() {
        byte [] opc = Milenage.calculateOPc(OP, key, factory);
        CConstants<BigIntegerBuffer> cConstants = new CConstants<>(factory);
        RConstants rConstants = new RConstants();

        Milenage<BigIntegerBuffer> milenage = new Milenage<>(opc, key, cConstants, rConstants, factory);

        Map<MilenageResult, byte []> f1All = milenage.f1All(RAND, SQN, AMF);
        byte [] f1 = f1All.get(MilenageResult.MAC_A);
        byte [] f1Star = f1All.get(MilenageResult.MAC_S);

        Map<MilenageResult, byte []> f2f5 = milenage.f2f5(RAND);
        byte [] f2 = f2f5.get(MilenageResult.RES);
        byte [] f5 = f2f5.get(MilenageResult.AK);

        byte [] f3 = milenage.f3(RAND);
        byte [] f4 = milenage.f4(RAND);
        byte [] f5Star = milenage.f5Star(RAND);

        assertArrayEquals(F1, f1);
        assertArrayEquals(F1_STAR, f1Star);
        assertArrayEquals(F2, f2);
        assertArrayEquals(F3, f3);
        assertArrayEquals(F4, f4);
        assertArrayEquals(F5, f5);
        assertArrayEquals(F5_STAR, f5Star);
    }

    @Test
    public void etsiTs135207AllTest() throws InterruptedException, ExecutionException {
        byte [] opc = Milenage.calculateOPc(OP, key, factory);
        CConstants<BigIntegerBuffer> cConstants = new CConstants<>(factory);
        RConstants rConstants = new RConstants();

        Milenage<BigIntegerBuffer> milenage = new Milenage<>(opc, key, cConstants, rConstants, factory);

        Map<MilenageResult, byte []> result = milenage.calculateAll(RAND, SQN, AMF, Executors.newCachedThreadPool());

        assertArrayEquals(F1, result.get(MilenageResult.MAC_A));
        assertArrayEquals(F1_STAR, result.get(MilenageResult.MAC_S));
        assertArrayEquals(F2, result.get(MilenageResult.RES));
        assertArrayEquals(F3, result.get(MilenageResult.CK));
        assertArrayEquals(F4, result.get(MilenageResult.IK));
        assertArrayEquals(F5, result.get(MilenageResult.AK));
        assertArrayEquals(F5_STAR, result.get(MilenageResult.AK_R));
    }

    @Test
    public void etsiTs135207CustomTest() {
        byte [] opc = Milenage.calculateOPc(OP, key, factory);
        RConstants rConstants = new RConstants(R1_C, R2_C, R3_C, R4_C, R5_C);

        Milenage<BigIntegerBuffer> milenage = new Milenage<>(
                opc, key, C1_C, C2_C, C3_C, C4_C, C5_C, rConstants, factory);

        Map<MilenageResult, byte []> f1All = milenage.f1All(RAND, SQN, AMF);
        byte [] f1 = f1All.get(MilenageResult.MAC_A);
        byte [] f1Star = f1All.get(MilenageResult.MAC_S);

        Map<MilenageResult, byte []> f2f5 = milenage.f2f5(RAND);
        byte [] f2 = f2f5.get(MilenageResult.RES);
        byte [] f5 = f2f5.get(MilenageResult.AK);

        byte [] f3 = milenage.f3(RAND);
        byte [] f4 = milenage.f4(RAND);
        byte [] f5Star = milenage.f5Star(RAND);

        assertArrayEquals(F1_C, f1);
        assertArrayEquals(F1_STAR_C, f1Star);
        assertArrayEquals(F2_C, f2);
        assertArrayEquals(F3_C, f3);
        assertArrayEquals(F4_C, f4);
        assertArrayEquals(F5_C, f5);
        assertArrayEquals(F5_STAR_C, f5Star);
    }

    @Test
    public void etsiTs135207CustomAllTest() throws InterruptedException, ExecutionException {
        byte [] opc = Milenage.calculateOPc(OP, key, factory);
        RConstants rConstants = new RConstants(R1_C, R2_C, R3_C, R4_C, R5_C);

        Milenage<BigIntegerBuffer> milenage = new Milenage<>(
                opc, key, C1_C, C2_C, C3_C, C4_C, C5_C, rConstants, factory);

        Map<MilenageResult, byte []> result = milenage.calculateAll(RAND, SQN, AMF, Executors.newCachedThreadPool());

        assertArrayEquals(F1_C, result.get(MilenageResult.MAC_A));
        assertArrayEquals(F1_STAR_C, result.get(MilenageResult.MAC_S));
        assertArrayEquals(F2_C, result.get(MilenageResult.RES));
        assertArrayEquals(F3_C, result.get(MilenageResult.CK));
        assertArrayEquals(F4_C,result.get(MilenageResult.IK));
        assertArrayEquals(F5_C, result.get(MilenageResult.AK));
        assertArrayEquals(F5_STAR_C, result.get(MilenageResult.AK_R));
    }
}
