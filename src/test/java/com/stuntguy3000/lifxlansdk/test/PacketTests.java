/*
 * Copyright 2022 Luke Anderson (stuntguy3000)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.stuntguy3000.lifxlansdk.test;

import com.stuntguy3000.lifxlansdk.handler.PacketHandler;
import com.stuntguy3000.lifxlansdk.object.protocol.FrameAddress;
import com.stuntguy3000.lifxlansdk.object.protocol.FrameHeader;
import com.stuntguy3000.lifxlansdk.object.protocol.ProtocolHeader;
import com.stuntguy3000.lifxlansdk.util.TypeUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PacketTests {
    @BeforeAll
    public static void setup() {
        try {
            PacketHandler.setBroadcastAddress(InetAddress.getByName("192.168.1.255"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void encodeDecodeFrameHeader() {
        FrameHeader frameHeader = new FrameHeader();

        frameHeader.setSize((short) 68);
        frameHeader.setTagged(false);
        frameHeader.setSource(2);

        byte[] output = frameHeader.toBytes();
        assertEquals("4400001402000000", TypeUtil.bytesToHex(output));

        FrameHeader decoded = new FrameHeader();
        decoded.decodeBytes(output);

        assertEquals("4400001402000000", TypeUtil.bytesToHex(decoded.toBytes()));
    }

    @Test
    public void encodeDecodeFrameAddress() {
        FrameAddress frameAddress = new FrameAddress();

        frameAddress.setTarget("d0:73:d5:43:47:86");
        frameAddress.setRes_required(false);
        frameAddress.setAck_required(true);
        frameAddress.setSequence((short) 1);

        byte[] output = frameAddress.toBytes();

        assertEquals("D073D543478600000000000000000201", TypeUtil.bytesToHex(output));

        FrameAddress decoded = new FrameAddress();
        decoded.decodeBytes(output);

        assertEquals("D073D543478600000000000000000201", TypeUtil.bytesToHex(decoded.toBytes()));
    }

    @Test
    public void encodeDecodeProtocolHeader1() {
        ProtocolHeader protocolHeader = new ProtocolHeader();

        protocolHeader.setType((short) 102);

        byte[] output = protocolHeader.toBytes();

        assertEquals("000000000000000066000000", TypeUtil.bytesToHex(output));

        ProtocolHeader decoded = new ProtocolHeader();
        decoded.decodeBytes(output);

        assertEquals("000000000000000066000000", TypeUtil.bytesToHex(decoded.toBytes()));
    }

    @Test
    public void encodeDecodeProtocolHeader2() {
        ProtocolHeader protocolHeader = new ProtocolHeader();

        protocolHeader.setType((short) 2);

        byte[] output = protocolHeader.toBytes();

        assertEquals("000000000000000002000000", TypeUtil.bytesToHex(output));

        ProtocolHeader decoded = new ProtocolHeader();
        decoded.decodeBytes(output);

        assertEquals("000000000000000002000000", TypeUtil.bytesToHex(decoded.toBytes()));
    }

    @Test
    public void encodeDecodeProtocolHeader3() {
        ProtocolHeader protocolHeader = new ProtocolHeader();

        protocolHeader.setType((short) 0);

        byte[] output = protocolHeader.toBytes();

        assertEquals("000000000000000000000000", TypeUtil.bytesToHex(output));

        ProtocolHeader decoded = new ProtocolHeader();
        decoded.decodeBytes(output);

        assertEquals("000000000000000000000000", TypeUtil.bytesToHex(decoded.toBytes()));
    }
}
