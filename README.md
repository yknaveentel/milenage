# Milenage

[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg?style=flat)](https://opensource.org/licenses/MIT) ![version: 1.0.0](https://img.shields.io/badge/version-1.0.0-green.svg?style=flat)

Java™ library designed to support [3GPP](http://www.3gpp.org)™ Milenage algorithm calculations according to **3GPP TS 35.206**.
Has written from scratch i.e. is not based on C sourse code from [3GPP](http://www.3gpp.org)™ TS 35.206 Annex 3.

## Features
 
 * <code>OP<sub>c</sub></code> calculation based on `OP` and `K`
 * All Authentication and Key Generation <code>f<sub>n</sub></code> functions including <code>f<sub>1</sub><sup>\*</sup></code> and <code>f<sub>5</sub><sup>\*</sup></code>
 * Algorithm customization with `R` and `C` constants 
 * Implementation customization:
 
    There is a `MilenageBuffer` interface supporting all operations Milenage uses under the hood, like bit shift, XOR etc.
    You can provide your own implementation, but library offers default implementation based on `BigInteger` class.
    For cryptography purposes library uses `Cipher` object from `javax.crypto` package so you can use any ciphering algorithm having the buffer size 128 bits as well as "default" [Rijndael](https://en.wikipedia.org/wiki/Advanced_Encryption_Standard?oldformat=true).
 
 * You can choose method of invocation of Milenage functions:
 
    * call each function separetely
    * call a full set of functions at once, in this case a call of every function _can_ be asynchronous.
        
        It's configurable through `ExecutorService` object.
        
## Requirements

Java 1.7 or higher.

## Download

#### Gradle

```gradle
compile 'com.github.brake.threegpp:milenage:1.0.0'
```

#### Maven

```xml
<dependency>
  <groupId>com.github.brake.threegpp</groupId>
  <artifactId>milenage</artifactId>
  <version>1.0.0</version>
</dependency>
```

## Usage
### Basic

```java
// 1. Create instance of MilenageBufferFactory<BigIntegerBuffer> interface by
//    instantiating BigIngegerBufferFactory object.
MilenageBufferFactory<BigIntegerBuffer> bufferFactory = BigIntegerBufferFactory.getInstance();

// 2. Create instance of javax.crypto.Cypher from a key
//    using a helper class threegpp.milenage.cipher.Ciphers.

Cipher cipher = Ciphers.createRijndaelCipher(keyBytes);

// 3. Create OPc value from OP bytes and K, represented by the
//    previously created Cipher object.

byte [] OPc = Milenage.calculateOPc(opBytes, cipher, bufferFactory);

// 4. Create the Milenage instance for `OPc` and Cipher instances with MilenageBufferFactory.

Milenage<BigIntegerBuffer> milenage = new Milenage<>(OPc, cipher, bufferFactory);

// 5. Use the Milenage instance with input data (RAND, SQN, AMF)

try {
    Map<MilenageResult, byte []> result = melenage.calculateAll(rand, sqn, amf, Executors.newCachedThreadPull);

} catch(InterruptedException | ExecutionExceprtion e) {
// handle exception
}

// 6. Use the result

useMacA(result.get(MilenageResult.MAC_A));
useMacS(result.get(MilenateResult.MAC_S));
useResponse(result.get(MilenageResult.RES));
useConfinentialityKey(result.get(MilenageResult.CK));
useIntegrityKey(result.get(MilenageResult.IK));
useAnonimityKey(result.get(MilenageResult.AK));
useResynchAnonimityKey(result.get(MilenageResult.AK_R))


```

You can see working example in [test code](src/test/java/threegpp/milenage/MilenageTest.java).

### Advanced Usage

TBD

## Documentation

[Javadoc](https://brake.github.io/milenage/)

## License

Copyright © 2015-2016 Constantin Roganov

Distributed under the [MIT License](https://opensource.org/licenses/MIT).  
