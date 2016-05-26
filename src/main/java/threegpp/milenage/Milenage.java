/**
 * Copyright (c) 2016 Constantin Roganov
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package threegpp.milenage;

import javax.crypto.Cipher;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * <h1>Milenage</h1>
 * Milenage algorithm
 * <p>
 * Implements all Milenage functions according 3GPP TS 35.206
 * <p>
 * @author Constantin Roganov {@literal <rccbox @ gmail . com>}
 * @version 1.0.0
 * @since 07.02.16
 */
@SuppressWarnings("unchecked")
public class Milenage<B extends MilenageBuffer> {

    public static final short BLOCK_LEN_BYTES = 16;
    public static final short HALF_BUFFER_BYTES = 8;
    public static final short AK_LENGTH_BYTES = 6;
    public static final short BLOCK_LEN_BITS = BLOCK_LEN_BYTES * 8;
    public static final short SQN_LEN_BYTES = 6;
    public static final short AK_LEN_BYTES = SQN_LEN_BYTES;
    public static final short AMF_LEN_BYTES = 2;

    private final B opc;
    private final Cipher key;
    private final CConstants<B> c;
    private final RConstants r;
    private final MilenageBufferFactory<B> factory;

    /**
     * Creates OPc from OP and K
     * <p>
     * OPC = OP ⊕ E[OP]K
     * <p>
     * @param op  Operator Variant Algorithm Configuration Field (128 bit)
     * @param k  {@link javax.crypto.Cipher} object representing the Subscriber Key
     * @param <B>  The type used by {@link MilenageBufferFactory} implementation
     * @return  Array of bytes representing calculated OPc value
     */
    public static <B extends MilenageBuffer> byte [] calculateOPc(
            final byte [] op, final Cipher k, MilenageBufferFactory<B> factory) {
        validateCipherOrThrowException(k);

        B opBuffer = factory.create(op);
        return opBuffer.encrypt(k).xor(opBuffer).toBytes();
    }

    /**
     * Constructor
     *
     * @param OPc  Byte array containing previously calculated OPc value (OPc = OP⊕E[OP]K)
     * @param k  {@link javax.crypto.Cipher} representing the encryption key (K)
     * @param cConstants  Block of C constants (C1...C5)
     * @param rConstants  Block of R constants (R1...R5)
     * @param bufferFactory  {@link MilenageBufferFactory} object for creating {@link MilenageBuffer} instances.
     */
    public Milenage(byte [] OPc, Cipher k,
                    CConstants<B> cConstants,
                    RConstants rConstants,
                    MilenageBufferFactory<B> bufferFactory) {
        validateCipherOrThrowException(k);

        opc = bufferFactory.create(OPc);
        key = k;
        c = cConstants;
        r = rConstants;
        factory = bufferFactory;
    }

    /**
     * Constructor
     *
     * @param OPc  Byte array containing previously calculated OPc value (OPc = OP⊕E[OP]K)
     * @param k  {@link javax.crypto.Cipher} representing the encryption key (K)
     * @param c1  C1 constant represented as an array of bytes
     * @param c2  C2 constant represented as an array of bytes
     * @param c3  C3 constant represented as an array of bytes
     * @param c4  C4 constant represented as an array of bytes
     * @param c5  C5 constant represented as an array of bytes
     * @param rConstants  Block of R constants (R1...R5)
     * @param bufferFactory  {@link MilenageBufferFactory} object for creating {@link MilenageBuffer} instances.
     */
    public Milenage(byte [] OPc, Cipher k,
                    byte [] c1, byte [] c2, byte [] c3, byte [] c4, byte [] c5,
                    RConstants rConstants,
                    MilenageBufferFactory<B> bufferFactory) {
        this(OPc, k, new CConstants<>(c1, c2, c3, c4, c5, bufferFactory), rConstants, bufferFactory);
    }

    /**
     * Constructor creating Milenage with sample constants given in 3GPP TS 35.206
     *
     * @param OPc  Byte array containing previously calculated OPc value (OPc = OP⊕E[OP]K)
     * @param k  {@link javax.crypto.Cipher} representing the encryption key (K)
     * @param bufferFactory  {@link MilenageBufferFactory} object for creating {@link MilenageBuffer} instances.
     */
    public Milenage(byte [] OPc, Cipher k, MilenageBufferFactory<B> bufferFactory) {
        this(OPc, k, new CConstants<>(bufferFactory), new RConstants(), bufferFactory);
    }

    /**
     * Calculates result of Milenage functions f1 and f1*
     * <p>
     * Output of f1 = MAC-A, where MAC-A[0] .. MAC-A[63] = OUT1[0] .. OUT1[63]
     * <p>
     * Output of f1* = MAC-S, where MAC-S[0] .. MAC-S[63] = OUT1[64] .. OUT1[127]
     *
     * @param rand  Random value generated by network (RAND)
     * @param sqn  A 48-bit SQN value
     * @param amf  A 16-bit AMF value
     * @return  Map containing results of f1 and f1* with keys {@link MilenageResult#MAC_A} and
     *          {@link MilenageResult#MAC_S} accordingly.
     */
    public Map<MilenageResult, byte []> f1All(byte [] rand, byte [] sqn, byte [] amf) {
        B randBuffer = factory.create(rand);
        B in1Buffer = factory.create(sqn, amf);

        return makeF1Result(out1(temp(randBuffer), in1Buffer));
    }

    /**
     * Calculates result of Milenage functions f2 and f5
     * <p>
     * Output of f2 = RES, where RES[0] .. RES[63] = OUT2[64] .. OUT2[127]
     * <p>
     * Output of f5 = AK, where AK[0] .. AK[47] = OUT2[0] .. OUT2[47]
     *
     * @param rand  Random value generated by the network (RAND)
     * @return  Map containing results of f2 and f5 with keys {@link MilenageResult#RES} and
     *          {@link MilenageResult#AK} accordingly.
     */
    public Map<MilenageResult, byte []> f2f5(byte [] rand) {
        B randBuffer = factory.create(rand);

        return makeF2F5Result(outX(temp(randBuffer), 1));
    }

    /**
     * Calculates result of Milenage function f3
     * <p>
     * Output of f3 = CK, where CK[0] .. CK[127] = OUT3[0] .. OUT3[127]
     *
     * @param rand  Random value generated by the network (RAND)
     * @return  Buffer containing CK value
     */
    public byte[] f3(byte [] rand) {
        return outX(temp(factory.create(rand)), 2).toBytes();
    }

    /**
     * Calculates result of Milenage function f4
     * <p>
     * Output of f4 = IK, where IK[0] .. IK[127] = OUT4[0] .. OUT4[127]
     *
     * @param rand  Random value generated by the network (RAND)
     * @return  Buffer containing IK value
     */
    public byte [] f4(byte [] rand) {
        return outX(temp(factory.create(rand)), 3).toBytes();
    }

    /**
     * Calculates result of Milenage function f5*
     * <p>
     * Output of f5* = AK, where AK[0] .. AK[47] = OUT5[0] .. OUT5[47]
     *
     * @param rand  Random value generated by the network (RAND)
     * @return  Buffer containing resynch AK value
     */
    public byte [] f5Star(byte [] rand) {
        return makeF5StarResult(outX(temp(factory.create(rand)), 4));
    }

    /**
     * Calculates all the Milenage function values.
     * <p>
     * Calculation can be performed synchronously or asynchronously.
     *
     * @param rand  Random value generated by the network (RAND)
     * @param sqn  A 48-bit SQN value
     * @param amf  A 16-bit AMF value
     * @param executor  {@link ExecutorService} object to be used for calculations.
     * @return  Map containing results of f1, f1*, f2, f3, f4, f5, f5* marked with appropriate
     *          key from {@link MilenageResult} enum.
     * @throws InterruptedException  can be thrown by the {@link ExecutorService}
     * @throws ExecutionException  can be thrown by the {@link ExecutorService}
     */
    public Map<MilenageResult, byte []> calculateAll(byte [] rand, byte [] sqn, byte [] amf, ExecutorService executor)
                                throws InterruptedException, ExecutionException {
        B tmp = temp(factory.create(rand));

        List<Callable<B>> routines = new ArrayList<>();
        routines.add(getOUT1Callable(tmp, factory.create(sqn, amf)));

        int [] indexes = new int[] {1, 2, 3, 4};
        for(int i: indexes) {
            routines.add(getOUTXCallable(tmp, i));
        }
        List<Future<B>> futures =  executor.invokeAll(routines);
        Map<MilenageResult, byte []> result = new HashMap<>(Constants.CONST_NUM);

        result.putAll(makeF1Result(futures.get(0).get()));
        result.putAll(makeF2F5Result(futures.get(1).get()));
        result.put(MilenageResult.CK, futures.get(2).get().toBytes());
        result.put(MilenageResult.IK, futures.get(3).get().toBytes());
        result.put(MilenageResult.AK_R, makeF5StarResult(futures.get(4).get()));

        return result;
    }

    /**
     * Calculates intermediate parameter TEMP
     * <p>
     * TEMP = E[RAND ⊕ OPC]K
     * <p>
     * @param rand  Network generated random value (RAND)
     * @return  A buffer containing TEMP value
     */
    private B temp(B rand) {
        return (B)rand.xor(opc).encrypt(key);
    }

    /**
     * Calculates the intermediate Milenage parameter OUT1
     * <p>
     * OUT1 = E[TEMP ⊕ rot(IN1 ⊕ OPC, r1) ⊕ c1]K ⊕ OPC
     * <p>
     * @param tmp  Intermediate parameter {@link Milenage#temp(MilenageBuffer) TEMP}
     * @param in1   A 128-bit value IN1 is constructed as follows:
     *                  IN1[0] .. IN1[47] = SQN[0] .. SQN[47]
     *                  IN1[48] .. IN1[63] = AMF[0] .. AMF[15]
     *                  IN1[64] .. IN1[111] = SQN[0] .. SQN[47]
     *                  IN1[112] .. IN1[127] = AMF[0] .. AMF[15]
     * @return  Buffer containing calculated OUT1 value
     */
    private B out1(B tmp, B in1) {
        return (B)in1
                .xor(opc)
                .leftCircularBitRotation(r.get(0))
                .xor(tmp)
                .xor(c.get(0))
                .encrypt(key)
                .xor(opc);
    }

    /**
     * Calculates the intermediate Milenage parameter OUTx (where x[2...5])
     *
     * @param tmp  Intermediate Milenage parameter
     * @param constIndex  Zero based index of appropriate R and C constant values.
     * @return  A buffer containing appropriate OUT value
     */
    private B outX(B tmp, int constIndex) {
        return (B)tmp
                .xor(opc)
                .leftCircularBitRotation(r.get(constIndex))
                .xor(c.get(constIndex))
                .encrypt(key)
                .xor(opc);
    }

    private Callable<B> getOUT1Callable(final B tmp, final B in1Val) {
        return new Callable<B>() {
            public B call() throws Exception {
                return out1(tmp, in1Val);
            }
        };
    }

    private Callable<B> getOUTXCallable(final B tmp, final int constIndex) {
        return new Callable<B>() {
            public B call() throws Exception {
                return outX(tmp, constIndex);
            }
        };
    }

    private Map<MilenageResult, byte []> makeF1Result(B buf) {
        byte [][] bytes = buf.takeBytes(
                0, HALF_BUFFER_BYTES,
                HALF_BUFFER_BYTES, BLOCK_LEN_BYTES
        );

        Map<MilenageResult, byte []> result = new HashMap<>();

        result.put(MilenageResult.MAC_A, bytes[0]);
        result.put(MilenageResult.MAC_S, bytes[1]);

        return result;
    }

    private Map<MilenageResult, byte []> makeF2F5Result(B buf) {
        byte [][] out2 = buf.takeBytes(
                0, AK_LENGTH_BYTES,
                HALF_BUFFER_BYTES, BLOCK_LEN_BYTES
        );
        Map<MilenageResult, byte []> result = new HashMap<>(2);

        result.put(MilenageResult.AK, out2[0]);
        result.put(MilenageResult.RES, out2[1]);

        return result;
    }

    private byte [] makeF5StarResult(B buf) {
        return buf.takeBytes(0, AK_LEN_BYTES)[0];
    }

    /**
     * Validates {@link Cipher} object.
     * If checked object is not suitable for Milenage algorithm calculations throws exception.
     * @param k  {@link Cipher} object to check.
     * @throws  IllegalArgumentException In case Cipher is invalid.
     */
    private static void validateCipherOrThrowException(final Cipher k) {
        if(k.getOutputSize(BLOCK_LEN_BYTES) != BLOCK_LEN_BYTES)
            throw new IllegalArgumentException("Cipher supplied is not suitable for Milenage");
    }
}
