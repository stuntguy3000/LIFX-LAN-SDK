/*
 * Copyright 2022 Luke Anderson (stuntguy3000)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.stuntguy3000.lifxlansdk.messages.state.light;

import com.stuntguy3000.lifxlansdk.object.protocol.abstracts.Message;
import com.stuntguy3000.lifxlansdk.util.TypeUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * This says the current power level of the device.
 * <p>
 * This packet is the reply to the {@link com.stuntguy3000.lifxlansdk.messages.get.light.GetLightPower} (116) and {@link
 * com.stuntguy3000.lifxlansdk.messages.set.light.SetLightPower} (117) messages
 */
@Getter
@Setter
@ToString
public class StateLightPower extends Message {
    private int level;

    public StateLightPower() {
        super(118);
    }

    @Override
    public void decodeBytes(byte[] data) {
        level = TypeUtil.littleEndianBytesToUint16(data[0], data[1]);
    }
}


