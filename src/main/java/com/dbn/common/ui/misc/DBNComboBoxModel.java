package com.dbn.common.ui.misc;

import com.dbn.common.ui.util.Listeners;
import com.dbn.common.ui.Presentable;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

public class DBNComboBoxModel<T extends Presentable> implements MutableComboBoxModel<T> {
    private final Listeners<ListDataListener> listDataListeners = Listeners.create();
    private final List<T> items = new ArrayList<>();
    private T selectedItem;

    @Override
    public void addElement(T item) {
        items.add(item);
    }

    @Override
    public void removeElement(Object obj) {
        items.remove(obj);
    }

    @Override
    public void insertElementAt(T item, int index) {
        items.add(index, item);
    }

    @Override
    public void removeElementAt(int index) {
        items.remove(index);
    }

    public void removeAllElements() {
        items.clear();
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selectedItem = (T) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selectedItem;
    }

    @Override
    public int getSize() {
        return items.size();
    }

    @Override
    public T getElementAt(int index) {
        return items.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        listDataListeners.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listDataListeners.remove(l);
    }

    public List<T> getItems() {
        return items;
    }

    public boolean containsItem(T item) {
        return items.contains(item);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
