package com.dbn.common.file;

import com.dbn.common.icon.Icons;
import com.intellij.ide.fileTemplates.FileTemplateDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptorFactory;

public class DBNFileTemplateGroupDescriptorFactory implements FileTemplateGroupDescriptorFactory {

    private static final FileTemplateDescriptor SQL_FILE_TEMPLATE = new FileTemplateDescriptor("*.sql", Icons.FILE_SQL);
    private static final FileTemplateGroupDescriptor SQL_FILE_TEMPLATE_GROUP = new FileTemplateGroupDescriptor("DBN SQL", Icons.FILE_SQL, SQL_FILE_TEMPLATE);

    @Override
    public FileTemplateGroupDescriptor getFileTemplatesDescriptor() {
        return SQL_FILE_TEMPLATE_GROUP;
    }


}
