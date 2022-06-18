/*
 * Copyright 2022 Luke Anderson (stuntguy3000)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.stuntguy3000.lifxlansdk.handler;

import com.stuntguy3000.lifxlansdk.messages.state.core.Acknowledgement;
import com.stuntguy3000.lifxlansdk.messages.state.device.*;
import com.stuntguy3000.lifxlansdk.messages.state.discovery.StateService;
import com.stuntguy3000.lifxlansdk.messages.state.light.*;
import com.stuntguy3000.lifxlansdk.messages.state.multizone.StateExtendedColorZones;
import com.stuntguy3000.lifxlansdk.messages.state.multizone.StateMultiZone;
import com.stuntguy3000.lifxlansdk.messages.state.multizone.StateMultiZoneEffect;
import com.stuntguy3000.lifxlansdk.messages.state.multizone.StateZone;
import com.stuntguy3000.lifxlansdk.messages.state.relay.StateRPower;
import com.stuntguy3000.lifxlansdk.messages.state.tile.State64;
import com.stuntguy3000.lifxlansdk.messages.state.tile.StateDeviceChain;
import com.stuntguy3000.lifxlansdk.messages.state.tile.StateTileEffect;
import com.stuntguy3000.lifxlansdk.object.product.Device;
import com.stuntguy3000.lifxlansdk.object.protocol.FrameAddress;
import com.stuntguy3000.lifxlansdk.object.protocol.FrameHeader;
import com.stuntguy3000.lifxlansdk.object.protocol.Packet;
import com.stuntguy3000.lifxlansdk.object.protocol.ProtocolHeader;
import com.stuntguy3000.lifxlansdk.object.protocol.abstracts.Message;
import com.stuntguy3000.lifxlansdk.util.TypeUtil;
import lombok.Setter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * A handler for all Packet related functions
 */
public class PacketHandler {
    /**
     * A map of packet ID and it's associated Message class for packet object response mapping
     */
    private static final HashMap<Integer, Class<? extends Message>> responseMessagesMap = new HashMap<>();
    /**
     * Packet sequence number
     * <p>
     * For usage, see {@link PacketHandler#getNextPacketSequenceNumber()}
     */
    private static short sequence = 0;
    /**
     * The broadcast address to send packets on.
     */
    @Setter
    private static InetAddress broadcastAddress = null;

    /**
     * Initializes the responseMessagesMap object
     */
    private static void init() {
        if (responseMessagesMap.isEmpty()) {
            //      Core
            responseMessagesMap.put(new Acknowledgement().getType(), Acknowledgement.class);

            //      Device
            responseMessagesMap.put(new EchoResponse("").getType(), EchoResponse.class);
            responseMessagesMap.put(new StateGroup().getType(), StateGroup.class);
            responseMessagesMap.put(new StateHostFirmware().getType(), StateHostFirmware.class);
            responseMessagesMap.put(new StateInfo().getType(), StateInfo.class);
            responseMessagesMap.put(new StateLabel().getType(), StateLabel.class);
            responseMessagesMap.put(new StateLocation().getType(), StateLocation.class);
            responseMessagesMap.put(new StatePower().getType(), StatePower.class);
            responseMessagesMap.put(new StateVersion().getType(), StateVersion.class);
            responseMessagesMap.put(new StateWifiFirmware().getType(), StateWifiFirmware.class);
            responseMessagesMap.put(new StateWifiInfo().getType(), StateWifiInfo.class);
            responseMessagesMap.put(new StateUnhandled().getType(), StateUnhandled.class);

            //      Discovery
            responseMessagesMap.put(new StateService().getType(), StateService.class);

            //      Light
            responseMessagesMap.put(new LightState().getType(), LightState.class);
            responseMessagesMap.put(new StateHevCycle().getType(), StateHevCycle.class);
            responseMessagesMap.put(new StateHevCycleConfiguration().getType(), StateHevCycleConfiguration.class);
            responseMessagesMap.put(new StateInfrared().getType(), StateInfrared.class);
            responseMessagesMap.put(new StateLastHevCycleResult().getType(), StateLastHevCycleResult.class);
            responseMessagesMap.put(new StateLightPower().getType(), StateLightPower.class);

            //      Multizone
            responseMessagesMap.put(new StateMultiZone().getType(), StateMultiZone.class);
            responseMessagesMap.put(new StateZone().getType(), StateZone.class);
            responseMessagesMap.put(new StateMultiZoneEffect().getType(), StateMultiZoneEffect.class);
            responseMessagesMap.put(new StateExtendedColorZones().getType(), StateExtendedColorZones.class);

            //      Relay
            responseMessagesMap.put(new StateRPower().getType(), StateRPower.class);

            //      Tile
            responseMessagesMap.put(new State64().getType(), State64.class);
            responseMessagesMap.put(new StateDeviceChain().getType(), StateDeviceChain.class);
            responseMessagesMap.put(new StateTileEffect().getType(), StateTileEffect.class);
        }
    }

    /**
     * Send a Message to a device
     * <p>
     * This function piggybacks off other functions to use default values for: - resultRequired - maxReceiveMessageCount
     * - timeout - retry
     *
     * @param message the message to send
     * @param device  the device to send it to
     *
     * @return a list of returned packets (usually 1), can be empty
     */
    public static List<Packet> sendMessage(Message message, Device device) {
        return sendMessage(message, device, true);
    }

    /**
     * Send a Message to a device
     * <p>
     * This function piggybacks off other functions to use default values for: - maxReceiveMessageCount - timeout -
     * retry
     *
     * @param message        the message to send
     * @param device         the device to send it to
     * @param resultRequired true if a result is required (return packet)
     *
     * @return a list of returned packets (usually 1), can be empty
     */
    public static List<Packet> sendMessage(Message message, Device device, boolean resultRequired) {
        return sendMessage(message, device, resultRequired, 1);
    }

    /**
     * Send a Message to a device
     * <p>
     * This function piggybacks off other functions to use default values for: - timeout - retry
     *
     * @param message                the message to send
     * @param device                 the device to send it to
     * @param resultRequired         true if a result is required (return packet)
     * @param maxReceiveMessageCount the amount of messages to receive before returning all packets (used for
     *                               optimization)
     *
     * @return a list of returned packets (usually 1), can be empty
     */
    public static List<Packet> sendMessage(Message message, Device device, boolean resultRequired, int maxReceiveMessageCount) {
        return sendMessage(message, device, resultRequired, maxReceiveMessageCount, 250);
    }

    /**
     * Send a Message to a device
     * <p>
     * This function piggybacks off other functions to use default values for: - retry
     *
     * @param message                the message to send
     * @param device                 the device to send it to
     * @param resultRequired         true if a result is required (return packet)
     * @param maxReceiveMessageCount the amount of messages to receive before returning all packets (used for
     *                               optimization)
     * @param timeout                the maximum wait time for replies (in ms)
     *
     * @return a list of returned packets (usually 1), can be empty
     */
    public static List<Packet> sendMessage(Message message, Device device, boolean resultRequired, int maxReceiveMessageCount, int timeout) {
        return sendMessage(message, device, resultRequired, maxReceiveMessageCount, timeout, 10);
    }

    /**
     * Send a Message to a device
     *
     * @param message                the message to send
     * @param device                 the device to send it to
     * @param resultRequired         true if a result is required (return packet)
     * @param maxReceiveMessageCount the amount of messages to receive before returning all packets (used for
     *                               optimization)
     * @param timeout                the maximum wait time for replies (in ms)
     * @param retry                  the amount of retries if socket the socket timeout is hit
     *
     * @return a list of returned packets (usually 1), can be empty
     */
    public static List<Packet> sendMessage(Message message, Device device, boolean resultRequired, int maxReceiveMessageCount, int timeout, int retry) {
        // Init
        init();

        // Build Packet
        Packet packet = buildPacket(message, device, resultRequired);
        List<Packet> returnedPackets = new ArrayList<>();

        while (retry > 0) {
            try {
                // Send Packet
                InetAddress targetAddress;
                int targetPort = 56700;

                if (device == null) {
                    // Broadcast Packet
                    targetAddress = broadcastAddress;
                } else {
                    // Targeted Packet
                    targetAddress = device.getIpAddress();
                    targetPort = device.getServicePort();
                }

                byte[] sendData = packet.toBytes();

                DatagramSocket socket = new DatagramSocket();
                DatagramPacket datagramPacket = new DatagramPacket(sendData, sendData.length, targetAddress, targetPort);

                socket.setReuseAddress(true);

                if (!resultRequired) {
                    socket.send(datagramPacket);
                    return returnedPackets;
                }

                socket.setSoTimeout(timeout);
                socket.send(datagramPacket);

                // Process Replies
                // Keep going until we time out
                while (true) {
                    // Wait to receive
                    byte[] receivedData = new byte[1024]; // LIFX Packets can get big! Biggest seen is stateDeviceChain at 918 bytes.
                    DatagramPacket receivedDatagramPacket = new DatagramPacket(receivedData, receivedData.length);
                    socket.receive(receivedDatagramPacket);

                    // Process Result
                    Packet receivedPacket = buildPacket(receivedData);

                    // Does it have a payload?
                    if (receivedPacket != null && receivedPacket.getMessage() != null) {
                        receivedPacket.setIpAddress(receivedDatagramPacket.getAddress());

                        // Is it unique?
                        //  This over-complex function compares a few things to try to figure this out
                        //
                        //  Packet delivery assurance techniques can mean duplicates can happen, but some API calls
                        //  return multiple packets per request, so, we try to accommodate that whilst comparing payload
                        //  information.
                        boolean unique = true;
                        for (Packet returnedPacket : returnedPackets) {
                            if (receivedPacket.getIpAddress().equals(returnedPacket.getIpAddress())) {
                                if (receivedPacket.getProtocolHeader().getType() == returnedPacket.getProtocolHeader().getType()) {
                                    if (returnedPacket.getFrameAddress().getSequence() == sequence) {
                                        byte[] receivedPacketMessageBytes = receivedPacket.getMessage().toBytes();
                                        byte[] returnedPacketMessageBytes = returnedPacket.getMessage().toBytes();

                                        // Only some packets are cared about, those have defined toBytes, if not, they will be zero and can be ignored
                                        if (receivedPacketMessageBytes.length > 0 && returnedPacketMessageBytes.length > 0) {
                                            if (Arrays.equals(receivedPacketMessageBytes, returnedPacketMessageBytes)) {
                                                unique = false;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (unique) {
                            returnedPackets.add(receivedPacket);

                            // Have we received enough packets?
                            --maxReceiveMessageCount;
                            if (maxReceiveMessageCount == 0) {
                                socket.close();
                                return returnedPackets;
                            }
                        }
                    }
                }
            } catch (SocketTimeoutException socketTimeoutException) {
                --retry;
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        // Return Received Messages
        return returnedPackets;
    }

    /**
     * Builds a LIFX Packet from received data
     *
     * @param receivedData the received data
     *
     * @return the constructed packet (or null if invalid)
     */
    private static Packet buildPacket(byte[] receivedData) {
        int packetSize = TypeUtil.littleEndianBytesToUint16(receivedData[0], receivedData[1]);
        if (packetSize < 36) {
            return null;
        }

        try {
            // Setup Packet Headers
            FrameHeader frameHeader = new FrameHeader();
            FrameAddress frameAddress = new FrameAddress();
            ProtocolHeader protocolHeader = new ProtocolHeader();

            byte[] frameHeaderBytes = new byte[8];
            byte[] frameAddressBytes = new byte[16];
            byte[] protocolHeaderBytes = new byte[12];

            System.arraycopy(receivedData, 0, frameHeaderBytes, 0, 8);
            System.arraycopy(receivedData, 8, frameAddressBytes, 0, 16);
            System.arraycopy(receivedData, 24, protocolHeaderBytes, 0, 12);

            frameHeader.decodeBytes(frameHeaderBytes);
            frameAddress.decodeBytes(frameAddressBytes);
            protocolHeader.decodeBytes(protocolHeaderBytes);

            // Process Message
            byte[] messageBytes = new byte[packetSize - 36];
            System.arraycopy(receivedData, 36, messageBytes, 0, messageBytes.length);
            Message message = buildMessage(protocolHeader.getType(), messageBytes);

            // Return Packet
            return new Packet(frameHeader, frameAddress, protocolHeader, message);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }

    /**
     * Internal packet builder for packets to be sent, not received. For received packet processing, see the other
     * buildPacket function
     *
     * @param message        the message
     * @param device         the targeted device, can be null to represent a broadcast
     * @param resultRequired if a result is required (only useful for controlling Set requests)
     *
     * @return the constructed packet (or null)
     */
    private static Packet buildPacket(Message message, Device device, boolean resultRequired) {
        FrameHeader frameHeader = new FrameHeader();
        FrameAddress frameAddress = new FrameAddress();
        ProtocolHeader protocolHeader = new ProtocolHeader();

        // Set the headers up
        protocolHeader.setType(message.getType());
        frameAddress.setSequence(getNextPacketSequenceNumber());

        // Is this a broadcast?
        if (message.isBroadcast()) {
            frameHeader.setTagged(true);
            frameAddress.setTarget("00:00:00:00:00:00");
        } else {
            frameHeader.setTagged(false);
            frameAddress.setTarget(device.getMacAddress());
        }

        // Set if we need a result returned, usually true
        frameAddress.setRes_required(resultRequired);

        return new Packet(frameHeader, frameAddress, protocolHeader, message);
    }

    /**
     * Builds a LIFX Message
     *
     * @param messageType  the type of message (packet ID)
     * @param messageBytes the payload data of the message
     *
     * @return the constructed message (or null)
     */
    private static Message buildMessage(int messageType, byte[] messageBytes) {
        Class<? extends Message> messageClazz = responseMessagesMap.get(messageType);

        // Sanity check
        if (messageClazz == null) {
            return null;
        }

        // And now, reflection
        try {
            Message message = messageClazz.newInstance();
            message.decodeBytes(messageBytes);

            return message;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Increment the packet sequence in a 0-255 loop.
     *
     * @return short the next packet sequence number
     */
    private static short getNextPacketSequenceNumber() {
        ++sequence;

        if (sequence > 255) {
            sequence = 0;
        }

        return sequence;
    }
}
