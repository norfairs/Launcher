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

package net.creationreborn.launcher.builder;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.skcraft.launcher.builder.DirectoryWalker;
import com.skcraft.launcher.builder.FileInfoScanner;
import com.skcraft.launcher.builder.PropertiesApplicator;
import com.skcraft.launcher.model.modpack.Manifest;
import lombok.NonNull;
import net.creationreborn.launcher.model.modpack.FileInstall;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Logger;

public class ClientFileCollector extends DirectoryWalker {

    public static final Logger LOGGER = Logger.getLogger(ClientFileCollector.class.getName());
    public static final String URL_FILE_SUFFIX = ".url.txt";
    private final Manifest manifest;
    private final PropertiesApplicator applicator;
    private final File destination;
    @SuppressWarnings({"deprecation", "UnstableApiUsage"})
    private HashFunction hf = Hashing.sha1();

    public ClientFileCollector(Manifest manifest, PropertiesApplicator applicator, File destination) {
        this.manifest = manifest;
        this.applicator = applicator;
        this.destination = destination;
    }

    @Override
    protected DirectoryBehavior getBehavior(@NonNull String name) {
        return getDirectoryBehavior(name);
    }

    @Override
    protected void onFile(File file, String relPath) throws IOException {
        if (file.getName().endsWith(FileInfoScanner.FILE_SUFFIX) || file.getName().endsWith(URL_FILE_SUFFIX)) {
            return;
        }

        FileInstall entry = new FileInstall();
        String hash = Files.asByteSource(file).hash(hf).toString();
        String to = FilenameUtils.separatorsToUnix(FilenameUtils.normalize(relPath));

        // url.txt override file
        File urlFile = new File(file.getAbsoluteFile().getParentFile(), file.getName() + URL_FILE_SUFFIX);
        String location;
        boolean copy = true;
        if (urlFile.exists() && !System.getProperty("com.skcraft.builder.ignoreURLOverrides", "false").equalsIgnoreCase("true")) {
            location = Files.asCharSource(urlFile, Charset.defaultCharset()).readFirstLine();
            copy = false;
        } else {
            location = hash.substring(0, 2) + "/" + hash.substring(2, 4) + "/" + hash;
        }

        entry.setDestination(to);
        entry.setSize(file.length());
        entry.setUrl(location);
        applicator.apply(entry);

        LOGGER.info(String.format("Adding %s from %s...", relPath, file.getAbsolutePath()));
        if (copy) {
            File destinationPath = new File(destination, location);
            Files.copy(file, destinationPath);
            destinationPath.getParentFile().mkdirs();
        }

        manifest.getTasks().add(entry);
    }

    public static DirectoryBehavior getDirectoryBehavior(@NonNull String name) {
        if (name.startsWith(".")) {
            return DirectoryBehavior.SKIP;
        } else if (name.equals("_OPTIONAL")) {
            return DirectoryBehavior.IGNORE;
        } else if (name.equals("_SERVER")) {
            return DirectoryBehavior.SKIP;
        } else if (name.equals("_CLIENT")) {
            return DirectoryBehavior.IGNORE;
        } else {
            return DirectoryBehavior.CONTINUE;
        }
    }
}