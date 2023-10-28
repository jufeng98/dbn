package com.dci.intellij.dbn.common.util;

import com.intellij.openapi.actionSystem.*;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.util.UUID;

@UtilityClass
public class Actions {
    public static final AnAction SEPARATOR = Separator.getInstance();

    public static ActionToolbar createActionToolbar(@NotNull JComponent component, String name, String place, boolean horizontal){
        ActionManager actionManager = ActionManager.getInstance();
        ActionGroup actionGroup = (ActionGroup) actionManager.getAction(name);
        ActionToolbar toolbar = actionManager.createActionToolbar(adjustPlace(place), actionGroup, horizontal);
        toolbar.setTargetComponent(component);
        return toolbar;
    }

    public static ActionToolbar createActionToolbar(@NotNull JComponent component, String place, boolean horizontal, ActionGroup actionGroup){
        ActionManager actionManager = ActionManager.getInstance();
        ActionToolbar toolbar = actionManager.createActionToolbar(adjustPlace(place), actionGroup, horizontal);
        toolbar.setTargetComponent(component);
        return toolbar;
    }

    public static ActionToolbar createActionToolbar(@NotNull JComponent component, String place, boolean horizontal, AnAction... actions){
        ActionManager actionManager = ActionManager.getInstance();
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        for (AnAction action : actions) {
            if (action == SEPARATOR)
                actionGroup.addSeparator(); else
                actionGroup.add(action);
        }

        ActionToolbar toolbar = actionManager.createActionToolbar(adjustPlace(place), actionGroup, horizontal);
        toolbar.setTargetComponent(component);
        return toolbar;
    }

    public static ActionPopupMenu createActionPopupMenu(@NotNull JComponent component, String place, ActionGroup actionGroup){
        ActionManager actionManager = ActionManager.getInstance();
        ActionPopupMenu popupMenu = actionManager.createActionPopupMenu(adjustPlace(place), actionGroup);
        popupMenu.setTargetComponent(component);
        return popupMenu;
    }

    public static String adjustActionName(@NotNull String name) {
        return name.replaceAll("_", "__");
    }

    private static String adjustPlace(String place) {
        if (Strings.isEmpty(place)) {
            return UUID.randomUUID().toString();
        }
        return place;
    }

    public static boolean isConsumed(AnActionEvent event) {
        InputEvent inputEvent = event.getInputEvent();
        if (inputEvent == null) return false;
        return inputEvent.isConsumed();
    }

    public static void consume(AnActionEvent event) {
        InputEvent inputEvent = event.getInputEvent();
        if (inputEvent == null) return;
        inputEvent.consume();
    }

}
