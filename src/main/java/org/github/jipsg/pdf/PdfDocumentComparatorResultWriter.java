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
package org.github.jipsg.pdf;

import org.github.jipsg.common.image.BufferedImageUtils;
import org.github.jipsg.common.image.compare.ImageComparatorResult;

import java.io.File;
import java.util.List;

public class PdfDocumentComparatorResultWriter {

    /**
     * Write the diff images to a directory for visual inspection.
     *
     * @param baseDirectory               the base directory for output
     * @param directoryName               the directory to create beneath the base directory
     * @param pdfDocumentComparatorResult the result of comparing two PDF documents
     * @throws Exception the result failed
     */
    public void writeToDirectory(File baseDirectory, String directoryName, PdfDocumentComparatorResult pdfDocumentComparatorResult) throws Exception {
        File directory = new File(baseDirectory, directoryName);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        List<ImageComparatorResult> imageDifferResultList = pdfDocumentComparatorResult.getImageDifferResultList();

        for (int i = 0; i < imageDifferResultList.size(); i++) {
            ImageComparatorResult imageDifferResult = imageDifferResultList.get(i);
            File currImageFile = new File(directory, "page-" + i + ".png");
            BufferedImageUtils.writeBufferedImage(imageDifferResult.getBufferedImage(), "png", currImageFile);
        }
    }

    public void writeToDirectory(File baseDirectory, PdfDocumentComparatorResults pdfDocumentComparatorResults, boolean skipIdenticalDocuments) throws Exception {
        for (PdfDocumentComparatorResult pdfDocumentComparatorResult : pdfDocumentComparatorResults.getPdfDocumentComparatorResultList()) {
            if (!pdfDocumentComparatorResult.isIdentical() || !skipIdenticalDocuments) {
                String directoryName = pdfDocumentComparatorResult.getReferenceName();
                writeToDirectory(baseDirectory, directoryName, pdfDocumentComparatorResult);
            }
        }
    }
}
