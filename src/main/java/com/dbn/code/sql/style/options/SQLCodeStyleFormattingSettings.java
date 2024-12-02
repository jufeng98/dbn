package com.dbn.code.sql.style.options;

import com.dbn.code.common.style.options.CodeStyleFormattingOption;
import com.dbn.code.common.style.options.CodeStyleFormattingSettings;
import com.dbn.code.common.style.options.DBLCodeStyleSettings;
import com.dbn.code.common.style.presets.clause.ClauseChopDownAlwaysPreset;
import com.dbn.code.common.style.presets.clause.ClauseChopDownIfLongPreset;
import com.dbn.code.common.style.presets.clause.ClauseChopDownIfLongStatementPreset;
import com.dbn.code.common.style.presets.clause.ClauseChopDownNeverPreset;
import com.dbn.code.common.style.presets.clause.ClauseIgnoreWrappingPreset;
import com.dbn.code.common.style.presets.iteration.IterationChopDownAlwaysPreset;
import com.dbn.code.common.style.presets.iteration.IterationChopDownIfLongPreset;
import com.dbn.code.common.style.presets.iteration.IterationChopDownIfLongStatementPreset;
import com.dbn.code.common.style.presets.iteration.IterationChopDownIfNotSinglePreset;
import com.dbn.code.common.style.presets.iteration.IterationIgnoreWrappingPreset;
import com.dbn.code.common.style.presets.iteration.IterationNoWrappingPreset;
import com.dbn.code.common.style.presets.statement.StatementIgnoreSpacingPreset;
import com.dbn.code.common.style.presets.statement.StatementLineBreakAtLeastPreset;
import com.dbn.code.common.style.presets.statement.StatementLineBreakPreset;
import com.dbn.code.common.style.presets.statement.StatementOneLineSpacingAtLeastPreset;
import com.dbn.code.common.style.presets.statement.StatementOneLineSpacingPreset;

class SQLCodeStyleFormattingSettings extends CodeStyleFormattingSettings {
    SQLCodeStyleFormattingSettings(DBLCodeStyleSettings<?, ?> parent) {
        super(parent);

        CodeStyleFormattingOption statementSpacing =
                new CodeStyleFormattingOption("STATEMENT_SPACING", "Statement spacing");
        statementSpacing.addPreset(new StatementLineBreakPreset());
        statementSpacing.addPreset(new StatementLineBreakAtLeastPreset());
        statementSpacing.addPreset(new StatementOneLineSpacingPreset(), true);
        statementSpacing.addPreset(new StatementOneLineSpacingAtLeastPreset());
        statementSpacing.addPreset(new StatementIgnoreSpacingPreset());
        addOption(statementSpacing);

        CodeStyleFormattingOption clauseChopDown =
                new CodeStyleFormattingOption("CLAUSE_CHOP_DOWN", "Clause chop down");
        clauseChopDown.addPreset(new ClauseChopDownAlwaysPreset());
        clauseChopDown.addPreset(new ClauseChopDownIfLongPreset());
        clauseChopDown.addPreset(new ClauseChopDownIfLongStatementPreset(), true);
        clauseChopDown.addPreset(new ClauseChopDownNeverPreset());
        clauseChopDown.addPreset(new ClauseIgnoreWrappingPreset());
        addOption(clauseChopDown);

        CodeStyleFormattingOption iterationsWrapOption =
                new CodeStyleFormattingOption("ITERATION_ELEMENTS_WRAPPING", "Iteration elements wrapping");
        iterationsWrapOption.addPreset(new IterationChopDownAlwaysPreset());
        iterationsWrapOption.addPreset(new IterationChopDownIfLongPreset());
        iterationsWrapOption.addPreset(new IterationChopDownIfLongStatementPreset());
        iterationsWrapOption.addPreset(new IterationChopDownIfNotSinglePreset(), true);
        iterationsWrapOption.addPreset(new IterationNoWrappingPreset());
        iterationsWrapOption.addPreset(new IterationIgnoreWrappingPreset());
        addOption(iterationsWrapOption);
    }
}
