package com.dbn.vfs.file.status;

import com.dbn.common.property.Property;
import com.dbn.common.property.PropertyGroup;

public enum DBFileStatus implements Property.IntBase {
    LATEST(Group.CODE, true),
    MERGED(Group.CODE, false),
    OUTDATED(Group.CODE, false),

    MODIFIED,

    LOADING,
    SAVING,
    REFRESHING;

    public static final DBFileStatus[] VALUES = values();

    private final IntMasks masks = new IntMasks(this);
    private final boolean implicit;
    private final PropertyGroup group;

    DBFileStatus(PropertyGroup group, boolean implicit) {
        this.implicit = implicit;
        this.group = group;
    }

    DBFileStatus() {
        this(null, false);
    }

    @Override
    public IntMasks masks() {
        return masks;
    }

    @Override
    public PropertyGroup group() {
        return group;
    }

    @Override
    public boolean implicit() {
        return implicit;
    }

    public enum Group implements PropertyGroup{
        CODE
    }
}
