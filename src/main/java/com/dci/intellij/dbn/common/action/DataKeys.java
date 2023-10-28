package com.dci.intellij.dbn.common.action;

import com.dci.intellij.dbn.connection.config.ui.ConnectionBundleSettingsForm;
import com.dci.intellij.dbn.data.editor.ui.array.ArrayEditorPopupProviderForm;
import com.dci.intellij.dbn.data.editor.ui.calendar.CalendarPopupProviderForm;
import com.dci.intellij.dbn.data.editor.ui.text.TextEditorPopupProviderForm;
import com.dci.intellij.dbn.diagnostics.ui.ParserDiagnosticsForm;
import com.dci.intellij.dbn.editor.data.DatasetEditor;
import com.dci.intellij.dbn.editor.session.SessionBrowser;
import com.dci.intellij.dbn.execution.explain.result.ExplainPlanResult;
import com.dci.intellij.dbn.execution.logging.DatabaseLoggingResult;
import com.dci.intellij.dbn.execution.method.result.MethodExecutionResult;
import com.dci.intellij.dbn.execution.method.result.ui.MethodExecutionCursorResultForm;
import com.dci.intellij.dbn.execution.statement.result.StatementExecutionCursorResult;
import com.dci.intellij.dbn.object.DBArgument;
import com.intellij.openapi.actionSystem.DataKey;

public interface DataKeys {
    DataKey<DatasetEditor> DATASET_EDITOR = DataKey.create("DBNavigator.DatasetEditor");
    DataKey<ConnectionBundleSettingsForm> CONNECTION_BUNDLE_SETTINGS = DataKey.create("DBNavigator.ConnectionSettingsEditor");
    DataKey<SessionBrowser> SESSION_BROWSER = DataKey.create("DBNavigator.SessionBrowser");
    DataKey<StatementExecutionCursorResult> STATEMENT_EXECUTION_CURSOR_RESULT = DataKey.create("DBNavigator.StatementExecutionCursorResult");
    DataKey<MethodExecutionResult> METHOD_EXECUTION_RESULT = DataKey.create("DBNavigator.MethodExecutionResult");
    DataKey<MethodExecutionCursorResultForm> METHOD_EXECUTION_CURSOR_RESULT_FORM = DataKey.create("DBNavigator.MethodExecutionCursorResult");
    DataKey<DBArgument> METHOD_EXECUTION_ARGUMENT = DataKey.create("DBNavigator.MethodExecutionArgument");
    DataKey<ExplainPlanResult> EXPLAIN_PLAN_RESULT = DataKey.create("DBNavigator.ExplainPlanResult");
    DataKey<DatabaseLoggingResult> DATABASE_LOG_OUTPUT = DataKey.create("DBNavigator.DatabaseLogOutput");
    DataKey<ParserDiagnosticsForm> PARSER_DIAGNOSTICS_FORM = DataKey.create("DBNavigator.ParserDiagnosticsForm");

    DataKey<CalendarPopupProviderForm> CALENDAR_POPUP_PROVIDER_FORM = DataKey.create("DBNavigator.CalendarPopupProviderForm");
    DataKey<ArrayEditorPopupProviderForm> ARRAY_EDITOR_POPUP_PROVIDER_FORM = DataKey.create("DBNavigator.ArrayEditorPopupProviderForm");
    DataKey<TextEditorPopupProviderForm> TEXT_EDITOR_POPUP_PROVIDER_FORM = DataKey.create("DBNavigator.TextEditorPopupProviderForm");
}
