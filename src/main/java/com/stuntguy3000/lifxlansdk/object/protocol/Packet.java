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
import com.stuntguy3000.lifxlansdk.object.protocol.abstracts.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class Packet implements ByteData {
    private final FrameHeader frameHeader;
    private final FrameAddress frameAddress;
    private final ProtocolHeader protocolHeader;
    private final Message message;

    /**
     * This is only filled on packets that are received, not constructed pre-sent packets
     */
    private InetAddress ipAddress;

    @Override
    public byte[] toBytes() {
        byte[] frameAddressBytes = frameAddress.toBytes();
        byte[] protocolHeaderBytes = protocolHeader.toBytes();
        byte[] messageBytes = message.toBytes();

        // Manipulate Frame Data if Required

        // Calculate Message Size
        //  A LIFX packet header is 36 bytes, plus the payload
        frameHeader.setSize((short) (32 + messageBytes.length));

        byte[] frameHeaderBytes = frameHeader.toBytes();

        // Bring it all together
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            byteArrayOutputStream.write(frameHeaderBytes);
            byteArrayOutputStream.write(frameAddressBytes);
            byteArrayOutputStream.write(protocolHeaderBytes);
            byteArrayOutputStream.write(messageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }
}
