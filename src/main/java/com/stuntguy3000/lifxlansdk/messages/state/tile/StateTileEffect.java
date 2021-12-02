/*
 * Copyright 2021 Luke Anderson (stuntguy3000)
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
import com.stuntguy3000.lifxlansdk.object.protocol.enums.TileEffectType;
import com.stuntguy3000.lifxlansdk.util.TypeUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The current Firmware Effect running on the device
 * <p>
 * This packet requires the device has the Matrix Zones capability. You may use {@link
 * com.stuntguy3000.lifxlansdk.messages.get.device.GetVersion} (32), {@link com.stuntguy3000.lifxlansdk.messages.get.device.GetHostFirmware}
 * (14) and the Product Registry to determine whether your device has this capability
 * <p>
 * This packet is the reply to the {@link com.stuntguy3000.lifxlansdk.messages.get.tile.GetTileEffect} (718) and {@link
 * StateTileEffect} (719) messages
 */
@Getter
@Setter
@ToString
public class StateTileEffect extends Message {
    /**
     * The unique value identifying the request
     */
    private int instanceid;
    private TileEffectType effectType;
    /**
     * The time it takes for one cycle in milliseconds.
     */
    private int speed;
    /**
     * The amount of time left in the current effect in nanoseconds
     */
    private long duration;
    /**
     * The parameters as specified in the request.
     */
    private byte[] parameters = new byte[32];
    /**
     * The number of colors in the palette that are relevant
     */
    private int palette_count;
    /**
     * The colors specified for the effect.
     */
    private Color[] palette = new Color[16];

    public StateTileEffect() {
        super(720);
    }

    @Override
    public void decodeBytes(byte[] data) {
        byte[] instanceidBytes = new byte[32];
        System.arraycopy(data, 1, instanceidBytes, 0, 32);
        instanceid = TypeUtil.littleEndianBytesToUint32(instanceidBytes);

        effectType = TileEffectType.getByValue(TypeUtil.littleEndianBytesToUint8(data[33]));
        speed = TypeUtil.littleEndianBytesToUint32(data[34], data[35], data[36], data[37]);
        duration = TypeUtil.littleEndianBytesToUint32(data[38], data[39], data[40], data[41], data[42], data[43], data[44], data[45]);

        palette_count = TypeUtil.littleEndianBytesToUint8(data[54]);

        int colorsIndex = 0;
        for (int dataOffset = 0; dataOffset < 16; dataOffset += 8) {
            byte[] colorBytes = new byte[8];
            System.arraycopy(data, 55 + dataOffset, colorBytes, 0, 8);

            Color color = new Color();
            color.decodeBytes(colorBytes);

            palette[colorsIndex] = color;
            colorsIndex++;
        }
    }
}


