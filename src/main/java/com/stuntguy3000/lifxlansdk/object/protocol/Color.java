/*
 * Copyright 2021 Luke Anderson (stuntguy3000)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.stuntguy3000.lifxlansdk.object.protocol;

import com.stuntguy3000.lifxlansdk.object.protocol.abstracts.ByteData;
import com.stuntguy3000.lifxlansdk.util.TypeUtil;
import lombok.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a LIFX color Structure (HSBK)
 * <p>
 * https://lan.developer.lifx.com/docs/representing-color-with-hsbk
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Color implements ByteData {
    public static final Color RED = Color.fromRGB(255, 0, 0);
    public static final Color GREEN = Color.fromRGB(0, 255, 0);
    public static final Color LIME = Color.fromRGB(100, 255, 0);
    public static final Color BLUE = Color.fromRGB(0, 0, 255);
    public static final Color YELLOW = Color.fromRGB(255, 210, 0);
    public static final Color ORANGE = Color.fromRGB(255, 150, 0);
    public static final Color CYAN = Color.fromRGB(0, 255, 255);
    public static final Color PINK = Color.fromRGB(255, 0, 128);
    public static final Color PURPLE = Color.fromRGB(80, 0, 205);

    public static final Color WHITE = Color.fromRGBK(255, 255, 255, 5500);
    public static final Color BLACK = Color.fromRGB(0, 0, 0);

    private int hue;
    private int saturation;
    private int brightness;
    private int kelvin;

    /**
     * Create a new Color object from RGB
     * <p>
     * Defaults to a kelvin of 5500k
     *
     * @param red   the red value (0-255)
     * @param green the green value (0-255)
     * @param blue  the blue value (0-255)
     *
     * @return the created color instance
     */
    public static Color fromRGB(int red, int green, int blue) {
        return Color.fromRGBK(red, green, blue, 5500);
    }

    /**
     * Create a new Color object from RGB
     *
     * @param red    the red value (0-255)
     * @param green  the green value (0-255)
     * @param blue   the blue value (0-255)
     * @param kelvin the kelvin value
     *
     * @return the created color instance
     */
    public static Color fromRGBK(int red, int green, int blue, int kelvin) {
        float[] hsb = java.awt.Color.RGBtoHSB(red, green, blue, null);
        return new Color((int) (hsb[0] * 65535), (int) (hsb[1] * 65535), (int) (hsb[2] * 65535), kelvin);
    }

    /**
     * Randomly choose a color from the predefined selections, and optionally exclude any colors (Can return WHITE if
     * all colors are removed)
     *
     * @param colorsToIgnore any colors to ignore from selection
     *
     * @return a randomly selected color
     */
    public static Color getRandomColor(Color... colorsToIgnore) {
        List<Color> allColors = new ArrayList<>();
        allColors.add(Color.RED);
        allColors.add(Color.GREEN);
        allColors.add(Color.BLUE);
        allColors.add(Color.CYAN);
        allColors.add(Color.PURPLE);
        allColors.add(Color.PINK);
        allColors.add(Color.ORANGE);
        allColors.add(Color.YELLOW);
        allColors.add(Color.LIME);

        // Shuffle
        Collections.shuffle(allColors);

        // Remove ignored colors
        for (Color color : colorsToIgnore) {
            allColors.remove(color);
        }

        // Pick our winner
        return (allColors.isEmpty() ? Color.WHITE : allColors.get(0));
    }

    /**
     * Converts this object to it's RGB equivalent
     *
     * @return the r g b values from this color
     */
    // http://www.java2s.com/Tutorial/Java/0261__2D-Graphics/ConvertHSBtoRGBvalue.htm
    public int[] toRGB() {
        int rgb = java.awt.Color.HSBtoRGB(hue / 65535f, saturation / 65535f, brightness / 65535f);

        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;

        return new int[]{red, green, blue};
    }

    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            byteArrayOutputStream.write(TypeUtil.uint16ToBytesLittleEndian(hue));
            byteArrayOutputStream.write(TypeUtil.uint16ToBytesLittleEndian(saturation));
            byteArrayOutputStream.write(TypeUtil.uint16ToBytesLittleEndian(brightness));
            byteArrayOutputStream.write(TypeUtil.uint16ToBytesLittleEndian(kelvin));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public void decodeBytes(byte[] data) {
        hue = TypeUtil.littleEndianBytesToUint16(data[0], data[1]);
        saturation = TypeUtil.littleEndianBytesToUint16(data[2], data[3]);
        brightness = TypeUtil.littleEndianBytesToUint16(data[4], data[5]);
        kelvin = TypeUtil.littleEndianBytesToUint16(data[6], data[7]);
    }

    @Override
    public Color clone() {
        return new Color(getHue(), getSaturation(), getBrightness(), getKelvin());
    }
}

