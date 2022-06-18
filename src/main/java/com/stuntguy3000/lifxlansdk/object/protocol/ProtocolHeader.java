/*
 * Copyright 2022 Luke Anderson (stuntguy3000)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.stuntguy3000.lifxlansdk.object.protocol;

import com.stuntguy3000.lifxlansdk.object.protocol.abstracts.ByteData;
import com.stuntguy3000.lifxlansdk.util.TypeUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.ByteArrayOutputStream;

/**
 * Represents a LIFX packet Frame Header
 * <p>
 * https://lan.developer.lifx.com/docs/packet-contents#protocol-header
 */
@Getter
@Setter
public class ProtocolHeader implements ByteData {
    /**
     * Message type determines the payload being used
     */
    private int type;

    @Override
    public void decodeBytes(byte[] data) {
        type = TypeUtil.littleEndianBytesToUint16(data[8], data[9]);
    }

    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            // Bytes 0 - 7 (LIFX 0-Indexed Location, Bytes 24 - 31)
            //  Reserved Bytes
            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write(0);

            // Bytes 8 - 9 (LIFX 0-Indexed Location, Bytes 32 - 33)
            byte[] typeBytes = TypeUtil.uint16ToBytesLittleEndian(type);
            byteArrayOutputStream.write(typeBytes[0]);
            byteArrayOutputStream.write(typeBytes[1]);

            // Bytes 0 - 7 (LIFX 0-Indexed Location, Bytes 34 - 35)
            //  Reserved Bytes
            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }
}
