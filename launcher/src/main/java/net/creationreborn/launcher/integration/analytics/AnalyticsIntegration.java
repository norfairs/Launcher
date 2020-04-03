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

package net.creationreborn.launcher.integration.analytics;

import com.skcraft.launcher.Launcher;
import com.skcraft.launcher.util.HttpRequest;
import net.creationreborn.launcher.Instance;

import java.util.logging.Logger;

public class AnalyticsIntegration {

    private static final Logger LOGGER = Logger.getLogger(AnalyticsIntegration.class.getName());

    public static void install(Launcher launcher, Instance instance) {
        if (instance.getAnalytics() != null && instance.getAnalytics().getInstallUrl() != null) {
            send(launcher, instance.getAnalytics().getInstallUrl());
        }
    }

    public static void play(Launcher launcher, Instance instance) {
        if (instance.getAnalytics() != null && instance.getAnalytics().getPlayUrl() != null) {
            send(launcher, instance.getAnalytics().getPlayUrl());
        }
    }

    private static void send(Launcher launcher, String url) {
        launcher.getExecutor().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    LOGGER.info("Connecting to " + url);
                    HttpRequest.get(HttpRequest.url(url)).execute().close();
                } catch (Exception ex) {
                    LOGGER.warning("Encountered an error while attempting to send analytics: " + ex.getMessage());
                }
            }
        });
    }
}