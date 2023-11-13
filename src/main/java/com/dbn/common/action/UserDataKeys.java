package com.dbn.common.action;

import com.dbn.common.compatibility.Compatibility;
import com.dbn.common.project.ProjectRef;
import com.dbn.common.ui.form.DBNForm;
import com.dbn.connection.mapping.FileConnectionContext;
import com.dbn.diagnostics.data.DiagnosticCategory;
import com.dbn.execution.statement.processor.StatementExecutionProcessor;
import com.dbn.language.common.DBLanguageDialect;
import com.intellij.debugger.ui.breakpoints.LineBreakpoint;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.List;

import static com.dbn.common.util.Commons.nvl;
import static com.dbn.common.util.Unsafe.cast;

public interface UserDataKeys {
    @Compatibility
    Key<TextEditor> TEXT_EDITOR_KEY = nvl(cast(Key.findKeyByName("textEditor")), () -> Key.create("textEditor"));

    Key<Boolean> INVALID_ENTITY = Key.create("DBNavigator.InvalidEntity");
    Key<String> ACTION_PLACE = Key.create("DBNavigator.ActionPlace");
    Key<Boolean> PROJECT_SETTINGS_LOADED = Key.create("DBNavigator.ProjectSettingsLoaded");
    Key<ProjectRef> PROJECT_REF = Key.create("DBNavigator.ProjectRef");
    Key<List<StatementExecutionProcessor>> STATEMENT_EXECUTION_PROCESSORS = Key.create("DBNavigator.StatementExecutionProcessors");
    Key<FileConnectionContext> FILE_CONNECTION_MAPPING = Key.create("DBNavigator.FileConnectionMapping");
    Key<Boolean> HAS_CONNECTIVITY_CONTEXT = Key.create("DBNavigator.HasConnectivityContext");
    Key<DBLanguageDialect> LANGUAGE_DIALECT = Key.create("DBNavigator.LanguageDialect");
    Key<String> GUARDED_BLOCK_REASON = Key.create("DBNavigator.GuardedBlockReason");
    Key<DiagnosticCategory> DIAGNOSTIC_CONTENT_CATEGORY = Key.create("DBNavigator.DiagnosticContentType");
    Key<DBNForm> DIAGNOSTIC_CONTENT_FORM = Key.create("DBNavigator.DiagnosticContentForm");
    Key<Integer> BREAKPOINT_ID = Key.create("DBNavigator.BreakpointId");
    Key<VirtualFile> BREAKPOINT_FILE = Key.create("DBNavigator.BreakpointFile");
    Key<LineBreakpoint> LINE_BREAKPOINT = Key.create("DBNavigator.LineBreakpoint");
    Key<Boolean> SKIP_BROWSER_AUTOSCROLL = Key.create("DBNavigator.SkipEditorScroll");
    Key<Long> LAST_ANNOTATION_REFRESH = Key.create("DBNavigator.LastAnnotationRefresh");
}
