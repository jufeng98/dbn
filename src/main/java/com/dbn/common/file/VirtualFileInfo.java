package com.dbn.common.file;

import com.dbn.common.file.util.VirtualFiles;
import com.dbn.common.project.Modules;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import lombok.Getter;

import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class VirtualFileInfo {
    private final VirtualFile file;
    private final VirtualFile moduleRoot;
    private final Module module;

    public VirtualFileInfo(VirtualFile file, Project project) {
        this.file = file;
        this.module = ModuleUtil.findModuleForFile(file, project);
        this.moduleRoot = Modules.getModuleContentRoot(module, file);
    }

    public String getPath() {
        return file.getPath();
    }

    public Icon getIcon() {
        return VirtualFiles.getIcon(file);
    }

    public static List<VirtualFileInfo> fromFiles(List<VirtualFile> files, Project project) {
        return files.stream().map(f -> new VirtualFileInfo(f, project)).collect(Collectors.toList());
    }
}
