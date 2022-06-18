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

import com.stuntguy3000.lifxlansdk.handler.PacketHandler;
import com.stuntguy3000.lifxlansdk.messages.get.discovery.GetService;
import com.stuntguy3000.lifxlansdk.messages.state.discovery.StateService;
import com.stuntguy3000.lifxlansdk.object.product.Device;
import com.stuntguy3000.lifxlansdk.object.protocol.Packet;
import com.stuntguy3000.lifxlansdk.object.protocol.abstracts.Message;

import java.util.ArrayList;
import java.util.List;

public class DeviceHelper {
    /**
     * Attempt to find all LIFX Devices on a network.
     * <p>
     * Please note: LIFX Devices include non-lights.
     * <p>
     * To target lights, see {@link LightHelper}
     *
     * @return the list of discovered devices (can be empty)
     */
    public static List<Device> findDevices() {
        List<Packet> packetList = PacketHandler.sendMessage(new GetService(), null, true, 0);
        List<Device> deviceList = new ArrayList<>();

        for (Packet packet : packetList) {
            Message message = packet.getMessage();

            if (message instanceof StateService) {
                StateService service = (StateService) message;

                Device device = new Device(packet.getIpAddress(), packet.getFrameAddress().getTarget(), service.getPort());

                // Are we already tracking it?
                // (Duplicates can happen)
                boolean isTracked = false;
                for (Device trackedDevice : deviceList) {
                    if (trackedDevice.getIpAddress().equals(packet.getIpAddress())) {
                        isTracked = true;
                        break;
                    }
                }

                if (!isTracked) {
                    deviceList.add(device);
                }
            }
        }

        return deviceList;
    }

    /**
     * Attempt to find a LIFX Device by it's label (name).
     * <p>
     * Please note: LIFX Devices include non-lights.
     * <p>
     * To target lights, see {@link LightHelper}
     *
     * @param label the associated label
     *
     * @return the discovered device (or null)
     */
    public static Device getDeviceByLabel(String label) {
        List<Device> deviceList = findDevices();

        for (Device device : deviceList) {
            if (device.getLabel().equalsIgnoreCase(label)) {
                return device;
            }
        }

        return null;
    }

    /**
     * Attempt to find a LIFX Device by it's MAC address.
     * <p>
     * Please note: LIFX Devices include non-lights.
     * <p>
     * To target lights, see {@link LightHelper}
     *
     * @param macAddress the associated MAC address.
     *
     * @return the discovered device (or null)
     */
    public static Device getDeviceByMacAddress(String macAddress) {
        List<Device> deviceList = findDevices();

        for (Device device : deviceList) {
            if (device.getMacAddress().equalsIgnoreCase(macAddress)) {
                return device;
            }
        }

        return null;
    }
}
