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
import com.stuntguy3000.lifxlansdk.object.product.Tile;
import com.stuntguy3000.lifxlansdk.object.protocol.enums.DeviceType;

import java.util.ArrayList;
import java.util.List;

public class TileHelper {

    /**
     * Attempt to find all LIFX Tile lights on a network.
     *
     * @return the list of discovered Tile (can be empty)
     */
    public static List<Tile> findTiles() {
        List<Device> deviceList = DeviceHelper.findDevices();
        List<Tile> tileList = new ArrayList<>();

        for (Device device : deviceList) {
            if (device.getType() == DeviceType.MATRIX) {
                tileList.add(new Tile(device));
            }
        }

        return tileList;
    }

    /**
     * Attempt to find a LIFX Tile by it's label.
     *
     * @param label the label of the Tile
     *
     * @return the discovered Tile (or null)
     */
    public static Tile getTileByLabel(String label) {
        List<Tile> tileList = findTiles();

        for (Tile tile : tileList) {
            if (tile.getLabel().equalsIgnoreCase(label)) {
                return tile;
            }
        }

        return null;
    }

    /**
     * Attempt to find a LIFX Tile by it's MAC address.
     *
     * @param macAddress the MAC address of the Tile
     *
     * @return the discovered Tile (or null)
     */
    public static Tile getTileByMacAddress(String macAddress) {
        List<Tile> tileList = findTiles();

        for (Tile tile : tileList) {
            if (tile.getMacAddress().equalsIgnoreCase(macAddress)) {
                return tile;
            }
        }

        return null;
    }
}
