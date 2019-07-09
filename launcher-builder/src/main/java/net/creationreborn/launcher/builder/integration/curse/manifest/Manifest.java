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

package net.creationreborn.launcher.builder.integration.curse.manifest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Manifest {

    @JsonProperty
    private Minecraft minecraft;

    @JsonProperty
    private String manifestType;

    @JsonProperty
    private int manifestVersion;

    @JsonProperty
    private String name;

    @JsonProperty
    private String version;

    @JsonProperty
    private String author;

    @JsonProperty
    private int projectID;

    @JsonProperty
    private List<AddonFile> files;

    @JsonProperty
    private String overrides;

    public Minecraft getMinecraft() {
        return minecraft;
    }

    public String getManifestType() {
        return manifestType;
    }

    public int getManifestVersion() {
        return manifestVersion;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getAuthor() {
        return author;
    }

    public int getProjectID() {
        return projectID;
    }

    public List<AddonFile> getFiles() {
        return files;
    }

    public String getOverrides() {
        return overrides;
    }
}