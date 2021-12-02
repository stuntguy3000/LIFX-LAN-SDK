/*
 * Copyright 2021 Luke Anderson (stuntguy3000)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.stuntguy3000.lifxlansdk.util;

import javax.xml.bind.DatatypeConverter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Various utility functions for handling different data types
 * <p>
 * Disclaimer: I am far from a Java Type expert. I have a sneaky suspicion that: - I need to do more masking to 0xFF -
 * The whole unsigned int definitions are secretly a lie
 * <p>
 * But... This seems to work during all of my testing?
 * <p>
 * Feel free to break it (or write more test util tests 😊)
 */
public class TypeUtil {
    public static float littleEndianBytesToFloat(byte... data) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        return byteBuffer.getFloat();
    }

    public static int littleEndianBytesToUint8(byte data) {
        return data & 0xff;
    }

    // Adapted from https://stackoverflow.com/a/13375236
    public static int littleEndianBytesToUint16(byte... data) {
        int low = ((int) data[0]) & 0xff;
        int high = ((int) data[1]) & 0xff;

        return (high << 8) | low;
    }

    public static int littleEndianBytesToUint32(byte... data) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        return byteBuffer.getInt();
    }

    public static long littleEndianBytesToUint64(byte... data) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        return byteBuffer.getLong();
    }

    public static byte[] uint8ToBytesLittleEndian(int number) {
        return new byte[]{(byte) number};
    }

    public static byte[] uint16ToBytesLittleEndian(int number) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(2);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        byteBuffer.putShort((short) number);

        return byteBuffer.array();
    }

    public static byte[] uint32ToBytesLittleEndian(int number) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        byteBuffer.putInt(number);

        return byteBuffer.array();
    }

    public static byte[] uint64ToBytesLittleEndian(long number) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        byteBuffer.putLong((int) number);

        return byteBuffer.array();
    }

    public static byte[] floatToBytesLittleEndian(float number) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        byteBuffer.putFloat(number);

        return byteBuffer.array();
    }

    public static String bytesToHex(byte... bytes) {
        return DatatypeConverter.printHexBinary(bytes);
    }

    public static byte[] hexToBytes(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }

    // https://stackoverflow.com/a/39660058
    public static String injectColonsIntoMacAddress(String macAddress) {
        if (macAddress.contains(":")) {
            return macAddress;
        }

        return macAddress.replaceAll(".{2}(?=.)", "$0:");
    }

    public static String bytesToString(byte... data) {
        return new String(data, StandardCharsets.UTF_8).split("\0")[0];
    }

    public static byte[] stringToBytesWithPadding(String inputData, int length) {
        byte[] data = new byte[length];
        byte[] stringBytes = inputData.getBytes();

        // Process Data
        if (stringBytes.length == length) {
            return stringBytes;
        } else {
            System.arraycopy(stringBytes, 0, data, 0, Math.min(stringBytes.length, length));
        }

        return data;
    }

    public static boolean bytesToBoolint(byte... bytes) {
        return bytes[0] == 1;
    }

    public static byte boolintToByte(boolean bool) {
        return (byte) (bool ? 1 : 0);
    }

    // This UUID stuff is crazy. Just saying.
    // https://stackoverflow.com/a/19399768
    public static UUID uuidBytesToUUIDObject(byte[] bytes) {
        String uuidHexString = bytesToHex(bytes);
        uuidHexString = uuidHexString.replaceFirst("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5");

        return UUID.fromString(uuidHexString);
    }

    public static byte[] uuidObjectToUUIDBytes(UUID uuid) {
        return hexToBytes(uuid.toString().replaceAll("-", ""));
    }
}
