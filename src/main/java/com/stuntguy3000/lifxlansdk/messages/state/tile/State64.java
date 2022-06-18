/*
 * Copyright 2022 Luke Anderson (stuntguy3000)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.stuntguy3000.lifxlansdk.messages.state.tile;

import com.stuntguy3000.lifxlansdk.object.protocol.Color;
import com.stuntguy3000.lifxlansdk.object.protocol.abstracts.Message;
import com.stuntguy3000.lifxlansdk.util.TypeUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The current HSBK values of the zones in a single device.
 * <p>
 * This packet requires the device has the Matrix Zones capability. You may use {@link
 * com.stuntguy3000.lifxlansdk.messages.get.device.GetVersion} (32), {@link com.stuntguy3000.lifxlansdk.messages.get.device.GetHostFirmware}
 * (14) and the Product Registry to determine whether your device has this capability
 * <p>
 * This packet is the reply to the {@link com.stuntguy3000.lifxlansdk.messages.get.tile.Get64} (707) and {@link
 * com.stuntguy3000.lifxlansdk.messages.set.tile.Set64} (715) messages
 */
@Getter
@Setter
@ToString
public class State64 extends Message {
    /**
     * The index of the device in the chain this packet refers to. This is 0 based starting from the device closest to
     * the controller.
     */
    private int start_index;
    /**
     * The x coordinate the colors start from
     */
    private int x;
    /**
     * The y coordinate the colors start from
     */
    private int y;
    /**
     * The width of each row
     */
    private int width;
    private Color[] colors = new Color[64];

    public State64() {
        super(711);
    }

    @Override
    public void decodeBytes(byte[] data) {
        start_index = TypeUtil.littleEndianBytesToUint8(data[0]);
        x = TypeUtil.littleEndianBytesToUint8(data[2]);
        y = TypeUtil.littleEndianBytesToUint8(data[3]);
        width = TypeUtil.littleEndianBytesToUint8(data[4]);

        int colorsIndex = 0;
        for (int dataOffset = 0; dataOffset < 64; dataOffset += 8) {
            byte[] colorBytes = new byte[8];
            System.arraycopy(data, 5 + dataOffset, colorBytes, 0, 8);

            Color color = new Color();
            color.decodeBytes(colorBytes);

            colors[colorsIndex] = color;
            colorsIndex++;
        }
    }
}


