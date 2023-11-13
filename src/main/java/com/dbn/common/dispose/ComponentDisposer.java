package com.dbn.common.dispose;

import com.dbn.common.Pair;
import com.dbn.common.thread.Dispatch;
import com.dbn.common.util.Unsafe;
import com.intellij.util.ui.UIUtil;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public final class ComponentDisposer {
    private static final Map<Class, List<Pair<Method, Class[]>>> LISTENER_REMOVE_METHODS = new ConcurrentHashMap<>();

    public static void removeListeners(Component comp) {
        List<Pair<Method, Class[]>> methodPairs = getListenerRemovalMethods(comp);
        for (Pair<Method, Class[]> methodPair : methodPairs) {
            Method method = methodPair.first();
            Class[] params = methodPair.second();
            EventListener[] listeners = Unsafe.silent(new EventListener[0], () -> comp.getListeners(params[0]));
            for (EventListener listener : listeners) {
                Unsafe.silent(() -> method.invoke(comp, listener));
            }
        }

    }

    @NotNull
    private static List<Pair<Method, Class[]>> getListenerRemovalMethods(Component comp) {
        Class<? extends Component> clazz = comp.getClass();
        return LISTENER_REMOVE_METHODS.computeIfAbsent(clazz, c -> {
            List<Pair<Method, Class[]>> listenerMethods = new ArrayList<>();
            Method[] methods = c.getMethods();
            for (Method method : methods) {
                String name = method.getName();
                if (name.startsWith("remove") && name.endsWith("Listener")) {
                    Class[] params = method.getParameterTypes();
                    if (params.length == 1) {
                        listenerMethods.add(Pair.of(method, params));
                    }
                }
            }
            return listenerMethods;
        });
    }

    public static void dispose(@Nullable Component component) {
        if (component == null) return;

        Dispatch.run(true, () -> {
            UIUtil.dispose(component);
            removeListeners(component);
            if (component instanceof Container) {
                Container container = (Container) component;
                Component[] components = container.getComponents();
                for (Component child : components) {
                    dispose(child);
                    //Unsafe.silent(() -> container.remove(child));
                }
            }
        });
    }
}
