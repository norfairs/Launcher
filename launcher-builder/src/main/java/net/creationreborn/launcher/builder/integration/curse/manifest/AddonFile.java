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

public class AddonFile {

    @JsonProperty
    private int projectID;

    @JsonProperty
    private int fileID;

    @JsonProperty
    private boolean required;

    public int getProjectID() {
        return projectID;
    }

    public int getFileID() {
        return fileID;
    }

    public boolean isRequired() {
        return required;
    }
}