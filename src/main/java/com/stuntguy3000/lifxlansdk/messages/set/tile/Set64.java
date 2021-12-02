/*
 * Copyright 2021 Luke Anderson (stuntguy3000)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.stuntguy3000.lifxlansdk.messages.set.tile;

import com.stuntguy3000.lifxlansdk.object.protocol.Color;
import com.stuntguy3000.lifxlansdk.object.protocol.abstracts.Message;
import com.stuntguy3000.lifxlansdk.util.TypeUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * This lets you set up to 64 HSBK values on the device.
 * <p>
 * This message has no response packet even if you set res_required=1.
 * <p>
 * This packet requires the device has the Matrix Zones capability. You may use {@link
 * com.stuntguy3000.lifxlansdk.messages.get.device.GetVersion} (32), {@link com.stuntguy3000.lifxlansdk.messages.get.device.GetHostFirmware}
 * (14) and the Product Registry to determine whether your device has this capability
 */
public class Set64 extends Message {
    /**
     * The device to change. This is 0 indexed and starts from the device closest to the controller.
     */
    private final int tile_index;
    /**
     * The number of devices in the chain to change starting from tile_index
     */
    private final int length;
    /**
     * The number of devices in the chain to change starting from tile_index
     */
    private final int x;
    /**
     * The number of devices in the chain to change starting from tile_index
     */
    private final int y;
    /**
     * The width of the square you're applying colors to. This should be 8 for the LIFX Tile and 5 for the LIFX Candle.
     */
    private final int width;
    /**
     * The width of the square you're applying colors to. This should be 8 for the LIFX Tile and 5 for the LIFX Candle.
     */
    private final int duration;
    /**
     * The HSBK values to assign to each zone specified by this packet.
     */
    private final Color[] colors;

    public Set64(int tile_index, int length, int x, int y, int width, int duration, Color[] colors) {
        super(715);

        this.tile_index = tile_index;
        this.length = length;
        this.x = x;
        this.y = y;
        this.width = width;
        this.duration = duration;
        this.colors = colors;
    }

    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            byteArrayOutputStream.write(TypeUtil.uint8ToBytesLittleEndian(tile_index));
            byteArrayOutputStream.write(TypeUtil.uint8ToBytesLittleEndian(length));

            byteArrayOutputStream.write(0);

            byteArrayOutputStream.write(TypeUtil.uint8ToBytesLittleEndian(x));
            byteArrayOutputStream.write(TypeUtil.uint8ToBytesLittleEndian(y));
            byteArrayOutputStream.write(TypeUtil.uint8ToBytesLittleEndian(width));
            byteArrayOutputStream.write(TypeUtil.uint32ToBytesLittleEndian(duration));

            Color[] colorsToWrite = new Color[64];
            System.arraycopy(colors, 0, colorsToWrite, 0, Math.min(colors.length, 64));

            for (Color color : colorsToWrite) {
                if (color == null) {
                    byteArrayOutputStream.write(TypeUtil.uint64ToBytesLittleEndian(0)); // Null
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
