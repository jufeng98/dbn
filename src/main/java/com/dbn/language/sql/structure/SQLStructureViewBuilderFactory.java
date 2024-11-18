package com.dbn.language.sql.structure;

import com.dbn.common.editor.structure.EmptyStructureViewModel;
import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.TreeBasedStructureViewBuilder;
import com.intellij.lang.PsiStructureViewFactory;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiEditorUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.dbn.diagnostics.Diagnostics.conditionallyLog;

public class SQLStructureViewBuilderFactory implements PsiStructureViewFactory {

    @Override
    public StructureViewBuilder getStructureViewBuilder(@NotNull final PsiFile psiFile) {
        return new TreeBasedStructureViewBuilder() {
            @NotNull
            //@Override TODO older versions support. Decommission
            public StructureViewModel createStructureViewModel() {
                return createStructureViewModel(null);
            }

            @NotNull
            @Override
            public StructureViewModel createStructureViewModel(@Nullable Editor editor) {
                try {
                    return !psiFile.isValid() ||
                            psiFile.getProject().isDisposed() ||
                            PsiEditorUtil.getInstance() == null ?
                            EmptyStructureViewModel.INSTANCE :
                            new SQLStructureViewModel(editor, psiFile);
                } catch (Throwable e) {
                    conditionallyLog(e);
                    // TODO dirty workaround (compatibility issue)
                    return EmptyStructureViewModel.INSTANCE;
                }
            }
        };
    }
}