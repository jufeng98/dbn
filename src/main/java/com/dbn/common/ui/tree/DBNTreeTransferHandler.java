package com.dbn.common.ui.tree;

import com.dbn.common.util.Strings;
import com.intellij.openapi.ide.CopyPasteManager;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.TreePath;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class DBNTreeTransferHandler extends TransferHandler {
    public static DBNTreeTransferHandler INSTANCE = new DBNTreeTransferHandler();

    private DBNTreeTransferHandler() {}

    @Override
    public void exportToClipboard(JComponent comp, Clipboard clip, int action) {
        JTree tree = (JTree)comp;
        TreePath[] paths = tree.getSelectionPaths();
        if(paths != null && paths.length > 0) {
            StringBuilder builder = new StringBuilder();
            for (TreePath path : paths) {
                builder.append(path.getLastPathComponent().toString());
                builder.append("\n");
            }
            builder.delete(builder.length() - 1, builder.length());

            String contentString = builder.toString().trim();
            if (Strings.isNotEmpty(contentString)) {
                StringSelection contents = new StringSelection(contentString);
                //clip.setContents(contents, null);

                CopyPasteManager copyPasteManager = CopyPasteManager.getInstance();
                copyPasteManager.setContents(contents);
            }

        }
    }
}
