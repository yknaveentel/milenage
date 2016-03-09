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
import threegpp.milenage.biginteger.BigIntegerBufferBuilder;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static threegpp.milenage.MilenageTestData.*;
import static threegpp.milenage.biginteger.BigIntegerBuffer.hexlify;

/**
 * <h1>MilenageTest</h1>
 * <p/>
 *
 * @author Constantin Roganov {@literal <rccbox @ gmail . com>}
 * @version 1.0.0
 * @since 05.03.16
 */
public class MilenageTest {

    private static MilenageBufferBuilder<BigInteger> builder = BigIntegerBufferBuilder.getInstance();
    private static Cipher key = builder.create(K).createCipher();

    @Test
    public void opcTest() {
        MilenageBuffer<BigInteger> op = builder.create(OP);
        MilenageBuffer<BigInteger> opc = Milenage.calculateOPc(op, key);

        assertEquals(opc.toString(), OPC);
    }

    @Test
    public void cipherTest() {
        MilenageBuffer<BigInteger> plain = builder.create(PLAIN);
        assertEquals(CIPHER, plain.encrypt(key).toString());
    }

    @Test
    public void etsiTs135207Test() {
        MilenageBuffer<BigInteger> op = builder.create(OP);
        MilenageBuffer<BigInteger> opc = Milenage.calculateOPc(op, key);
        MilenageBuffer<BigInteger> rand = builder.create(RAND);
        CConstants<BigInteger> cConstants = new CConstants<>(builder.createSampleCConstants());
        RConstants rConstants = new RConstants();

        Milenage<BigInteger> milenage = new Milenage<>(opc, key, cConstants, rConstants);

        Map<MilenageResult, byte []> f1All = milenage.f1All(rand, builder.create(IN1));
        String f1 = hexlify(f1All.get(MilenageResult.MAC_A));
        String f1Star = hexlify(f1All.get(MilenageResult.MAC_S));

        Map<MilenageResult, byte []> f2f5 = milenage.f2f5(rand);
        String f2 = hexlify(f2f5.get(MilenageResult.RES));
        String f5 = hexlify(f2f5.get(MilenageResult.AK));

        String f3 = hexlify(milenage.f3(rand));
        String f4 = hexlify(milenage.f4(rand));
        String f5Star = hexlify(milenage.f5Star(rand));

        assertEquals(F1, f1);
        assertEquals(F1_STAR, f1Star);
        assertEquals(F2, f2);
        assertEquals(F3, f3);
        assertEquals(F4, f4);
        assertEquals(F5, f5);
        assertEquals(F5_STAR, f5Star);
    }

    @Test
    public void etsiTs135207AllTest() throws InterruptedException, ExecutionException {
        MilenageBuffer<BigInteger> op = builder.create(OP);
        MilenageBuffer<BigInteger> opc = Milenage.calculateOPc(op, key);
        MilenageBuffer<BigInteger> rand = builder.create(RAND);
        CConstants<BigInteger> cConstants = new CConstants<>(builder.createSampleCConstants());
        RConstants rConstants = new RConstants();

        Milenage<BigInteger> milenage = new Milenage<>(opc, key, cConstants, rConstants);

        Map<MilenageResult, byte []> result = milenage.calculateAll(
                rand,
                builder.create(IN1),
                Executors.newCachedThreadPool());

        assertEquals(F1, hexlify(result.get(MilenageResult.MAC_A)));
        assertEquals(F1_STAR, hexlify(result.get(MilenageResult.MAC_S)));
        assertEquals(F2, hexlify(result.get(MilenageResult.RES)));
        assertEquals(F3, hexlify(result.get(MilenageResult.CK)));
        assertEquals(F4,hexlify(result.get(MilenageResult.IK)));
        assertEquals(F5, hexlify(result.get(MilenageResult.AK)));
        assertEquals(F5_STAR, hexlify(result.get(MilenageResult.AK_R)));
    }

    @Test
    public void etsiTs135207CustomTest() {
        MilenageBuffer<BigInteger> op = builder.create(OP);
        MilenageBuffer<BigInteger> opc = Milenage.calculateOPc(op, key);
        MilenageBuffer<BigInteger> rand = builder.create(RAND);
        @SuppressWarnings("unchecked")
        CConstants<BigInteger> cConstants = new CConstants<>(
                builder.createArray(
                    builder.create(C1_C),
                    builder.create(C2_C),
                    builder.create(C3_C),
                    builder.create(C4_C),
                    builder.create(C5_C)
                )
        );
        RConstants rConstants = new RConstants(R1_C, R2_C, R3_C, R4_C, R5_C);

        Milenage<BigInteger> milenage = new Milenage<>(opc, key, cConstants, rConstants);

        Map<MilenageResult, byte []> f1All = milenage.f1All(rand, builder.create(IN1));
        String f1 = hexlify(f1All.get(MilenageResult.MAC_A));
        String f1Star = hexlify(f1All.get(MilenageResult.MAC_S));

        Map<MilenageResult, byte []> f2f5 = milenage.f2f5(rand);
        String f2 = hexlify(f2f5.get(MilenageResult.RES));
        String f5 = hexlify(f2f5.get(MilenageResult.AK));

        String f3 = hexlify(milenage.f3(rand));
        String f4 = hexlify(milenage.f4(rand));
        String f5Star = hexlify(milenage.f5Star(rand));

        assertEquals(F1_C, f1);
        assertEquals(F1_STAR_C, f1Star);
        assertEquals(F2_C, f2);
        assertEquals(F3_C, f3);
        assertEquals(F4_C, f4);
        assertEquals(F5_C, f5);
        assertEquals(F5_STAR_C, f5Star);
    }

    @Test
    public void etsiTs135207CustomAllTest() throws InterruptedException, ExecutionException {
        MilenageBuffer<BigInteger> op = builder.create(OP);
        MilenageBuffer<BigInteger> opc = Milenage.calculateOPc(op, key);
        MilenageBuffer<BigInteger> rand = builder.create(RAND);
        @SuppressWarnings("unchecked")
        CConstants<BigInteger> cConstants = new CConstants<>(
                builder.createArray(
                        builder.create(C1_C),
                        builder.create(C2_C),
                        builder.create(C3_C),
                        builder.create(C4_C),
                        builder.create(C5_C)
                )
        );
        RConstants rConstants = new RConstants(R1_C, R2_C, R3_C, R4_C, R5_C);

        Milenage<BigInteger> milenage = new Milenage<>(opc, key, cConstants, rConstants);

        Map<MilenageResult, byte []> result = milenage.calculateAll(
                rand,
                builder.create(IN1),
                Executors.newCachedThreadPool());

        assertEquals(F1_C, hexlify(result.get(MilenageResult.MAC_A)));
        assertEquals(F1_STAR_C, hexlify(result.get(MilenageResult.MAC_S)));
        assertEquals(F2_C, hexlify(result.get(MilenageResult.RES)));
        assertEquals(F3_C, hexlify(result.get(MilenageResult.CK)));
        assertEquals(F4_C,hexlify(result.get(MilenageResult.IK)));
        assertEquals(F5_C, hexlify(result.get(MilenageResult.AK)));
        assertEquals(F5_STAR_C, hexlify(result.get(MilenageResult.AK_R)));
    }
}
