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

import com.stuntguy3000.lifxlansdk.object.protocol.Color;
import com.stuntguy3000.lifxlansdk.object.protocol.abstracts.Message;
import com.stuntguy3000.lifxlansdk.object.protocol.enums.MultiZoneExtendedApplicationRequest;
import com.stuntguy3000.lifxlansdk.util.TypeUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * This message lets you change the HSBK values for all zones on the strip in one message.
 * <p>
 * Will return one {@link com.stuntguy3000.lifxlansdk.messages.state.multizone.StateExtendedColorZones} (512) message
 * <p>
 * This packet requires the device has the Extended Linear Zones capability. You may use {@link
 * com.stuntguy3000.lifxlansdk.messages.get.device.GetVersion} (32), {@link com.stuntguy3000.lifxlansdk.messages.get.device.GetHostFirmware}
 * (14) and the Product Registry to determine whether your device has this capability
 */
public class SetExtendedColorZones extends Message {

    private final int duration;
    private final MultiZoneExtendedApplicationRequest apply;
    private final int zone_index;
    private final int colors_count;
    private final Color[] colors;

    public SetExtendedColorZones(int duration, MultiZoneExtendedApplicationRequest apply, int zone_index, int colors_count, Color[] colors) {
        super(510);
        this.duration = duration;
        this.apply = apply;
        this.zone_index = zone_index;
        this.colors_count = colors_count;
        this.colors = colors;
    }

    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            byteArrayOutputStream.write(TypeUtil.uint32ToBytesLittleEndian(duration));
            byteArrayOutputStream.write(TypeUtil.uint8ToBytesLittleEndian(apply.getNumericValue()));

            byteArrayOutputStream.write(TypeUtil.uint16ToBytesLittleEndian(zone_index));
            byteArrayOutputStream.write(TypeUtil.uint8ToBytesLittleEndian(colors_count));

            Color[] colorsToWrite = new Color[82];
            System.arraycopy(colors, 0, colorsToWrite, 0, Math.min(colors.length, 82));

            for (Color color : colorsToWrite) {
                if (color == null) {
                    //byteArrayOutputStream.write(TypeUtil.uint64ToBytesLittleEndian(0)); // Null?
                    byteArrayOutputStream.write(Color.BLACK.toBytes());
                } else {
                    byteArrayOutputStream.write(color.toBytes());
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }
}
