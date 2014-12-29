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

import org.junit.Test;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import java.io.File;

import static org.junit.Assert.*;

public class PdfDocumentComparatorTest {

    private File testDocumentDir = new File("./src/test/documents/pdf");
    private File testResultDir = new File("./target/out/pdfdocumentcomparator");

    /**
     * We compare a document with itself.
     */
    @Test
    public void testIdenticalPdfDocuments() throws Exception {
        DataSource referenceDataSource = new FileDataSource(new File(testDocumentDir, "open-office-01.pdf"));
        DataSource documentDataSource = new FileDataSource(new File(testDocumentDir, "open-office-01.pdf"));
        PdfDocumentComparatorResult pdfDocumentComparatorResult = new PdfDocumentComparator().compareDocuments(referenceDataSource, documentDataSource);
        System.out.println(pdfDocumentComparatorResult);
        new PdfDocumentComparatorResultWriter().writeToDirectory(testResultDir, "testIdenticalPdfDocuments", pdfDocumentComparatorResult);
        assertNotNull(pdfDocumentComparatorResult);
        assertEquals(1, pdfDocumentComparatorResult.getReferenceNrOfPages());
        assertEquals(1, pdfDocumentComparatorResult.getDocumentNrOfPages());
        assertEquals(1, pdfDocumentComparatorResult.getImageDifferResultList().size());
        assertTrue(pdfDocumentComparatorResult.isIdentical());
        assertEquals("open-office-01.pdf", pdfDocumentComparatorResult.getReferenceName());
        assertEquals("open-office-01.pdf", pdfDocumentComparatorResult.getDocumentName());
        assertTrue(pdfDocumentComparatorResult.toString().length() > 16);
    }

    /**
     * We compare a document with its slightly modified version assuming that the
     * small difference will be detected.
     */
    @Test
    public void testSimilarPdfDocuments() throws Exception {
        DataSource referenceDataSource = new FileDataSource(new File(testDocumentDir, "open-office-02.pdf"));
        DataSource documentDataSource = new FileDataSource(new File(testDocumentDir, "open-office-02-similar.pdf"));
        PdfDocumentComparatorResult pdfDocumentComparatorResult = new PdfDocumentComparator().compareDocuments(referenceDataSource, documentDataSource);
        new PdfDocumentComparatorResultWriter().writeToDirectory(testResultDir, "testSimilarPdfDocuments", pdfDocumentComparatorResult);
        assertNotNull(pdfDocumentComparatorResult);
        assertEquals(1, pdfDocumentComparatorResult.getReferenceNrOfPages());
        assertEquals(1, pdfDocumentComparatorResult.getDocumentNrOfPages());
        assertEquals(1, pdfDocumentComparatorResult.getImageDifferResultList().size());
        assertTrue(pdfDocumentComparatorResult.hasSameNrOfPages());
        assertFalse(pdfDocumentComparatorResult.isIdentical());
        assertEquals("open-office-02.pdf", pdfDocumentComparatorResult.getReferenceName());
        assertEquals("open-office-02-similar.pdf", pdfDocumentComparatorResult.getDocumentName());
        assertTrue(pdfDocumentComparatorResult.toString().length() > 16);
    }

    /**
     * We compare two different PDF documents both having one page.
     */
    @Test
    public void testDifferentPdfDocuments() throws Exception {
        DataSource referenceDataSource = new FileDataSource(new File(testDocumentDir, "open-office-01.pdf"));
        DataSource documentDataSource = new FileDataSource(new File(testDocumentDir, "open-office-02.pdf"));
        PdfDocumentComparatorResult pdfDocumentComparatorResult = new PdfDocumentComparator().compareDocuments(referenceDataSource, documentDataSource);
        new PdfDocumentComparatorResultWriter().writeToDirectory(testResultDir, "testDifferentPdfDocuments", pdfDocumentComparatorResult);
        assertNotNull(pdfDocumentComparatorResult);
        assertEquals(1, pdfDocumentComparatorResult.getReferenceNrOfPages());
        assertEquals(1, pdfDocumentComparatorResult.getDocumentNrOfPages());
        assertEquals(1, pdfDocumentComparatorResult.getImageDifferResultList().size());
        assertTrue(pdfDocumentComparatorResult.hasSameNrOfPages());
        assertFalse(pdfDocumentComparatorResult.isIdentical());
        assertEquals("open-office-01.pdf", pdfDocumentComparatorResult.getReferenceName());
        assertEquals("open-office-02.pdf", pdfDocumentComparatorResult.getDocumentName());
        assertTrue(pdfDocumentComparatorResult.toString().length() > 16);
    }

    /**
     * We compare two different PDF documents but the documents have a different
     * number of pages.                                                                       x
     */
    @Test
    public void testPdfDocumentsWithDifferentPageCounts() throws Exception {
        DataSource referenceDataSource = new FileDataSource(new File(testDocumentDir, "open-office-01.pdf"));
        DataSource documentDataSource = new FileDataSource(new File(testDocumentDir, "multi-page-01.pdf"));
        PdfDocumentComparatorResult pdfDocumentComparatorResult = new PdfDocumentComparator().compareDocuments(referenceDataSource, documentDataSource);
        new PdfDocumentComparatorResultWriter().writeToDirectory(testResultDir, "testPdfDocumentsWithDifferentPageCounts", pdfDocumentComparatorResult);
        assertNotNull(pdfDocumentComparatorResult);
        assertEquals(1, pdfDocumentComparatorResult.getReferenceNrOfPages());
        assertEquals(3, pdfDocumentComparatorResult.getDocumentNrOfPages());
        assertFalse(pdfDocumentComparatorResult.hasSameNrOfPages());
        assertEquals(1, pdfDocumentComparatorResult.getImageDifferResultList().size());
        assertFalse(pdfDocumentComparatorResult.isIdentical());
    }

    @Test
    public void testCompareIdenticalDirectories() throws Exception {
        PdfDocumentComparatorResults pdfDocumentComparatorResults = new PdfDocumentComparator().compareDirectories(testDocumentDir, testDocumentDir);
        File resultDirectory = new File(testResultDir, "testCompareIdenticalDirectories");
        new PdfDocumentComparatorResultWriter().writeToDirectory(resultDirectory, pdfDocumentComparatorResults, false);
        assertTrue(pdfDocumentComparatorResults.isIdentical());
        assertEquals(4, pdfDocumentComparatorResults.size());
        assertEquals(0, pdfDocumentComparatorResults.getNrOfErrors());
        assertEquals(0, pdfDocumentComparatorResults.getNrOfDiffs());
        assertEquals(4, resultDirectory.listFiles().length);
    }

    /**
     * Compare two directories where one document is identical, one document is modified and one
     * document is missing.
     */
    @Test
    public void testDifferentDirectories() throws Exception {
        File referenceDirectory = new File("./src/test/documents/directory/reference");
        File documentDirectory = new File("./src/test/documents/directory/current");
        File resultDirectory = new File(testResultDir, "testDifferentDirectories");

        PdfDocumentComparatorResults pdfDocumentComparatorResults = new PdfDocumentComparator().compareDirectories(referenceDirectory, documentDirectory);
        new PdfDocumentComparatorResultWriter().writeToDirectory(resultDirectory, pdfDocumentComparatorResults, true);
        assertFalse(pdfDocumentComparatorResults.isIdentical());
        assertEquals(3, pdfDocumentComparatorResults.size());
        assertEquals(1, pdfDocumentComparatorResults.getNrOfMatches());
        assertEquals(1, pdfDocumentComparatorResults.getNrOfDiffs());
        assertEquals(1, pdfDocumentComparatorResults.getNrOfErrors());
        assertEquals(2, resultDirectory.listFiles().length);
    }
}
