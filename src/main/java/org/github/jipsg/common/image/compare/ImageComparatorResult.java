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
package org.github.jipsg.common.image.compare;

import java.awt.image.BufferedImage;

/**
 * Contains the result of comparing two images.
 */
public class ImageComparatorResult {

    /**
     * Are the compared images identical?
     */
    private boolean isIdentical;

    /**
     * The generated diff between the two images for visual inspection
     */
    private BufferedImage bufferedImage;

    public ImageComparatorResult(boolean isIdentical, BufferedImage bufferedImage) {
        this.isIdentical = isIdentical;
        this.bufferedImage = bufferedImage;
    }

    public boolean isIdentical() {
        return isIdentical;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ImageDifferResult{");
        sb.append(", isIdentical=").append(isIdentical);
        sb.append(", bufferedImage=").append(bufferedImage);
        sb.append('}');
        return sb.toString();
    }
}
