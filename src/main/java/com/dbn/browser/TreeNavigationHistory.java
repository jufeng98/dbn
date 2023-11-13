package com.dbn.browser;

import com.dbn.browser.model.BrowserTreeNode;
import com.dbn.browser.options.DatabaseBrowserSettings;
import com.dbn.common.thread.ReadWriteMonitor;
import com.intellij.openapi.Disposable;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TreeNavigationHistory implements Disposable{
    private final List<BrowserTreeNode> history = new ArrayList<>();
    private final ReadWriteMonitor monitor = new ReadWriteMonitor();
    private final AtomicInteger offset = new AtomicInteger(0);

    public void add(BrowserTreeNode treeNode) {
        monitor.write(() -> {
            offset.set(Math.min(offset.get(), history.size() -1));
            int offset = this.offset.get();
            if (history.size() > 0 && treeNode == history.get(offset)) {
                return;
            }
            while (history.size() > offset + 1) {
                history.remove(offset + 1);
            }

            DatabaseBrowserSettings browserSettings = DatabaseBrowserSettings.getInstance(treeNode.getProject());

            int historySize = browserSettings.getGeneralSettings().getNavigationHistorySize().value();
            while (history.size() > historySize) {
                history.remove(0);
            }
            history.add(treeNode);
            this.offset.set(history.size() -1);
        });
    }

    private int getOffset() {
        return offset.get();
    }

    public void clear() {
        monitor.write(() -> history.clear());
    }

    public boolean hasNext() {
        return getOffset() < history.size()-1;
    }

    public boolean hasPrevious() {
        return getOffset() > 0;
    }

    @Nullable
    public BrowserTreeNode next() {
        return monitor.read(() -> {
            if (getOffset() < history.size() -1) {
                int offset = this.offset.incrementAndGet();
                BrowserTreeNode browserTreeNode = history.get(offset);
                if (browserTreeNode.isDisposed()) {
                    history.remove(browserTreeNode);
                    return next();
                }
                return browserTreeNode;
            }
            return null;
        });
    }

    @Nullable
    public BrowserTreeNode previous() {
        return monitor.read(() -> {
            if (getOffset() > 0) {
                int offset = this.offset.decrementAndGet();
                BrowserTreeNode browserTreeNode = history.get(offset);
                if (browserTreeNode.isDisposed()) {
                    history.remove(browserTreeNode);
                    return previous();
                }
                return browserTreeNode;
            }
            return null;
        });
    }

    @Override
    public void dispose() {
        history.clear();
    }
}
