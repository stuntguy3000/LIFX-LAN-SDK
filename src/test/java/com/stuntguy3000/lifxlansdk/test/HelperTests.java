/*
 * Copyright 2022 Luke Anderson (stuntguy3000)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.stuntguy3000.lifxlansdk.test;

import com.stuntguy3000.lifxlansdk.handler.PacketHandler;
import com.stuntguy3000.lifxlansdk.helper.DeviceHelper;
import com.stuntguy3000.lifxlansdk.helper.LightHelper;
import com.stuntguy3000.lifxlansdk.helper.MultiZoneHelper;
import com.stuntguy3000.lifxlansdk.helper.TileHelper;
import com.stuntguy3000.lifxlansdk.messages.state.device.EchoResponse;
import com.stuntguy3000.lifxlansdk.messages.state.device.StateGroup;
import com.stuntguy3000.lifxlansdk.messages.state.device.StateLabel;
import com.stuntguy3000.lifxlansdk.messages.state.device.StateLocation;
import com.stuntguy3000.lifxlansdk.messages.state.multizone.StateMultiZone;
import com.stuntguy3000.lifxlansdk.messages.state.multizone.StateZone;
import com.stuntguy3000.lifxlansdk.object.product.Device;
import com.stuntguy3000.lifxlansdk.object.product.Light;
import com.stuntguy3000.lifxlansdk.object.product.MultiZone;
import com.stuntguy3000.lifxlansdk.object.product.Tile;
import com.stuntguy3000.lifxlansdk.object.protocol.Color;
import com.stuntguy3000.lifxlansdk.object.protocol.abstracts.Message;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class HelperTests {
    @BeforeAll
    public static void setup() {
        try {
            PacketHandler.setBroadcastAddress(InetAddress.getByName("192.168.1.255"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFindDevices() {
        List<Device> deviceList = DeviceHelper.findDevices();

        for (Device device : deviceList) {
            System.out.println(device.getStateLabel());
            System.out.println(" " + device.getMacAddress());
            System.out.println(" " + device.getIpAddress());

            System.out.println(" " + device.getStateVersion());
            System.out.println(" " + device.isPowered());
            System.out.println(" " + device.getHostFirmware());
            System.out.println(" " + device.getWifiFirmware());
            System.out.println(" " + device.getWifiInfo());
            System.out.println(" " + device.getInfo());
            System.out.println(" " + device.getStateGroup());
            System.out.println(" " + device.getStateLocation());

            System.out.println("---");
        }

        assertTrue(deviceList.size() > 0);
    }

    //@Test
    public void testFindLights() {
        List<Light> lightList = LightHelper.findLights();

        for (Light light : lightList) {
            System.out.println(light.getLabel());
            System.out.println(light.getColor());
            System.out.println(light.getLightPower());

            System.out.println("---");
        }

        assertTrue(lightList.size() > 0);
    }

    @Test
    public void testFindMultizone() {
        List<MultiZone> multiZoneList = MultiZoneHelper.findMultiZones();

        for (MultiZone multiZone : multiZoneList) {
            System.out.println(multiZone.getLabel());

            List<Message> stateZoneList = multiZone.getColorZones(0, 0);
            List<Message> stateMultiZoneList = multiZone.getColorZones(0, 255);

            assertTrue(stateZoneList.size() > 0);
            assertTrue(stateMultiZoneList.size() > 0);

            for (Message message : stateZoneList) {
                assertTrue(message instanceof StateZone);
                StateZone stateZone = (StateZone) message;
                System.out.println(stateZone);
            }

            for (Message message : stateMultiZoneList) {
                assertTrue(message instanceof StateMultiZone);
                StateMultiZone stateMultiZone = (StateMultiZone) message;
                System.out.println(stateMultiZone);
            }

            System.out.println(multiZone.getMultiZoneEffect());
            System.out.println(multiZone.getExtendedColorZones());

            System.out.println("---");
        }

        assertTrue(multiZoneList.size() > 0);
    }

    /*@Test
    public void testFindTile() {
        List<Tile> tileList = TileHelper.findTiles();

        for (Tile tile : tileList) {
            System.out.println(tile.getLabel());

            System.out.println(tile.getDeviceChain());
            System.out.println(tile.get64(0, 3, 0, 0, 5));
            System.out.println(tile.getTileEffect());

            System.out.println("---");
        }

        assertTrue(tileList.size() > 0);
    }*/

    @Test
    public void testDeviceSet() {
        Device device = DeviceHelper.getDeviceByLabel("Doors");

        // Label
        StateLabel stateLabel = device.getStateLabel();
        StateLabel stateLabelReturn = device.setLabel("SDK Test");

        assertEquals("SDK Test", stateLabelReturn.getLabel());
        device.setLabel(stateLabel.getLabel());

        // Location
        UUID randomLocationID = UUID.randomUUID();

        StateLocation stateLocation = device.getStateLocation();
        StateLocation stateLocationReturn = device.setLocation(randomLocationID, "SDK Test Location");

        assertEquals(randomLocationID, stateLocationReturn.getLocation());
        assertEquals("SDK Test Location", stateLocationReturn.getLabel());

        device.setLocation(stateLocation.getLocation(), stateLocation.getLabel());

        // Group
        UUID randomGroupID = UUID.randomUUID();

        StateGroup stateGroup = device.getStateGroup();
        StateGroup stateGroupReturn = device.setGroup(randomGroupID, "SDK Test Group");

        assertEquals(randomGroupID, stateGroupReturn.getGroup());
        assertEquals("SDK Test Group", stateGroupReturn.getLabel());

        device.setGroup(stateGroup.getGroup(), stateGroup.getLabel());

        // Echo Request
        EchoResponse echoResponse = device.echoRequest("This is an echo test.");
        assertEquals("This is an echo test.", echoResponse.getEchoing());

        // Device Power Level
        boolean statePower = device.isPowered();

        device.setPower(true);

        // Some Delay is required
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boolean statePowerReturn = device.isPowered();
        assertTrue(statePowerReturn);

        device.setPower(false);

        // Some Delay is required
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        statePowerReturn = device.isPowered();
        assertFalse(statePowerReturn);

        // Some Delay is required
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        device.setPower(statePower);
    }

    @Test
    public void testLightSet() {
        List<Light> lights = LightHelper.findLights();

        try {
            Thread.sleep(2000);

            for (Light light : lights) {
                System.out.println(light.getLabel() + " - Light color Test");
                light.setLightPower(65535, 1000, true);

                light.setColor(Color.RED, 0, true);
                Thread.sleep(1000);
                light.setColor(Color.CYAN, 0, true);
                Thread.sleep(1000);
                light.setColor(Color.BLUE, 0, true);
                Thread.sleep(1000);
                light.setColor(Color.PINK, 0, true);
                Thread.sleep(1000);
                light.setColor(Color.PURPLE, 0, true);
                Thread.sleep(1000);
                light.setColor(Color.GREEN, 0, true);
                Thread.sleep(1000);
                light.setColor(Color.YELLOW, 0, true);
                Thread.sleep(1000);
                light.setColor(Color.ORANGE, 0, true);
                Thread.sleep(1000);

                light.setLightPower(0, 1000, true);
                Thread.sleep(1000);

                light.setInfrared(true, true);
                Thread.sleep(1000);
                light.setInfrared(false, true);
                Thread.sleep(1000);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMultizoneSet() {
        MultiZone multiZone = MultiZoneHelper.getMultiZoneByLabel("Doors");

        try {
            Thread.sleep(2000);

            System.out.println("Doors (" + multiZone.getIpAddress().toString() + ") - Light color Test");
            multiZone.setLightPower(65535, 1000, true);

            // Setup
            Color[] colors = new Color[8];
            colors[0] = Color.RED;
            colors[1] = Color.PURPLE;
            colors[2] = Color.BLUE;
            colors[3] = Color.CYAN;
            colors[4] = Color.GREEN;
            colors[5] = Color.ORANGE;
            colors[6] = Color.YELLOW;
            colors[7] = Color.WHITE;

            // Basic Full colors
            for (int loop = 0; loop < 3; loop++) {
                for (Color color : colors) {
                    multiZone.setColor(color, 0, true);
                    Thread.sleep(1000);
                }
            }

            // Zones Test
            Color[] zonecolors = new Color[multiZone.getZonesCount()];
            int colorIndex = 0;
            int colorIndexOffset = 0;

            for (int loop = 0; loop < 3; loop++) {
                for (int zoneIndex = 0; zoneIndex < multiZone.getZonesCount(); zoneIndex++) {
                    zonecolors[zoneIndex] = colors[(colorIndex + colorIndexOffset) > 7 ? (colorIndex + colorIndexOffset) - 7 : colorIndex + colorIndexOffset];

                    // Set Next color
                    colorIndex += 1;

                    if (colorIndex > 4) {
                        colorIndex = 0;
                    }
                }

                // Make it rain
                multiZone.setExtendedColorZones(1000, 0, true, zonecolors);
                Thread.sleep(1000);

                colorIndexOffset += 1;
                if (colorIndexOffset > 4) {
                    colorIndexOffset = 0;
                }
            }

            multiZone.setLightPower(0, 1000, true);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTileEffects() {
        List<Tile> tileList = TileHelper.findTiles();

        try {
            for (Tile tile : tileList) {
                tile.setPower(true);
                Thread.sleep(1000);

                tile.runTileEffectFlame(1000, 10000000000L);
                Thread.sleep(5000);
                tile.stopTileEffect();
                Thread.sleep(2000);
                tile.runTileEffectMorph(1000, 10000000000L, Color.RED, Color.BLUE, Color.GREEN);
                Thread.sleep(5000);
                tile.stopTileEffect();
                Thread.sleep(2000);

                tile.setPower(false);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
