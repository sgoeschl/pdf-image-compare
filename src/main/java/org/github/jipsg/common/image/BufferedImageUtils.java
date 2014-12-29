/*
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
package org.github.jipsg.common.image;

/**
 * Created with IntelliJ IDEA.
 * User: sgoeschl
 * Date: 13/08/14
 * Time: 21:43
 * To change this template use File | Settings | File Templates.
 */

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Contains ready-to use image operations without additional dependencies.
 */
public class BufferedImageUtils {

    public static boolean writeBufferedImage(BufferedImage bufferedImage, String formatName, File file) throws Exception {

        assert bufferedImage != null : "bufferedImage is null";
        assert formatName != null : "formatName is null";
        assert file != null : "file is null";

        file.getParentFile().mkdirs();
        return ImageIO.write(bufferedImage, formatName, file);
    }
}