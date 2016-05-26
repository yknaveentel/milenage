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

import java.util.Arrays;

/**
 * <h1>RConstants</h1>
 *
 *
 * @author Constantin Roganov {@literal <rccbox @ gmail . com>}
 * @version 1.0.0
 * @since 07.02.16
 */
public class RConstants extends Constants<Byte> {
    private static final byte LOWER_BOUND = 0;
    private static final byte UPPER_BOUND = 127;

    public static final byte R1_SAMPLE =64;
    public static final byte R2_SAMPLE =0;
    public static final byte R3_SAMPLE =32;
    public static final byte R4_SAMPLE =64;
    public static final byte R5_SAMPLE =96;

    /**
     * Constructs object from given R-constant values
     *
     * @param r1  Value of constant #
     * @param r2  Value of constant #
     * @param r3  Value of constant #
     * @param r4  Value of constant #
     * @param r5  Value of constant #
     * @throws IllegalArgumentException  If passed values are not between<p>
     *         {@link RConstants#LOWER_BOUND} and {@link RConstants#UPPER_BOUND} (inclusive).
     */
    public RConstants(byte r1, byte r2, byte r3, byte r4, byte r5)
                throws IllegalArgumentException {
        super(r1, r2, r3, r4, r5);
    }

    /**
     * Default constructor
     *
     * Creates object from sample values given in 3GPP TS 35.206
     */
    public RConstants() {
        this(R1_SAMPLE, R2_SAMPLE, R3_SAMPLE, R4_SAMPLE, R5_SAMPLE);
    }

    @Override
    protected void validateArgs(Byte r1, Byte r2, Byte r3, Byte r4, Byte r5)
            throws IllegalArgumentException {
        super.validateArgs(r1, r2, r3, r4, r5);

        for(byte r: Arrays.asList(r1, r2, r3, r4, r5)) {
            if(!isConstantValid(r)) {
                throw new IllegalArgumentException(
                        "R constant value must be between"
                                + LOWER_BOUND
                                + " and "
                                + UPPER_BOUND
                                + " (inclusive)"
                );
            }
        }
    }

    private static boolean isConstantValid(byte v) {
        return (v >= LOWER_BOUND && v <= UPPER_BOUND);
    }
}
