package com.dci.intellij.dbn.common;

import com.intellij.openapi.util.ScalableIcon;
import com.intellij.ui.RowIcon;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

import static com.intellij.openapi.util.IconLoader.findIcon;

@Slf4j
@UtilityClass
public class Icons {
    private static final Map<String, Icon> REGISTERED_ICONS = new HashMap<>();

    public static final Icon DBN_SPLASH = load("/img/DBN.png");
    public static final Icon DONATE = load("/img/Donate.png");
    public static final Icon DONATE_DISABLED = load("/img/DonateDisabled.png");

    public static final Icon COMMON_INFO = load("/img/common/Info.png");
    public static final Icon COMMON_INFO_DISABLED = load("/img/common/InfoDisabled.png");
    public static final Icon COMMON_WARNING = load("/img/common/Warning.png");
    public static final Icon COMMON_ERROR = load("/img/common/Error.png");
    public static final Icon COMMON_CHECK = load("/img/common/Check.png");
    public static final Icon COMMON_RIGHT = load("/img/common/SplitRight.png");
    public static final Icon COMMON_LEFT = load("/img/common/SplitLeft.png");
    public static final Icon COMMON_UP = load("/img/common/SplitUp.png");
    public static final Icon COMMON_DOWN = load("/img/common/SplitDown.png");
    public static final Icon COMMON_ARROW_DOWN = load("/img/ComboBoxArrow.png");
    public static final Icon COMMON_TIMER = load("/img/Timer.png");
    public static final Icon COMMON_DATA_GRID = load("/img/common/DataGrid.png");

    public static final Icon COMMON_FILTER_ACTIVE =    load("/img/ActiveFilterCondition.png");
    public static final Icon COMMON_FILTER_INACTIVE =    load("/img/InactiveFilterCondition.png");



    public static final Icon ACTION_COPY = load("/img/action/Copy.png");
    public static final Icon ACTION_SORT_ALPHA = load("/img/action/SortAlphabetically.png");
    public static final Icon ACTION_SORT_NUMERIC = load("/img/action/SortNumerically.png");
    public static final Icon ACTION_SORT_ASC = load("/img/action/SortAscending.png");
    public static final Icon ACTION_SORT_DESC = load("/img/action/SortDescending.png");
    public static final Icon ACTION_ADD = load("/img/action/Add.png");
    public static final Icon ACTION_ADD_MORE = load("/img/action/AddMore.png");
    public static final Icon ACTION_ADD_SPECIAL = load("/img/action/AddSpecial.png");
    public static final Icon ACTION_REMOVE = load("/img/action/Remove.png");
    public static final Icon ACTION_MOVE_UP = load("/img/action/MoveUp.png");
    public static final Icon ACTION_MOVE_DOWN = load("/img/action/MoveDown.png");
    public static final Icon ACTION_EDIT = load("/img/action/EditSource.png");
    public static final Icon ACTION_SETTINGS = load("/img/action/Properties.png");
    public static final Icon ACTION_LOCAL_SETTINGS = load("/img/action/LocalSettings.png");
    public static final Icon ACTION_COLLAPSE_ALL = load("/img/action/CollapseAll.png");
    public static final Icon ACTION_EXPAND_ALL = load("/img/action/ExpandAll.png");
    public static final Icon ACTION_GROUP = load("/img/action/Group.png");
    public static final Icon ACTION_DELETE = load("/img/action/Delete.png");
    public static final Icon ACTION_EXECUTE = load("/img/action/Execute.png");
    public static final Icon ACTION_CLOSE = load("/img/action/Close.png");
    public static final Icon ACTION_UP_DOWN = load("/img/action/UpDown.png");
    public static final Icon ACTION_REFRESH = load("/img/action/Synchronize.png");
    public static final Icon ACTION_TIMED_REFRESH = load("/img/action/TimedSynchronize.png");
    public static final Icon ACTION_TIMED_REFRESH_INTERRUPTED = load("/img/action/TimedSynchronizeInterrupted.png");
    public static final Icon ACTION_TIMED_REFRESH_OFF = load("/img/action/TimedSynchronizeOff.png");
    public static final Icon ACTION_FIND = load("/img/action/Find.png");
    public static final Icon ACTION_WRAP_TEXT = load("/img/action/WrapText.png");
    public static final Icon ACTION_RERUN = load("/img/action/Rerun.png");
    public static final Icon ACTION_PIN = load("/img/action/Pin.png");
    public static final Icon ACTION_REVERT_CHANGES = load("/img/action/RevertChanges.png");
    public static final Icon ACTION_SAVE_CHANGES = load("/img/action/Save.png");
    public static final Icon ACTION_SELECT_ALL = load("/img/action/SelectAll.png");
    public static final Icon ACTION_OPTIONS = load("/img/action/Options.png");
    public static final Icon ACTION_OPTIONS_MENU = load("/img/action/OptionsMenu.png");
    public static final Icon ACTION_NAVIGATE = load("/img/action/Navigate.png");
    public static final Icon ACTION_DISCONNECT_SESSION = load("/img/action/DisconnectSession.png");
    public static final Icon ACTION_KILL_SESSION = load("/img/action/KillSession.png");

    public static final Icon ACTION_SAVE_TO_SETTINGS = load("/img/action/SaveToSettings.png");
    public static final Icon ACTION_REVERT_FROM_SETTINGS = load("/img/action/RevertFromSettings.png");
    public static final Icon FOLDER_CONNECTION_ASSOCIATION = load("/img/action/FolderConnectionLink.png");
    public static final Icon FOLDER_SCHEMA_ASSOCIATION = load("/img/action/FolderSchemaLink.png");


    public static final Icon DATABASE_NAVIGATOR = load("/img/project/DatabaseNavigator.svg");
    public static final Icon DATABASE_MODULE = load("/img/project/DatabaseModule.png");
    public static final Icon DATABASE_MODULE_SMALL_OPEN = load("/img/project/DatabaseModuleOpen.png");
    public static final Icon DATABASE_MODULE_SMALL_CLOSED = load("/img/project/DatabaseModuleClosed.png");

    public static final Icon WINDOW_DATABASE_BROWSER = load("/img/window/DatabaseBrowser.png");
    public static final Icon WINDOW_EXECUTION_CONSOLE = load("/img/window/ExecutionConsole.png");
    public static final Icon WINDOW_DATABASE_DIAGNOSTICS = load("/img/window/DatabaseDiagnostics.png");

    public static final Icon FILE_SQL_CONSOLE = load("/img/file/SQLConsole.png");
    public static final Icon FILE_SQL_DEBUG_CONSOLE = load("/img/file/SQLDebugConsole.png");
    public static final Icon FILE_SESSION_BROWSER = load("/img/file/SessionBrowser.png");
    public static final Icon FILE_SQL = load("/img/file/SQLFile.png");
    public static final Icon FILE_PLSQL = load("/img/file/PLSQLFile.png");
    public static final Icon FILE_BLOCK_PLSQL = load("FILE_BLOCK_PLSQL", "/img/PLSQLBlock.png");
    public static final Icon FILE_BLOCK_PSQL = load("FILE_BLOCK_PSQL", "/img/file/PSQLBlock.png");
    public static final Icon FILE_BLOCK_SQL = load("FILE_BLOCK_SQL", "/img/file/SQLBlock.png");


    public static final Icon DIALOG_INFO = load("/img/dialog/Info.png");
    public static final Icon DIALOG_INFORMATION = load("/img/dialog/Information.png");
    public static final Icon DIALOG_WARNING  = load("/img/dialog/Warning.png");
    public static final Icon DIALOG_ERROR    = load("/img/dialog/Error.png");
    public static final Icon DIALOG_QUESTION = load("/img/dialog/Question.png");


    public static final Icon METHOD_EXECUTION_RUN     = load("/img/action/ExecuteMethod.png");
    public static final Icon METHOD_EXECUTION_DEBUG   = load("/img/action/DebugMethod.png");
    public static final Icon METHOD_EXECUTION_RERUN   = load("/img/RerunMethodExecution.png");
    public static final Icon METHOD_EXECUTION_DIALOG  = load("/img/ExecuteMethodDialog.png");
    public static final Icon METHOD_EXECUTION_STOP    = load("/img/action/ExecutionStop.png");
    public static final Icon METHOD_EXECUTION_HISTORY = load("/img/MethodExecutionHistory.png");
    public static final Icon METHOD_LOOKUP            = load("/img/MethodLookup.png");
    public static final Icon METHOD_CALL              = load("METHOD_CALL", "/img/MethodCall.png");
    public static final Icon SQL_STATEMENT            = load("SQL_STATEMENT", "/img/SQLStatement.png");


    public static final Icon KILL_PROCESS               = load("/img/action/KillProcess.png");
    public static final Icon EXECUTE_SQL_SCRIPT         = load("/img/action/ExecuteAsScript.png");

    public static final Icon STMT_EXECUTION_EXPLAIN       = load("/img/action/StatementExplain.png");
    public static final Icon STMT_EXECUTION_RUN           = load("/img/action/StatementExecution.png");
    public static final Icon STMT_EXECUTION_STOP          = load("/img/action/StatementExecutionStop.png");
    public static final Icon STMT_EXECUTION_STOP_QUEUED   = load("/img/action/StatementExecutionStopQueued.png");
    public static final Icon STMT_EXECUTION_DEBUG         = load("/img/action/StatementDebug.png");
    public static final Icon STMT_EXECUTION_RERUN         = load("/img/action/StatementExecutionRerun.png");
    public static final Icon STMT_EXECUTION_RESUME        = load("/img/action/StatementExecutionResume.png");
    public static final Icon STMT_EXECUTION_REFRESH       = load("/img/action/Refresh.png");
    public static final Icon STMT_EXECUTION_ERROR         = load("/img/common/Error.png");
    public static final Icon STMT_EXECUTION_ERROR_RERUN   = load("/img/action/StatementExecutionError.png");
    public static final Icon STMT_EXECUTION_WARNING       = load("/img/common/Warning.png");
    public static final Icon STMT_EXECUTION_WARNING_RERUN = load("/img/action/StatementExecutionWarning.png");
    public static final Icon STMT_EXECUTION_INFO_RERUN    = load("/img/action/StatementExecutionInfo.png");
    public static final Icon STMT_EXECUTION_NAVIGATE      = load("/img/action/NavigateToResult.png");

    public static final Icon EXPLAIN_PLAN_RESULT        = load("/img/ExplainPlanResult.png");
    public static final Icon STMT_EXEC_RESULTSET        = load("/img/ExecutionResultSet.png");
    public static final Icon STMT_EXEC_RESULTSET_RERUN  = load("/img/ExecutionResultSetRerun.png");
    public static final Icon STMT_EXEC_RESULTSET_ORPHAN = load("/img/ExecutionResultSetOrphan.png");

    public static final Icon EXEC_RESULT_RERUN              = load("/img/action/Refresh.png");
    public static final Icon EXEC_RESULT_OPEN_EXEC_DIALOG   = load("/img/ExecuteMethodDialog.png");
    public static final Icon EXEC_RESULT_RESUME             = load("/img/action/ExecutionResume.png");
    public static final Icon EXEC_RESULT_STOP               = load("/img/action/ExecutionStop.png");
    public static final Icon EXEC_RESULT_CLOSE              = load("/img/action/Close.png");
    public static final Icon EXEC_RESULT_VIEW_STATEMENT     = load("/img/action/Preview.png");
    public static final Icon EXEC_RESULT_VIEW_RECORD        = load("/img/RecordViewer.png");
    public static final Icon EXEC_RESULT_OPTIONS            = load("/img/action/Options.png");
    public static final Icon EXEC_RESULT_MESSAGES           = load("/img/common/Messages.png");
    public static final Icon EXEC_CONFIG                    = load("/img/DBProgram.png");
    public static final Icon EXEC_METHOD_CONFIG             = load("/img/DBMethodExecution.png");
    public static final Icon EXEC_STATEMENT_CONFIG          = load("/img/DBStatementExecution.png");
    public static final Icon EXEC_LOG_OUTPUT_CONSOLE        = load("/img/LogOutputConsole.png");
    public static final Icon EXEC_LOG_OUTPUT_CONSOLE_UNREAD = load("/img/LogOutputConsoleUnread.png");
    public static final Icon EXEC_LOG_OUTPUT_ENABLE         = load("/img/LogOutputEnable.png");
    public static final Icon EXEC_LOG_OUTPUT_DISABLE        = load("/img/LogOutputDisable.png");

    public static final Icon NAVIGATION_GO_TO_SPEC       = load("/img/GoToSpec.png");
    public static final Icon NAVIGATION_GO_TO_BODY       = load("/img/GoToBody.png");

    public static final Icon BROWSER_BACK = load("/img/action/BrowserBack.png");
    public static final Icon BROWSER_NEXT = load("/img/action/BrowserForward.png");
    public static final Icon BROWSER_AUTOSCROLL_TO_EDITOR = load("/img/action/AutoscrollToSource.png");
    public static final Icon BROWSER_AUTOSCROLL_FROM_EDITOR = load("/img/action/AutoscrollFromSource.png");
    public static final Icon BROWSER_OBJECT_PROPERTIES = load("/img/ObjectProperties.png");


    public static final Icon DATA_EDITOR_ROW_DEFAULT = load("/img/DefaultRow.png");
    public static final Icon DATA_EDITOR_ROW_INSERT = load("/img/InsertRow.png");
    public static final Icon DATA_EDITOR_ROW_INSERTED = load("/img/NewRow.png");
    public static final Icon DATA_EDITOR_ROW_MODIFIED = load("/img/ModifiedRow.png");
    public static final Icon DATA_EDITOR_ROW_DELETED = load("/img/DeletedRow.png");

    public static final Icon DATA_EDITOR_DUPLICATE_RECORD = load("/img/action/DuplicateRecord.png");
    public static final Icon DATA_EDITOR_INSERT_RECORD = load("/img/action/InsertRecord.png");
    public static final Icon DATA_EDITOR_DELETE_RECORD = load("/img/action/DeleteRecord.png");
    public static final Icon DATA_EDITOR_SWITCH_EDITABLE_STATUS = load("/img/DatasetEditorSwitchEditableStatus.png");
    public static final Icon DATA_EDITOR_FETCH_NEXT_RECORDS = load("/img/action/ExecutionResume.png");
    public static final Icon DATA_EDITOR_EDIT_RECORD = load("/img/EditDatasetRecord.png");
    public static final Icon DATA_EDITOR_NEXT_RECORD = load("/img/NextRecord.png");
    public static final Icon DATA_EDITOR_PREVIOUS_RECORD = load("/img/PreviousRecord.png");
    public static final Icon DATA_EDITOR_FIRST_RECORD = load("/img/FirstRecord.png");
    public static final Icon DATA_EDITOR_LAST_RECORD = load("/img/LastRecord.png");
    public static final Icon DATA_EDITOR_LOCK_EDITING = load("/img/LockEditing.png");
    public static final Icon DATA_EDITOR_SORT_ASC = load("/img/action/DataEditorSortAscending.png");
    public static final Icon DATA_EDITOR_SORT_DESC = load("/img/action/DataEditorSortDescending.png");
    public static final Icon DATA_EDITOR_STOP_LOADING = load("/img/action/StopLoading.png");


    public static final Icon DATA_EDITOR_RELOAD_DATA = load("/img/action/Refresh.png");
    public static final Icon DATA_EDITOR_BROWSE =    load("/img/ButtonBrowse.png");
    public static final Icon DATA_EDITOR_CALENDAR =    load("/img/ButtonCalendar.png");
    public static final Icon DATA_EDITOR_LIST =    load("/img/ButtonList.png");

    public static final Icon DATA_EXPORT =    load("/img/action/DataExport.png");
    public static final Icon DATA_IMPORT =    load("/img/action/DataImport.png");
    public static final Icon DATA_SORTING =    load("/img/action/DataSorting.png");
    public static final Icon DATA_SORTING_ASC =    load("/img/action/DataSortingAsc.png");
    public static final Icon DATA_SORTING_DESC =    load("/img/action/DataSortingDesc.png");
    public static final Icon DATA_COLUMNS =    load("/img/action/ColumnSetup.png");

    public static final Icon TOP_LEVEL_FILTER =    load("/img/TopLevelFilter.png");
    public static final Icon DATASET_FILTER =    load("/img/filter/DatasetFilter.png");
    public static final Icon DATASET_FILTER_CLEAR =    load("/img/filter/DatasetFilterClear.png");
    public static final Icon DATASET_FILTER_NEW =    load("/img/filter/DatasetFilterNew.png");
    public static final Icon DATASET_FILTER_EDIT =    load("/img/filter/DatasetFilterEdit.png");
    public static final Icon DATASET_FILTER_BASIC =    load("/img/filter/DatasetFilterBasic.png");
    public static final Icon DATASET_FILTER_BASIC_ERR =    load("/img/filter/DatasetFilterBasicErr.png");
    public static final Icon DATASET_FILTER_BASIC_TEMP =    load("/img/filter/DatasetFilterBasicTemp.png");
    public static final Icon DATASET_FILTER_BASIC_TEMP_ERR =    load("/img/filter/DatasetFilterBasicTempErr.png");
    public static final Icon DATASET_FILTER_CUSTOM =    load("/img/filter/DatasetFilterCustom.png");
    public static final Icon DATASET_FILTER_CUSTOM_ERR =    load("/img/filter/DatasetFilterCustomErr.png");
    public static final Icon DATASET_FILTER_GLOBAL =    load("/img/filter/DatasetFilterGlobal.png");
    public static final Icon DATASET_FILTER_GLOBAL_ERR =    load("/img/filter/DatasetFilterGlobalErr.png");
    public static final Icon DATASET_FILTER_EMPTY =    load("/img/filter/DatasetFilterEmpty.png");

    public static final Icon DATASET_FILTER_CONDITION_REMOVE =    load("/img/RemoveFilterCondition.png");
    public static final Icon DATASET_FILTER_CONDITION_NEW =    load("/img/NewFilterCondition.png");


    public static final Icon CONDITION_JOIN_TYPE =    load("/img/JoinTypeSwitch.png");

    public static final Icon TEXT_CELL_EDIT_ACCEPT = load("/img/CellEditAccept.png");
    public static final Icon TEXT_CELL_EDIT_REVERT = load("/img/CellEditRevert.png");
    public static final Icon TEXT_CELL_EDIT_DELETE = load("/img/CellEditDelete.png");
    public static final Icon ARRAY_CELL_EDIT_ADD    = load("/img/CellEditAdd.png");
    public static final Icon ARRAY_CELL_EDIT_REMOVE = load("/img/CellEditRemove.png");

    public static final Icon CALENDAR_NEXT_MONTH = load("/img/CalendarNextMonth.png");
    public static final Icon CALENDAR_NEXT_YEAR = load("/img/CalendarNextYear.png");
    public static final Icon CALENDAR_PREVIOUS_MONTH = load("/img/CalendarPreviousMonth.png");
    public static final Icon CALENDAR_PREVIOUS_YEAR = load("/img/CalendarPreviousYear.png");
    public static final Icon CALENDAR_CLEAR_TIME = load("/img/CalendarResetTime.png");

    public static final Icon EXEC_MESSAGES_INFO             = load("/img/common/Info.png");
    public static final Icon EXEC_MESSAGES_INFO_INACTIVE    = load("/img/common/InfoInactive.png");
    public static final Icon EXEC_MESSAGES_WARNING          = load("/img/common/Warning.png");
    public static final Icon EXEC_MESSAGES_WARNING_INACTIVE = load("/img/common/WarningInactive.png");
    public static final Icon EXEC_MESSAGES_ERROR            = load("/img/common/Error.png");
    public static final Icon EXEC_MESSAGES_ERROR_INACTIVE   = load("/img/common/ErrorInactive.png");

    public static final Icon CHECK   = load("/img/common/Checked.png");
    public static final Icon PROJECT = load("/img/project/Project.png");
    public static final Icon FILE_CONNECTION_MAPPING = load("/img/FileConnection.png");
    public static final Icon FILE_SCHEMA_MAPPING = load("/img/FileSchema.png");
    public static final Icon FILE_SESSION_MAPPING = load("/img/FileSession.png");

    public static final Icon CODE_EDITOR_SAVE_TO_DATABASE = load("/img/action/SaveToDatabase.png");
    public static final Icon CODE_EDITOR_SAVE_TO_FILE = load("/img/action/SaveToFile.png");
    public static final Icon CODE_EDITOR_RESET = load("/img/action/Reset.png");
    public static final Icon CODE_EDITOR_RELOAD = load("/img/action/Refresh.png");
    public static final Icon CODE_EDITOR_DIFF = load("/img/action/ShowDiff.png");
    public static final Icon CODE_EDITOR_DIFF_DB = load("/img/action/ShowDbDiff.png");
    public static final Icon CODE_EDITOR_DDL_FILE = load("/img/DDLFile.png");
    public static final Icon CODE_EDITOR_DDL_FILE_NEW = load("/img/DDLFileNew.png");
    public static final Icon CODE_EDITOR_DDL_FILE_DETACH = load("/img/DDLFileUnbind.png");
    public static final Icon CODE_EDITOR_DDL_FILE_ATTACH = load("/img/DDLFileBind.png");
    public static final Icon CODE_EDITOR_SPEC = load("/img/CodeSpec.png");
    public static final Icon CODE_EDITOR_BODY = load("/img/CodeBody.png");

    public static final Icon OBEJCT_COMPILE     = load("/img/action/Compile.png");
    public static final Icon OBEJCT_COMPILE_DEBUG = load("/img/action/CompileDebug.png");
    //public static final Icon OBEJCT_COMPILE_KEEP = load("/main.resources.img/CompileKeep.png");
    public static final Icon OBEJCT_COMPILE_ASK = load("/img/action/CompileAsk.png");
    public static final Icon OBEJCT_EDIT_SOURCE = load("/img/EditSource.png");
    public static final Icon OBEJCT_EDIT_DATA = load("/img/EditData.png");
    public static final Icon OBEJCT_VIEW_DATA = load("/img/ViewData.png");

    public static final Icon CONNECTION_COMMIT   = load("CONNECTION_COMMIT", "/img/action/ConnectionCommit.png");
    public static final Icon CONNECTION_ROLLBACK = load("CONNECTION_ROLLBACK", "/img/action/ConnectionRollback.png");
    public static final Icon CONNECTION_DUPLICATE = load("/img/action/DuplicateConnection.png");
    public static final Icon CONNECTION_COPY = load("/img/action/CopyConnection.png");
    public static final Icon CONNECTION_PASTE = load("/img/action/PasteConnection.png");

    public static final Icon COMMON_DIRECTION_IN = load("/img/common/DirectionIn.png");
    public static final Icon COMMON_DIRECTION_OUT = load("/img/common/DirectionOut.png");
    public static final Icon COMMON_DIRECTION_IN_OUT = load("/img/common/DirectionInOut.png");




    public static final Icon CONN_STATUS_INVALID      = load("/img/common/ErrorBig.png");
    public static final Icon CONN_STATUS_CONNECTED    = load("/img/common/BulbOn.png");
    public static final Icon CONN_STATUS_DISCONNECTED = load("/img/common/BulbOff.png");

    public static final Icon CONNECTION_VIRTUAL       = load("/img/connection/ConnectionVirtual.png");
    public static final Icon CONNECTION_CONNECTED     = load("/img/connection/ConnectionConnected.png");
    public static final Icon CONNECTION_ACTIVE        = load("/img/connection/ConnectionActive.png");
    public static final Icon CONNECTION_BUSY          = load("/img/connection/ConnectionBusy.png");
    public static final Icon CONNECTION_CONNECTED_NEW = load("/img/connection/ConnectionConnectedNew.png");
    public static final Icon CONNECTION_INACTIVE      = load("/img/connection/ConnectionInactive.png");
    public static final Icon CONNECTION_DISABLED      = load("/img/connection/ConnectionDisabled.png");
    public static final Icon CONNECTION_NEW           = load("/img/connection/ConnectionNew.png");
    public static final Icon CONNECTION_INVALID       = load("/img/connection/ConnectionInvalid.png");
    public static final Icon CONNECTIONS              = load("/img/connection/Connections.png");

    public static final Icon SESSION_CUSTOM               = load("/img/connection/SessionCustom.png");
    public static final Icon SESSION_CUSTOM_CONNECTED     = load("/img/connection/SessionCustomConnected.png");
    public static final Icon SESSION_CUSTOM_TRANSACTIONAL = load("/img/connection/SessionCustomTransactional.png");
    public static final Icon SESSION_MAIN                 = load("/img/connection/SessionMain.png");
    public static final Icon SESSION_MAIN_CONNECTED       = load("/img/connection/SessionMainConnected.png");
    public static final Icon SESSION_MAIN_TRANSACTIONAL   = load("/img/connection/SessionMainTransactional.png");
    public static final Icon SESSION_POOL                 = load("/img/connection/SessionPool.png");
    public static final Icon SESSION_POOL_CONNECTED       = load("/img/connection/SessionPoolConnected.png");
    public static final Icon SESSION_POOL_TRANSACTIONAL   = load("/img/connection/SessionPoolTransactional.png");
    public static final Icon SESSION_DEBUG                = load("/img/connection/SessionDebug.png");
    public static final Icon SESSION_DEBUG_CONNECTED      = load("/img/connection/SessionDebugConnected.png");
    public static final Icon SESSION_DEBUG_TRANSACTIONAL  = load("/img/connection/SessionDebugTransactional.png");


    public static final Icon DB_ORACLE            = load("/img/database/Oracle.png");
    public static final Icon DB_ORACLE_LARGE      = load("/img/database/Oracle_large.png");
    public static final Icon DB_POSTGRESQL        = load("/img/database/PostgreSQL.png");
    public static final Icon DB_POSTGRESQL_LARGE  = load("/img/database/PostgreSQL_large.png");
    public static final Icon DB_MYSQL             = load("/img/database/MySQL.png");
    public static final Icon DB_MYSQL_LARGE       = load("/img/database/MySQL_large.png");
    public static final Icon DB_SQLITE            = load("/img/database/SQLite.png");
    public static final Icon DB_SQLITE_LARGE      = load("/img/database/SQLite_large.png");
    public static final Icon DB_GENERIC           = load("/img/database/Generic.png");
    public static final Icon DB_GENERIC_LARGE     = load("/img/database/Generic_large.png");

//    public static final Icon DBO_ARGUMENT_IN         = createRowIcon(DBO_ARGUMENT, COMMON_DIRECTION_IN);
//    public static final Icon DBO_ARGUMENT_OUT        = createRowIcon(DBO_ARGUMENT, COMMON_DIRECTION_OUT);
//    public static final Icon DBO_ARGUMENT_IN_OUT     = createRowIcon(DBO_ARGUMENT, COMMON_DIRECTION_IN_OUT);

    public static final Icon DBO_ATTRIBUTE                       = load("/img/object/Attribute.png");
    public static final Icon DBO_ATTRIBUTES                      = load("/img/object/Attributes.png");
    public static final Icon DBO_ARGUMENT                        = load("/img/object/Argument.png");
    public static final Icon DBO_ARGUMENTS                       = load("/img/object/Arguments.png");
    public static final Icon DBO_ARGUMENT_IN                     = load("/img/object/ArgumentIn.png");
    public static final Icon DBO_ARGUMENT_OUT                    = load("/img/object/ArgumentOut.png");
    public static final Icon DBO_ARGUMENT_IN_OUT                 = load("/img/object/ArgumentInOut.png");
    public static final Icon DBO_CONSOLE                         = load("/img/object/console/Console.png");
    public static final Icon DBO_CONSOLES                        = load("/img/object/console/Consoles.png");
    public static final Icon DBO_CONSOLE_DEBUG                   = load("/img/object/console/ConsoleDebug.png");
    public static final Icon DBO_CLUSTER                         = load("/img/object/Cluster.png");
    public static final Icon DBO_CLUSTERS                        = load("/img/object/Clusters.png");
    public static final Icon DBO_COLUMN                          = load("/img/object/column/Column.png");
    public static final Icon DBO_COLUMN_PK                       = load("/img/object/column/ColumnPk.png");
    public static final Icon DBO_COLUMN_FK                       = load("/img/object/column/ColumnFk.png");
    public static final Icon DBO_COLUMN_PFK                      = load("/img/object/column/ColumnPkFk.png");
    public static final Icon DBO_COLUMN_HIDDEN                   = load("/img/object/column/ColumnHidden.png");
    public static final Icon DBO_COLUMNS                         = load("/img/object/column/Columns.png");
    public static final Icon DBO_CONSTRAINT                      = load("/img/object/constraint/Constraint.png");
    public static final Icon DBO_CONSTRAINT_DISABLED             = load("/img/object/constraint/ConstraintDisabled.png");
    public static final Icon DBO_CONSTRAINTS                     = load("/img/object/constraint/Constraints.png");
    public static final Icon DBO_DATABASE_LINK                   = load("/img/object/DatabaseLink.png");
    public static final Icon DBO_DATABASE_LINKS                  = load("/img/object/DatabaseLinks.png");
    public static final Icon DBO_DIMENSION                       = load("/img/object/Dimension.png");
    public static final Icon DBO_DIMENSIONS                      = load("/img/object/Dimensions.png");
    public static final Icon DBO_FUNCTION                        = load("DBO_FUNCTION", "/img/object/function/Function.png");
    public static final Icon DBO_FUNCTION_DEBUG                  = load("/img/object/function/FunctionDebug.png");
    public static final Icon DBO_FUNCTION_ERR                    = load("/img/object/function/FunctionErr.png");
    public static final Icon DBO_FUNCTIONS                       = load("/img/object/function/Functions.png");
    public static final Icon DBO_INDEX                           = load("/img/object/index/Index.png");
    public static final Icon DBO_INDEX_DISABLED                  = load("/img/object/index/IndexDisabled.png");
    public static final Icon DBO_INDEXES                         = load("/img/object/index/Indexes.png");
    public static final Icon DBO_MATERIALIZED_VIEW               = load("/img/object/view/MaterializedView.png");
    public static final Icon DBO_MATERIALIZED_VIEWS              = load("/img/object/view/MaterializedViews.png");
    public static final Icon DBO_METHOD                          = load("/img/object/Method.png");
    public static final Icon DBO_METHODS                         = load("/img/object/Methods.png");
    public static final Icon DBO_NESTED_TABLE                    = load("/img/object/NestedTable.png");
    public static final Icon DBO_NESTED_TABLES                   = load("/img/object/NestedTables.png");
    public static final Icon DBO_PACKAGE                         = load("/img/object/package/Package.png");
    public static final Icon DBO_PACKAGE_ERR                     = load("/img/object/package/PackageErr.png");
    public static final Icon DBO_PACKAGE_DEBUG                   = load("/img/object/package/PackageDebug.png");
    public static final Icon DBO_PACKAGES                        = load("/img/object/package/Packages.png");
    public static final Icon DBO_PACKAGE_SPEC                    = load("DBO_PACKAGE_SPEC", "/img/object/package/PackageSpec.png");
    public static final Icon DBO_PACKAGE_BODY                    = load("DBO_PACKAGE_BODY", "/img/object/package/PackageBody.png");
    public static final Icon DBO_PROCEDURE                       = load("DBO_PROCEDURE", "/img/object/Procedure.png");
    public static final Icon DBO_PROCEDURE_ERR                   = load("/img/object/ProcedureErr.png");
    public static final Icon DBO_PROCEDURE_DEBUG                 = load("/img/object/ProcedureDebug.png");
    public static final Icon DBO_PROCEDURES                      = load("/img/object/Procedures.png");
    public static final Icon DBO_PRIVILEGE                       = load("/img/object/privilege/Privilege.png");
    public static final Icon DBO_PRIVILEGES                      = load("/img/object/privilege/Privileges.png");
    public static final Icon DBO_ROLE                            = load("/img/object/role/Role.png");
    public static final Icon DBO_ROLES                           = load("/img/object/role/Roles.png");
    public static final Icon DBO_SCHEMA                          = load("/img/object/schema/Schema.png");
    public static final Icon DBO_SCHEMAS                         = load("/img/object/schema/Schemas.png");
    public static final Icon DBO_SYNONYM                         = load("/img/object/synonym/Synonym.png");
    public static final Icon DBO_SYNONYM_ERR                     = load("/img/object/synonym/SynonymErr.png");
    public static final Icon DBO_SYNONYMS                        = load("/img/object/synonym/Synonyms.png");
    public static final Icon DBO_SEQUENCE                        = load("/img/object/sequence/Sequence.png");
    public static final Icon DBO_SEQUENCES                       = load("/img/object/sequence/Sequences.png");
    public static final Icon DBO_TMP_TABLE                       = load("/img/object/table/TableTmp.png");
    public static final Icon DBO_TABLE                           = load("/img/object/table/Table.png");
    public static final Icon DBO_TABLES                          = load("/img/object/table/Tables.png");
    public static final Icon DBO_TRIGGER                         = load("DBO_TRIGGER", "/img/object/trigger/Trigger.png");
    public static final Icon DBO_TRIGGER_ERR                     = load("/img/object/trigger/TriggerErr.png");
    public static final Icon DBO_TRIGGER_DEBUG                   = load("/img/object/trigger/TriggerDebug.png");
    public static final Icon DBO_TRIGGER_ERR_DISABLED            = load("/img/object/trigger/TriggerErrDisabled.png");
    public static final Icon DBO_TRIGGER_DISABLED                = load("/img/object/trigger/TriggerDisabled.png");
    public static final Icon DBO_TRIGGER_DISABLED_DEBUG          = load("/img/object/trigger/TriggerDisabledDebug.png");
    public static final Icon DBO_TRIGGERS                        = load("/img/object/trigger/Triggers.png");
    public static final Icon DBO_DATABASE_TRIGGER                = load("DBO_DATABASE_TRIGGER","/img/object/DatabaseTrigger.png");
    public static final Icon DBO_DATABASE_TRIGGER_ERR            = load("/img/object/DatabaseTriggerErr.png");
    public static final Icon DBO_DATABASE_TRIGGER_DEBUG          = load("/img/object/DatabaseTriggerDebug.png");
    public static final Icon DBO_DATABASE_TRIGGER_ERR_DISABLED   = load("/img/object/DatabaseTriggerErrDisabled.png");
    public static final Icon DBO_DATABASE_TRIGGER_DISABLED       = load("/img/object/DatabaseTriggerDisabled.png");
    public static final Icon DBO_DATABASE_TRIGGER_DISABLED_DEBUG = load("/img/object/DatabaseTriggerDisabledDebug.png");
    public static final Icon DBO_DATABASE_TRIGGERS               = load("/img/object/DatabaseTriggers.png");
    public static final Icon DBO_TYPE                            = load("/img/object/type/Type.png");
    public static final Icon DBO_TYPE_COLLECTION                 = load("/img/object/type/TypeCollection.png");
    public static final Icon DBO_TYPE_COLLECTION_ERR             = load("/img/object/type/TypeCollectionErr.png");
    public static final Icon DBO_TYPE_ERR                        = load("/img/object/type/TypeErr.png");
    public static final Icon DBO_TYPE_DEBUG                      = load("/img/object/type/TypeDebug.png");
    public static final Icon DBO_TYPE_SPEC                       = load("DBO_TYPE_SPEC", "/img/object/type/TypeSpec.png");
    public static final Icon DBO_TYPE_BODY                       = load("DBO_TYPE_BODY", "/img/object/type/TypeBody.png");
    public static final Icon DBO_TYPES                           = load("/img/object/type/Types.png");
    public static final Icon DBO_USER                            = load("/img/object/user/User.png");
    public static final Icon DBO_USER_EXPIRED                    = load("/img/object/user/UserExpired.png");
    public static final Icon DBO_USER_LOCKED                     = load("/img/object/user/UserLocked.png");
    public static final Icon DBO_USER_EXPIRED_LOCKED             = load("/img/object/user/UserExpiredLocked.png");
    public static final Icon DBO_USERS                           = load("/img/object/user/Users.png");
    public static final Icon DBO_VIEW                            = load("/img/object/view/View.png");
    public static final Icon DBO_VIEW_SYNONYM                    = load("/img/object/view/ViewSynonym.png");
    public static final Icon DBO_VIEWS                           = load("/img/object/view/Views.png");
    public static final Icon DBO_VARIABLE                        = load("/img/object/Variable.png");
    public static final Icon DBO_CURSOR                          = load("/img/object/Cursor.png");
    public static final Icon DBO_LABEL_PK_FK                     = load("/img/object/PrimaryKeyForeignKey.png");
    public static final Icon DBO_LABEL_PK                        = load("/img/object/PrimaryKey.png");
    public static final Icon DBO_LABEL_FK                        = load("/img/object/ForeignKey.png");


    public static final Icon DBO_OUTGOING_REF                    = load("/img/object/OutgoingReference.png");
    public static final Icon DBO_INCOMING_REF                    = load("/img/object/IncomingReference.png");
    public static final Icon DBO_OUTGOING_REF_SOFT               = load("/img/object/OutgoingRefSoft.png");
    public static final Icon DBO_INCOMING_REF_SOFT               = load("/img/object/IncomingRefSoft.png");

    public static final Icon SB_FILTER_SERVER                    = load("/img/filter/SessionFilterServer.png");
    public static final Icon SB_FILTER_STATUS                    = load("/img/filter/SessionFilterStatus.png");
    public static final Icon SB_FILTER_USER                      = load("/img/filter/SessionFilterUser.png");


    public static final Icon DEBUG_INVALID_BREAKPOINT  = load("/img/InvalidBreakpoint.png");



    public static final Icon SPACE                        = load("/img/Space.png");
    public static final Icon TREE_BRANCH                  = load("/img/TreeBranch.png");
    public static final Icon SMALL_TREE_BRANCH            = load("/img/SmallTreeBranch.png");






    private static Icon load(String path) {
        ClassLoader classLoader = Icons.class.getClassLoader();
        String svgPath = path.replace(".png", ".svg");

        try {
            Icon icon = findIcon(svgPath, classLoader);
            if (icon != null && icon.getIconWidth() > 1) return icon;
        } catch (Throwable t) {
            log.error("Failed to load icon {}", svgPath, t);
        }

        return findIcon(path, classLoader);
    }

    public static Icon scaleToWidth(Icon icon, float newWidth) {
        if (icon instanceof ScalableIcon) {
            ScalableIcon scalableIcon = (ScalableIcon) icon;

            int iconWidth = scalableIcon.getIconWidth();
            if (newWidth != iconWidth) {
                return scalableIcon.scale(newWidth / iconWidth);
            }
        }
        return icon;
    }

    private static Icon load(String key, String path) {
        Icon icon = load(path);
        REGISTERED_ICONS.put(key, icon);
        return icon;
    }

    public static Icon getIcon(String key) {
        return REGISTERED_ICONS.get(key);
    }

    private static Icon createRowIcon(Icon left, Icon right) {
        RowIcon rowIcon = new RowIcon(2);
        rowIcon.setIcon(left, 0);
        rowIcon.setIcon(right, 1);
        return rowIcon;
    }

}
