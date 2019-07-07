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

package net.creationreborn.launcher.integration.mojang.yggdrasil;

import com.skcraft.launcher.auth.Session;
import com.skcraft.launcher.auth.UserType;
import net.creationreborn.launcher.auth.Account;

import java.util.Collections;
import java.util.Map;

public class YggdrasilSession implements Session {

    private final String accessToken;
    private final String clientToken;
    private final Profile profile;

    public YggdrasilSession(Account account, Profile profile) {
        this.accessToken = account.getAccessToken();
        this.clientToken = account.getClientToken();
        this.profile = profile;
    }

    @Override
    public String getUuid() {
        return profile.getId();
    }

    @Override
    public String getName() {
        return profile.getName();
    }

    @Override
    public String getClientToken() {
        return clientToken;
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public Map<String, String> getUserProperties() {
        return Collections.emptyMap();
    }

    @Override
    public String getSessionToken() {
        return "token:" + getAccessToken() + ":" + getUuid();
    }

    @Override
    public UserType getUserType() {
        if (profile.isLegacyProfile()) {
            return UserType.LEGACY;
        }

        return UserType.MOJANG;
    }

    @Override
    public boolean isOnline() {
        return true;
    }
}