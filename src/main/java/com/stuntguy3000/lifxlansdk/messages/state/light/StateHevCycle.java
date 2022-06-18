/*
 * Copyright 2022 Luke Anderson (stuntguy3000)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.stuntguy3000.lifxlansdk.messages.state.light;

import com.stuntguy3000.lifxlansdk.object.protocol.abstracts.Message;
import com.stuntguy3000.lifxlansdk.util.TypeUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * This says whether a HEV cycle is running on the device.
 * <p>
 * This packet requires the device has the hev capability. You may use {@link com.stuntguy3000.lifxlansdk.messages.get.device.GetVersion}
 * (32), {@link com.stuntguy3000.lifxlansdk.messages.get.device.GetHostFirmware} (14) and the Product Registry to
 * determine whether your device has this capability
 * <p>
 * This packet is the reply to the {@link com.stuntguy3000.lifxlansdk.messages.get.light.GetHevCycle} (142) and {@link
 * StateHevCycle} (143) messages
 */
@Getter
@Setter
@ToString
public class StateHevCycle extends Message {
    /**
     * The duration, in seconds, this cycle was set to.
     */
    private int duration_s;
    /**
     * The duration, in seconds, remaining in this cycle
     */
    private int remaining_s;
    /**
     * The power state before the HEV cycle started, which will be the power state once the cycle completes. This is
     * only relevant if remaining_s is larger than 0.
     */
    private boolean last_power;

    public StateHevCycle() {
        super(144);
    }

    @Override
    public void decodeBytes(byte[] data) {
        duration_s = TypeUtil.littleEndianBytesToUint32(data[0], data[1], data[2], data[3]);
        remaining_s = TypeUtil.littleEndianBytesToUint32(data[4], data[5], data[6], data[7]);
        last_power = TypeUtil.bytesToBoolint(data[8], data[9], data[10], data[11]);
    }
}


