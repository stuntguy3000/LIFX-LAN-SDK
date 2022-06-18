/*
 * Copyright 2022 Luke Anderson (stuntguy3000)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.stuntguy3000.lifxlansdk.messages.state.discovery;

import com.stuntguy3000.lifxlansdk.object.protocol.abstracts.Message;
import com.stuntguy3000.lifxlansdk.object.protocol.enums.Service;
import com.stuntguy3000.lifxlansdk.util.TypeUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * This packet is used to tell you what services are available and the port each service is on.
 * <p>
 * This packet is the reply to the {@link com.stuntguy3000.lifxlansdk.messages.get.discovery.GetService} (2) message
 */
@Getter
@Setter
@ToString
public class StateService extends Message {
    private Service service;
    /**
     * The port of the service. This value is usually 56700 but you should not assume this is always the case.
     */
    private int port;

    public StateService() {
        super(3);
    }

    @Override
    public void decodeBytes(byte[] data) {
        service = Service.getByValue(TypeUtil.littleEndianBytesToUint8(data[0]));

        port = TypeUtil.littleEndianBytesToUint32(data[1], data[2], data[3], data[4]);
    }

    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            byteArrayOutputStream.write(TypeUtil.uint8ToBytesLittleEndian(service.getNumericValue()));
            byteArrayOutputStream.write(TypeUtil.uint32ToBytesLittleEndian(port));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        //return byteArrayOutputStream.toByteArray();
        return new byte[0];
    }
}
