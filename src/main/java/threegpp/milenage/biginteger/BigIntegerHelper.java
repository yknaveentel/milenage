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

import java.math.BigInteger;

/**
 * <h1>BigIntegerHelper</h1>
 * <p>
 * A set of static functions to make use of the BigInteger class more comfortable.
 *
 * @author Constantin Roganov {@literal <rccbox @ gmail . com>}
 * @version 1.0.0
 * @since 15.02.16
 */
public class BigIntegerHelper {
    static public BigInteger unhexlify(String hexString) {
        return new BigInteger("00" + hexString, 16);
    }

    static public boolean isPositive(BigInteger i) {
        return i.signum() > 0;
    }

    static public boolean isNegative(BigInteger i) {
        return i.signum() < 0;
    }

    static public boolean isZero(BigInteger i) {
        return i.signum() == 0;
    }

    static public BigInteger getAllOnes() {
        return new BigInteger(
                new String(new char[Milenage.BLOCK_LEN_BYTES * 2]).replace('\0', 'F'),
                16
        );
    }

    static public BigInteger ensureUnsigned(BigInteger that, int bitLength) {

        if(isNegative(that)) {
            if(bitLength <= 0) {
                throw new IllegalArgumentException("ensureUnsigned(): bitLength should be positive");
            }
            return BigInteger.ONE
                    .shiftLeft(bitLength)
                    .add(that);
        }
        return that;
    }

    static public BigInteger ensureUnsigned(BigInteger that) {
        return ensureUnsigned(that, Milenage.BLOCK_LEN_BYTES);
    }
}
