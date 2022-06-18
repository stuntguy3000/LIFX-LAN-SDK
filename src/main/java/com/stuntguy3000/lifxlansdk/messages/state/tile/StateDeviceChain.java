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

import com.stuntguy3000.lifxlansdk.object.protocol.TileData;
import com.stuntguy3000.lifxlansdk.object.protocol.abstracts.Message;
import com.stuntguy3000.lifxlansdk.util.TypeUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Information about each device in the chain.
 * <p>
 * This packet requires the device has the Matrix Zones capability. You may use {@link
 * com.stuntguy3000.lifxlansdk.messages.get.device.GetVersion} (32), {@link com.stuntguy3000.lifxlansdk.messages.get.device.GetHostFirmware}
 * (14) and the Product Registry to determine whether your device has this capability
 * <p>
 * This packet is the reply to the {@link com.stuntguy3000.lifxlansdk.messages.get.tile.GetDeviceChain} (701) message
 */
@Getter
@Setter
@ToString
public class StateDeviceChain extends Message {
    /**
     * The index of the first device in the chain this packet refers to
     */
    private int start_index;
    /**
     * The information for each device in the chain
     */
    private TileData[] tile_devices = new TileData[16];
    /**
     * The number of device in tile_devices that map to devices in the chain.
     */
    private int tile_devices_count;

    public StateDeviceChain() {
        super(702);
    }

    @Override
    public void decodeBytes(byte[] data) {
        start_index = TypeUtil.littleEndianBytesToUint8(data[0]);
        tile_devices_count = TypeUtil.littleEndianBytesToUint8(data[881]);

        int tilesIndex = 0;
        for (int dataOffset = 0; dataOffset < tile_devices_count * 55; dataOffset += 55) {
            byte[] tileBytes = new byte[55];
            System.arraycopy(data, 1 + dataOffset, tileBytes, 0, 55);

            TileData tileData = new TileData();
            tileData.decodeBytes(tileBytes);

            tile_devices[tilesIndex] = tileData;
            tilesIndex++;
        }
    }
}


