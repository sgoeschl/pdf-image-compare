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
package org.github.jipsg.pdfbox;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to work with Apache PDFBox.
 */
public class PdfToImageConverter {
    private final int DPI_72 = 72;

    /**
     * Generates preview images of the given PDF document.
     *
     * @param source      the opaque source of the PDF
     * @param imageFormat the requested image format, e.g. "jpeg"
     * @param startPage   the first extracted page
     * @param endPage     the las extracted page
     * @param resolution  the resolution of the extracted images
     * @param color       the color model, e.g. "rgb"
     * @return list of images
     * @throws Exception the operation failed
     */
    public List<BufferedImage> toImages(Object source, String imageFormat, int startPage, int endPage, int resolution, String color) throws Exception {
        PDDocument pdDocument = new PDDocumentFactory().create(source);

        try {
            return toImages(pdDocument, imageFormat, startPage, endPage, resolution, color);
        } finally {
            if (pdDocument != null) {
                pdDocument.close();
            }
        }
    }

    /**
     * Split a PDF document into images.
     *
     * @param pdDocument  the source document
     * @param imageFormat the requested image format, e.g. "jpeg"
     * @param startPage   the first extracted page
     * @param endPage     the las extracted page
     * @param resolution  the resolution of the extracted images
     * @param color       the color model, e.g. "rgb", "gray"
     * @return a list of images
     * @throws Exception the conversion failed
     */
    @SuppressWarnings("unchecked")
    public List<BufferedImage> toImages(PDDocument pdDocument, String imageFormat, int startPage, int endPage, int resolution, String color) throws Exception {
        /**
         Validate.notNull(pdDocument, "pdDocument is null");
         Validate.notEmpty(imageFormat, "imageFormat is null");
         Validate.isTrue(startPage > 0, "invalid start page : " + startPage);
         Validate.isTrue(endPage >= startPage, "invalid end page : " + endPage);
         Validate.isTrue(resolution >= 0, "invalid resolution : " + resolution);
         */

        List<BufferedImage> result = new ArrayList<BufferedImage>();

        int imageType = getImageType(color);
        List<PDPage> pages = pdDocument.getDocumentCatalog().getAllPages();
        int pagesSize = pages.size();

        for (int i = startPage - 1; i < endPage && i < pagesSize; i++) {
            PDPage page = pages.get(i);
            PDRectangle cropBox = page.findCropBox();
            int currResolution = calculateResolution(resolution, cropBox.getWidth(), cropBox.getHeight());
            BufferedImage image = page.convertToImage(imageType, currResolution);
            result.add(image);
        }

        return result;
    }

    private int getImageType(String color) {
        int result;
        String currColor = (color != null && color.length() > 0 ? color : "rgb");

        if ("bilevel".equalsIgnoreCase(currColor)) {
            result = BufferedImage.TYPE_BYTE_BINARY;
        } else if ("indexed".equalsIgnoreCase(currColor)) {
            result = BufferedImage.TYPE_BYTE_INDEXED;
        } else if ("gray".equalsIgnoreCase(currColor)) {
            result = BufferedImage.TYPE_BYTE_GRAY;
        } else if ("rgb".equalsIgnoreCase(currColor)) {
            result = BufferedImage.TYPE_INT_RGB;
        } else if ("rgba".equalsIgnoreCase(currColor)) {
            result = BufferedImage.TYPE_INT_ARGB;
        } else {
            throw new IllegalArgumentException("Error: the number of bits per pixel must be 1, 8 or 24.");
        }

        return result;
    }

    /**
     * Calculate the resolution being used assuming that the DPI is used
     * for an A4 page.
     */
    protected int calculateResolution(int dpi, float cropBoxWidth, float cropBoxHeight) {
        int result;

        float maxPoints = Math.max(cropBoxWidth, cropBoxHeight);
        float pointForRequestedResolution = 29.7f * dpi / 2.54f;
        result = Math.round((pointForRequestedResolution * DPI_72 / maxPoints));
        return result;
    }
}
