package com.dbn.mybatis;

import com.intellij.openapi.project.Project;
import lombok.Getter;
import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;

@Getter
public class MyDefaultShellCallback extends DefaultShellCallback {
    private File directory;
    private final Project project;

    public MyDefaultShellCallback(boolean overwrite, Project project) {
        super(overwrite);
        this.project = project;
    }

    @Override
    public File getDirectory(String targetProject, String targetPackage) throws ShellException {
        directory = super.getDirectory(targetProject, targetPackage);
        return directory;
    }

    @Override
    public String mergeJavaFile(String newFileSource, File existingFile, String[] javadocTags, String fileEncoding) {
        return newFileSource;
    }

    @Override
    public boolean isMergeSupported() {
        return true;
    }
}
