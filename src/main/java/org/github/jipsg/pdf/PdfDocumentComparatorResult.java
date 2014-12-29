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

import org.github.jipsg.common.image.compare.ImageComparatorResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Result of comparing two PDF documents.
 */
public class PdfDocumentComparatorResult {

    /**
     * Name of the reference
     */
    private String referenceName;

    /**
     * Number of pages of the reference document
     */
    private int referenceNrOfPages;

    /**
     * Name of the current document
     */
    private String documentName;

    /**
     * Number of pages of the current document
     */
    private int documentNrOfPages;

    /**
     * List of diff images
     */
    private List<ImageComparatorResult> imageDifferResultList;

    /**
     * Error message if the processing failed
     */
    private String errorMessage;

    public PdfDocumentComparatorResult(String referenceName, String documentName, String errorMessage) {
        this.referenceName = referenceName;
        this.referenceNrOfPages = -1;
        this.documentName = documentName;
        this.documentNrOfPages = -1;
        this.errorMessage = errorMessage;
        this.imageDifferResultList = new ArrayList<ImageComparatorResult>();
    }

    public PdfDocumentComparatorResult(String referenceName, int referenceNrOfPages, String documentName, int documentNrOfPages, List<ImageComparatorResult> imageDifferResultList) {
        assert referenceName != null & !referenceName.isEmpty();
        assert documentName != null & !documentName.isEmpty();
        assert referenceNrOfPages >= 0;
        assert documentNrOfPages >= 0;
        assert imageDifferResultList != null;

        this.referenceName = referenceName;
        this.referenceNrOfPages = referenceNrOfPages;
        this.documentName = documentName;
        this.documentNrOfPages = documentNrOfPages;
        this.imageDifferResultList = imageDifferResultList;
    }

    public boolean isIdentical() {
        boolean hasSameNumberOfPages = hasSameNrOfPages();
        boolean hasIdenticalImages = hasIdenticalImages();
        boolean hasErrorMessage = hasErrorMessage();

        return !hasErrorMessage && hasSameNumberOfPages && hasIdenticalImages;
    }

    public boolean hasErrorMessage() {
        return getErrorMessage() != null;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean hasSameNrOfPages() {
        return referenceNrOfPages == documentNrOfPages;
    }

    public boolean hasIdenticalImages() {
        boolean result = true;

        for (ImageComparatorResult imageDifferResult : imageDifferResultList) {
            result &= imageDifferResult.isIdentical();
        }

        return result;
    }

    public String getReferenceName() {
        return referenceName;
    }

    public int getReferenceNrOfPages() {
        return referenceNrOfPages;
    }

    public String getDocumentName() {
        return documentName;
    }

    public int getDocumentNrOfPages() {
        return documentNrOfPages;
    }

    public List<ImageComparatorResult> getImageDifferResultList() {
        return imageDifferResultList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PdfDocumentComparatorResult{");
        sb.append("referenceName='").append(referenceName).append('\'');
        sb.append(", referenceNrOfPages=").append(referenceNrOfPages);
        sb.append(", hasErrorMessage").append(hasErrorMessage()).append('\'');
        sb.append(", documentName='").append(documentName).append('\'');
        sb.append(", documentNrOfPages=").append(documentNrOfPages);
        sb.append(", isIdentical=").append(isIdentical());
        sb.append(", hasSameNumberOfPages=").append(hasSameNrOfPages());
        sb.append(", imageDifferResultList=").append(imageDifferResultList);
        sb.append(", errorMessage").append(errorMessage).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
