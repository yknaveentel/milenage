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
package threegpp.milenage.biginteger;

import threegpp.milenage.Milenage;
import threegpp.milenage.MilenageBuffer;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * <h1>MilenageBuffer implementation based on a {@link BigInteger} class</h1>
 * Data buffer to use in calculations of algorithm output blocks.
 * <p>
 * The size of buffer assumed as {@link Milenage#BLOCK_LEN_BYTES}
 * <p>
 * Incapsulates bit/cryptography operations.
 *
 * @author Constantin Roganov {@literal <rccbox @ gmail . com>}
 * @version 1.0.0
 * @since 06.02.16
 */
public class BigIntegerBuffer implements MilenageBuffer<BigInteger> {

    private static final BigInteger ALL_ONES = BigIntegerHelper.getAllOnes();
    private static final int SQN_AMF_LENGTH = Milenage.AMF_LEN_BYTES + Milenage.SQN_LEN_BYTES;
    private static final int HEX_BUFFER_LENGTH = Milenage.BLOCK_LEN_BYTES * 2;

    private final BigInteger buffer;

    public BigIntegerBuffer(byte [] bytes) {
        this(hexlify(bytes));
    }

    public BigIntegerBuffer(BigInteger val) {
        buffer = BigIntegerHelper.ensureUnsigned(val);
    }

    public BigIntegerBuffer(String hexVal) {
        if(hexVal.length() != HEX_BUFFER_LENGTH) {
            throw new IllegalArgumentException("Hex value have to represent "
                                                + Milenage.BLOCK_LEN_BYTES + "bytes");
        }
        buffer = BigIntegerHelper.unhexlify(hexVal);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MilenageBuffer<BigInteger> xor(MilenageBuffer<BigInteger> that) {
        return new BigIntegerBuffer(buffer.xor(that.getRawBuffer()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MilenageBuffer<BigInteger> leftCircularBitRotation(byte numBits)
                                    throws IllegalArgumentException {
        if(numBits < 0) {
            throw new IllegalArgumentException("leftCircularBitRotation(): numBits can not be negative");
        }
        if(numBits == 0) {
            return new BigIntegerBuffer(buffer);
        }
        int reversedShift = Milenage.BLOCK_LEN_BITS - numBits;
        BigInteger lShifted = buffer.shiftLeft(numBits);
        BigInteger rShifted = buffer.shiftRight(reversedShift);
        BigInteger cleanResult = lShifted.or(rShifted).and(ALL_ONES);

        return new BigIntegerBuffer(cleanResult);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MilenageBuffer<BigInteger> encrypt(Cipher cipher) {
        try {
            return new BigIntegerBuffer(cipher.doFinal(toBytes()));

        } catch (IllegalBlockSizeException|BadPaddingException e) {
            // that can't be a case
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte [][] takeBytes(int... args)
                            throws IndexOutOfBoundsException {
        if(args.length % 2 != 0) {
            throw new IllegalArgumentException("takeBytes() expects an even number of arguments");
        }
        byte [] buf = toBytes();
        byte [][] result = new byte [args.length / 2][];

        for(int i = 0, j = 0; i < args.length; j++) {
            int beg = args[i++];
            int end = args[i++];

            result[j] = Arrays.copyOfRange(buf, beg, end);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] toBytes() {
        byte [] result = buffer.toByteArray();

        int delta = Milenage.BLOCK_LEN_BYTES - result.length;
        if(delta < 0) {
            // BigInteger.toByteArray adds extra zero byte to result so here we'll remove it.
            return Arrays.copyOfRange(result, 1, Milenage.BLOCK_LEN_BYTES + 1);

        } else if(delta > 0) {
            return arrayPadLeft(result, Milenage.BLOCK_LEN_BYTES);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigInteger getRawBuffer() {
        return buffer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return hexlify(toBytes());
    }

    /**
     * Convert byte array to a hex string.
     *
     * @param bytes  Array containing bytes to be converted.
     * @return  Result hex string
     */
    public static String hexlify(byte [] bytes) {
        StringBuilder hexVal = new StringBuilder(bytes.length * 2);

        for(byte b: bytes) {
            hexVal.append(String.format("%02X", b));
        }
        return hexVal.toString();
    }

    /**
     * Pad an array with zeroes at the head up to {@code newLength} bytes.
     *
     * @param src  Initial buffer with useful content.
     * @param newLength  New length of buffer, must be greater than the old one.
     * @return  New array with zero bytes at the head content from {@code src} in the rest.
     */
    private static byte [] arrayPadLeft(byte [] src, int newLength) {
        byte [] result = new byte [newLength];

        System.arraycopy(src, 0, result, result.length - src.length, src.length);

        return result;
    }

    /**
     * Create buffer IN1 based on contents of SQN and AMF fields.
     *
     * A 128-bit value IN1 is constructed as follows:
     * IN1[0] .. IN1[47] = SQN[0] .. SQN[47]
     * IN1[48] .. IN1[63] = AMF[0] .. AMF[15]
     * IN1[64] .. IN1[111] = SQN[0] .. SQN[47]
     * IN1[112] .. IN1[127] = AMF[0] .. AMF[15]
     * <p>
     * @param sqn  Array of length {@link Milenage#SQN_LEN_BYTES}.
     * @param amf  Array of length {@link Milenage#AMF_LEN_BYTES}.
     * @return  Result buffer with N1 contents.
     */
    public static BigIntegerBuffer createIN1(byte [] sqn, byte [] amf) {

        String errorMessage = null;
        if(sqn.length != Milenage.SQN_LEN_BYTES) {
            errorMessage = "Invalid length of SQN buffer";
        } else if(amf.length != Milenage.AMF_LEN_BYTES) {
            errorMessage = "Invalid length of AMF buffer";
        }
        if(null != errorMessage) {
            throw new IllegalArgumentException(errorMessage);
        }
        byte [] n1 = Arrays.copyOf(sqn, Milenage.BLOCK_LEN_BYTES);

        System.arraycopy(amf, 0, n1, Milenage.SQN_LEN_BYTES, Milenage.AMF_LEN_BYTES);
        System.arraycopy(n1, 0, n1, SQN_AMF_LENGTH, SQN_AMF_LENGTH);

        return new BigIntegerBuffer(n1);
    }
}
