package com.dci.intellij.dbn.connection.console;

import com.dci.intellij.dbn.DatabaseNavigator;
import com.dci.intellij.dbn.common.component.PersistentState;
import com.dci.intellij.dbn.common.component.ProjectComponentBase;
import com.dci.intellij.dbn.common.event.ProjectEvents;
import com.dci.intellij.dbn.common.thread.Progress;
import com.dci.intellij.dbn.common.thread.Write;
import com.dci.intellij.dbn.common.util.*;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionId;
import com.dci.intellij.dbn.connection.ConnectionManager;
import com.dci.intellij.dbn.connection.console.ui.CreateRenameConsoleDialog;
import com.dci.intellij.dbn.connection.mapping.FileConnectionContextManager;
import com.dci.intellij.dbn.connection.session.DatabaseSession;
import com.dci.intellij.dbn.connection.session.DatabaseSessionBundle;
import com.dci.intellij.dbn.connection.session.SessionManagerListener;
import com.dci.intellij.dbn.object.DBConsole;
import com.dci.intellij.dbn.object.common.DBObjectBundle;
import com.dci.intellij.dbn.object.common.list.DBObjectList;
import com.dci.intellij.dbn.object.type.DBObjectType;
import com.dci.intellij.dbn.vfs.DBConsoleType;
import com.dci.intellij.dbn.vfs.DatabaseFileManager;
import com.dci.intellij.dbn.vfs.file.DBConsoleVirtualFile;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.fileChooser.FileSaverDescriptor;
import com.intellij.openapi.fileChooser.FileSaverDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import org.jdom.CDATA;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.dci.intellij.dbn.common.component.Components.projectService;
import static com.dci.intellij.dbn.common.dispose.Checks.isNotValid;
import static com.dci.intellij.dbn.common.file.util.VirtualFiles.*;
import static com.dci.intellij.dbn.common.message.MessageCallback.when;
import static com.dci.intellij.dbn.common.options.setting.Settings.*;
import static com.dci.intellij.dbn.diagnostics.Diagnostics.conditionallyLog;

@State(
    name = DatabaseConsoleManager.COMPONENT_NAME,
    storages = @Storage(DatabaseNavigator.STORAGE_FILE)
)
public class DatabaseConsoleManager extends ProjectComponentBase implements PersistentState {
    public static final String COMPONENT_NAME = "DBNavigator.Project.DatabaseConsoleManager";

    private DatabaseConsoleManager(@NotNull Project project) {
        super(project, COMPONENT_NAME);
        ProjectEvents.subscribe(project, this, SessionManagerListener.TOPIC, sessionManagerListener);
    }

    public static DatabaseConsoleManager getInstance(@NotNull Project project) {
        return projectService(project, DatabaseConsoleManager.class);
    }

    public void showCreateConsoleDialog(ConnectionHandler connection, DBConsoleType consoleType) {
        showCreateRenameConsoleDialog(connection, null, consoleType);
    }

    public void showRenameConsoleDialog(@NotNull DBConsole console) {
        ConnectionHandler connection = console.getConnection();
        showCreateRenameConsoleDialog(
                connection,
                console,
                console.getConsoleType());
    }


    private void showCreateRenameConsoleDialog(ConnectionHandler connection, DBConsole console, DBConsoleType consoleType) {
        Dialogs.show(() -> console == null ?
                new CreateRenameConsoleDialog(connection, consoleType) :
                new CreateRenameConsoleDialog(connection, console));
    }

    public void createConsole(ConnectionHandler connection, String name, DBConsoleType type) {
        Project project = connection.getProject();
        Progress.background(project, connection, true,
                "Creating console",
                "Creating " + type.getName() + " \"" + name + "\"",indicator -> {
            DBConsole console = connection.getConsoleBundle().createConsole(name, type);
            DBConsoleVirtualFile consoleFile = console.getVirtualFile();
            consoleFile.setText("");
            consoleFile.setDatabaseSchema(connection.getDefaultSchema());

            reloadConsoles(connection);
            Editors.openFileEditor(project, consoleFile, true);
        });
    }

    public void renameConsole(@NotNull DBConsole console, String newName) {
        String oldName = console.getName();
        if (!Objects.equals(oldName, newName)) {
            ConnectionHandler connection = console.getConnection();
            DatabaseConsoleBundle consoleBundle = connection.getConsoleBundle();

            DBConsoleVirtualFile virtualFile = console.getVirtualFile();
            VFileEvent renameEvent = createFileRenameEvent(virtualFile, oldName, newName);
            notifiedFileChange(renameEvent, () -> consoleBundle.renameConsole(oldName, newName));

            reloadConsoles(connection);
        }
    }

    public void deleteConsole(DBConsole console) {
        Project project = getProject();
        Messages.showQuestionDialog(
                project,
                "Delete console",
                "You will loose the information contained in this console.\n" +
                        "Are you sure you want to delete the console?",
                Messages.OPTIONS_YES_NO, 0,
                option -> when(option == 0, () -> {
                    ConnectionHandler connection = console.getConnection();
                    DatabaseConsoleBundle consoleBundle = connection.getConsoleBundle();

                    DBConsoleVirtualFile virtualFile = console.getVirtualFile();

                    DatabaseFileManager fileManager = DatabaseFileManager.getInstance(project);
                    fileManager.closeFile(virtualFile);

                    VFileEvent deleteEvent = createFileDeleteEvent(virtualFile);
                    notifiedFileChange(deleteEvent, () -> consoleBundle.removeConsole(console));

                    reloadConsoles(connection);
                }));

    }

    private void reloadConsoles(@NotNull ConnectionHandler connection) {
        DBObjectBundle objectBundle = connection.getObjectBundle();
        DBObjectList<?> objectList = objectBundle.getObjectList(DBObjectType.CONSOLE);
        Safe.run(objectList, target -> target.markDirty());
    }

    public void saveConsoleToFile(DBConsoleVirtualFile consoleFile) {
        Project project = getProject();
        FileSaverDescriptor fileSaverDescriptor = new FileSaverDescriptor(
                Titles.signed("Save Console to File"),
                "Save content of the console \"" + consoleFile.getName() + "\" to file", "sql");

        FileChooserFactory fileChooserFactory = FileChooserFactory.getInstance();
        FileSaverDialog fileSaverDialog = fileChooserFactory.createSaveFileDialog(fileSaverDescriptor, project);
        Document document = Documents.getDocument(consoleFile);
        if (document == null) return;

        VirtualFileWrapper fileWrapper = fileSaverDialog.save((VirtualFile) null, consoleFile.getName());
        if (fileWrapper == null) return;

        VirtualFile file = fileWrapper.getVirtualFile(true);
        if (file == null) return;

        byte[] content = document.getCharsSequence().toString().getBytes();
        Write.run(project, () -> {
            try {
                file.setBinaryContent(content);
            } catch (IOException e) {
                conditionallyLog(e);
                Messages.showErrorDialog(project, "Error saving to file", "Could not save console content to file \"" + fileWrapper.getFile().getName() + "\"", e);
            }
        });

        FileConnectionContextManager contextManager = FileConnectionContextManager.getInstance(project);
        contextManager.setConnection(file, consoleFile.getConnection());
        contextManager.setDatabaseSchema(file, consoleFile.getSchemaId());
        contextManager.setDatabaseSession(file, consoleFile.getSession());
        Editors.openFileEditor(project, file, true);
    }

    /***************************************
     *         SessionManagerListener      *
     ***************************************/
    private final SessionManagerListener sessionManagerListener = new SessionManagerListener() {
        @Override
        public void sessionDeleted(DatabaseSession session) {
            ConnectionManager connectionManager = ConnectionManager.getInstance(getProject());
            List<ConnectionHandler> connections = connectionManager.getConnectionBundle().getAllConnections();
            for (ConnectionHandler connection : connections) {
                List<DBConsole> consoles = connection.getConsoleBundle().getConsoles();
                for (DBConsole console : consoles) {
                    DBConsoleVirtualFile virtualFile = console.getVirtualFile();
                    if (virtualFile.getSession() == session) {
                        DatabaseSession mainSession = connection.getSessionBundle().getMainSession();
                        virtualFile.setDatabaseSession(mainSession);
                    }
                }
            }
        }
    };

    /*********************************************
     *            PersistentStateComponent       *
     *********************************************/
    @Nullable
    @Override
    public Element getComponentState() {
        Element element = new Element("state");
        ConnectionManager connectionManager = ConnectionManager.getInstance(getProject());
        List<ConnectionHandler> connections = connectionManager.getConnectionBundle().getAllConnections();
        for (ConnectionHandler connection : connections) {
            Element connectionElement = newElement(element, "connection");
            connectionElement.setAttribute("id", connection.getConnectionId().id());

            List<DBConsole> consoles = connection.getConsoleBundle().getConsoles();
            for (DBConsole console : consoles) {
                DBConsoleVirtualFile virtualFile = console.getVirtualFile();
                Element consoleElement = newElement(connectionElement, "console");

                DatabaseSession databaseSession = Commons.nvl(
                        virtualFile.getSession(),
                        connection.getSessionBundle().getMainSession());

                consoleElement.setAttribute("name", console.getName());
                consoleElement.setAttribute("type", console.getConsoleType().name());
                consoleElement.setAttribute("schema", Commons.nvl(virtualFile.getDatabaseSchemaName(), ""));
                consoleElement.setAttribute("session", databaseSession.getName());
                consoleElement.addContent(new CDATA(virtualFile.getContent().exportContent()));
            }
        }
        return element;
    }

    @Override
    public void loadComponentState(@NotNull Element element) {
        for (Element connectionElement : element.getChildren()) {
            ConnectionId connectionId = connectionIdAttribute(connectionElement, "id");
            ConnectionHandler connection = ConnectionHandler.get(connectionId);
            if (isNotValid(connection)) continue;

            DatabaseConsoleBundle consoleBundle = connection.getConsoleBundle();
            for (Element consoleElement : connectionElement.getChildren()) {
                String consoleName = stringAttribute(consoleElement, "name");

                // schema
                String schema = stringAttribute(consoleElement, "schema");

                // session
                String session = stringAttribute(consoleElement, "session");
                DatabaseSessionBundle sessionBundle = connection.getSessionBundle();
                DatabaseSession databaseSession = Strings.isEmpty(session) ?
                        sessionBundle.getMainSession() :
                        sessionBundle.getSession(session);


                DBConsoleType consoleType = enumAttribute(consoleElement, "type", DBConsoleType.class);

                String consoleText = readCdata(consoleElement);

                DBConsole console = consoleBundle.getConsole(consoleName, consoleType, true);
                DBConsoleVirtualFile virtualFile = console.getVirtualFile();
                virtualFile.setText(consoleText);
                virtualFile.setDatabaseSchemaName(schema);
                virtualFile.setDatabaseSession(databaseSession);
            }
        }
    }
}
