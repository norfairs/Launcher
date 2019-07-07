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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(
        value = {
                "email",
                "registerIp",
                "registeredAt",
                "passwordChangedAt",
                "dateOfBirth",
                "suspended",
                "blocked",
                "secured",
                "migrated",
                "emailVerified",
                "legacyUser",
                "emailSubscriptionStatus",
                "emailSubscriptionKey",
                "verifiedByParent",
                "hashed",
                "fromMigratedUser"
        },
        allowSetters = true
)
public class User {

    private String id;
    private String email;
    private String username;
    private String registerIp;
    private long registeredAt;
    private long passwordChangedAt;
    private long dateOfBirth;
    private boolean suspended;
    private boolean blocked;
    private boolean secured;
    private boolean migrated;
    private boolean emailVerified;
    private boolean legacyUser;
    private String emailSubscriptionStatus;
    private String emailSubscriptionKey;
    private boolean verifiedByParent;
    private boolean hashed;
    private boolean fromMigratedUser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRegisterIp() {
        return registerIp;
    }

    public void setRegisterIp(String registerIp) {
        this.registerIp = registerIp;
    }

    public long getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(long registeredAt) {
        this.registeredAt = registeredAt;
    }

    public long getPasswordChangedAt() {
        return passwordChangedAt;
    }

    public void setPasswordChangedAt(long passwordChangedAt) {
        this.passwordChangedAt = passwordChangedAt;
    }

    public long getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isSecured() {
        return secured;
    }

    public void setSecured(boolean secured) {
        this.secured = secured;
    }

    public boolean isMigrated() {
        return migrated;
    }

    public void setMigrated(boolean migrated) {
        this.migrated = migrated;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public boolean isLegacyUser() {
        return legacyUser;
    }

    public void setLegacyUser(boolean legacyUser) {
        this.legacyUser = legacyUser;
    }

    public String getEmailSubscriptionStatus() {
        return emailSubscriptionStatus;
    }

    public void setEmailSubscriptionStatus(String emailSubscriptionStatus) {
        this.emailSubscriptionStatus = emailSubscriptionStatus;
    }

    public String getEmailSubscriptionKey() {
        return emailSubscriptionKey;
    }

    public void setEmailSubscriptionKey(String emailSubscriptionKey) {
        this.emailSubscriptionKey = emailSubscriptionKey;
    }

    public boolean isVerifiedByParent() {
        return verifiedByParent;
    }

    public void setVerifiedByParent(boolean verifiedByParent) {
        this.verifiedByParent = verifiedByParent;
    }

    public boolean isHashed() {
        return hashed;
    }

    public void setHashed(boolean hashed) {
        this.hashed = hashed;
    }

    public boolean isFromMigratedUser() {
        return fromMigratedUser;
    }

    public void setFromMigratedUser(boolean fromMigratedUser) {
        this.fromMigratedUser = fromMigratedUser;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        User user = (User) obj;
        return Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}