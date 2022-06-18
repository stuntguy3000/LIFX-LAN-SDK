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
import com.stuntguy3000.lifxlansdk.messages.get.tile.Get64;
import com.stuntguy3000.lifxlansdk.messages.get.tile.GetDeviceChain;
import com.stuntguy3000.lifxlansdk.messages.get.tile.GetTileEffect;
import com.stuntguy3000.lifxlansdk.messages.set.tile.Set64;
import com.stuntguy3000.lifxlansdk.messages.set.tile.SetTileEffect;
import com.stuntguy3000.lifxlansdk.messages.set.tile.SetUserPosition;
import com.stuntguy3000.lifxlansdk.messages.state.tile.State64;
import com.stuntguy3000.lifxlansdk.messages.state.tile.StateDeviceChain;
import com.stuntguy3000.lifxlansdk.messages.state.tile.StateTileEffect;
import com.stuntguy3000.lifxlansdk.object.protocol.Color;
import com.stuntguy3000.lifxlansdk.object.protocol.Packet;
import com.stuntguy3000.lifxlansdk.object.protocol.abstracts.Message;
import com.stuntguy3000.lifxlansdk.object.protocol.enums.TileEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a LIFX Tile (or Candle - aka, matrix) light
 * <p>
 * Technically speaking, Lights (bulbs) are not Tiles, but since they share common API calls, we ignore that definition
 * issue and implement the Light feature set.
 */
public class Tile extends Light {
    /**
     * Constructs a new Tile object
     *
     * @param device the generic device representing the tile
     */
    public Tile(Device device) {
        super(device.getIpAddress(), device.getMacAddress(), device.getServicePort());
    }

    /**
     * Get device chain information
     *
     * @return the response packet object for this request
     */
    public StateDeviceChain getDeviceChain() {
        List<Packet> packets = PacketHandler.sendMessage(new GetDeviceChain(), this);

        return (StateDeviceChain) packets.get(0).getMessage();
    }

    /**
     * Get colour values for the zones of devices in the chain+
     *
     * @param tile_index the first item in the chain you want zones
     * @param length     the number of tiles after tile_index you want colour values from
     * @param x          the x value to start from, likely 0
     * @param y          the y value to start from, likely 0
     * @param width      the width of each item in the chain (8 fort the tile, 5 for the candle)
     *
     * @return the response packet object for this request
     */
    public List<State64> get64(int tile_index, int length, int x, int y, int width) {
        List<Packet> packets = PacketHandler.sendMessage(new Get64(tile_index, length, x, y, width), this, true, length);
        List<State64> messages = new ArrayList<>();

        for (Packet packet : packets) {
            Message message = packet.getMessage();

            if (message instanceof State64) {
                State64 state64 = (State64) message;
                messages.add(state64);
            }
        }

        return messages;
    }

    /**
     * Get the tile's current effect
     *
     * @return the response packet object for this request
     */
    public StateTileEffect getTileEffect() {
        List<Packet> packets = PacketHandler.sendMessage(new GetTileEffect(), this);

        return (StateTileEffect) packets.get(0).getMessage();
    }

    /**
     * Set the user's position
     *
     * @param tile_index the tile to change
     * @param user_x     the user's x position
     * @param user_y     the user's y position
     */
    public void setUserPosition(int tile_index, float user_x, float user_y) {
        PacketHandler.sendMessage(new SetUserPosition(tile_index, user_x, user_y), this, false);
    }

    /**
     * Set the colour zones for the tile
     *
     * @param tile_index the starting tile to change
     * @param length     the number of devices in the chain to change starting from the index
     * @param x          the x co-ordinate to start applying colors from
     * @param y          the y co-ordinate to start applying colors from
     * @param width      the width of the square you're applying colors to - this should be 8 for the LIFX Tile and 5
     *                   for the LIFX Candle
     * @param duration   the time it will take to transition to new state in milliseconds.
     * @param colors     the colours to apply to each zone
     */
    public void set64(int tile_index, int length, int x, int y, int width, int duration, Color... colors) {
        PacketHandler.sendMessage(new Set64(tile_index, length, x, y, width, duration, colors), this, false);
    }

    /**
     * Set the tile effect
     *
     * @param tileEffectType the type of effect
     * @param speed          the time it takes for one cycle of the effect in milliseconds
     * @param duration       the time the effect will run for in nanoseconds
     * @param palette        the colours in the effect
     *
     * @return the response packet object for this request
     */
    public StateTileEffect setTileEffect(TileEffectType tileEffectType, int speed, long duration, Color... palette) {
        List<Packet> packets = PacketHandler.sendMessage(new SetTileEffect(new Random().nextInt(), tileEffectType, speed, duration, new byte[32], palette.length, palette), this);

        return (StateTileEffect) packets.get(0).getMessage();
    }

    /**
     * Stops any active tile effect
     */
    public void stopTileEffect() {
        PacketHandler.sendMessage(new SetTileEffect(new Random().nextInt(), TileEffectType.OFF, 0, 0, new byte[32], 0, new Color[16]), this);
    }

    /**
     * Run the tile FLAME effect
     *
     * @param speed    the time it takes for one cycle of the effect in milliseconds
     * @param duration the time the effect will run for in nanoseconds
     *
     * @return the response packet object for this request
     */
    public StateTileEffect runTileEffectFlame(int speed, long duration) {
        return setTileEffect(TileEffectType.FLAME, speed, duration, new Color[16]);
    }

    /**
     * Run the tile MORPH effect
     *
     * @param speed    the time it takes for one cycle of the effect in milliseconds
     * @param duration the time the effect will run for in nanoseconds
     * @param color    the colours to morph through
     *
     * @return the response packet object for this request
     */
    public StateTileEffect runTileEffectMorph(int speed, long duration, Color... color) {
        return setTileEffect(TileEffectType.MORPH, speed, duration, color);
    }
}
