/*
 * Copyright 2021 Luke Anderson (stuntguy3000)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.stuntguy3000.lifxlansdk.messages.get.multizone;

import com.stuntguy3000.lifxlansdk.object.protocol.abstracts.Message;

/**
 * There are two possible messages that this request may return. If the number of zones in the response is just one then
 * you will get a single {@link com.stuntguy3000.lifxlansdk.messages.state.multizone.StateZone} (503). Otherwise you
 * will get one or more {@link com.stuntguy3000.lifxlansdk.messages.state.multizone.StateMultiZone} (506) replies.
 * <p>
 * This packet requires the device has the Linear Zones capability. You may use {@link
 * com.stuntguy3000.lifxlansdk.messages.get.device.GetVersion} (32), {@link com.stuntguy3000.lifxlansdk.messages.get.device.GetHostFirmware}
 * (14) and the Product Registry to determine whether your device has this capability
 */
@Deprecated
public class GetColorZones extends Message {
    private final int start_index;
    private final int end_index;

    public GetColorZones(int start_index, int end_index) {
        super(502);

        this.start_index = start_index;
        this.end_index = end_index;
    }

    @Override
    public byte[] toBytes() {
        return new byte[]{(byte) start_index, (byte) end_index};
    }
}
