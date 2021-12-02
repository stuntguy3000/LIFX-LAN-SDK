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

import com.stuntguy3000.lifxlansdk.object.protocol.abstracts.Message;
import com.stuntguy3000.lifxlansdk.object.protocol.enums.MultiZoneEffectType;
import com.stuntguy3000.lifxlansdk.util.TypeUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Start a multizone Firmware Effect on the device.
 * <p>
 * Will return one {@link com.stuntguy3000.lifxlansdk.messages.state.multizone.StateMultiZoneEffect} (509) message
 * <p>
 * This packet requires the device has the Linear Zones capability. You may use {@link
 * com.stuntguy3000.lifxlansdk.messages.get.device.GetVersion} (32), {@link com.stuntguy3000.lifxlansdk.messages.get.device.GetHostFirmware}
 * (14) and the Product Registry to determine whether your device has this capability
 */
public class SetMultiZoneEffect extends Message {

    private final int instanceid;
    private final MultiZoneEffectType type;

    private final int speed;
    private final long duration;

    private final byte[] parameters;

    public SetMultiZoneEffect(int instanceid, MultiZoneEffectType type, int speed, long duration, byte[] parameters) {
        super(508);

        this.instanceid = instanceid;
        this.type = type;
        this.speed = speed;
        this.duration = duration;
        this.parameters = parameters;
    }

    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            byteArrayOutputStream.write(TypeUtil.uint32ToBytesLittleEndian(instanceid));
            byteArrayOutputStream.write(TypeUtil.uint8ToBytesLittleEndian(type.getNumericValue()));

            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write(0);

            byteArrayOutputStream.write(TypeUtil.uint32ToBytesLittleEndian(speed));
            byteArrayOutputStream.write(TypeUtil.uint64ToBytesLittleEndian(duration));

            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write(0);

            byteArrayOutputStream.write(parameters);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }
}
