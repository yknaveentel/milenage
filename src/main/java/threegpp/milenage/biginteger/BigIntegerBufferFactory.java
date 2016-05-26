package threegpp.milenage.biginteger;

import threegpp.milenage.MilenageBufferFactory;

import java.math.BigInteger;

/**
 * <h1>BigIntegerBufferFactory</h1>
 * <p>
 *
 * @author Constantin Roganov {@literal <rccbox @ gmail . com>}
 * @version 1.0.0
 * @since 07.03.16
 */
public class BigIntegerBufferFactory implements MilenageBufferFactory<BigIntegerBuffer> {
    private static BigIntegerBufferFactory ourInstance = new BigIntegerBufferFactory();

    public static BigIntegerBufferFactory getInstance() {
        return ourInstance;
    }

    private BigIntegerBufferFactory() {}

    @Override
    public BigIntegerBuffer create(byte[] data) {
        return new BigIntegerBuffer(data);
    }

    @Override
    public BigIntegerBuffer create(String hexString) {
        return new BigIntegerBuffer(hexString);
    }

    @Override
    public BigIntegerBuffer [] createSampleCConstants() {
        return new BigIntegerBuffer [] {
            new BigIntegerBuffer(BigInteger.ZERO),
            new BigIntegerBuffer(BigInteger.valueOf(1)),
            new BigIntegerBuffer(BigInteger.valueOf(2)),
            new BigIntegerBuffer(BigInteger.valueOf(4)),
            new BigIntegerBuffer(BigInteger.valueOf(8))
        };
    }

    @Override
    public BigIntegerBuffer create(byte[] sqn, byte[] amf) {
        return BigIntegerBuffer.createIN1(sqn, amf);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigIntegerBuffer[] createArray(byte[]... buffers) {
        BigIntegerBuffer [] array = new BigIntegerBuffer[buffers.length];

        for(int i = 0; i < buffers.length; i++) {
            array[i] = create(buffers[i]);
        }
        return array;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigIntegerBuffer[] createArray(String... buffers) {
        BigIntegerBuffer [] array = new BigIntegerBuffer[buffers.length];

        for(int i = 0; i < buffers.length; i++) {
            array[i] = create(buffers[i]);
        }
        return array;
    }
}
