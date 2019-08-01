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

package net.creationreborn.launcher.builder.integration.curse.meta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

@JsonIgnoreProperties(
        ignoreUnknown = true
)
public class Metadata {

    @JsonProperty
    private Long id;

    @JsonProperty
    private String displayName;

    @JsonProperty
    private String fileName;

    @JsonProperty
    private String fileDate;

    @JsonProperty
    private Long fileLength;

    @JsonProperty
    private Integer releaseType;

    @JsonProperty
    private Integer fileStatus;

    @JsonProperty
    private String downloadUrl;

    @JsonProperty
    private Boolean isAlternate;

    @JsonProperty
    private Long alternateFileId;

    @JsonProperty
    private Set<Dependency> dependencies;

    @JsonProperty
    private Boolean isAvailable;

    @JsonProperty
    private Set<Module> modules;

    @JsonProperty
    private Long packageFingerprint;

    @JsonProperty
    private Set<String> gameVersion;

    @JsonProperty
    private Object installMetadata;

    @JsonProperty
    private Long serverPackFileId;

    @JsonProperty
    private Boolean hasInstallScript;

    @JsonProperty
    private String gameVersionDateReleased;

    public Long getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getFileName() {
        return fileName;
    }

    public String geFileDate() {
        return fileDate;
    }

    public Long getFileLength() {
        return fileLength;
    }

    public Integer getReleaseType() {
        return releaseType;
    }

    public Integer getFileStatus() {
        return fileStatus;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public Boolean getAlternate() {
        return isAlternate;
    }

    public Long getAlternateFileId() {
        return alternateFileId;
    }

    public Set<Dependency> getDependencies() {
        return dependencies;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public Set<Module> getModules() {
        return modules;
    }

    public Long getPackageFingerprint() {
        return packageFingerprint;
    }

    public Set<String> getGameVersion() {
        return gameVersion;
    }

    public Object getInstallMetadata() {
        return installMetadata;
    }

    public Long getServerPackFileId() {
        return serverPackFileId;
    }

    public Boolean getHasInstallScript() {
        return hasInstallScript;
    }

    public String getGameVersionDateReleased() {
        return gameVersionDateReleased;
    }
}