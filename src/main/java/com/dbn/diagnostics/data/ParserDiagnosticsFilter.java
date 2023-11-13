package com.dbn.diagnostics.data;

import com.dbn.common.filter.Filter;
import com.dbn.common.util.Strings;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParserDiagnosticsFilter implements Filter<ParserDiagnosticsEntry> {
    public static final ParserDiagnosticsFilter EMPTY = new ParserDiagnosticsFilter();

    private StateTransition.Category stateCategory;
    private String fileType;

    @Override
    public boolean accepts(ParserDiagnosticsEntry entry) {
        return matchesState(entry) && matchesFileType(entry);
    }

    private boolean matchesState(ParserDiagnosticsEntry object) {
        return stateCategory == null || stateCategory == object.getStateTransition().getCategory();
    }

    private boolean matchesFileType(ParserDiagnosticsEntry entry) {
        return Strings.isEmpty(fileType) || Strings.endsWithIgnoreCase(entry.getFilePath(), "." + fileType);
    }
}
