/*
 * Copyright 2022 Luke Anderson (stuntguy3000)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.stuntguy3000.lifxlansdk.messages.state.device;

import com.stuntguy3000.lifxlansdk.object.protocol.abstracts.Message;
import com.stuntguy3000.lifxlansdk.util.TypeUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

/**
 * This packet provides the details of the group set on the device.
 * <p>
 * To determine the label of a group you need to send a {@link com.stuntguy3000.lifxlansdk.messages.get.device.GetGroup}
 * (51) to all the devices you can find and for each group uuid determine which label is accompanied by the latest
 * updated_at value.
 * <p>
 * This packet is the reply to the {@link com.stuntguy3000.lifxlansdk.messages.get.device.GetGroup} (51) and {@link
 * com.stuntguy3000.lifxlansdk.messages.set.device.SetGroup} (52) messages
 */
@Getter
@Setter
@ToString
public class StateGroup extends Message {
    /**
     * The unique identifier of this group as a uuid.
     */
    private UUID group;
    /**
     * The name assigned to this group
     */
    private String label;
    /**
     * An epoch in nanoseconds of when this group was set on the device
     */
    private long updated_at;

    public StateGroup() {
        super(53);
    }

    @Override
    public void decodeBytes(byte[] data) {
        byte[] labelBytes = new byte[32];
        System.arraycopy(data, 16, labelBytes, 0, 32);
        label = TypeUtil.bytesToString(labelBytes);

        byte[] groupBytes = new byte[16];
        System.arraycopy(data, 0, groupBytes, 0, 16);
        group = TypeUtil.uuidBytesToUUIDObject(groupBytes);

        updated_at = TypeUtil.littleEndianBytesToUint64(data[48], data[49], data[50], data[51], data[52], data[53], data[54], data[55]);
    }
}


