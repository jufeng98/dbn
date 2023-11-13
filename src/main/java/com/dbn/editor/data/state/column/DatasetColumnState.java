package com.dbn.editor.data.state.column;

import com.dbn.common.state.PersistentStateElement;
import com.dbn.common.util.Strings;
import com.dbn.object.DBColumn;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import static com.dbn.common.options.setting.Settings.*;

@Getter
@Setter
@EqualsAndHashCode
public class DatasetColumnState implements Comparable<DatasetColumnState>, PersistentStateElement {
    private String name;
    private short position = -1;
    private boolean visible = true;

    private DatasetColumnState(DatasetColumnState columnState) {
        name = columnState.name;
        position = columnState.position;
        visible = columnState.visible;
    }
    public DatasetColumnState(DBColumn column) {
        init(column);
    }

    public void init(DBColumn column) {
        if (Strings.isEmpty(name)) {
            // not initialized yet
            name = column.getName();
            position = (short) (column.getPosition() -1);
            visible = true;
        }
    }

    public DatasetColumnState(Element element) {
        readState(element);
    }

    @Override
    public void readState(Element element) {
        name = stringAttribute(element, "name");
        position = shortAttribute(element, "position", (short) -1);
        visible = booleanAttribute(element, "visible", true);
    }

    @Override
    public void writeState(Element element) {
        element.setAttribute("name", name);
        setIntegerAttribute(element, "position", position);
        setBooleanAttribute(element, "visible", visible);
    }

    @Override
    public int compareTo(@NotNull DatasetColumnState remote) {
        return position-remote.position;
    }

    @Override
    protected DatasetColumnState clone() {
        return new DatasetColumnState(this);
    }

    @Override
    public String toString() {
        return name + ' ' + position + (visible ? " visible" : " hidden");
    }
}
