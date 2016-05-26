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

import java.util.Collections;

/**
 * <h1>CConstants</h1>
 *
 * @author Constantin Roganov {@literal <rccbox @ gmail . com>}
 * @version 1.0.0
 * @since 11.02.16
 */
public class CConstants<T extends MilenageBuffer> extends Constants<T>{

    /**
     * Constructor.
     *
     * @param c  Array of values of C constants (C1...C5)
     */
    @SafeVarargs
    public CConstants(T... c) {
        super(c[0], c[1], c[2], c[3], c[4]);
    }

    public CConstants(byte [] c1, byte [] c2, byte [] c3, byte [] c4, byte [] c5,
                      MilenageBufferFactory<T> factory) {
        super(factory.create(c1),
                factory.create(c2),
                factory.create(c3),
                factory.create(c4),
                factory.create(c5));
    }

    /**
     * Create object containing sample (AKA default) Ci constant values according to
     * 3GPP TS 35.206 (4.1)
     *
     * @param factory  {@link MilenageBufferFactory} object.
     */
    public CConstants(MilenageBufferFactory<T> factory) {

        T [] constants = factory.createSampleCConstants();
        Collections.addAll(list, constants);
    }
}
