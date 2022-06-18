/*
 * Copyright 2022 Luke Anderson (stuntguy3000)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.stuntguy3000.lifxlansdk.helper;

import com.stuntguy3000.lifxlansdk.object.product.Device;
import com.stuntguy3000.lifxlansdk.object.product.Light;
import com.stuntguy3000.lifxlansdk.object.protocol.enums.DeviceType;

import java.util.ArrayList;
import java.util.List;

public class LightHelper {

    /**
     * Attempt to find all LIFX Lights on a network.
     *
     * @return the list of discovered lights (can be empty)
     */
    public static List<Light> findLights() {
        List<Device> deviceList = DeviceHelper.findDevices();
        List<Light> lightList = new ArrayList<>();

        for (Device device : deviceList) {
            if (device.getType() == DeviceType.LIGHT) {
                lightList.add(new Light(device));
            }
        }

        return lightList;
    }

    /**
     * Attempt to find a LIFX light by it's label.
     *
     * @param label the label of the light
     *
     * @return the discovered light (or null)
     */
    public static Light getLightByLabel(String label) {
        List<Light> lightList = findLights();

        for (Light light : lightList) {
            if (light.getLabel().equalsIgnoreCase(label)) {
                return light;
            }
        }

        return null;
    }

    /**
     * Attempt to find a LIFX light by it's MAC address.
     *
     * @param macAddress the MAC address of the light
     *
     * @return the discovered light (or null)
     */
    public static Light getLightByMacAddress(String macAddress) {
        List<Light> lightList = findLights();

        for (Light light : lightList) {
            if (light.getMacAddress().equalsIgnoreCase(macAddress)) {
                return light;
            }
        }

        return null;
    }
}
