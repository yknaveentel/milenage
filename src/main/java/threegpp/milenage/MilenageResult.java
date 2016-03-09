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
 * <h1>MilenageResult</h1>
 * <p>
 * Types of result returned by Milenage functions f1, f1*, f2, f3, f4, f5, f5*
 * <ul>
 * <li>{@link #MAC_A}
 * <li>{@link #MAC_S}
 * <li>{@link #RES}
 * <li>{@link #CK}
 * <li>{@link #IK}
 * <li>{@link #AK}
 * <li>{@link #AK_R}
 * </ul><p>
 *
 * @author Constantin Roganov {@literal <rccbox @ gmail . com>}
 * @version 1.0.0
 * @since 07.02.16
 */
public enum MilenageResult {
    /**
     * Network authentication code
     */
    MAC_A,

    /**
     * Resynch authentication code
     */
    MAC_S,

    /**
     * Response
     */
    RES,

    /**
     * Confidentiality key
     */
    CK,

    /**
     * Integrity key
     */
    IK,

    /**
     * Anonymity key (f5)
     */
    AK,

    /**
     * Resynch anonymity key (f5*)
     */
    AK_R
}
