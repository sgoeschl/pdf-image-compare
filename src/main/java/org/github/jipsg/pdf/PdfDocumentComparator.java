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

import org.github.jipsg.common.image.compare.ImageComparator;
import org.github.jipsg.common.image.compare.ImageComparatorResult;
import org.github.jipsg.common.image.compare.impl.XorImageComparator;
import org.github.jipsg.pdfbox.PdfToImageConverter;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Compare PDF documents.
 */
public class PdfDocumentComparator {

    public PdfDocumentComparator() {
    }

    public PdfDocumentComparatorResult compareDocuments(DataSource referenceDataSource, DataSource documentDataSource) throws Exception {
        final List<BufferedImage> referenceImageList = new PdfToImageConverter().toImages(referenceDataSource, "png", 1, Byte.MAX_VALUE, 72, "RGB");
        final List<BufferedImage> documentImageList = new PdfToImageConverter().toImages(documentDataSource, "png", 1, Byte.MAX_VALUE, 72, "RGB");

        int referenceImageListSize = referenceImageList.size();
        int documentImageListSize = documentImageList.size();

        List<ImageComparatorResult> imageDifferResultList = new ArrayList<ImageComparatorResult>();

        for (int i = 0; i < Math.min(referenceImageListSize, documentImageListSize); i++) {
            BufferedImage currReferenceImage = referenceImageList.get(i);
            BufferedImage currDocumentImage = documentImageList.get(i);
            ImageComparator imageComparator = new XorImageComparator();
            ImageComparatorResult imageComparatorResult = imageComparator.compare(currReferenceImage, currDocumentImage);
            imageDifferResultList.add(imageComparatorResult);
        }

        return new PdfDocumentComparatorResult(referenceDataSource.getName(), referenceImageListSize, documentDataSource.getName(), documentImageListSize, imageDifferResultList);
    }

    public PdfDocumentComparatorResult compareFiles(File referenceFile, File documentFile) throws Exception {
        PdfDocumentComparatorResult result;
        FileDataSource referenceDataSource;
        FileDataSource documentDataSource;

        if (!referenceFile.exists()) {
            throw new FileNotFoundException(referenceFile.getAbsolutePath());
        } else {
            referenceDataSource = new FileDataSource(referenceFile);
        }

        if (documentFile.exists()) {
            documentDataSource = new FileDataSource(documentFile);
            result = compareDocuments(referenceDataSource, documentDataSource);
        } else {
            result = new PdfDocumentComparatorResult(referenceFile.getName(), documentFile.getName(), "The following file does not exist : " + documentFile.getName());
        }

        return result;
    }

    public PdfDocumentComparatorResults compareDirectories(File referenceDirectory, File documentDirectory) throws Exception {
        if (!referenceDirectory.exists()) {
            throw new FileNotFoundException(referenceDirectory.getAbsolutePath());
        }

        if (!documentDirectory.exists()) {
            throw new FileNotFoundException(documentDirectory.getAbsolutePath());
        }

        List<PdfDocumentComparatorResult> result = new ArrayList<PdfDocumentComparatorResult>();

        // get all PDF documents in the given directory

        File[] referenceFileList = referenceDirectory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile() && pathname.getName().toLowerCase().endsWith(".pdf");
            }
        });

        // compare each reference document with the current one

        for (File referenceFile : referenceFileList) {
            File currDocumentFile = new File(documentDirectory, referenceFile.getName());
            result.add(compareFiles(referenceFile, currDocumentFile));
        }

        return new PdfDocumentComparatorResults(result);
    }

}
