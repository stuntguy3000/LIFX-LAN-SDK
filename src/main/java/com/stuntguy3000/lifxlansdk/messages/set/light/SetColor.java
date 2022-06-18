/*
 * Copyright 2022 Luke Anderson (stuntguy3000)
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
 * This packet lets you set the HSBK value for the light. For devices that have multiple zones, this will set all Zones
 * on the device to this color.
 * <p>
 * Will return one LightState (107) message
 */
public class SetColor extends Message {
    private final int hue;
    private final int saturation;
    private final int brightness;
    private final int kelvin;
    private final int duration;

    public SetColor(int hue, int saturation, int brightness, int kelvin, int duration) {
        super(102);

        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
        this.kelvin = kelvin;
        this.duration = duration;
    }

    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(13);

        try {
            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write(TypeUtil.uint16ToBytesLittleEndian(hue));
            byteArrayOutputStream.write(TypeUtil.uint16ToBytesLittleEndian(saturation));
            byteArrayOutputStream.write(TypeUtil.uint16ToBytesLittleEndian(brightness));
            byteArrayOutputStream.write(TypeUtil.uint16ToBytesLittleEndian(kelvin));
            byteArrayOutputStream.write(TypeUtil.uint32ToBytesLittleEndian(duration));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }
}
