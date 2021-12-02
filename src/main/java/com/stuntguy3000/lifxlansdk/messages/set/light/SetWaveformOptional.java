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
import com.stuntguy3000.lifxlansdk.object.protocol.enums.Waveform;
import com.stuntguy3000.lifxlansdk.util.TypeUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * This behaves like {@link SetWaveform} (103) but allows you to keep certain parts of the original HSBK values during
 * the transition.
 * <p>
 * Will return one {@link com.stuntguy3000.lifxlansdk.messages.state.light.LightState} (107) message
 */
public class SetWaveformOptional extends Message {
    private final boolean waveformTransient;
    private final int hue;
    private final int saturation;
    private final int brightness;
    private final int kelvin;
    private final int period;
    private final float cycles;
    private final int skew_ratio;
    private final Waveform waveform;
    private final boolean set_hue;
    private final boolean set_saturation;
    private final boolean set_brightness;
    private final boolean set_kelvin;

    public SetWaveformOptional(boolean waveformTransient, int hue, int saturation, int brightness, int kelvin, int period, float cycles, int skew_ratio, Waveform waveform, boolean set_hue, boolean set_saturation, boolean set_brightness, boolean set_kelvin) {
        super(119);

        this.waveformTransient = waveformTransient;
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
        this.kelvin = kelvin;
        this.period = period;
        this.cycles = cycles;
        this.skew_ratio = skew_ratio;
        this.waveform = waveform;
        this.set_hue = set_hue;
        this.set_saturation = set_saturation;
        this.set_brightness = set_brightness;
        this.set_kelvin = set_kelvin;
    }

    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            for (int i = 0; i < 8; i++) {
                byteArrayOutputStream.write(1);
            }

            byteArrayOutputStream.write(TypeUtil.boolintToByte(waveformTransient));
            byteArrayOutputStream.write(TypeUtil.uint16ToBytesLittleEndian(hue));
            byteArrayOutputStream.write(TypeUtil.uint16ToBytesLittleEndian(saturation));
            byteArrayOutputStream.write(TypeUtil.uint16ToBytesLittleEndian(brightness));
            byteArrayOutputStream.write(TypeUtil.uint16ToBytesLittleEndian(kelvin));
            byteArrayOutputStream.write(TypeUtil.uint32ToBytesLittleEndian(period));
            byteArrayOutputStream.write(TypeUtil.floatToBytesLittleEndian(cycles));

            // This is meant to be an Int16 (signed integer), but since all Java data types are already signed...
            byteArrayOutputStream.write(TypeUtil.uint16ToBytesLittleEndian(skew_ratio));

            byteArrayOutputStream.write(TypeUtil.uint8ToBytesLittleEndian(waveform.getNumericValue()));

            byteArrayOutputStream.write(TypeUtil.boolintToByte(set_hue));
            byteArrayOutputStream.write(TypeUtil.boolintToByte(set_saturation));
            byteArrayOutputStream.write(TypeUtil.boolintToByte(set_brightness));
            byteArrayOutputStream.write(TypeUtil.boolintToByte(set_kelvin));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }
}
