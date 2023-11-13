package com.dbn.ddl;

import com.dbn.common.util.Strings;
import com.dbn.editor.DBContentType;
import com.dbn.language.common.DBLanguageFileType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
public class DDLFileType {
    private final DBLanguageFileType languageFileType;
    private final DDLFileTypeId id;
    private final String description;
    private final DBContentType contentType;
    private Set<String> extensions = new LinkedHashSet<>();

    public DDLFileType(DDLFileTypeId id, String description, String extension, DBLanguageFileType languageFileType, DBContentType contentType) {
        this.id = id;
        this.description = description;
        this.extensions.add(extension);
        this.languageFileType = languageFileType;
        this.contentType = contentType;
    }

    public boolean setExtensions(Collection<String> extensions) {
        extensions = new LinkedHashSet<>(extensions);
        if (!extensions.containsAll(this.extensions) || !this.extensions.containsAll(extensions)) {
            this.extensions = (Set<String>) extensions;
            return true;
        }
        return false;
    }

    public String getFirstExtension() {
        return extensions.stream().findFirst().orElse(null);
    }

    public String getExtensionsAsString() {
        return Strings.concatenate(extensions, ", ");
    }

    public boolean setExtensionsAsString(String extensions) {
        return setExtensions(Strings.tokenize(extensions, ","));
    }

}
