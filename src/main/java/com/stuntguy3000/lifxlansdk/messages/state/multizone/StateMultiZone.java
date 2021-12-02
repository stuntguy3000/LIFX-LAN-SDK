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

import com.stuntguy3000.lifxlansdk.object.protocol.Color;
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
public class StateMultiZone extends Message {
    /**
     * The total number of zones on your strip
     */
    private int zones_count;
    /**
     * The first zone represented by this packet
     */
    private int zone_index;
    /**
     * The HSBK values of the zones this packet refers to.
     */
    private Color[] colors = new Color[8];

    public StateMultiZone() {
        super(506);
    }

    @Override
    public void decodeBytes(byte[] data) {
        zones_count = TypeUtil.littleEndianBytesToUint8(data[0]);
        zone_index = TypeUtil.littleEndianBytesToUint8(data[1]);

        int colorsIndex = 0;
        for (int dataOffset = 0; dataOffset < 64; dataOffset += 8) {
            byte[] colorBytes = new byte[8];
            System.arraycopy(data, 2 + dataOffset, colorBytes, 0, 8);

            Color color = new Color();
            color.decodeBytes(colorBytes);

            colors[colorsIndex] = color;
            colorsIndex++;

            if ((colorsIndex + zone_index) == zones_count) {
                // No more color zones to process
                break;
            }
        }
    }

    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            byteArrayOutputStream.write(TypeUtil.uint8ToBytesLittleEndian(zones_count));
            byteArrayOutputStream.write(TypeUtil.uint8ToBytesLittleEndian(zone_index));

            for (int i = 0; i < 8; i++) {
                Color color = colors[i];

                if (color != null) {
                    byteArrayOutputStream.write(colors[i].toBytes());
                } else {
                    byteArrayOutputStream.write(0);
                    byteArrayOutputStream.write(0);
                    byteArrayOutputStream.write(0);
                    byteArrayOutputStream.write(0);
                    byteArrayOutputStream.write(0);
                    byteArrayOutputStream.write(0);
                    byteArrayOutputStream.write(0);
                    byteArrayOutputStream.write(0);
                }
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }
}


