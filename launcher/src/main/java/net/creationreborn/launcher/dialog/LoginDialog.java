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

package net.creationreborn.launcher.dialog;

import com.skcraft.concurrency.ObservableFuture;
import com.skcraft.concurrency.ProgressObservable;
import com.skcraft.launcher.Launcher;
import com.skcraft.launcher.auth.Session;
import com.skcraft.launcher.dialog.ProgressDialog;
import com.skcraft.launcher.persistence.Persistence;
import com.skcraft.launcher.swing.ActionListeners;
import com.skcraft.launcher.swing.FormPanel;
import com.skcraft.launcher.swing.LinedBoxPanel;
import com.skcraft.launcher.swing.LinkButton;
import com.skcraft.launcher.swing.SwingHelper;
import com.skcraft.launcher.swing.TextFieldPopupMenu;
import com.skcraft.launcher.util.SharedLocale;
import com.skcraft.launcher.util.SwingExecutor;
import net.creationreborn.launcher.auth.Account;
import net.creationreborn.launcher.integration.mojang.AuthenticationException;
import net.creationreborn.launcher.integration.mojang.MojangIntegration;
import net.creationreborn.launcher.integration.mojang.yggdrasil.YggdrasilSession;
import net.creationreborn.launcher.util.Toolbox;
import org.apache.commons.lang3.StringUtils;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class LoginDialog extends JDialog {

    private final Launcher launcher;
    private final JTextField usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JCheckBox rememberAccountCheck = new JCheckBox(SharedLocale.tr("login.rememberAccount"));
    private final LinkButton recoverButton = new LinkButton(SharedLocale.tr("login.recoverAccount"));
    private final JButton loginButton = new JButton(SharedLocale.tr("login.login"));
    private final JButton cancelButton = new JButton(SharedLocale.tr("button.cancel"));
    private final FormPanel formPanel = new FormPanel();
    private final LinedBoxPanel buttonsPanel = new LinedBoxPanel(true);
    private boolean cancelled;
    private Session session;

    /**
     * Create a new login dialog.
     *
     * @param owner    the owner
     * @param launcher the launcher
     */
    public LoginDialog(Window owner, Launcher launcher) {
        super(owner, ModalityType.DOCUMENT_MODAL);

        this.launcher = launcher;

        setTitle(SharedLocale.tr("login.title"));
        initComponents();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(420, 0));
        setResizable(false);
        pack();
        setLocationRelativeTo(owner);

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                dispose();
            }
        });
    }

    @SuppressWarnings("Duplicates")
    private void initComponents() {
        rememberAccountCheck.setBorder(BorderFactory.createEmptyBorder());
        rememberAccountCheck.setSelected(true);

        loginButton.setFont(loginButton.getFont().deriveFont(Font.BOLD));

        formPanel.addRow(new JLabel(SharedLocale.tr("login.username")), usernameField);
        formPanel.addRow(new JLabel(SharedLocale.tr("login.password")), passwordField);
        formPanel.addRow(new JLabel(), rememberAccountCheck);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(26, 13, 13, 13));

        buttonsPanel.addElement(recoverButton);
        buttonsPanel.addGlue();
        buttonsPanel.addElement(loginButton);
        buttonsPanel.addElement(cancelButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(loginButton);

        passwordField.setComponentPopupMenu(TextFieldPopupMenu.INSTANCE);

        recoverButton.addActionListener(
                ActionListeners.openURL(recoverButton, launcher.getProperties().getProperty("resetPasswordUrl")));

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                prepareLogin();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                setResult(true, null);
            }
        });
    }

    private void prepareLogin() {
        if (StringUtils.isBlank(usernameField.getText())) {
            SwingHelper.showErrorDialog(this, SharedLocale.tr("login.noLoginError"), SharedLocale.tr("login.noLoginTitle"));
            return;
        }

        char[] characters = passwordField.getPassword();
        String password = String.valueOf(characters);
        Arrays.fill(characters, '0');
        if (StringUtils.isBlank(password)) {
            SwingHelper.showErrorDialog(this, SharedLocale.tr("login.noPasswordError"), SharedLocale.tr("login.noPasswordTitle"));
            return;
        }

        Account account = launcher.getAccounts().getOrCreate(usernameField.getText());
        attemptLogin(account, password);
    }

    private void attemptLogin(Account account, String password) {
        LoginCallable callable = new LoginCallable(account, password);
        ObservableFuture<Session> future = new ObservableFuture<>(launcher.getExecutor().submit(callable), callable);

        ProgressDialog.showProgress(this, future, SharedLocale.tr("login.loggingInTitle"), SharedLocale.tr("login.loggingInStatus"));

        Toolbox.addCallback(future, success -> {
            setResult(false, success);
        }, failure -> {
            setResult(false, null);
        }, SwingExecutor.INSTANCE);

        SwingHelper.addErrorDialogCallback(this, future);
    }

    public void setUsername(String username) {
        usernameField.setText(username);
        rememberAccountCheck.setSelected(true);
    }

    public void setResult(boolean cancelled, Session session) {
        this.cancelled = cancelled;
        this.session = session;
        dispose();
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public Session getSession() {
        return session;
    }

    public class LoginCallable implements Callable<Session>, ProgressObservable {

        private final Account account;
        private final String password;

        private LoginCallable(Account account, String password) {
            this.account = account;
            this.password = password;
        }

        @Override
        public Session call() throws AuthenticationException, IOException, InterruptedException {
            MojangIntegration.login(account, password);
            if (rememberAccountCheck.isSelected()) {
                launcher.getAccounts().add(account);
                launcher.getAccounts().setCurrentAccount(account);
            } else {
                launcher.getAccounts().remove(account);
            }

            Persistence.commitAndForget(launcher.getAccounts());
            return account.getCurrentProfile().map(profile -> new YggdrasilSession(account, profile)).orElse(null);
        }

        @Override
        public double getProgress() {
            return -1;
        }

        @Override
        public String getStatus() {
            return SharedLocale.tr("login.loggingInStatus");
        }
    }
}