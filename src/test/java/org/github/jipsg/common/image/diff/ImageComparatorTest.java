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
package org.github.jipsg.common.image.diff;

import org.github.jipsg.common.image.BufferedImageFactory;
import org.github.jipsg.common.image.BufferedImageUtils;
import org.github.jipsg.common.image.diff.impl.XorImageDiffer;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;

public class ImageComparatorTest {

    private File outDir = new File("./target/out/imagediff");
    private XorImageDiffer imageDiffer = new XorImageDiffer();

    @Test
    public void testIdenticalImage() throws Exception {
        final BufferedImage bufferedImage1 = BufferedImageFactory.create("./src/test/documents/png/open-office-01.pdf.300.0.png");
        final BufferedImage bufferedImage2 = BufferedImageFactory.create("./src/test/documents/png/open-office-01.pdf.300.0.png");
        final BufferedImage bufferedImage = imageDiffer.diff(bufferedImage1, bufferedImage2);
        BufferedImageUtils.writeBufferedImage(bufferedImage, "png", new File(outDir, "testIdenticalImage.png"));
    }

    @Test
    public void testSimilarImage() throws Exception {
        final BufferedImage bufferedImage1 = BufferedImageFactory.create("./src/test/documents/png/open-office-02.pdf.300.0.png");
        final BufferedImage bufferedImage2 = BufferedImageFactory.create("./src/test/documents/png/open-office-02-similar.pdf.300.0.png");
        final BufferedImage bufferedImage = imageDiffer.diff(bufferedImage1, bufferedImage2);
        BufferedImageUtils.writeBufferedImage(bufferedImage, "png", new File(outDir, "testSimilarImage.png"));
    }

    @Test
    public void testDifferentImage() throws Exception {
        final BufferedImage bufferedImage1 = BufferedImageFactory.create("./src/test/documents/png/open-office-01.pdf.300.0.png");
        final BufferedImage bufferedImage2 = BufferedImageFactory.create("./src/test/documents/png/open-office-02.pdf.300.0.png");
        final BufferedImage bufferedImage = imageDiffer.diff(bufferedImage1, bufferedImage2);
        BufferedImageUtils.writeBufferedImage(bufferedImage, "png", new File(outDir, "testDifferentImage.png"));
    }
}
