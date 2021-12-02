/*
 * Copyright 2021 Luke Anderson (stuntguy3000)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.stuntguy3000.lifxlansdk.messages.set.light;

import com.stuntguy3000.lifxlansdk.object.protocol.abstracts.Message;
import com.stuntguy3000.lifxlansdk.util.TypeUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * This is the same as {@link com.stuntguy3000.lifxlansdk.messages.set.device.SetPower} (21) but allows you to specify
 * how long it will take to transition to the new power state.
 * <p>
 * Will return one {@link com.stuntguy3000.lifxlansdk.messages.state.light.StateLightPower} (118) message
 */
public class SetLightPower extends Message {
    /**
     * If you specify 0 the light will turn off and if you specify 65535 the device will turn on.
     */
    private final int level;
    /**
     * The time it will take to transition to the new state in milliseconds.
     */
    private final int duration;

    public SetLightPower(int level, int duration) {
        super(117);

        this.level = level;
        this.duration = duration;
    }

    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(6);

        try {
            byteArrayOutputStream.write(TypeUtil.uint16ToBytesLittleEndian(level));
            byteArrayOutputStream.write(TypeUtil.uint32ToBytesLittleEndian(duration));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }
}
