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

package net.creationreborn.launcher.integration.mojang;

import com.skcraft.launcher.util.HttpRequest;
import net.creationreborn.launcher.auth.Account;
import net.creationreborn.launcher.integration.mojang.yggdrasil.Agent;
import net.creationreborn.launcher.integration.mojang.yggdrasil.request.AuthenticateRequest;
import net.creationreborn.launcher.integration.mojang.yggdrasil.request.RefreshRequest;
import net.creationreborn.launcher.integration.mojang.yggdrasil.request.ValidateRequest;
import net.creationreborn.launcher.integration.mojang.yggdrasil.response.AuthenticateResponse;
import net.creationreborn.launcher.integration.mojang.yggdrasil.response.ErrorResponse;
import net.creationreborn.launcher.integration.mojang.yggdrasil.response.RefreshResponse;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class MojangIntegration {

    public static boolean login(Account account, String password) throws AuthenticationException, InterruptedException, IOException {
        AuthenticateRequest authenticateRequest = new AuthenticateRequest();
        authenticateRequest.setAgent(Agent.MINECRAFT);
        authenticateRequest.setUsername(account.getUser().getUsername());
        authenticateRequest.setPassword(password);
        authenticateRequest.setClientToken(account.getClientToken());
        authenticateRequest.setRequestUser(true);

        HttpRequest httpRequest = HttpRequest.post(HttpRequest.url("https://authserver.mojang.com/authenticate")).bodyJson(authenticateRequest).execute();
        if (httpRequest.getResponseCode() == 200) {
            AuthenticateResponse authenticateResponse = httpRequest.returnContent().asJson(AuthenticateResponse.class);

            if (StringUtils.isBlank(authenticateResponse.getClientToken())) {
                throw new AuthenticationException("Authentication server didn't send a client token.");
            }

            if (StringUtils.isNotBlank(account.getClientToken()) && !account.getClientToken().equals(authenticateResponse.getClientToken())) {
                throw new AuthenticationException("Authentication server attempted to change the client token. This isn't supported.");
            }

            account.setClientToken(authenticateResponse.getClientToken());
            if (StringUtils.isBlank(authenticateResponse.getAccessToken())) {
                throw new AuthenticationException("Authentication server didn't send an access token.");
            }

            account.setAccessToken(authenticateResponse.getAccessToken());
            account.setProfiles(authenticateResponse.getAvailableProfiles());

            if (authenticateResponse.getSelectedProfile() == null) {
                throw new AuthenticationException("Authentication server didn't specify a currently selected profile. The account exists, but likely isn't premium.");
            }

            if (!account.setCurrentProfile(authenticateResponse.getSelectedProfile())) {
                throw new AuthenticationException("Authentication server specified a selected profile that wasn't in the available profiles list.");
            }

            if (authenticateRequest.isRequestUser()) {
                account.setUser(authenticateResponse.getUser());
            }

            return true;
        } else {
            ErrorResponse errorResponse = httpRequest.returnContent().asJson(ErrorResponse.class);
            throw new AuthenticationException(errorResponse.getErrorMessage());
        }
    }

    public static boolean refresh(Account account) throws AuthenticationException, InterruptedException, IOException {
        RefreshRequest refreshRequest = new RefreshRequest();
        refreshRequest.setAccessToken(account.getAccessToken());
        refreshRequest.setClientToken(account.getClientToken());
        if (account.getUser() == null) {
            refreshRequest.setRequestUser(true);
        }

        HttpRequest httpRequest = HttpRequest.post(HttpRequest.url("https://authserver.mojang.com/refresh")).bodyJson(refreshRequest).execute();
        if (httpRequest.getResponseCode() == 200) {
            RefreshResponse refreshResponse = httpRequest.returnContent().asJson(RefreshResponse.class);
            if (StringUtils.isBlank(refreshResponse.getClientToken())) {
                throw new AuthenticationException("Authentication server didn't send a client token.");
            }

            if (StringUtils.isNotBlank(account.getClientToken()) && !account.getClientToken().equals(refreshResponse.getClientToken())) {
                throw new AuthenticationException("Authentication server attempted to change the client token. This isn't supported.");
            }

            if (StringUtils.isBlank(refreshResponse.getAccessToken())) {
                throw new AuthenticationException("Authentication server didn't send an access token.");
            }

            if (!account.getCurrentProfile().map(profile -> profile.equals(refreshResponse.getSelectedProfile())).orElse(false)) {
                throw new AuthenticationException("Authentication server didn't specify the same profile as expected.");
            }

            account.setAccessToken(refreshResponse.getAccessToken());
            if (refreshRequest.isRequestUser()) {
                account.setUser(refreshResponse.getUser());
            }

            return true;
        } else {
            ErrorResponse errorResponse = httpRequest.returnContent().asJson(ErrorResponse.class);
            throw new AuthenticationException(errorResponse.getErrorMessage());
        }
    }

    public static boolean validate(Account account) throws AuthenticationException, InterruptedException, IOException {
        ValidateRequest validateRequest = new ValidateRequest();
        validateRequest.setAccessToken(account.getAccessToken());
        validateRequest.setClientToken(account.getClientToken());

        HttpRequest httpRequest = HttpRequest.post(HttpRequest.url("https://authserver.mojang.com/validate")).bodyJson(validateRequest).execute();
        if (httpRequest.getResponseCode() == 204) {
            return true;
        } else {
            ErrorResponse errorResponse = httpRequest.returnContent().asJson(ErrorResponse.class);
            throw new AuthenticationException(errorResponse.getErrorMessage());
        }
    }
}