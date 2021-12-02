/*
 * Copyright 2021 Luke Anderson (stuntguy3000)
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
 * This packet lets you set default values for a HEV cycle on the device
 * <p>
 * This packet requires the device has the hev capability. You may use {@link com.stuntguy3000.lifxlansdk.messages.get.device.GetVersion}
 * (32), {@link com.stuntguy3000.lifxlansdk.messages.get.device.GetHostFirmware} (14) and the Product Registry to
 * determine whether your device has this capability
 * <p>
 * This packet is the reply to the {@link com.stuntguy3000.lifxlansdk.messages.get.light.GetHevCycleConfiguration} (145)
 * and {@link com.stuntguy3000.lifxlansdk.messages.set.light.SetHevCycleConfiguration} (146) messages
 */
@Getter
@Setter
@ToString
public class StateHevCycleConfiguration extends Message {
    /**
     * Whether a short flashing indication is run at the end of an HEV cycle.
     */
    private boolean indication;
    /**
     * This is the default duration that is used when SetHevCycle (143) is given 0 for duration_s.
     */
    private int duration_s;

    public StateHevCycleConfiguration() {
        super(144);
    }

    @Override
    public void decodeBytes(byte[] data) {
        indication = TypeUtil.bytesToBoolint(data[0], data[1], data[2], data[3]);
        duration_s = TypeUtil.littleEndianBytesToUint32(data[4], data[5], data[6], data[7]);
    }
}


