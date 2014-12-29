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

import javax.activation.DataSource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Helper class to create PDFBox documents.
 */
public class PDDocumentFactory {
    /**
     * Create a PDFBox document.
     *
     * @param source An opaque source
     * @return the document
     * @throws IOException the creation failed
     */
    public PDDocument create(Object source) throws IOException {

        PDDocument result;
        InputStream is = null;
        String sourceName = "unknown";

        try {
            if (source instanceof File) {
                File sourceFile = (File) source;
                sourceName = sourceFile.getName();
                result = PDDocument.load(sourceFile);
            } else if (source instanceof InputStream) {
                is = (InputStream) source;
                result = PDDocument.load(is, true);
            } else if (source instanceof DataSource) {
                is = ((DataSource) source).getInputStream();
                result = PDDocument.load(is, true);
            } else if (source instanceof byte[]) {
                is = new ByteArrayInputStream((byte[]) source);
                result = PDDocument.load(is, true);
            } else if (source instanceof String) {
                File sourceFile = new File((String) source);
                sourceName = sourceFile.getName();
                result = PDDocument.load(sourceFile.getAbsoluteFile());
            } else {
                throw new IllegalAccessException("Don't know how to handle : " + source.getClass().getName());
            }

            if (result.isEncrypted()) {
                result.decrypt("");
            }

            return result;
        } catch (Exception e) {
            String msg = "Parsing the PDF document failed : name=" + sourceName + ", type=" + source.getClass().getName();
            throw new IOException(msg, e);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
}
