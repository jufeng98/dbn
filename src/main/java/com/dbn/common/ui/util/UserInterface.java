package com.dbn.common.ui.util;

import com.dbn.common.compatibility.Compatibility;
import com.dbn.common.lookup.Visitor;
import com.dbn.common.thread.Dispatch;
import com.dbn.common.util.Environment;
import com.dbn.common.util.Strings;
import com.dbn.common.util.Unsafe;
import com.intellij.openapi.actionSystem.ActionToolbarPosition;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.registry.Registry;
import com.intellij.ui.AncestorListenerAdapter;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.border.IdeaTitledBorder;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Arrays;
import java.util.function.Predicate;

import static com.dbn.common.ui.util.Borderless.isBorderless;
import static com.dbn.diagnostics.Diagnostics.conditionallyLog;

public class UserInterface {

    public static final String NEW_UI_REGISTRY_KEY = "ide.experimental.ui";
    public static final String NEW_UI_RELEASE_VERSION = "2022.3";

    @Compatibility
    @Getter(lazy = true)
    private static final boolean newUI = Unsafe.silent(false, () ->
            Environment.isIdeNewerThan(NEW_UI_RELEASE_VERSION) &&
            Registry.is(NEW_UI_REGISTRY_KEY));

    public static void stopTableCellEditing(JComponent root) {
        visitRecursively(root, component -> {
            if (component instanceof JTable) {
                JTable table = (JTable) component;
                TableCellEditor cellEditor = table.getCellEditor();
                if (cellEditor != null) {
                    cellEditor.stopCellEditing();
                }
            }
        });
    }

    public static void whenShown(JComponent component, Runnable runnable) {
        component.addAncestorListener(new AncestorListenerAdapter() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                runnable.run();
            }
        });
    }


    public static void removeBorders(JComponent root) {
        UserInterface.visitRecursively(root, component -> component.setBorder(null));
    }

    @Nullable
    public static Point getRelativeMouseLocation(Component component) {
        try {
            PointerInfo pointerInfo = MouseInfo.getPointerInfo();
            if (pointerInfo != null) {
                Point mouseLocation = pointerInfo.getLocation();
                return getRelativeLocation(mouseLocation, component);
            }
        } catch (IllegalComponentStateException e) {
            conditionallyLog(e);
        }
        return null;
    }
    
    public static Point getRelativeLocation(Point locationOnScreen, Component component) {
        Point componentLocation = component.getLocationOnScreen();
        Point relativeLocation = locationOnScreen.getLocation();
        relativeLocation.move(
                (int) (locationOnScreen.getX() - componentLocation.getX()), 
                (int) (locationOnScreen.getY() - componentLocation.getY()));
        return relativeLocation;
    }

    public static boolean isChildOf(Component component, Component child) {
        Component parent = child == null ? null : child.getParent();
        while (parent != null) {
            if (parent == component) {
                return true;
            }
            parent = parent.getParent();
        }
        return false;
    }

    public static boolean isFocused(Component component, boolean recursive) {
        if (component.isFocusOwner()) return true;
        if (recursive && component instanceof JComponent) {
            JComponent parentComponent = (JComponent) component;
            for (Component childComponent : parentComponent.getComponents()) {
                if (isFocused(childComponent, recursive)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void updateTitledBorder(JPanel panel) {
        Border border = panel.getBorder();
        if (border instanceof TitledBorder) {
            TitledBorder titledBorder = (TitledBorder) border;
            String title = titledBorder.getTitle();
            int indent = Strings.isEmpty(title) ? 0 : 20;
            IdeaTitledBorder replacement = new IdeaTitledBorder(title, indent, Borders.EMPTY_INSETS);
/*
            titledBorder.setTitleColor(Colors.HINT_COLOR);
            titledBorder.setBorder(Borders.TOP_LINE_BORDER);
            border = new CompoundBorder(Borders.topInsetBorder(8), titledBorder);
*/
            border = new CompoundBorder(Borders.topInsetBorder(8), replacement);
            panel.setBorder(border);
        }
    }

    public static void repaint(JComponent component) {
        Dispatch.run(true, () -> {
            component.revalidate();
            component.repaint();
        });
    }

    public static void repaintAndFocus(JComponent component) {
        Dispatch.run(true, () -> {
            component.revalidate();
            component.repaint();
            component.requestFocus();
        });
    }

    public static void changePanelBackground(JPanel panel, Color background) {
        panel.setBackground(background);
        for (Component component : panel.getComponents()) {
            if (component instanceof JPanel) {
                JPanel childPanel = (JPanel) component;
                changePanelBackground(childPanel, background);
            }
        }
    }

    public static int ctrlDownMask() {
        return SystemInfo.isMac ? InputEvent.META_DOWN_MASK : InputEvent.CTRL_DOWN_MASK;
    }

    public static void visitRecursively(JComponent component, Visitor<JComponent> visitor) {
        visitor.visit(component);
        Component[] childComponents = component.getComponents();
        for (Component childComponent : childComponents) {
            if (childComponent instanceof JComponent) {
                visitRecursively((JComponent) childComponent, visitor);
            }

        }
    }

    public static <T extends JComponent> void visitRecursively(JComponent component, Class<T> type, Visitor<T> visitor) {
        if (type.isAssignableFrom(component.getClass())) visitor.visit(Unsafe.cast(component));

        Component[] childComponents = component.getComponents();
        for (Component childComponent : childComponents) {
            if (childComponent instanceof JComponent) {
                visitRecursively((JComponent) childComponent, type, visitor);
            }

        }
    }

    public static void updateTitledBorders(JComponent component) {
        visitRecursively(component, JPanel.class, p -> updateTitledBorder(p));
    }

    public static void updateScrollPaneBorders(JComponent component) {
        visitRecursively(component, JScrollPane.class, sp -> {
            sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            sp.setBorder(isBorderlessPane(sp) ? null : Borders.COMPONENT_OUTLINE_BORDER);
        });
    }

    private static boolean isBorderlessPane(JScrollPane scrollPane) {
        Component component = scrollPane.getViewport().getView();
        return isBorderless(component);
    }

    @Nullable
    public static <T extends JComponent> T getParentOfType(JComponent component, Class<T> type) {
        Component parent = component.getParent();
        while (parent != null) {
            if (type.isAssignableFrom(parent.getClass())) return Unsafe.cast(parent);
            parent = parent.getParent();
        }
        return null;
    }

    public static <T extends JComponent> T getParent(JComponent component, Predicate<Component> check) {
        Component parent = component.getParent();
        while (parent != null) {
            if (check.test(parent)) return Unsafe.cast(parent);
            parent = parent.getParent();
        }
        return null;
    }

    public static Dimension adjust(Dimension dimension, int widthAdjustment, int heightAdjustment) {
        return new Dimension((int) dimension.getWidth() + widthAdjustment, (int) dimension.getHeight() + heightAdjustment);
    }

    @NotNull
    public static ToolbarDecorator createToolbarDecorator(JTable table) {
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(table);
        decorator.setToolbarPosition(ActionToolbarPosition.TOP);
        decorator.setToolbarBorder(Borders.TOOLBAR_DECORATOR_BORDER);
        decorator.setPanelBorder(Borders.EMPTY_BORDER);
        return decorator;
    }


    public static void updateSplitPanes(JComponent component) {
        visitRecursively(component, JSplitPane.class, sp -> Splitters.replaceSplitPane(sp));
    }

    public static void setBackgroundRecursive(JComponent component, Color color) {
        component.setBackground(color);
        Component[] children = component.getComponents();
        Arrays
            .stream(children)
            .filter(child -> child instanceof JComponent)
            .map(child -> (JComponent) child)
            .forEach(child -> setBackgroundRecursive(child, color));

    }

    public static void replaceComponent(JComponent oldComponent, JComponent newComponent) {
        Container container = oldComponent.getParent();
        LayoutManager layout = container.getLayout();
        for (int i = 0; i < container.getComponentCount(); i++) {
            if (container.getComponent(i) != oldComponent) continue;

            if (layout instanceof GridLayoutManager) {
                GridLayoutManager gridLayout = (GridLayoutManager) layout;
                GridConstraints constraints = gridLayout.getConstraintsForComponent(oldComponent);
                container.remove(i);
                container.add(newComponent, constraints);
            } else {
                container.remove(i);
                container.add(newComponent, i);
            }

        }
    }

    public static int getComponentIndex(Container container, Component component) {
        for (int i = 0; i < container.getComponentCount(); i++) {
            if (container.getComponent(i) == component) {
                return i;
            }
        }
        return -1;
    }
}
