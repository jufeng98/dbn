package com.dbn.connection.transaction.options.ui;

import com.dbn.common.options.ui.ConfigurationEditorForm;
import com.dbn.connection.transaction.TransactionOption;
import com.dbn.connection.transaction.options.TransactionManagerSettings;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.NotNull;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import static com.dbn.common.ui.util.ComboBoxes.*;

public class TransactionManagerSettingsForm extends ConfigurationEditorForm<TransactionManagerSettings> {
    private JPanel mainPanel;
    private JComboBox<TransactionOption> uncommittedChangesOnProjectCloseComboBox;
    private JComboBox<TransactionOption> uncommittedChangesOnSwitchComboBox;
    private JComboBox<TransactionOption> uncommittedChangesOnDisconnectComboBox;
    private JComboBox<TransactionOption> multipleChangesOnCommitComboBox;
    private JComboBox<TransactionOption> multipleChangesOnRollbackComboBox;

    public TransactionManagerSettingsForm(TransactionManagerSettings settings) {
        super(settings);

        initComboBox(uncommittedChangesOnProjectCloseComboBox,
                TransactionOption.ASK,
                TransactionOption.COMMIT,
                TransactionOption.ROLLBACK,
                TransactionOption.REVIEW_CHANGES);


        initComboBox(uncommittedChangesOnSwitchComboBox,
                TransactionOption.ASK,
                TransactionOption.COMMIT,
                TransactionOption.ROLLBACK,
                TransactionOption.REVIEW_CHANGES);

        initComboBox(uncommittedChangesOnDisconnectComboBox,
                TransactionOption.ASK,
                TransactionOption.COMMIT,
                TransactionOption.ROLLBACK,
                TransactionOption.REVIEW_CHANGES);

        initComboBox(multipleChangesOnCommitComboBox,
                TransactionOption.ASK,
                TransactionOption.COMMIT,
                TransactionOption.REVIEW_CHANGES);

        initComboBox(multipleChangesOnRollbackComboBox,
                TransactionOption.ASK,
                TransactionOption.ROLLBACK,
                TransactionOption.REVIEW_CHANGES);

        resetFormChanges();
        registerComponent(mainPanel);
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    @Override
    public void applyFormChanges() throws ConfigurationException {
        TransactionManagerSettings settings = getConfiguration();
        settings.getCloseProject().set(           getSelection(uncommittedChangesOnProjectCloseComboBox));
        settings.getToggleAutoCommit().set(       getSelection(uncommittedChangesOnSwitchComboBox));
        settings.getDisconnect().set(             getSelection(uncommittedChangesOnDisconnectComboBox));
        settings.getCommitMultipleChanges().set(  getSelection(multipleChangesOnCommitComboBox));
        settings.getRollbackMultipleChanges().set(getSelection(multipleChangesOnRollbackComboBox));
    }

    @Override
    public void resetFormChanges() {
        TransactionManagerSettings settings = getConfiguration();
        setSelection(uncommittedChangesOnProjectCloseComboBox, settings.getCloseProject().get());
        setSelection(uncommittedChangesOnSwitchComboBox,       settings.getToggleAutoCommit().get());
        setSelection(uncommittedChangesOnDisconnectComboBox,   settings.getDisconnect().get());
        setSelection(multipleChangesOnCommitComboBox,          settings.getCommitMultipleChanges().get());
        setSelection(multipleChangesOnRollbackComboBox,        settings.getRollbackMultipleChanges().get());

    }
}
