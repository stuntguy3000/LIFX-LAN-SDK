/*
 * Copyright 2021 Luke Anderson (stuntguy3000)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.stuntguy3000.lifxlansdk.object.product;

import com.stuntguy3000.lifxlansdk.handler.PacketHandler;
import com.stuntguy3000.lifxlansdk.messages.get.relay.GetRPower;
import com.stuntguy3000.lifxlansdk.messages.set.relay.SetRPower;
import com.stuntguy3000.lifxlansdk.messages.state.relay.StateRPower;
import com.stuntguy3000.lifxlansdk.object.protocol.Packet;

import java.util.List;

/**
 * Represents a LIFX Relay device
 */
public class Relay extends Device {
    /**
     * Constructs a new Relay object
     *
     * @param device the generic device representing the relay
     */
    public Relay(Device device) {
        super(device.getIpAddress(), device.getMacAddress(), device.getServicePort());
    }

    /**
     * Returns the power level of a relay item at a particular index
     *
     * @param relay_index the specified object's index
     *
     * @return the response packet object for this request
     */
    public StateRPower getRPower(int relay_index) {
        List<Packet> packets = PacketHandler.sendMessage(new GetRPower(relay_index), this);

        return (StateRPower) packets.get(0).getMessage();
    }

    /**
     * Sets the relay power of an item at a particular index
     *
     * @param relay_index the specified object's index
     * @param powered     true if the relay should be powered
     */
    public void setRPower(int relay_index, boolean powered) {
        PacketHandler.sendMessage(new SetRPower(relay_index, powered ? 65535 : 0), this, false);
    }
}
