/*
 * Copyright 2021 Luke Anderson (stuntguy3000)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.stuntguy3000.lifxlansdk.messages.state.device;

import com.stuntguy3000.lifxlansdk.object.protocol.abstracts.Message;
import com.stuntguy3000.lifxlansdk.util.TypeUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * For some firmware, this packet is returned when the device receives a packet it does not know how to handle. For now,
 * only the LIFX Switch has this behaviour.
 * <p>
 * It will return the type of packet it couldn't handle. For example, if you send a {@link
 * com.stuntguy3000.lifxlansdk.messages.get.light.GetColor} (101) to a LIFX switch, then you would receive one of these
 * with a unhandled_type of 101.
 */
@Getter
@Setter
@ToString
public class StateUnhandled extends Message {
    /**
     * The type of the packet that was ignored.
     */
    private int unhandled_type;

    public StateUnhandled() {
        super(223);
    }

    @Override
    public void decodeBytes(byte[] data) {
        unhandled_type = TypeUtil.littleEndianBytesToUint16(data[0], data[1]);
    }
}


