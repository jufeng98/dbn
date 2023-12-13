package com.dbn.common.file;

import com.dbn.common.ref.WeakRef;
import com.intellij.openapi.vfs.VirtualFile;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.dbn.common.dispose.Checks.isValid;
import static com.dbn.common.dispose.Failsafe.nn;

@EqualsAndHashCode
public class VirtualFileRef{
    private final WeakRef<VirtualFile> file;

    private VirtualFileRef(VirtualFile file) {
        this.file = WeakRef.of(file);
    }

    @Nullable
    public VirtualFile get() {
        VirtualFile file = this.file.get();
        return isValid(file) ? file : null;
    }

    public static VirtualFileRef of(@NotNull VirtualFile file) {
        return new VirtualFileRef(file);
    }

    @Nullable
    public static VirtualFile get(@Nullable VirtualFileRef ref) {
        return ref == null ? null : ref.get();
    }

    @NotNull
    public static VirtualFile ensure(@Nullable VirtualFileRef ref) {
        return nn(get(ref));
    }

    @NotNull
    public VirtualFile ensure() {
        return nn(get());
    }
}
