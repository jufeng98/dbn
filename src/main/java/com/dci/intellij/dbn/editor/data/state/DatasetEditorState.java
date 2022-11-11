package com.dci.intellij.dbn.editor.data.state;

import com.dci.intellij.dbn.common.state.PersistentStateElement;
import com.dci.intellij.dbn.common.util.Cloneable;
import com.dci.intellij.dbn.data.model.sortable.SortableDataModelState;
import com.dci.intellij.dbn.editor.data.state.column.DatasetColumnSetup;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.fileEditor.FileEditorStateLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static com.dci.intellij.dbn.common.options.setting.SettingsSupport.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DatasetEditorState extends SortableDataModelState implements FileEditorState, PersistentStateElement, Cloneable<DatasetEditorState> {
    public static final DatasetEditorState VOID = new DatasetEditorState();

    private DatasetColumnSetup columnSetup = new DatasetColumnSetup();

    @Override
    public boolean canBeMergedWith(@NotNull FileEditorState fileEditorState, @NotNull FileEditorStateLevel fileEditorStateLevel) {
        return fileEditorState instanceof DatasetEditorState && fileEditorStateLevel == FileEditorStateLevel.FULL;
    }

    public DatasetColumnSetup getColumnSetup() {
        return columnSetup;
    }

    @Override
    public void readState(@NotNull Element element) {
        setRowCount(integerAttribute(element, "row-count", 100));
        setReadonly(booleanAttribute(element, "readonly", false));

        Element columnsElement = element.getChild("columns");
        columnSetup.readState(columnsElement);

        Element sortingElement = element.getChild("sorting");
        sortingState.readState(sortingElement);


        Element contentTypesElement = element.getChild("content-types");
        if (contentTypesElement != null) {
            for (Element child : contentTypesElement.getChildren()) {
                String columnName = stringAttribute(child, "column-name");
                String contentTypeName = stringAttribute(child, "type-name");
                setTextContentType(columnName, contentTypeName);
            }
        }
    }

    @Override
    public void writeState(Element targetElement) {
        targetElement.setAttribute("row-count", Integer.toString(getRowCount()));
        targetElement.setAttribute("readonly", Boolean.toString(isReadonly()));

        Element columnsElement = new Element("columns");
        targetElement.addContent(columnsElement);
        columnSetup.writeState(columnsElement);

        Element sortingElement = new Element("sorting");
        targetElement.addContent(sortingElement);
        sortingState.writeState(sortingElement);

        Element contentTypesElement = new Element("content-types");
        targetElement.addContent(contentTypesElement);
        if (contentTypesMap != null && contentTypesMap.size() > 0) {
            for (val entry : contentTypesMap.entrySet()) {
                String columnName = entry.getKey();
                String contentTypeName = entry.getValue();

                Element contentTypeElement = new Element("content-type");
                contentTypeElement.setAttribute("column-name", columnName);
                contentTypeElement.setAttribute("type-name", contentTypeName);
                contentTypesElement.addContent(contentTypeElement);
            }
        }
    }

    @Override
    public DatasetEditorState clone() {
        DatasetEditorState clone = new DatasetEditorState();
        clone.setReadonly(isReadonly());
        clone.setRowCount(getRowCount());
        clone.setSortingState(getSortingState());
        clone.columnSetup = columnSetup.clone();
        if (contentTypesMap != null) {
            clone.contentTypesMap = new HashMap<>(contentTypesMap);
        }

        return clone;
    }
}