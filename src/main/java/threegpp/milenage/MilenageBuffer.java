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

/**
 * <h1>MilenageBuffer</h1>
 *
 *
 * @author Constantin Roganov {@literal <rccbox @ gmail . com>}
 * @version 1.0.0
 * @since 06.02.16
 */
public interface MilenageBuffer<T> {

    /**
     * Performs bit exclusive OR (XOR) operation.
     *
     * @param that  Another {@link threegpp.milenage.MilenageBuffer} object to XOR with this
     * @return  New result {@link threegpp.milenage.MilenageBuffer} object
     */
    MilenageBuffer<T> xor(final MilenageBuffer<T> that);

    /**
     * Performs a circular bit shift to the left.
     *
     * @param numBits  Number of bits to be shifted to the left.
     * @return  New result {@link threegpp.milenage.MilenageBuffer} object.
     */
    MilenageBuffer<T> leftCircularBitRotation(final byte numBits);

    /**
     * Encrypts this buffer with given {@link javax.crypto.Cipher} object
     *
     * @param cipher {@link javax.crypto.Cipher} object
     * @return New result {@link threegpp.milenage.MilenageBuffer} object
     */
    MilenageBuffer<T> encrypt(Cipher cipher);

    /**
     * Returns contents of underlying buffer as an array of bytes
     * @return The byte array representation of this
     */
    byte [] toBytes();

    /**
     * Takes number of bytes beginning from given offset.
     *
     * @param  args  An array of even integers where each pair
     *               consists from start and end position in bytes inside buffer.
     * @return  An array of byte arrays where each element contains appropriate fragment of
     *          source buffer.
     * @throws  IndexOutOfBoundsException if {@code offset} or {@code numBytes}
     *          point outside of this buffer.
     */
     byte [][] takeBytes(int... args)
            throws IndexOutOfBoundsException;

    /**
     * @return  Internal buffer object (probably its copy)
     */
    T getRawBuffer();
}
