package com.dbn.common.util;

import com.intellij.openapi.application.ApplicationInfo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Environment {
    public static boolean isIdeNewerThan(String targetVersion) {
        String currentVersion = ApplicationInfo.getInstance().getFullVersion();
        return isVersionGreaterThan(currentVersion, targetVersion);
    }


    private static boolean isVersionGreaterThan(String currentVersion, String targetVersion) {
        String[] currentParts = currentVersion.split("\\.");
        String[] targetParts = targetVersion.split("\\.");

        int currentMajor = Integer.parseInt(currentParts[0]);
        int targetMajor = Integer.parseInt(targetParts[0]);

        if (currentMajor > targetMajor) return true;
        if (currentMajor < targetMajor) return false;

        int currentMinor = Integer.parseInt(currentParts[1]);
        int targetMinor = Integer.parseInt(targetParts[1]);

        return currentMinor > targetMinor;
    }
}
