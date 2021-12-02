/*
 * Copyright 2021 Luke Anderson (stuntguy3000)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.stuntguy3000.lifxlansdk.helper;

import com.stuntguy3000.lifxlansdk.object.product.Device;
import com.stuntguy3000.lifxlansdk.object.product.Relay;
import com.stuntguy3000.lifxlansdk.object.protocol.enums.DeviceType;

import java.util.ArrayList;
import java.util.List;

public class RelayHelper {

    /**
     * Attempt to find all LIFX Relay devices on a network.
     *
     * @return the list of discovered Relay (can be empty)
     */
    public static List<Relay> findRelay() {
        List<Device> deviceList = DeviceHelper.findDevices();
        List<Relay> relayList = new ArrayList<>();

        for (Device device : deviceList) {
            if (device.getType() == DeviceType.RELAY) {
                relayList.add(new Relay(device));
            }
        }

        return relayList;
    }

    /**
     * Attempt to find a LIFX Relay by it's label.
     *
     * @param label the label of the Relay
     *
     * @return the discovered Relay (or null)
     */
    public static Relay getRelayByLabel(String label) {
        List<Relay> relayList = findRelay();

        for (Relay relay : relayList) {
            if (relay.getLabel().equalsIgnoreCase(label)) {
                return relay;
            }
        }

        return null;
    }

    /**
     * Attempt to find a LIFX Relay by it's MAC address.
     *
     * @param macAddress the MAC address of the Relay
     *
     * @return the discovered Relay (or null)
     */
    public static Relay getRelayByMacAddress(String macAddress) {
        List<Relay> relayList = findRelay();

        for (Relay relay : relayList) {
            if (relay.getMacAddress().equalsIgnoreCase(macAddress)) {
                return relay;
            }
        }

        return null;
    }
}
