/*
 * Copyright 2019 creationreborn.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.creationreborn.launcher.install;

import com.google.common.io.ByteSource;
import org.apache.commons.io.IOUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipExtract implements Runnable {

    private final ByteSource source;
    private final File destination;
    private final Function<String, String> function;

    public ZipExtract(ByteSource source, File destination) {
        this(source, destination, name -> name);
    }

    public ZipExtract(ByteSource source, File destination, Function<String, String> function) {
        this.source = source;
        this.destination = destination;
        this.function = function;
    }

    @Override
    public void run() {
        try (ZipInputStream inputStream = new ZipInputStream(source.openBufferedStream())) {
            destination.getParentFile().mkdirs();
            ZipEntry entry;

            while ((entry = inputStream.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                }

                String name = function.apply(entry.getName());
                if (name != null) {
                    File file = new File(destination, name);
                    file.getParentFile().mkdirs();
                    writeEntry(inputStream, file);
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void writeEntry(ZipInputStream inputStream, File file) throws IOException {
        Files.createDirectories(file.getParentFile().toPath());
        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            IOUtils.copy(inputStream, outputStream);
        }
    }

    @Override
    public String toString() {
        return destination.getName();
    }
}