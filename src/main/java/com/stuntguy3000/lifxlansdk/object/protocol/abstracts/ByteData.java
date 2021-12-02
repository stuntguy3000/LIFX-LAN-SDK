/*
 * Copyright 2021 Luke Anderson (stuntguy3000)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.stuntguy3000.lifxlansdk.object.protocol.abstracts;

/**
 * Represents an object that has an associated byte data structure and allows conversions to and from byte arrays
 * <p>
 * Not all ByteData objects may have both, if any, functions defined - this depends on the usage for the object
 * throughout the application. Some objects do not need to be decoded, and some do not need to be encoded.
 */
public interface ByteData {
    /**
     * Encode the object to a byte array
     *
     * @return the data encoded in a byte array
     */
    default byte[] toBytes() {
        return new byte[0];
    }

    /**
     * Decodes the byte array to fill object properties
     *
     * @param data the data to decode
     */
    default void decodeBytes(byte[] data) {
    }
}
