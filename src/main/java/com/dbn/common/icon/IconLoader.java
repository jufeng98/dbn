package com.dbn.common.icon;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

import static com.intellij.openapi.util.IconLoader.findIcon;

@Slf4j
@UtilityClass
class IconLoader {
    static final Map<String, Icon> REGISTRY = new HashMap<>();

    static Icon load(String path) {
        return new LatentIcon(path) {
            @Override
            protected Icon load() {
                String path = getPath();
                log.info("Loading icon {}", path);
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
        };
    }


    static Icon load(String key, String path) {
        Icon icon = load(path);
        REGISTRY.put(key, icon);
        return icon;
    }

}
