package com.dbn.language.common.element.parser;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;

@Getter
@EqualsAndHashCode(callSuper = true)
public class BranchCheck extends Branch{
    private double version = 0;
    private Type type;

    public boolean check(Branch branch, double currentVersion) {
        switch (type) {
            case ALLOWED: return Objects.equals(name, branch.name) && currentVersion >= version;
            case FORBIDDEN: return !Objects.equals(name, branch.name) || currentVersion < version;
        }
        return true;
    }

    @Override
    public String toString() {
        return getName() + "@" + version;
    }

    public enum Type {
        ALLOWED,
        FORBIDDEN
    }

    public BranchCheck(String def) {
        int startIndex = 0;
        if (def.startsWith("-")) {
            type = Type.FORBIDDEN;
            startIndex = 1;
        } else if (def.startsWith("+")) {
            type = Type.ALLOWED;
            startIndex = 1;
        }

        int atIndex = def.indexOf("@", startIndex);
        if (atIndex > -1) {
            name = def.substring(startIndex, atIndex).trim();
            version = Double.parseDouble(def.substring(atIndex + 1));
        } else {
            name = def.substring(startIndex).trim();
        }
    }
}
