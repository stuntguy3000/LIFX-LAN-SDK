/*
 * Copyright 2022 Luke Anderson (stuntguy3000)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.stuntguy3000.lifxlansdk.messages.set.tile;

import com.stuntguy3000.lifxlansdk.object.protocol.abstracts.Message;
import com.stuntguy3000.lifxlansdk.util.TypeUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Allows you to specify the position of this device in the chain relative to other device in the chain.
 * <p>
 * You can find more information about this data by looking at Tile Positions.
 * <p>
 * This message has no response packet even if you set res_required=1.
 * <p>
 * This packet requires the device has the Matrix Zones capability. You may use {@link
 * com.stuntguy3000.lifxlansdk.messages.get.device.GetVersion} (32), {@link com.stuntguy3000.lifxlansdk.messages.get.device.GetHostFirmware}
 * (14) and the Product Registry to determine whether your device has this capability
 */
public class SetUserPosition extends Message {
    /**
     * The device to change. This is 0 indexed and starts from the device closest to the controller.
     */
    private final int tile_index;
    private final float user_x;
    private final float user_y;

    public SetUserPosition(int tile_index, float user_x, float user_y) {
        super(703);

        this.tile_index = tile_index;
        this.user_x = user_x;
        this.user_y = user_y;
    }

    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            byteArrayOutputStream.write(TypeUtil.uint8ToBytesLittleEndian(tile_index));
            byteArrayOutputStream.write(TypeUtil.floatToBytesLittleEndian(user_x));
            byteArrayOutputStream.write(TypeUtil.floatToBytesLittleEndian(user_y));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }
}
