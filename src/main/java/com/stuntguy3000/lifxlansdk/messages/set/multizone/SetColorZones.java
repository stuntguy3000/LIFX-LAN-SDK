/*
 * Copyright 2021 Luke Anderson (stuntguy3000)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.stuntguy3000.lifxlansdk.messages.set.multizone;

import com.stuntguy3000.lifxlansdk.object.protocol.abstracts.Message;
import com.stuntguy3000.lifxlansdk.object.protocol.enums.MultiZoneApplicationRequest;
import com.stuntguy3000.lifxlansdk.util.TypeUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Set a segment of your strip to a HSBK value. If your devices supports extended multizone messages it is recommended
 * you use those messages instead.
 * <p>
 * Will return one {@link com.stuntguy3000.lifxlansdk.messages.state.multizone.StateMultiZone} (506) message
 * <p>
 * This packet requires the device has the Linear Zones capability. You may use {@link
 * com.stuntguy3000.lifxlansdk.messages.get.device.GetVersion} (32), {@link com.stuntguy3000.lifxlansdk.messages.get.device.GetHostFirmware}
 * (14) and the Product Registry to determine whether your device has this capability
 */
public class SetColorZones extends Message {

    private final int start_index;
    private final int end_index;

    private final int hue;
    private final int saturation;
    private final int brightness;
    private final int kelvin;

    private final int duration;
    private final MultiZoneApplicationRequest apply;

    public SetColorZones(int start_index, int end_index, int hue, int saturation, int brightness, int kelvin, int duration, MultiZoneApplicationRequest apply) {
        super(501);

        this.start_index = start_index;
        this.end_index = end_index;
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
        this.kelvin = kelvin;
        this.duration = duration;
        this.apply = apply;
    }

    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(15);

        try {
            byteArrayOutputStream.write(TypeUtil.uint8ToBytesLittleEndian(start_index));
            byteArrayOutputStream.write(TypeUtil.uint8ToBytesLittleEndian(end_index));

            byteArrayOutputStream.write(TypeUtil.uint16ToBytesLittleEndian(hue));
            byteArrayOutputStream.write(TypeUtil.uint16ToBytesLittleEndian(saturation));
            byteArrayOutputStream.write(TypeUtil.uint16ToBytesLittleEndian(brightness));
            byteArrayOutputStream.write(TypeUtil.uint16ToBytesLittleEndian(kelvin));

            byteArrayOutputStream.write(TypeUtil.uint32ToBytesLittleEndian(duration));
            byteArrayOutputStream.write(TypeUtil.uint8ToBytesLittleEndian(apply.getNumericValue()));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }
}
