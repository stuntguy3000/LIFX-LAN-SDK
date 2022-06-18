/*
 * Copyright 2022 Luke Anderson (stuntguy3000)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.stuntguy3000.lifxlansdk.messages.set.device;

import com.stuntguy3000.lifxlansdk.object.protocol.abstracts.Message;
import com.stuntguy3000.lifxlansdk.util.TypeUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * This packet lets you set the group information on the device.
 * <p>
 * Will return one StateGroup (53) message
 */
public class SetGroup extends Message {
    private final UUID group;
    private final String label;
    private final long updated_at;

    public SetGroup(UUID group, String label, long updated_at) {
        super(52);

        this.group = group;
        this.label = label;
        this.updated_at = updated_at;
    }

    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            byteArrayOutputStream.write(TypeUtil.uuidObjectToUUIDBytes(group));

            byteArrayOutputStream.write(TypeUtil.stringToBytesWithPadding(label, 32));
            byteArrayOutputStream.write(TypeUtil.uint64ToBytesLittleEndian(updated_at));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }
}
