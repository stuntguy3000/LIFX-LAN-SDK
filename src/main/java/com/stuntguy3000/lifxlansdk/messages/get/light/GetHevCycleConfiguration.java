/*
 * Copyright 2022 Luke Anderson (stuntguy3000)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.stuntguy3000.lifxlansdk.messages.get.light;

import com.stuntguy3000.lifxlansdk.object.protocol.abstracts.Message;

/**
 * This packet is used to determine the default configuration for using the HEV LEDs on the device.
 * <p>
 * Will return one {@link com.stuntguy3000.lifxlansdk.messages.state.light.StateHevCycleConfiguration} (147) message
 * <p>
 * This packet requires the device has the hev capability. You may use {@link com.stuntguy3000.lifxlansdk.messages.get.device.GetVersion}
 * (32), {@link com.stuntguy3000.lifxlansdk.messages.get.device.GetHostFirmware} (14) and the Product Registry to
 * determine whether your device has this capability
 */
public class GetHevCycleConfiguration extends Message {
    public GetHevCycleConfiguration() {
        super(142);
    }
}
