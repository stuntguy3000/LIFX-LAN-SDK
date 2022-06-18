/*
 * Copyright 2022 Luke Anderson (stuntguy3000)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.stuntguy3000.lifxlansdk.messages.state.multizone;

import com.stuntguy3000.lifxlansdk.object.protocol.abstracts.Message;
import com.stuntguy3000.lifxlansdk.object.protocol.enums.MultiZoneEffectType;
import com.stuntguy3000.lifxlansdk.util.TypeUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * This packet tells us what Firmware Effect is current running on the device.
 * <p>
 * This packet requires the device has the Linear Zones capability. You may use {@link
 * com.stuntguy3000.lifxlansdk.messages.get.device.GetVersion} (32), {@link com.stuntguy3000.lifxlansdk.messages.get.device.GetHostFirmware}
 * (14) and the Product Registry to determine whether your device has this capability
 * <p>
 * This packet is the reply to the {@link com.stuntguy3000.lifxlansdk.messages.get.multizone.GetMultiZoneEffect} (507)
 * and {@link com.stuntguy3000.lifxlansdk.messages.set.multizone.SetMultiZoneEffect} (508) messages
 */
@Getter
@Setter
@ToString
public class StateMultiZoneEffect extends Message {
    /**
     * The unique value identifying this effect
     */
    private int instanceid;
    private MultiZoneEffectType effectType;
    /**
     * The time it takes for one cycle of the effect in milliseconds
     */
    private int speed;
    /**
     * The amount of time left in the current effect in nanoseconds
     */
    private long duration;
    /**
     * The parameters that was used in the request.
     */
    private byte[] parameters = new byte[32];

    public StateMultiZoneEffect() {
        super(509);
    }

    @Override
    public void decodeBytes(byte[] data) {
        instanceid = TypeUtil.littleEndianBytesToUint32(data[0], data[1], data[2], data[3]);
        effectType = MultiZoneEffectType.getByValue(TypeUtil.littleEndianBytesToUint8(data[4]));
        speed = TypeUtil.littleEndianBytesToUint32(data[7], data[8], data[9], data[10]);
        duration = TypeUtil.littleEndianBytesToUint64(data[11], data[12], data[13], data[14], data[15], data[16], data[17], data[18]);

        System.arraycopy(data, 27, parameters, 0, 32);
    }
}


