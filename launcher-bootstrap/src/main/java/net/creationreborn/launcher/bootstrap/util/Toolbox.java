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

package net.creationreborn.launcher.bootstrap.util;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Toolbox {

    public static final String USER_AGENT = "Mozilla/5.0 (Java) CRLauncher";

    public static Path getPath(String name) {
        String osName = System.getProperty("os.name");
        String userHome = System.getProperty("user.home");

        if (osName != null && userHome != null) {
            if (osName.startsWith("Linux")) {
                return Paths.get(userHome, name);
            }

            if (osName.startsWith("MacOS")) {
                return Paths.get(userHome, "Library", "Application Support", name);
            }

            if (osName.startsWith("Windows")) {
                return Paths.get(userHome, "AppData", "Roaming", name);
            }
        }

        return Paths.get(name);
    }
}