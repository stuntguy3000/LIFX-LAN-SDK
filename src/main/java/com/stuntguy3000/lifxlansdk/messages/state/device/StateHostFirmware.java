/*
 * Copyright 2022 Luke Anderson (stuntguy3000)
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
 * This packet will tell you what version of firmware is on the device.
 * <p>
 * Typically you would use this information along with {@link StateVersion} (33) to determine the capabilities of your
 * device as specified in our Product Registry.
 * <p>
 * The version_major and version_minor should be thought of as a pair of (major, minor). So say major is 3 and minor is
 * 60, then the version is (3, 60). This means that (2, 80) is considered less than (3, 60) and (3, 70) is considered
 * greater.
 * <p>
 * LIFX products will specify a different major for each generation of our devices.
 * <p>
 * This packet is the reply to the {@link com.stuntguy3000.lifxlansdk.messages.get.device.GetHostFirmware} (14) message
 */
@Getter
@Setter
@ToString
public class StateHostFirmware extends Message {
    /**
     * The timestamp of the firmware that is on the device as an epoch
     */
    private long build;
    /**
     * The minor component of the firmware version
     */
    private int version_minor;
    /**
     * The major component of the firmware version
     */
    private int version_major;

    public StateHostFirmware() {
        super(15);
    }

    @Override
    public void decodeBytes(byte[] data) {
        build = TypeUtil.littleEndianBytesToUint64(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7]);
        version_minor = TypeUtil.littleEndianBytesToUint16(data[16], data[17]);
        version_major = TypeUtil.littleEndianBytesToUint16(data[18], data[19]);
    }
}


