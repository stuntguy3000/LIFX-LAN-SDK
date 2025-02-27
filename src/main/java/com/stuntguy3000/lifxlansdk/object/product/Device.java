/*
 * Copyright 2022 Luke Anderson (stuntguy3000)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.stuntguy3000.lifxlansdk.object.product;

import com.stuntguy3000.lifxlansdk.handler.PacketHandler;
import com.stuntguy3000.lifxlansdk.messages.get.device.*;
import com.stuntguy3000.lifxlansdk.messages.set.device.*;
import com.stuntguy3000.lifxlansdk.messages.state.device.*;
import com.stuntguy3000.lifxlansdk.object.protocol.Packet;
import com.stuntguy3000.lifxlansdk.object.protocol.abstracts.StateSavable;
import com.stuntguy3000.lifxlansdk.object.protocol.enums.DeviceType;
import lombok.Getter;

import java.net.InetAddress;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Represents a LIFX Device
 * <p>
 * LIFX devices are more than lights, they can include relay switches.
 * <p>
 * Are you looking for {@link Light}?
 */
@Getter
public class Device implements StateSavable {
    // Network Information
    private final InetAddress ipAddress;
    private final String macAddress;
    private final int servicePort;

    // Device Information
    private StateLabel stateLabel;
    private StateLocation stateLocation;
    private StateGroup stateGroup;
    private StateVersion stateVersion;

    private DeviceType type;

    /**
     * Construct a new Device
     *
     * @param ipAddress   the IP address of the device (can be null if unknown)
     * @param macAddress  the MAC address of the device
     * @param servicePort the port to communicate on
     */
    public Device(InetAddress ipAddress, String macAddress, int servicePort) {
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.servicePort = servicePort;

        // The device initialization only occurs when we have an IP address, meaning actual communication has occupied
        // and the device object reflects a real-world device and not a hypothetical one
        if (ipAddress != null) {
            init();
        }
    }

    /**
     * For static (or mostly-static) device information, save the developer some time and pre-fetch this information.
     * <p>
     * This can lead to some inconsistencies if these pieces of information change, which is unlikely.
     * <p>
     * Specific fetch functions exist for these data fields if they must be up-to-date.
     */
    private void init() {
        // Get Label
        List<Packet> packetsLabel = PacketHandler.sendMessage(new GetLabel(), this);
        stateLabel = (StateLabel) packetsLabel.get(0).getMessage();

        // Get Location
        List<Packet> packetsLocation = PacketHandler.sendMessage(new GetLocation(), this);
        stateLocation = (StateLocation) packetsLocation.get(0).getMessage();

        // Get Group
        List<Packet> packetsGroup = PacketHandler.sendMessage(new GetGroup(), this);
        stateGroup = (StateGroup) packetsGroup.get(0).getMessage();

        // Get Device Version
        List<Packet> packetsType = PacketHandler.sendMessage(new GetVersion(), this);
        stateVersion = (StateVersion) packetsType.get(0).getMessage();

        // Set Device Type
        //  https://lan.developer.lifx.com/docs/product-registry
        switch (stateVersion.getProduct()) {
            case 70:
            case 71:
            case 89:
			case 115:
			case 116: {
                type = DeviceType.RELAY;
                break;
            }
            case 31:
            case 32:
            case 38:
			case 118:
			case 119: 
			case 120:
			case 141:
			case 142:
			case 143:
			case 144:
			case 161:
			case 162:
			case 203:
			case 204:
			case 205:
			case 206:
			case 213:
			case 214: {
                type = DeviceType.MULTIZONE;
                break;
            }
            case 55:
            case 57:
            case 68:
			case 137:
			case 138:
			case 171:
			case 173:
			case 174:
			case 176:
			case 177:
			case 185:
			case 186:
			case 201:
			case 202:
			case 215:
			case 216:
			case 217:
			case 218: {
                type = DeviceType.MATRIX;
                break;
            }
            default: {
                type = DeviceType.LIGHT;
                break;
            }
        }
    }

    /**
     * Fetches the device's label (and updates local cache)
     * <p>
     * This information is precached by {@link Device#init()}, but, it can change. If you rely on this information
     * always being correct, update the cache through this function.
     *
     * @return the response packet object for this request
     */
    public StateLabel fetchLabel() {
        List<Packet> packetsLabel = PacketHandler.sendMessage(new GetLabel(), this);
        stateLabel = (StateLabel) packetsLabel.get(0).getMessage();

        return stateLabel;
    }

    /**
     * Fetches the device's location (and updates local cache)
     * <p>
     * This information is precached by {@link Device#init()}, but, it can change. If you rely on this information
     * always being correct, update the cache through this function.
     *
     * @return the response packet object for this request
     */
    public StateLocation fetchLocation() {
        List<Packet> packetsLocation = PacketHandler.sendMessage(new GetLocation(), this);
        stateLocation = (StateLocation) packetsLocation.get(0).getMessage();

        return stateLocation;
    }

    /**
     * Fetches the device's group (and updates local cache)
     * <p>
     * This information is precached by {@link Device#init()}, but, it can change. If you rely on this information
     * always being correct, update the cache through this function.
     *
     * @return the response packet object for this request
     */
    public StateGroup fetchGroup() {
        List<Packet> packetsGroup = PacketHandler.sendMessage(new GetGroup(), this);
        stateGroup = (StateGroup) packetsGroup.get(0).getMessage();

        return stateGroup;
    }

    /**
     * A special helper function to streamline accessing the cached label of this device
     *
     * @return the label of the device
     */
    public String getLabel() {
        return stateLabel.getLabel();
    }

    /**
     * Get Host Firmware
     * <p>
     * See returned object for more information.
     *
     * @return the response packet object for this request
     */
    public StateHostFirmware getHostFirmware() {
        List<Packet> packets = PacketHandler.sendMessage(new GetHostFirmware(), this);

        return (StateHostFirmware) packets.get(0).getMessage();
    }

    /**
     * Get WiFi Info
     * <p>
     * See returned object for more information.
     *
     * @return the response packet object for this request
     */
    public StateWifiInfo getWifiInfo() {
        List<Packet> packets = PacketHandler.sendMessage(new GetWifiInfo(), this);

        return (StateWifiInfo) packets.get(0).getMessage();
    }

    /**
     * Get WiFi Firmware
     * <p>
     * See returned object for more information.
     *
     * @return the response packet object for this request
     */
    public StateWifiFirmware getWifiFirmware() {
        List<Packet> packets = PacketHandler.sendMessage(new GetWifiFirmware(), this);

        return (StateWifiFirmware) packets.get(0).getMessage();
    }

    /**
     * Get device power
     * <p>
     * See returned object for more information.
     *
     * @return the response packet object for this request
     */
    private StatePower getPower() {
        List<Packet> packets = PacketHandler.sendMessage(new GetPower(), this);

        return (StatePower) packets.get(0).getMessage();
    }

    /**
     * Set the power state of the device
     *
     * @param powered true if the device is powered
     */
    public void setPower(boolean powered) {
        PacketHandler.sendMessage(new SetPower(powered ? 65535 : 0), this);
    }

    /**
     * Returns if the device is powered
     * <p>
     * Shortcut-function from {@link #getPower()}
     *
     * @return true if the device is powered
     */
    public boolean isPowered() {
        return getPower().getLevel() > 0;
    }

    /**
     * Get device information
     * <p>
     * See returned object for more information.
     *
     * @return the response packet object for this request
     */
    public StateInfo getInfo() {
        List<Packet> packets = PacketHandler.sendMessage(new GetInfo(), this);

        return (StateInfo) packets.get(0).getMessage();
    }

    /**
     * Echo request
     *
     * @param message the message to be echoed by the device
     *
     * @return the response packet object for this request
     */
    public EchoResponse echoRequest(String message) {
        List<Packet> packets = PacketHandler.sendMessage(new EchoRequest(message), this);

        return (EchoResponse) packets.get(0).getMessage();
    }

    /**
     * Set the label of the device
     *
     * @param label the desired label
     *
     * @return the response packet object for this request
     */
    public StateLabel setLabel(String label) {
        List<Packet> packets = PacketHandler.sendMessage(new SetLabel(label), this);

        return (StateLabel) packets.get(0).getMessage();
    }

    /**
     * Reboot the device
     */
    public void reboot() {
        PacketHandler.sendMessage(new SetReboot(), this);
    }

    /**
     * Set the location of the device
     *
     * @param location the UUID id of the location
     * @param label    the label of the location
     *
     * @return the response packet object for this request
     */
    public StateLocation setLocation(UUID location, String label) {
        Clock clock = Clock.systemDefaultZone();
        Instant instant = clock.instant();

        List<Packet> packets = PacketHandler.sendMessage(new SetLocation(location, label, instant.getNano()), this);

        return (StateLocation) packets.get(0).getMessage();
    }

    /**
     * Set the group of the device
     *
     * @param group the UUID id of the group
     * @param label the label of the group
     *
     * @return the response packet object for this request
     */
    public StateGroup setGroup(UUID group, String label) {
        Clock clock = Clock.systemDefaultZone();
        Instant instant = clock.instant();

        List<Packet> packets = PacketHandler.sendMessage(new SetGroup(group, label, instant.getNano()), this);

        return (StateGroup) packets.get(0).getMessage();
    }
}
