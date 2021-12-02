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
 * This packet provides information about the device.
 * <p>
 * This packet is the reply to the {@link com.stuntguy3000.lifxlansdk.messages.get.device.GetInfo} (34) message
 */
@Getter
@Setter
@ToString
public class StateInfo extends Message {
    /**
     * The current time according to the device. Note that this is most likely inaccurate.
     */
    private long time;
    /**
     * The amount of time in nanoseconds the device has been online since last power on
     */
    private long uptime;
    /**
     * The amount of time in nanseconds of power off time accurate to 5 seconds.
     */
    private long downtime;

    public StateInfo() {
        super(35);
    }

    @Override
    public void decodeBytes(byte[] data) {
        time = TypeUtil.littleEndianBytesToUint64(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7]);
        uptime = TypeUtil.littleEndianBytesToUint64(data[8], data[9], data[10], data[11], data[12], data[13], data[14], data[15]);
        downtime = TypeUtil.littleEndianBytesToUint64(data[16], data[17], data[18], data[19], data[20], data[21], data[22], data[23]);
    }
}


