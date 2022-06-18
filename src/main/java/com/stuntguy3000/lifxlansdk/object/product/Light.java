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
import com.stuntguy3000.lifxlansdk.messages.get.light.*;
import com.stuntguy3000.lifxlansdk.messages.set.light.SetColor;
import com.stuntguy3000.lifxlansdk.messages.set.light.SetInfrared;
import com.stuntguy3000.lifxlansdk.messages.set.light.SetLightPower;
import com.stuntguy3000.lifxlansdk.messages.state.light.*;
import com.stuntguy3000.lifxlansdk.object.protocol.Color;
import com.stuntguy3000.lifxlansdk.object.protocol.Packet;

import java.net.InetAddress;
import java.util.List;

/**
 * Represents a LIFX Light
 */
public class Light extends Device {

    /**
     * Stores any saved state of the light for later restoration
     */
    private LightState savedState;

    /**
     * Construct a new light
     *
     * @param ipAddress   the IP address of the light
     * @param macAddress  the MAC address of the light
     * @param servicePort the UDP service port to communicate on
     */
    public Light(InetAddress ipAddress, String macAddress, int servicePort) {
        super(ipAddress, macAddress, servicePort);
    }

    /**
     * Constructs a new Light object
     *
     * @param device the generic device representing the light
     */
    public Light(Device device) {
        super(device.getIpAddress(), device.getMacAddress(), device.getServicePort());
    }

    /**
     * Gets the current color of the light
     *
     * @return the response packet object for this request
     */
    public LightState getColor() {
        List<Packet> packets = PacketHandler.sendMessage(new GetColor(), this);

        return (LightState) packets.get(0).getMessage();
    }

    /**
     * Gets the current HEV cycle of the light
     *
     * @return the response packet object for this request
     */
    public StateHevCycle getHevCycle() {
        List<Packet> packets = PacketHandler.sendMessage(new GetHevCycle(), this);

        return (StateHevCycle) packets.get(0).getMessage();
    }

    /**
     * Gets the HEV cycle configuration for the light
     *
     * @return the response packet object for this request
     */
    public StateHevCycleConfiguration getHevCycleConfiguration() {
        List<Packet> packets = PacketHandler.sendMessage(new GetHevCycleConfiguration(), this);

        return (StateHevCycleConfiguration) packets.get(0).getMessage();
    }

    /**
     * Gets the infrared status for the light
     *
     * @return the response packet object for this request
     */
    public StateInfrared getInfrared() {
        List<Packet> packets = PacketHandler.sendMessage(new GetInfrared(), this);

        return (StateInfrared) packets.get(0).getMessage();
    }

    /**
     * Gets the last HEV cycle's result for the light
     *
     * @return the response packet object for this request
     */
    public StateLastHevCycleResult getLastHevCycleResult() {
        List<Packet> packets = PacketHandler.sendMessage(new GetLastHevCycleResult(), this);

        return (StateLastHevCycleResult) packets.get(0).getMessage();
    }

    /**
     * Gets the current power level of the light
     *
     * @return the response packet object for this request
     */
    public StateLightPower getLightPower() {
        List<Packet> packets = PacketHandler.sendMessage(new GetLightPower(), this);

        return (StateLightPower) packets.get(0).getMessage();
    }

    /**
     * Set the color of the light
     *
     * @param color      the desired color
     * @param duration   the duration (in milliseconds) it takes to make this change
     * @param awaitReply true to await a reply, false to just send a single packet with no acknowledgement
     */
    public void setColor(Color color, int duration, boolean awaitReply) {
        PacketHandler.sendMessage(new SetColor(color.getHue(), color.getSaturation(), color.getBrightness(), color.getKelvin(), duration), this, awaitReply);
    }

    /**
     * Set the light's power level (on or off)
     *
     * @param level      the power level between 0 and 65535
     * @param duration   the duration (in milliseconds) it takes to make this change
     * @param awaitReply true to await a reply, false to just send a single packet with no acknowledgement
     */
    public void setLightPower(int level, int duration, boolean awaitReply) {
        PacketHandler.sendMessage(new SetLightPower(level, duration), this, awaitReply);
    }

    /**
     * Set the infrared brightness for the light
     *
     * @param powered    true for powered, false for off
     * @param awaitReply true to await a reply, false to just send a single packet with no acknowledgement
     */
    public void setInfrared(boolean powered, boolean awaitReply) {
        PacketHandler.sendMessage(new SetInfrared(powered ? 65535 : 0), this, awaitReply);
    }

    @Override
    public void saveState() {
        this.savedState = getColor();
    }

    @Override
    public boolean restoreState(int duration) {
        if (savedState != null) {
            setColor(new Color(savedState.getHue(), savedState.getSaturation(), savedState.getBrightness(), savedState.getKelvin()), duration, true);
            setLightPower(savedState.getPower(), duration, true);
        }

        return false;
    }
}
