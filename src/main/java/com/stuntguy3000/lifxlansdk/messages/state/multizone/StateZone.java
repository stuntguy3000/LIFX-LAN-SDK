/*
 * Copyright 2021 Luke Anderson (stuntguy3000)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.stuntguy3000.lifxlansdk.messages.state.multizone;

import com.stuntguy3000.lifxlansdk.object.protocol.abstracts.Message;
import com.stuntguy3000.lifxlansdk.util.TypeUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * This represents the HSBK value of a single zone on your strip.
 * <p>
 * This packet requires the device has the Linear Zones capability. You may use {@link
 * com.stuntguy3000.lifxlansdk.messages.get.device.GetVersion} (32), {@link com.stuntguy3000.lifxlansdk.messages.get.device.GetHostFirmware}
 * (14) and the Product Registry to determine whether your device has this capability
 * <p>
 * This packet is the reply to the {@link com.stuntguy3000.lifxlansdk.messages.get.multizone.GetColorZones} (502) and
 * {@link com.stuntguy3000.lifxlansdk.messages.set.multizone.SetColorZones} (501) messages
 */
@Getter
@Setter
@ToString
@Deprecated
public class StateZone extends Message {
    /**
     * The total number of zones on the strip.
     */
    private int zones_count;
    /**
     * The zone this packet refers to.
     */
    private int zone_index;
    private int hue;
    private int saturation;
    private int brightness;
    private int kelvin;

    public StateZone() {
        super(503);
    }

    @Override
    public void decodeBytes(byte[] data) {
        zones_count = TypeUtil.littleEndianBytesToUint8(data[0]);
        zone_index = TypeUtil.littleEndianBytesToUint8(data[1]);
        hue = TypeUtil.littleEndianBytesToUint16(data[2], data[3]);
        saturation = TypeUtil.littleEndianBytesToUint16(data[4], data[5]);
        brightness = TypeUtil.littleEndianBytesToUint16(data[6], data[7]);
        kelvin = TypeUtil.littleEndianBytesToUint16(data[8], data[9]);
    }

    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            byteArrayOutputStream.write(TypeUtil.uint8ToBytesLittleEndian(zones_count));
            byteArrayOutputStream.write(TypeUtil.uint8ToBytesLittleEndian(zone_index));
            byteArrayOutputStream.write(TypeUtil.uint16ToBytesLittleEndian(hue));
            byteArrayOutputStream.write(TypeUtil.uint16ToBytesLittleEndian(saturation));
            byteArrayOutputStream.write(TypeUtil.uint16ToBytesLittleEndian(brightness));
            byteArrayOutputStream.write(TypeUtil.uint16ToBytesLittleEndian(kelvin));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }
}


