/*
 * Copyright 2021 Luke Anderson (stuntguy3000)
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
import java.util.BitSet;

/**
 * Represents a LIFX packet Frame Header
 * <p>
 * https://lan.developer.lifx.com/docs/packet-contents#frame-header
 */
@Getter
@Setter
public class FrameHeader implements ByteData {
    /**
     * Protocol number: must be 1024 (decimal)
     */
    private final byte[] protocol = new byte[]{0, 64};
    /**
     * Message includes a target address: must be one (1)
     */
    private final boolean addressable = true;
    /**
     * Size of entire message in bytes including this field
     */
    private int size;
    /**
     * Determines usage of the Frame Address target field
     * <p>
     * The tagged field is a boolean flag that indicates whether the Frame Address target field is being used to address
     * an individual device or all devices. When you broadcast a message to the network (i.e. broadcasting a GetService
     * (2) for discovery) you should set this field to 1 and for all other messages you should set this to 0.
     */
    private boolean tagged = false;
    /**
     * Source identifier: unique value set by the client, used by responses
     * <p>
     * The source identifier allows each client to provide a unique value, which will be included in the corresponding
     * field in Acknowledgement (45) and State packets the device sends back to you.
     */
    private int source = 1337;

    @Override
    public void decodeBytes(byte[] data) {
        // size
        size = TypeUtil.littleEndianBytesToUint16(data[0], data[1]);

        // tagged
        BitSet bitSet = BitSet.valueOf(new byte[]{data[3]});
        tagged = bitSet.get(5);

        // source
        source = TypeUtil.littleEndianBytesToUint32(data[4], data[5], data[6], data[7]);
    }

    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            // Bytes 0 - 1 (Size)
            byteArrayOutputStream.write(TypeUtil.uint16ToBytesLittleEndian((short) size));

            // Byte 2 (First Protocol Byte)
            // byteArrayOutputStream.write(protocol[0]);
            byteArrayOutputStream.write(0);

            // Byte 3
            // LIFX cram a ton of data into Byte 3, so we have to glue it all together
            //  Byte 3:
            //      Bit 6-7     origin
            //      Bit 5       tagged
            //      Bit 4       addressable
            //      Bit 3       protocol[1] Bit 8
            //      Bit 2       protocol[1] Bit 9
            //      Bit 1       protocol[1] Bit 10
            //      Bit 0       protocol[1] Bit 11
            BitSet bitSet = new BitSet(8);

            //bitSet.set(6, false); - Commented out for optimisation
            //bitSet.set(7, false); - Commented out for optimisation

            bitSet.set(5, tagged);
            bitSet.set(4, addressable);

            //bitSet.set(3, TypeUtil.getBitFromByte(protocol, 8) == 1); - Commented out for optimisation
            //bitSet.set(3, TypeUtil.getBitFromByte(protocol, 9) == 1); - Commented out for optimisation
            bitSet.set(2, true);
            //bitSet.set(1, TypeUtil.getBitFromByte(protocol, 10) == 1); - Commented out for optimisation
            //bitSet.set(0, TypeUtil.getBitFromByte(protocol, 11) == 1); - Commented out for optimisation

            byteArrayOutputStream.write(bitSet.toByteArray());

            // Bytes 4 - 7 (Source)
            byte[] sourceBytes = TypeUtil.uint32ToBytesLittleEndian(source);
            byteArrayOutputStream.write(sourceBytes[0]);
            byteArrayOutputStream.write(sourceBytes[1]);
            byteArrayOutputStream.write(sourceBytes[2]);
            byteArrayOutputStream.write(sourceBytes[3]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }
}
