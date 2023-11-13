package com.dbn.credentials;

import com.dbn.common.component.ApplicationComponentBase;
import com.dbn.common.util.Strings;
import com.dbn.connection.ConnectionId;
import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.dbn.common.component.Components.applicationService;

public class DatabaseCredentialManager extends ApplicationComponentBase {
    public static boolean USE = false;

    public DatabaseCredentialManager() {
        super("DBNavigator.DatabaseCredentialManager");
    }

    public static DatabaseCredentialManager getInstance() {
        return applicationService(DatabaseCredentialManager.class);
    }


    public void removePassword(@NotNull ConnectionId connectionId, @NotNull String userName) {
        setPassword(connectionId, userName, null);
    }

    public void setPassword(@NotNull ConnectionId connectionId, @NotNull String userName, @Nullable String password) {
        CredentialAttributes credentialAttributes = createCredentialAttributes(connectionId, userName);
        Credentials credentials = Strings.isEmpty(password) ? null : new Credentials(userName, password);

        PasswordSafe passwordSafe = PasswordSafe.getInstance();
        passwordSafe.set(credentialAttributes, credentials, false);
    }

    @Nullable
    public String getPassword(@NotNull ConnectionId connectionId, @NotNull String userName) {
        CredentialAttributes credentialAttributes = createCredentialAttributes(connectionId, userName);

        PasswordSafe passwordSafe = PasswordSafe.getInstance();
        Credentials credentials = passwordSafe.get(credentialAttributes);
        return credentials == null ? null : credentials.getPasswordAsString() ;
    }

    @NotNull
    private CredentialAttributes createCredentialAttributes(ConnectionId connectionId, String userName) {
        String serviceName = "DBNavigator.Connection." + connectionId;
        return new CredentialAttributes(serviceName, userName, this.getClass(), false);
    }

    private boolean isMemoryStorage() {
        return PasswordSafe.getInstance().isMemoryOnly();
    }
}
