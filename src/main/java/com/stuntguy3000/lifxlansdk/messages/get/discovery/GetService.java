/*
 * Copyright 2022 Luke Anderson (stuntguy3000)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.stuntguy3000.lifxlansdk.messages.get.discovery;

import com.stuntguy3000.lifxlansdk.object.protocol.abstracts.Message;

/**
 * This packet is used for Discovery of devices. Typically you would broadcast this message to the network (with tagged
 * field in the header set to 0 and the target field in the header set to all zeros)
 * <p>
 * Each device on the network that receives this packet will send back multiple {@link
 * com.stuntguy3000.lifxlansdk.messages.state.discovery.StateService} (3) messages that say what services are available
 * and the port those services are on.
 * <p>
 * The only {@link com.stuntguy3000.lifxlansdk.messages.state.discovery.StateService} (3) message you care about will
 * tell you that UDP is available on a port that is usually 56700. You can determine the IP address of the device from
 * information your UDP socket should receive when it gets those bytes.
 */
public class GetService extends Message {
    public GetService() {
        super(2, true);
    }
}
