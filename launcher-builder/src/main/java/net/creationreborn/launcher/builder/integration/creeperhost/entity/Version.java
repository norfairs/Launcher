/*
 * Copyright 2020 creationreborn.net
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

package net.creationreborn.launcher.builder.integration.creeperhost.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Version {

    @JsonProperty
    private List<File> files;

    @JsonProperty
    private Specs specs;

    @JsonProperty
    private List<Target> targets;

    @JsonProperty
    private long installs;

    @JsonProperty
    private long plays;

    @JsonProperty
    private long refreshed;

    @JsonProperty
    private String status;

    @JsonProperty
    private String changelog;

    @JsonProperty
    private int parent;

    @JsonProperty
    private int id;

    @JsonProperty
    private String name;

    @JsonProperty
    private String type;

    @JsonProperty
    private long updated;

    public List<File> getFiles() {
        return files;
    }

    public Specs getSpecs() {
        return specs;
    }

    public List<Target> getTargets() {
        return targets;
    }

    public long getInstalls() {
        return installs;
    }

    public long getPlays() {
        return plays;
    }

    public long getRefreshed() {
        return refreshed;
    }

    public String getStatus() {
        return status;
    }

    public String getChangelog() {
        return changelog;
    }

    public int getParent() {
        return parent;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public long getUpdated() {
        return updated;
    }

    public static class File {

        @JsonProperty
        private String version;

        @JsonProperty
        private String path;

        @JsonProperty
        private String url;

        @JsonProperty
        private String sha1;

        @JsonProperty
        private long size;

        @JsonProperty
        private List<Object> tags;

        @JsonProperty(value = "clientonly")
        private boolean clientOnly;

        @JsonProperty(value = "serveronly")
        private boolean serverOnly;

        @JsonProperty
        private boolean optional;

        @JsonProperty
        private int id;

        @JsonProperty
        private String name;

        @JsonProperty
        private String type;

        @JsonProperty
        private long updated;

        public String getVersion() {
            return version;
        }

        public String getPath() {
            return path;
        }

        public String getUrl() {
            return url;
        }

        public String getSha1() {
            return sha1;
        }

        public long getSize() {
            return size;
        }

        public List<Object> getTags() {
            return tags;
        }

        public boolean isClientOnly() {
            return clientOnly;
        }

        public boolean isServerOnly() {
            return serverOnly;
        }

        public boolean isOptional() {
            return optional;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public long getUpdated() {
            return updated;
        }
    }

    public static class Target {

        @JsonProperty
        private String version;

        @JsonProperty
        private int id;

        @JsonProperty
        private String name;

        @JsonProperty
        private String type;

        @JsonProperty
        private long updated;

        public String getVersion() {
            return version;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public long getUpdated() {
            return updated;
        }
    }
}