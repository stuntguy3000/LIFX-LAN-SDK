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
 * The current visual state of the device and it's label
 * <p>
 * This packet is the reply to {@link com.stuntguy3000.lifxlansdk.messages.get.light.GetColor} (101), {@link
 * com.stuntguy3000.lifxlansdk.messages.set.light.SetColor} (102), {@link com.stuntguy3000.lifxlansdk.messages.set.light.SetWaveform}
 * (103) and {@link com.stuntguy3000.lifxlansdk.messages.set.light.SetWaveformOptional} (119) messages
 */
@Getter
@Setter
@ToString
public class LightState extends Message {
    private int hue;
    private int saturation;
    private int brightness;
    private int kelvin;
    /**
     * The current power level of the device.
     */
    private int power;
    /**
     * The current label on the device.
     */
    private String label;

    public LightState() {
        super(107);
    }

    @Override
    public void decodeBytes(byte[] data) {
        hue = TypeUtil.littleEndianBytesToUint16(data[0], data[1]);
        saturation = TypeUtil.littleEndianBytesToUint16(data[2], data[3]);
        brightness = TypeUtil.littleEndianBytesToUint16(data[4], data[5]);
        kelvin = TypeUtil.littleEndianBytesToUint16(data[6], data[7]);
        power = TypeUtil.littleEndianBytesToUint16(data[10], data[11]);

        byte[] lightBytes = new byte[32];
        System.arraycopy(data, 12, lightBytes, 0, 32);
        label = TypeUtil.bytesToString(lightBytes);
    }
}


