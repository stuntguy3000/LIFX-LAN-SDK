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
 * https://lan.developer.lifx.com/docs/packet-contents#frame-address
 */
@Getter
@Setter
public class FrameAddress implements ByteData {
    /**
     * 6 byte device address (MAC address) or zero (0) means all devices. The last two bytes should be 0 bytes.
     */
    private String target;
    /**
     * State message required. However, Get messages will send State messages anyway and State messages to Set messages
     * are usually not useful.
     */
    private boolean res_required = true;    // Defaulted to True to support SDK functions of returning data
    /**
     * Acknowledgement message required
     */
    private boolean ack_required;           // Whilst supported, acknowledgements are not used by this SDK
    /**
     * Wrap around message sequence number
     * <p>
     * The sequence number allows the client to provide a unique value, which will be included by the LIFX device in any
     * message that is sent in response to a message sent by the client. This allows the client to distinguish between
     * different messages sent with the same source identifier in the Frame Header. We recommend that your program has
     * one source value and keeps incrementing sequence per device for each message you send.
     * <p>
     * Once sequence reaches the maximum value of 255 for that device, roll it back to 0 and keep incrementing from
     * there.
     */
    private short sequence;

    @Override
    public void decodeBytes(byte[] data) {
        // target
        byte[] targetBytes = new byte[6];
        System.arraycopy(data, 0, targetBytes, 0, 6);
        target = TypeUtil.injectColonsIntoMacAddress(TypeUtil.bytesToHex(targetBytes));

        // res_required
        // ack_required
        BitSet bitSet = BitSet.valueOf(new byte[]{data[14]});
        res_required = bitSet.get(0);
        ack_required = bitSet.get(1);

        // sequence
        sequence = data[15];
    }

    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            // Bytes 0 - 7 (LIFX 0-Indexed Location, Bytes 8 - 15)
            byteArrayOutputStream.write(TypeUtil.hexToBytes(target.replace("-", "").replaceAll(":", "")));
            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write(0);

            // Bytes 8 - 13 (LIFX 0-Indexed Location, Bytes 16 - 21)
            //  Reserved Bytes
            for (int i = 0; i < 6; i++) {
                byteArrayOutputStream.write(0);
            }

            // Byte 14 (LIFX 0-Indexed Location, Bytes 22)
            //  Includes 6 Reserved Bits
            if (!res_required && !ack_required) {
                byteArrayOutputStream.write(0);
            } else {
                BitSet bitSet = new BitSet(8);
                bitSet.set(0, res_required);
                bitSet.set(1, ack_required);
                byteArrayOutputStream.write(bitSet.toByteArray());
            }

            // Byte 15 (LIFX 0-Indexed Location, Bytes 23)
            byte[] sequenceBytes = TypeUtil.uint16ToBytesLittleEndian(sequence);
            byteArrayOutputStream.write(sequenceBytes[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }
}
