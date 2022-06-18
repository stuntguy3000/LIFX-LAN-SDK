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

import com.stuntguy3000.lifxlansdk.object.protocol.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

// I reckon a few more test cases would be nice here... the whole program relies on TypeUtil to function :)
public class TypeUtilTests {
    @Test
    public void testColorToRGB() {
        assertArrayEquals(new int[]{0, 0, 0}, Color.BLACK.toRGB());
        assertArrayEquals(new int[]{255, 255, 255}, Color.WHITE.toRGB());
        assertArrayEquals(new int[]{255, 0, 0}, Color.RED.toRGB());
        assertArrayEquals(new int[]{0, 255, 0}, Color.GREEN.toRGB());
        assertArrayEquals(new int[]{0, 0, 255}, Color.BLUE.toRGB());
        assertArrayEquals(new int[]{0, 255, 255}, Color.CYAN.toRGB());
        assertArrayEquals(new int[]{255, 0, 128}, Color.PINK.toRGB());
        assertArrayEquals(new int[]{255, 150, 0}, Color.ORANGE.toRGB());
        assertArrayEquals(new int[]{255, 210, 0}, Color.YELLOW.toRGB());
    }

    @Test
    public void testColorFromRGB() {
        assertEquals(Color.BLACK.toString(), Color.fromRGB(0, 0, 0).toString());
        assertEquals(Color.WHITE.toString(), Color.fromRGB(255, 255, 255).toString());
        assertEquals(Color.RED.toString(), Color.fromRGB(255, 0, 0).toString());
        assertEquals(Color.GREEN.toString(), Color.fromRGB(0, 255, 0).toString());
        assertEquals(Color.BLUE.toString(), Color.fromRGB(0, 0, 255).toString());
    }
}
