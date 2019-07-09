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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skcraft.launcher.builder.BuilderOptions;
import com.skcraft.launcher.model.modpack.Manifest;
import com.skcraft.launcher.model.modpack.ManifestEntry;
import com.skcraft.launcher.util.HttpRequest;
import net.creationreborn.launcher.builder.integration.curse.CurseIntegration;
import net.creationreborn.launcher.util.Toolbox;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public class Builder {

    public static final Logger LOGGER = Logger.getLogger(Builder.class.getName());
    private final BuilderOptions options;
    private final ObjectMapper mapper;

    public Builder(BuilderOptions options, ObjectMapper mapper) {
        this.options = options;
        this.mapper = mapper;
    }

    public void process(Manifest manifest) {
        File tempDirectory = new File(options.getOutputPath(), String.valueOf(System.currentTimeMillis()));
        tempDirectory.mkdir();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> deleteDirectory(tempDirectory.toPath())));

        options.setLoadersDir(tempDirectory);
        options.setOutputPath(tempDirectory);

        if (StringUtils.isNotBlank(options.getBaseUrl())) {
            manifest.setBaseUrl(HttpRequest.url(options.getBaseUrl()));
        }

        if (StringUtils.isNotBlank(options.getCurse())) {
            processCurse(manifest, options.getCurse());
        }
    }

    public void processCurse(Manifest manifest, String source) {
        try {
            CurseIntegration curse = new CurseIntegration();
            curse.prepare(options.getOutputPath(), mapper, source);
            curse.downloadLoaders(options.getLoadersDir());

            manifest.setName(Toolbox.filter(curse.getName())
                    .toLowerCase()
                    .trim()
                    .replace(' ', '_')
                    .replace('.', '_')
            );

            manifest.setTitle(curse.getName().trim());
            manifest.setVersion(curse.getVersion());
            if (options.getManifestPath().isDirectory()) {
                options.setManifestPath(new File(options.getManifestPath(), manifest.getName() + ".json"));
            }

            List<ManifestEntry> entries = curse.getEntries();
            LOGGER.info("Adding " + entries.size() + " entries");
            manifest.getTasks().addAll(entries);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean deleteDirectory(Path path) {
        try {
            LOGGER.info("Deleting " + path.toAbsolutePath().toString());
            Files.walk(path)
                    .map(Path::toFile)
                    .sorted(Comparator.reverseOrder())
                    .forEach(File::delete);

            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}