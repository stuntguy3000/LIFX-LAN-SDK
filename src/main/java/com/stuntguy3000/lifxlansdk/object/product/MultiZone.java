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
import com.stuntguy3000.lifxlansdk.messages.get.multizone.GetColorZones;
import com.stuntguy3000.lifxlansdk.messages.get.multizone.GetExtendedColorZones;
import com.stuntguy3000.lifxlansdk.messages.get.multizone.GetMultiZoneEffect;
import com.stuntguy3000.lifxlansdk.messages.set.multizone.SetColorZones;
import com.stuntguy3000.lifxlansdk.messages.set.multizone.SetExtendedColorZones;
import com.stuntguy3000.lifxlansdk.messages.set.multizone.SetMultiZoneEffect;
import com.stuntguy3000.lifxlansdk.messages.state.multizone.StateExtendedColorZones;
import com.stuntguy3000.lifxlansdk.messages.state.multizone.StateMultiZone;
import com.stuntguy3000.lifxlansdk.messages.state.multizone.StateMultiZoneEffect;
import com.stuntguy3000.lifxlansdk.messages.state.multizone.StateZone;
import com.stuntguy3000.lifxlansdk.object.protocol.Color;
import com.stuntguy3000.lifxlansdk.object.protocol.Packet;
import com.stuntguy3000.lifxlansdk.object.protocol.abstracts.Message;
import com.stuntguy3000.lifxlansdk.object.protocol.enums.Direction;
import com.stuntguy3000.lifxlansdk.object.protocol.enums.MultiZoneApplicationRequest;
import com.stuntguy3000.lifxlansdk.object.protocol.enums.MultiZoneEffectType;
import com.stuntguy3000.lifxlansdk.object.protocol.enums.MultiZoneExtendedApplicationRequest;
import com.stuntguy3000.lifxlansdk.util.TypeUtil;
import lombok.Getter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a LIFX MultiZone light
 * <p>
 * Technically speaking, Lights (bulbs) are not MultiZones (strips), but since they share common API calls, we ignore
 * that definition issue and implement the Light feature set.
 */
@Getter
public class MultiZone extends Light {
    private int zonesCount;
    private StateExtendedColorZones savedState;

    /**
     * Constructs a new MultiZone object
     *
     * @param device the generic device representing the multizone
     */
    public MultiZone(Device device) {
        super(device.getIpAddress(), device.getMacAddress(), device.getServicePort());

        // The device initialization only occurs when we have an IP address, meaning actual communication has occupied
        // and the device object reflects a real-world device and not a hypothetical one
        if (device.getIpAddress() != null) {
            init();
        }
    }

    /**
     * For static (or mostly-static) MultiZone information, save the developer some time and pre-fetch this
     * information.
     * <p>
     * This can lead to some inconsistencies if these pieces of information change, which is unlikely.
     * <p>
     * Specific fetch functions exist for these data fields if they must be up-to-date.
     */
    private void init() {
        zonesCount = getExtendedColorZones().getZones_count();
    }

    /**
     * Fetches the MultiZones's zonesCount (and updates local cache)
     * <p>
     * This information is precached by {@link MultiZone#init()}, but, it can change. If you rely on this information
     * always being correct, update the cache through this function.
     *
     * @return the amount of zones this multizone has
     */
    public int fetchZonesCount() {
        zonesCount = getExtendedColorZones().getZones_count();

        return zonesCount;
    }

    /**
     * Returns all color zones specified between two indexes
     *
     * @param start_index the index to start at (inclusive)
     * @param end_index   the index to end at (inclusive)
     *
     * @return the response packet object for this request
     */
    @Deprecated
    public List<Message> getColorZones(int start_index, int end_index) {
        List<Packet> packets = PacketHandler.sendMessage(new GetColorZones(start_index, end_index), this, true, 0);
        List<Message> messages = new ArrayList<>();

        for (Packet packet : packets) {
            Message message = packet.getMessage();

            if (message instanceof StateZone) {
                StateZone stateZone = (StateZone) message;
                messages.add(stateZone);
            } else if (message instanceof StateMultiZone) {
                StateMultiZone stateMultiZone = (StateMultiZone) message;
                messages.add(stateMultiZone);
            }
        }

        return messages;
    }

    /**
     * Gets the current multizone effect
     *
     * @return the response packet object for this request
     */
    public StateMultiZoneEffect getMultiZoneEffect() {
        List<Packet> packets = PacketHandler.sendMessage(new GetMultiZoneEffect(), this);

        return (StateMultiZoneEffect) packets.get(0).getMessage();
    }

    /**
     * Gets the current extended color zones for the multizone
     *
     * @return the response packet object for this request
     */
    public StateExtendedColorZones getExtendedColorZones() {
        List<Packet> packets = PacketHandler.sendMessage(new GetExtendedColorZones(), this);

        return (StateExtendedColorZones) packets.get(0).getMessage();
    }

    /**
     * Sets zones between two indexes to a particular color
     *
     * @param start_index the index to start at (inclusive)
     * @param end_index   the index to end at (inclusive)
     * @param color       the desired color
     * @param duration    the duration (in milliseconds) it takes to make this change
     * @param awaitReply  true to await a reply, false to just send a single packet with no acknowledgement
     */
    public void setColorZones(int start_index, int end_index, Color color, int duration, boolean awaitReply) {
        PacketHandler.sendMessage(new SetColorZones(start_index, end_index, color.getHue(), color.getSaturation(), color.getBrightness(), color.getKelvin(), duration, MultiZoneApplicationRequest.APPLY), this, awaitReply);
    }

    /**
     * Sets the zones on a multizone to each item in array (one color per zone)
     *
     * @param duration   the duration (in milliseconds) it takes to make this change
     * @param zone_index the index to start at (usually 0)
     * @param awaitReply true to await a reply, false to just send a single packet with no acknowledgement
     * @param colors     the desired colours, in order
     */
    public void setExtendedColorZones(int duration, int zone_index, boolean awaitReply, Color... colors) {
        PacketHandler.sendMessage(new SetExtendedColorZones(duration, MultiZoneExtendedApplicationRequest.APPLY, zone_index, colors.length, colors), this, awaitReply);
    }

    /**
     * Stops any active multizone effect
     *
     * @param awaitReply true to await a reply, false to just send a single packet with no acknowledgement
     */
    public void stopMultiZoneEffect(boolean awaitReply) {
        PacketHandler.sendMessage(new SetMultiZoneEffect(new Random().nextInt(), MultiZoneEffectType.OFF, 0, 0, new byte[32]), this, awaitReply);
    }

    /**
     * Run the multizone MOVE effect
     *
     * @param speed     the time it takes for one cycle of the effect in milliseconds
     * @param duration  the time the effect will run for in nanoseconds
     * @param direction the direction to move the zones in
     *
     * @return the response packet object for this request
     */
    public StateMultiZoneEffect runMultiZoneEffectMove(int speed, long duration, Direction direction) {
        // Build Parameters
        ByteArrayOutputStream parameters = new ByteArrayOutputStream(64);

        for (int i = 0; i < 4; i++) {
            parameters.write(0);
        }

        try {
            parameters.write(TypeUtil.uint32ToBytesLittleEndian(direction.getNumericValue()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 8; i++) {
            parameters.write(0);
        }

        // Send Packet
        List<Packet> packets = PacketHandler.sendMessage(new SetMultiZoneEffect(new Random().nextInt(), MultiZoneEffectType.MOVE, speed, duration, parameters.toByteArray()), this);

        return (StateMultiZoneEffect) packets.get(0).getMessage();
    }

    @Override
    public void saveState() {
        this.savedState = getExtendedColorZones();
    }

    @Override
    public boolean restoreState(int duration) {
        if (savedState != null) {
            // Disable Firmware Effect
            stopMultiZoneEffect(true);

            // Restore Lights
            setExtendedColorZones(duration, savedState.getZone_index(), true, savedState.getColors());
        }

        return false;
    }
}
