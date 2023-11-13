package com.dbn.common.ui.component;

import com.dbn.common.dispose.StatefulDisposable;
import com.dbn.common.project.ProjectSupplier;
import com.intellij.openapi.Disposable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static com.dbn.common.dispose.Failsafe.nn;

public interface DBNComponent extends StatefulDisposable, ProjectSupplier {
    @Nullable
    <T extends Disposable> T getParentComponent();

    @NotNull
    default <T extends Disposable> T ensureParentComponent() {
        return nn(getParentComponent());
    }

    @NotNull
    JComponent getComponent();


}
