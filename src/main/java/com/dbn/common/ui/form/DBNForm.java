package com.dbn.common.ui.form;

import com.dbn.common.action.DataProviderDelegate;
import com.dbn.common.ui.component.DBNComponent;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public interface DBNForm extends DBNComponent, DataProviderDelegate {

    @Nullable
    default JComponent getPreferredFocusedComponent() {return null;}

}
