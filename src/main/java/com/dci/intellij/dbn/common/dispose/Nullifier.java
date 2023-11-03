package com.dci.intellij.dbn.common.dispose;

import com.dci.intellij.dbn.common.latent.Latent;
import com.dci.intellij.dbn.common.latent.Loader;
import com.dci.intellij.dbn.common.options.Configuration;
import com.dci.intellij.dbn.common.ui.component.DBNComponent;
import com.dci.intellij.dbn.connection.DatabaseEntity;
import com.dci.intellij.dbn.data.model.DataModel;
import com.intellij.openapi.components.NamedComponent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.util.ReflectionUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.swing.tree.TreePath;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.EventListener;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.dci.intellij.dbn.common.util.Unsafe.silent;

@Slf4j
@UtilityClass
public final class Nullifier {

    private static final Map<Class<?>, List<Field>> NULLIFIABLE_FIELDS = new ConcurrentHashMap<>(100);
    private static final Class[] NULLIFIABLE_CLASSES = new Class[] {
            Map.class,
            Collection.class,
            Latent.class,
            Loader.class,
            Editor.class,
            Document.class,
            PsiElement.class,
            VirtualFile.class,
            Configuration.class,
            DatabaseEntity.class,
            AutoCloseable.class,
            NamedComponent.class,
            EventListener.class,
            DataModel.class,
            TreePath.class,
            EventListener.class,
            DBNComponent.class
    };

    public static void clearCollection(Collection<?> collection) {
        if (collection == null) return;
        if (collection.isEmpty()) return;

        boolean cleared = silent(collection, c -> c.clear());
        if (!cleared && collection instanceof List) {
            silent(collection, c -> nullify((List<?>) c));
        }
    }

    public static void clearMap(Map<?, ?> map) {
        silent(map, m -> m.clear());
    }

    private static void nullify(List<?> collection) {
        for (int i = 0; i<collection.size(); i++) {
            collection.set(i, null);
        }
    }

    public static void nullify(Object object) {
        if (object instanceof Component) return;

        BackgroundDisposer.queue(() -> nullifyFields(object));
    }

    private static void nullifyFields(Object object) {
        List<Field> fields = nullifiableFields(object.getClass());
        for (Field field : fields) {
            try {
                nullifyField(object, field);
            } catch (UnsupportedOperationException ignore) {
            } catch (Throwable e) {
                log.error("Failed to nullify field", e);
            }
        }
    }

    private static void nullifyField(Object object, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        Object fieldValue = field.get(object);
        if (fieldValue == null) return;

        if (fieldValue instanceof Collection<?>) {
            Collection collection = (Collection) fieldValue;
            clearCollection(collection);
        } else if (fieldValue instanceof Map) {
            Map map = (Map) fieldValue;
            clearMap(map);
        } else if (fieldValue instanceof Latent){
            Latent latent = (Latent) fieldValue;
            latent.reset();
            nullify(latent);
        } else {
            field.set(object, null);
        }
    }

    private static List<Field> nullifiableFields(Class clazz) {
        return NULLIFIABLE_FIELDS.computeIfAbsent(clazz, c ->
                ReflectionUtil.
                        collectFields(c).
                        stream().
                        filter(field -> isNullifiable(field)).
                        collect(Collectors.toList()));
    }

    private static boolean isNullifiable(Field field) {
        int modifiers = field.getModifiers();
        if (Modifier.isStatic(modifiers) ||
                //Modifier.isFinal(modifiers) ||
                Modifier.isNative(modifiers)) {
            return false;
        }

        Sticky sticky = field.getAnnotation(Sticky.class);
        if (sticky != null) return false;

        for (Class<?> nullifiableClass : NULLIFIABLE_CLASSES) {
            Class<?> fieldType = field.getType();
            if (nullifiableClass.isAssignableFrom(fieldType)) return true;
        }
        return false;
    }
}
