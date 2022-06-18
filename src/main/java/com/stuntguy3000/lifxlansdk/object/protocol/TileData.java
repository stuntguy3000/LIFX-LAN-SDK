/*
 * Copyright 2022 Luke Anderson (stuntguy3000)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.stuntguy3000.lifxlansdk.object.protocol;

import com.stuntguy3000.lifxlansdk.object.protocol.abstracts.ByteData;
import com.stuntguy3000.lifxlansdk.util.TypeUtil;
import lombok.*;

/**
 * Represents a LIFX Tile Structure
 * <p>
 * https://lan.developer.lifx.com/docs/field-types#tile
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TileData implements ByteData {
    /**
     * See Tile Orientation (https://lan.developer.lifx.com/docs/tile-control#tile-orientation)
     */
    private int accel_meas_x;
    /**
     * See Tile Orientation (https://lan.developer.lifx.com/docs/tile-control#tile-orientation)
     */
    private int accel_meas_y;
    /**
     * See Tile Orientation (https://lan.developer.lifx.com/docs/tile-control#tile-orientation)
     */
    private int accel_meas_z;
    /**
     * See Tile Positions (https://lan.developer.lifx.com/docs/tile-control#tile-positioning)
     */
    private float user_x;
    /**
     * See Tile Positions (https://lan.developer.lifx.com/docs/tile-control#tile-positioning)
     */
    private float user_y;
    /**
     * The number of zones that make up each row
     */
    private int width;
    /**
     * The number of zones that make up each column
     */
    private int height;
    /**
     * The vendor id of the device (See {@link com.stuntguy3000.lifxlansdk.messages.state.device.StateVersion} (33))
     */
    private int device_version_vendor;
    /**
     * The product id of the device (See {@link com.stuntguy3000.lifxlansdk.messages.state.device.StateVersion} (33))
     */
    private int device_version_product;
    /**
     * The epoch of the time the firmware was created (See {@link com.stuntguy3000.lifxlansdk.messages.state.device.StateHostFirmware}
     * (15))
     */
    private long firmware_build;
    /**
     * The minor component of the firmware version  (See {@link com.stuntguy3000.lifxlansdk.messages.state.device.StateHostFirmware}
     * (15))
     */
    private int firmware_version_minor;
    /**
     * The major component of the firmware version  (See {@link com.stuntguy3000.lifxlansdk.messages.state.device.StateHostFirmware}
     * (15))
     */
    private int firmware_version_major;

    @Override
    public void decodeBytes(byte[] data) {
        accel_meas_x = TypeUtil.littleEndianBytesToUint16(data[0], data[1]);
        accel_meas_y = TypeUtil.littleEndianBytesToUint16(data[2], data[3]);
        accel_meas_z = TypeUtil.littleEndianBytesToUint16(data[4], data[5]);

        user_x = TypeUtil.littleEndianBytesToFloat(data[8], data[9], data[10], data[11]);
        user_y = TypeUtil.littleEndianBytesToFloat(data[12], data[13], data[14], data[15]);

        width = TypeUtil.littleEndianBytesToUint8(data[16]);
        height = TypeUtil.littleEndianBytesToUint8(data[17]);

        device_version_vendor = TypeUtil.littleEndianBytesToUint32(data[19], data[20], data[21], data[22]);
        device_version_product = TypeUtil.littleEndianBytesToUint32(data[23], data[24], data[25], data[26]);

        firmware_build = TypeUtil.littleEndianBytesToUint64(data[31], data[32], data[33], data[34], data[35], data[36], data[37], data[38]);

        firmware_version_minor = TypeUtil.littleEndianBytesToUint16(data[47], data[48]);
        firmware_version_major = TypeUtil.littleEndianBytesToUint16(data[49], data[50]);
    }
}
