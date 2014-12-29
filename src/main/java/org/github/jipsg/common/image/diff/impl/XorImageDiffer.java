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
package org.github.jipsg.common.image.diff.impl;

import org.github.jipsg.common.image.diff.ImageDiffer;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Compare two images to check if they are identical - based on Apache PDFBox test code.
 */
public class XorImageDiffer implements ImageDiffer {

    /**
     * The number of color steps to be considered equal when diffing two RGB images
     */
    private int colorStepDifferenceThreshold;

    public XorImageDiffer() {
        this.colorStepDifferenceThreshold = 1;
    }

    @Override
    public BufferedImage diff(BufferedImage bim1, BufferedImage bim2) {
        return diffImages(bim1, bim2, colorStepDifferenceThreshold);
    }

    /**
     * Get the difference between two images, identical colors are set to white,
     * differences are xor-ed, the highest bit of each color is reset to avoid
     * colors that are too light
     *
     * @param bim1      Buffered image
     * @param bim2      Buffered image
     * @param threshold the color steps difference to ne considered identical
     * @return If the images are different, the function returns a diff image If
     * the images are identical, the function returns null If the size is
     * different, a black border on the bottom and the right is created
     */
    public BufferedImage diffImages(BufferedImage bim1, BufferedImage bim2, int threshold) {
        BufferedImage result = null;

        int minWidth = Math.min(bim1.getWidth(), bim2.getWidth());
        int minHeight = Math.min(bim1.getHeight(), bim2.getHeight());
        int maxWidth = Math.max(bim1.getWidth(), bim2.getWidth());
        int maxHeight = Math.max(bim1.getHeight(), bim2.getHeight());

        if (minWidth != maxWidth || minHeight != maxHeight) {
            result = createEmptyDiffImage(minWidth, minHeight, maxWidth, maxHeight);
        }
        for (int x = 0; x < minWidth; ++x) {
            for (int y = 0; y < minHeight; ++y) {
                int rgb1 = bim1.getRGB(x, y);
                int rgb2 = bim2.getRGB(x, y);
                if (rgb1 != rgb2
                        // don't bother about differences of 1 color step
                        && (Math.abs((rgb1 & 0xFF) - (rgb2 & 0xFF)) > threshold
                        || Math.abs(((rgb1 >> 8) & 0xFF) - ((rgb2 >> 8) & 0xFF)) > threshold
                        || Math.abs(((rgb1 >> 16) & 0xFF) - ((rgb2 >> 16) & 0xFF)) > threshold)) {
                    if (result == null) {
                        result = createEmptyDiffImage(minWidth, minHeight, maxWidth, maxHeight);
                    }
                    int r = Math.abs((rgb1 & 0xFF) - (rgb2 & 0xFF));
                    int g = Math.abs((rgb1 & 0xFF00) - (rgb2 & 0xFF00));
                    int b = Math.abs((rgb1 & 0xFF0000) - (rgb2 & 0xFF0000));
                    result.setRGB(x, y, 0xFFFFFF - (r | g | b));
                } else {
                    if (result != null) {
                        result.setRGB(x, y, Color.WHITE.getRGB());
                    }
                }
            }
        }

        if (result == null) {
            result = createEmpty(minWidth, minHeight);
        }

        return result;
    }

    /**
     * Create an image; the part between the smaller and the larger image is
     * painted black, the rest in white
     *
     * @param minWidth  width of the smaller image
     * @param minHeight width of the smaller image
     * @param maxWidth  height of the larger image
     * @param maxHeight height of the larger image
     */
    private BufferedImage createEmptyDiffImage(int minWidth, int minHeight, int maxWidth, int maxHeight) {
        BufferedImage bim3 = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bim3.getGraphics();
        if (minWidth != maxWidth || minHeight != maxHeight) {
            graphics.setColor(Color.BLACK);
            graphics.fillRect(0, 0, maxWidth, maxHeight);
        }
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, minWidth, minHeight);
        graphics.dispose();
        return bim3;
    }

    /**
     * Create an empty RGB image in white.
     */
    private BufferedImage createEmpty(int width, int height) {
        BufferedImage bim3 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bim3.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);
        graphics.dispose();
        return bim3;
    }
}
