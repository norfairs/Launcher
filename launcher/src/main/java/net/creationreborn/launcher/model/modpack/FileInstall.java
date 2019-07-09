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

package net.creationreborn.launcher.model.modpack;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.skcraft.launcher.install.InstallLog;
import com.skcraft.launcher.install.InstallLogFileMover;
import com.skcraft.launcher.install.Installer;
import com.skcraft.launcher.install.UpdateCache;
import com.skcraft.launcher.model.modpack.ManifestEntry;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.net.URL;

public class FileInstall extends ManifestEntry {

    @JsonProperty
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String destination;

    @JsonProperty
    private long size;

    @JsonProperty
    private String url;

    @JsonProperty
    private boolean userFile;

    @Override
    public void install(Installer installer, InstallLog log, UpdateCache cache, File contentDir) throws Exception {
        if (getWhen() != null && !getWhen().matches()) {
            return;
        }

        File targetFile = new File(contentDir, getDestination());
        URL url = new URL(getUrl());

        Preconditions.checkState(getSize() > 0, "Invalid Size");
        if (shouldUpdate(cache, targetFile)) {
            File tempFile = installer.getDownloader().download(url, getDestination(), getSize(), getDestination());
            installer.queue(new InstallLogFileMover(log, tempFile, targetFile));
        } else {
            log.add(getDestination(), getDestination());
        }
    }

    protected boolean shouldUpdate(UpdateCache cache, File targetFile) {
        if (targetFile.exists() && isUserFile()) {
            return false;
        }

        if (!targetFile.exists()) {
            return true;
        }

        if (targetFile.length() == getSize()) {
            return false;
        }

        return cache.mark(FilenameUtils.normalize(getDestination()), String.valueOf(getSize()));
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isUserFile() {
        return userFile;
    }

    public void setUserFile(boolean userFile) {
        this.userFile = userFile;
    }
}