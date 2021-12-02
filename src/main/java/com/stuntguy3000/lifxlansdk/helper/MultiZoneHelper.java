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
import com.stuntguy3000.lifxlansdk.object.product.MultiZone;
import com.stuntguy3000.lifxlansdk.object.protocol.enums.DeviceType;

import java.util.ArrayList;
import java.util.List;

public class MultiZoneHelper {

    /**
     * Attempt to find all LIFX MultiZone lights on a network.
     *
     * @return the list of discovered MultiZone (can be empty)
     */
    public static List<MultiZone> findMultiZones() {
        List<Device> deviceList = DeviceHelper.findDevices();
        List<MultiZone> multiZoneList = new ArrayList<>();

        for (Device device : deviceList) {
            if (device.getType() == DeviceType.MULTIZONE) {
                multiZoneList.add(new MultiZone(device));
            }
        }

        return multiZoneList;
    }

    /**
     * Attempt to find a LIFX MultiZone by it's label.
     *
     * @param label the label of the MultiZone
     *
     * @return the discovered MultiZone (or null)
     */
    public static MultiZone getMultiZoneByLabel(String label) {
        List<MultiZone> multiZoneList = findMultiZones();

        for (MultiZone multiZone : multiZoneList) {
            if (multiZone.getLabel().equalsIgnoreCase(label)) {
                return multiZone;
            }
        }

        return null;
    }

    /**
     * Attempt to find a LIFX MultiZone by it's MAC address.
     *
     * @param macAddress the MAC address of the MultiZone
     *
     * @return the discovered MultiZone (or null)
     */
    public static MultiZone getMultiZoneByMacAddress(String macAddress) {
        List<MultiZone> multiZoneList = findMultiZones();

        for (MultiZone multiZone : multiZoneList) {
            if (multiZone.getMacAddress().equalsIgnoreCase(macAddress)) {
                return multiZone;
            }
        }

        return null;
    }
}
