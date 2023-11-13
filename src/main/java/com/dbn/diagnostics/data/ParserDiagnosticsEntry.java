package com.dbn.diagnostics.data;

import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

import static com.dbn.common.util.Commons.nvl;
import static com.dbn.diagnostics.data.ParserDiagnosticsUtil.computeStateTransition;

@Getter
public class ParserDiagnosticsEntry implements Comparable<ParserDiagnosticsEntry>{

    private final String filePath;
    private final IssueCounter oldIssues;
    private final IssueCounter newIssues;

    public ParserDiagnosticsEntry(String filePath, IssueCounter oldIssues, IssueCounter newIssues) {
        this.filePath = filePath;
        this.oldIssues = nvl(oldIssues, IssueCounter.EMPTY);
        this.newIssues = nvl(newIssues, IssueCounter.EMPTY);
    }

    @Nullable
    public VirtualFile getFile() {
        LocalFileSystem localFileSystem = LocalFileSystem.getInstance();
        return localFileSystem.findFileByIoFile(new File(filePath));
    }

    public StateTransition getStateTransition() {
        return computeStateTransition(oldIssues, newIssues);
    }

    @Override
    public int compareTo(@NotNull ParserDiagnosticsEntry o) {
        return filePath.compareTo(o.getFilePath());
    }
}
