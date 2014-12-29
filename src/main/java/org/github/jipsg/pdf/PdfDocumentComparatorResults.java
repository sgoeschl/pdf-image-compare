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

import java.util.ArrayList;
import java.util.List;

/**
 * Manages a list of PdfDocumentComparatorResult to simplify handling of
 * multiple PDF documents to be compared.
 */
public class PdfDocumentComparatorResults {

    private List<PdfDocumentComparatorResult> pdfDocumentComparatorResultList;

    public PdfDocumentComparatorResults() {
        this.pdfDocumentComparatorResultList = new ArrayList<PdfDocumentComparatorResult>();
    }

    public PdfDocumentComparatorResults(List<PdfDocumentComparatorResult> pdfDocumentComparatorResultList) {
        this.pdfDocumentComparatorResultList = pdfDocumentComparatorResultList;
    }

    public PdfDocumentComparatorResults add(PdfDocumentComparatorResult pdfDocumentComparatorResult) {
        this.pdfDocumentComparatorResultList.add(pdfDocumentComparatorResult);
        return this;
    }

    public List<PdfDocumentComparatorResult> getPdfDocumentComparatorResultList() {
        return pdfDocumentComparatorResultList;
    }

    public int size() {
        return this.pdfDocumentComparatorResultList.size();
    }

    public boolean isIdentical() {
        return (size() == getNrOfMatches());
    }

    public int getNrOfDiffs() {
        return (size() - getNrOfMatches() - getNrOfErrors());
    }

    public int getNrOfErrors() {
        int result = 0;

        for (PdfDocumentComparatorResult temp : this.pdfDocumentComparatorResultList) {
            result = (temp.hasErrorMessage() ? result + 1 : result);
        }

        return result;
    }

    public int getNrOfMatches() {
        int result = 0;

        for (PdfDocumentComparatorResult temp : this.pdfDocumentComparatorResultList) {
            result = (temp.isIdentical() ? result + 1 : result);
        }

        return result;
    }
}
