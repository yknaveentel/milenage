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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * <h1>Constants</h1>
 * <p>
 * Common implementation of class representing a list of 5 constants
 * <p>
 * @author Constantin Roganov {@literal <rccbox @ gmail . com>}
 * @version 1.0.0
 * @since 11.02.16
 */
public class Constants<T> {

    public static final short CONST_NUM = 5;
    protected final List<T> list;

    /**
     * Constructor
     *
     * @param const1  Constant #1.
     * @param const2  Constant #2.
     * @param const3  Constant #3.
     * @param const4  Constant #4.
     * @param const5  Constant #5.
     */
    public Constants(T const1, T const2, T const3, T const4, T const5)
        throws IllegalArgumentException {
        validateArgs(const1, const2, const3, const4, const5);
        list = Arrays.asList(const1, const2, const3, const4, const5);
    }

    protected Constants() {
        list = new ArrayList<>(CONST_NUM);
    }

    /**
     * Validates values of initializers according to domain specific rules.
     *
     * @param const1  Constant #1.
     * @param const2  Constant #2.
     * @param const3  Constant #3.
     * @param const4  Constant #4.
     * @param const5  Constant #5.
     */
    protected void validateArgs(T const1, T const2,
                                T const3, T const4, T const5)
            throws IllegalArgumentException {}

    /**
     * Accessor for a particular constant.
     *
     * @param n  A zero based constant number
     * @return  The value of constant having index {@code n}
     */
    public T get(int n) {
        return list.get(n);
    }

    public Iterator<T> iterator() {
        return list.iterator();
    }
}
