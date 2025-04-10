package com.dbn.common.file.util;

import com.dbn.common.event.ApplicationEvents;
import com.dbn.common.thread.Write;
import com.dbn.ddl.DDLFileAttachmentManager;
import com.dbn.object.common.DBSchemaObject;
import com.dbn.vfs.DBVirtualFileBase;
import com.dbn.vfs.DatabaseFileSystem;
import com.dbn.vfs.file.DBEditableObjectVirtualFile;
import com.intellij.injected.editor.VirtualFileWindow;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.io.NioFiles;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.StandardFileSystems;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.VirtualFilePathWrapper;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileDeleteEvent;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.intellij.openapi.vfs.newvfs.events.VFilePropertyChangeEvent;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.util.io.ReadOnlyAttributeUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static com.dbn.common.dispose.Checks.isNotValid;

@Slf4j
@UtilityClass
public final class VirtualFiles {

    public static Icon getIcon(VirtualFile virtualFile) {
        if (virtualFile instanceof DBVirtualFileBase file) {
            return file.getIcon();
        }
        return virtualFile.getFileType().getIcon();
    }

    @Nullable
    public static VirtualFile findFileByUrl(String fileUrl) {
        try {
            VirtualFileManager fileManager = VirtualFileManager.getInstance();
            VirtualFile file = fileManager.findFileByUrl(fileUrl);

            if (isNotValid(file)) return null;
            return file;
        } catch (Exception e) {
            log.warn("Failed to resolve file {}", fileUrl, e);
        }

        return null;
    }

    public static boolean isValidFile(String fileUrl) {
        VirtualFile file = findFileByUrl(fileUrl);
        return file != null;
    }

    public static boolean isDatabaseFileSystem(@NotNull VirtualFile file) {
        return file.getFileSystem() instanceof DatabaseFileSystem;
    }

    public static boolean isLocalFileSystem(@NotNull VirtualFile file) {
        return file.isInLocalFileSystem();
    }

    public static boolean isVirtualFileSystem(@NotNull VirtualFile file) {
        return !isDatabaseFileSystem(file) && !isLocalFileSystem(file);
    }

    public static VirtualFile ioFileToVirtualFile(File file) {
        return LocalFileSystem.getInstance().findFileByIoFile(file);
    }

    public static void setReadOnlyAttribute(VirtualFile file, boolean readonly) {
        try {
            ReadOnlyAttributeUtil.setReadOnlyAttribute(file, readonly);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setReadOnlyAttribute(String path, boolean readonly) {
        try {
            NioFiles.setReadOnly(Paths.get(path), readonly);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static VirtualFile[] findFiles(Project project, FileSearchRequest request) {
        FileCollector collector = FileCollector.create(request);
        ProjectRootManager projectRootManager = ProjectRootManager.getInstance(project);
        VirtualFile[] contentRoots = projectRootManager.getContentRoots();
        for (VirtualFile contentRoot : contentRoots) {
            VfsUtilCore.visitChildrenRecursively(contentRoot, collector);
        }
        return collector.files();
    }

    public static String ensureFilePath(String fileUrlOrPath) {
        if (fileUrlOrPath != null && fileUrlOrPath.startsWith(StandardFileSystems.FILE_PROTOCOL_PREFIX)) {
            return fileUrlOrPath.substring(StandardFileSystems.FILE_PROTOCOL_PREFIX.length());
        }
        return fileUrlOrPath;
    }

    public static String ensureFileUrl(String fileUrlOrPath) {
        if (fileUrlOrPath != null && !fileUrlOrPath.startsWith(StandardFileSystems.FILE_PROTOCOL_PREFIX)) {
            return StandardFileSystems.FILE_PROTOCOL_PREFIX + fileUrlOrPath;
        }
        return fileUrlOrPath;
    }

    @Nullable
    public static VirtualFile getOriginalFile(VirtualFile file) {
        if (file instanceof LightVirtualFile lightVirtualFile) {
            VirtualFile originalFile = lightVirtualFile.getOriginalFile();
            if (originalFile != null && !originalFile.equals(file)) {
                return getOriginalFile(originalFile);
            }
        }
        return file;
    }

    @Contract("null -> null; !null-> !null")
    public static VirtualFile getUnderlyingFile(VirtualFile file) {
        file = getOriginalFile(file);

        if (file instanceof VirtualFileWindow fileWindow) {
            return fileWindow.getDelegate();
        }

        if (file instanceof LightVirtualFile) {
            // TODO is this ever the case?
        }
        return file;

    }

    public static VFileEvent createFileRenameEvent(
            @NotNull VirtualFile virtualFile,
            @NotNull String oldName,
            @NotNull String newName) {
        try {
            Constructor<VFilePropertyChangeEvent> constructor = VFilePropertyChangeEvent.class.getDeclaredConstructor(
                    Object.class, VirtualFile.class, String.class, Object.class, Object.class
            );
            constructor.setAccessible(true);
            return constructor.newInstance(null, virtualFile, VirtualFile.PROP_NAME, oldName, newName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static VFileEvent createFileDeleteEvent(@NotNull VirtualFile virtualFile) {
        try {
            Constructor<VFileDeleteEvent> constructor = VFileDeleteEvent.class.getDeclaredConstructor(
                    Object.class, VirtualFile.class
            );
            constructor.setAccessible(true);
            return constructor.newInstance(null, virtualFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void notifiedFileChange(VFileEvent event, Runnable changeAction) {
        BulkFileListener publisher = ApplicationEvents.publisher(VirtualFileManager.VFS_CHANGES);
        List<VFileEvent> events = Collections.singletonList(event);
        Write.run(() -> {
            publisher.before(events);
            changeAction.run();
            publisher.after(events);
        });

    }


    @Nullable
    public static DBEditableObjectVirtualFile resolveObjectFile(@NotNull Project project, @NotNull VirtualFile virtualFile) {
        if (virtualFile instanceof DBEditableObjectVirtualFile) {
            return (DBEditableObjectVirtualFile) virtualFile;
        }

        if (virtualFile.isInLocalFileSystem()) {
            DDLFileAttachmentManager fileAttachmentManager = DDLFileAttachmentManager.getInstance(project);
            DBSchemaObject schemaObject = fileAttachmentManager.getMappedObject(virtualFile);
            if (schemaObject == null) return null;

            return schemaObject.getEditableVirtualFile();
        }
        return null;
    }

    @Nullable
    public static String getPresentablePath(@Nullable VirtualFile file) {
        if (file == null) return null;

        if (file instanceof VirtualFilePathWrapper) {
            VirtualFilePathWrapper databaseFile = (VirtualFilePathWrapper) file;
            return databaseFile.getPresentablePath();
        }
        return file.getPath();
    }
}

