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

/**
 * <h1>MilenageBufferFactory</h1>
 * <p>
 * An interface to build {@link threegpp.milenage.MilenageBuffer} objects.
 *
 * @author Constantin Roganov {@literal <rccbox @ gmail . com>}
 * @version 1.0.0
 * @since 21.02.16
 */
public interface MilenageBufferFactory<B extends MilenageBuffer> {

    /**
     * Create {@link threegpp.milenage.MilenageBuffer} object from the array of bytes.
     *
     * @param data  Array of bytes with length equal to {@link Milenage#BLOCK_LEN_BYTES}.
     * @return  {@link threegpp.milenage.MilenageBuffer} object.
     */
    B create(byte [] data);

    /**
     * Create {@link threegpp.milenage.MilenageBuffer} object from a hex string.
     *
     * @param hexString  A hexadecimal string representation of a buffer with length {@link Milenage#BLOCK_LEN_BYTES} * 2.
     * @return  {@link threegpp.milenage.MilenageBuffer} object.
     */
    B create(String hexString);

    /**
     * Create {@link threegpp.milenage.MilenageBuffer} object from SQN and AMF values.
     *
     * @param sqn  Array of bytes with length equal to {@link Milenage#SQN_LEN_BYTES}.
     * @param amf  Array of bytes with length equal to {@link Milenage#AMF_LEN_BYTES}.
     * @return  {@link threegpp.milenage.MilenageBuffer} object.
     */
    B create(byte [] sqn, byte [] amf);

    /**
     * Create an array of {@link Constants#CONST_NUM} {@link threegpp.milenage.MilenageBuffer} objects
     * containing example C-constants values according to 3GPP 35.206 (chapter 4.1).
     * <p>
     *
     * @return  An array of {@link threegpp.milenage.MilenageBuffer} object.
     */
    B[] createSampleCConstants();

    /**
     * Create an array of {@link threegpp.milenage.MilenageBuffer} objects.
     *
     * @param buffers  Array of byte arrays where each element represents a contents of single buffer.
     * @return  Array of {@link MilenageBuffer} objects
     */
    B[] createArray(byte []... buffers);

    /**
     * Create an array of {@link threegpp.milenage.MilenageBuffer} objects.
     *
     * @param buffers  Array of hex strings where each element represents a contents of single buffer.
     * @return  Array of {@link MilenageBuffer} objects
     */
    B[] createArray(String... buffers);

}
