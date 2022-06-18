/*
 * Copyright 2022 Luke Anderson (stuntguy3000)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.stuntguy3000.lifxlansdk.messages.get.device;

import com.stuntguy3000.lifxlansdk.object.protocol.abstracts.Message;

/**
 * Our first and second generation devices have separate wifi firmware to the host firmware. For these devices, this
 * message will return the version of the wifi firmware on the device. This message can be used to determine the version
 * of this firmware.
 * <p>
 * Will return one {@link com.stuntguy3000.lifxlansdk.messages.state.device.StateWifiFirmware} (19) message
 */
public class GetWifiFirmware extends Message {
    public GetWifiFirmware() {
        super(18);
    }
}
