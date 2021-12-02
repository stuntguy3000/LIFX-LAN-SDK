/*
 * Copyright 2021 Luke Anderson (stuntguy3000)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.stuntguy3000.lifxlansdk.messages.get.tile;

import com.stuntguy3000.lifxlansdk.object.protocol.abstracts.Message;

/**
 * Used to get the HSBK values of all the zones in devices connected in the chain.
 * <p>
 * this will return one or more {@link com.stuntguy3000.lifxlansdk.messages.state.tile.State64} (711) messages. The
 * maximum number of messages you will receive is the number specified by length in your request.
 * <p>
 * This packet requires the device has the Matrix Zones capability. You may use {@link
 * com.stuntguy3000.lifxlansdk.messages.get.device.GetVersion} (32), {@link com.stuntguy3000.lifxlansdk.messages.get.device.GetHostFirmware}
 * (14) and the Product Registry to determine whether your device has this capability
 */
public class Get64 extends Message {
    private final int tile_index;
    private final int length;
    private final int x;
    private final int y;
    private final int width;

    public Get64(int tile_index, int length, int x, int y, int width) {
        super(707);

        this.tile_index = tile_index;
        this.length = length;
        this.x = x;
        this.y = y;
        this.width = width;
    }

    @Override
    public byte[] toBytes() {
        return new byte[]{(byte) tile_index, (byte) length, 0, (byte) x, (byte) y, (byte) width};
    }
}
