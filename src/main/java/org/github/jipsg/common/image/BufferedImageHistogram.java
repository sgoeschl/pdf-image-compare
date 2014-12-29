/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.github.jipsg.common.image;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Creates a RGB value histogram for an image.
 */
public class BufferedImageHistogram {

    private final int SIZE = 256;
    private int nrOfPixels;
    private int[] red = new int[SIZE];
    private int[] green = new int[SIZE];
    private int[] blue = new int[SIZE];

    public BufferedImageHistogram(BufferedImage bufferedImage) {

        assert bufferedImage != null : "No bufferedImage provided";

        for (int i = 0; i < red.length; i++) red[i] = 0;
        for (int i = 0; i < green.length; i++) green[i] = 0;
        for (int i = 0; i < blue.length; i++) blue[i] = 0;

        for (int i = 0; i < bufferedImage.getWidth(); i++) {
            for (int j = 0; j < bufferedImage.getHeight(); j++) {

                int redIndex = new Color(bufferedImage.getRGB(i, j)).getRed();
                int greenIndex = new Color(bufferedImage.getRGB(i, j)).getGreen();
                int blueIndex = new Color(bufferedImage.getRGB(i, j)).getBlue();

                red[redIndex]++;
                green[greenIndex]++;
                blue[blueIndex]++;
            }
        }

        nrOfPixels = bufferedImage.getHeight() * bufferedImage.getWidth();
    }

    public int[] getRed() {
        return red;
    }

    public int[] getGreen() {
        return green;
    }

    public int[] getBlue() {
        return blue;
    }

    public int getNrOfPixels() {
        return nrOfPixels;
    }

    public boolean isBlack(int threshold) {
        int redLowValues = 0;
        int greenLowValues = 0;
        int blueLowValues = 0;

        for (int i = 0; i < threshold + 1; i++) {
            redLowValues += getRed()[i];
            greenLowValues += getGreen()[i];
            blueLowValues += getBlue()[i];
        }

        return redLowValues == getNrOfPixels() && greenLowValues == getNrOfPixels() && blueLowValues == getNrOfPixels();
    }

    public boolean isWhite(int threshold) {
        int redHighValues = 0;
        int greenHighValues = 0;
        int blueHighValues = 0;

        for (int i = SIZE - threshold - 1; i < SIZE; i++) {
            redHighValues += getRed()[i];
            greenHighValues += getGreen()[i];
            blueHighValues += getBlue()[i];
        }

        return redHighValues == getNrOfPixels() && greenHighValues == getNrOfPixels() && blueHighValues == getNrOfPixels();
    }
}
