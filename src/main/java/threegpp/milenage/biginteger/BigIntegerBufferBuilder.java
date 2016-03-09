package threegpp.milenage.biginteger;

import threegpp.milenage.MilenageBuffer;
import threegpp.milenage.MilenageBufferBuilder;

import java.math.BigInteger;

/**
 * <h1>BigIntegerBufferBuilder</h1>
 * <p>
 *
 * @author Constantin Roganov {@literal <rccbox @ gmail . com>}
 * @version 1.0.0
 * @since 07.03.16
 */
public class BigIntegerBufferBuilder implements MilenageBufferBuilder<BigInteger> {
    private static BigIntegerBufferBuilder ourInstance = new BigIntegerBufferBuilder();

    public static BigIntegerBufferBuilder getInstance() {
        return ourInstance;
    }

    private BigIntegerBufferBuilder() {}

    @Override
    public MilenageBuffer<BigInteger> create(byte[] data) {
        return new BigIntegerBuffer(data);
    }

    @Override
    public MilenageBuffer<BigInteger> create(String hexString) {
        return new BigIntegerBuffer(hexString);
    }

    @Override
    public MilenageBuffer<BigInteger> create(BigInteger value) {
        return new BigIntegerBuffer(value);
    }

    @Override
    public MilenageBuffer<BigInteger>[] createSampleCConstants() {
        return new BigIntegerBuffer [] {
            new BigIntegerBuffer(BigInteger.ZERO),
            new BigIntegerBuffer(BigInteger.valueOf(1)),
            new BigIntegerBuffer(BigInteger.valueOf(2)),
            new BigIntegerBuffer(BigInteger.valueOf(4)),
            new BigIntegerBuffer(BigInteger.valueOf(8))
        };
    }

    @Override
    public MilenageBuffer<BigInteger> create(byte[] sqn, byte[] amf) {
        return BigIntegerBuffer.createIN1(sqn, amf);
    }

    @Override
    @SafeVarargs
    public final MilenageBuffer<BigInteger>[] createArray(MilenageBuffer<BigInteger>... buffers) {
        return buffers;
    }
}
