package com.dbn.editor.data.filter;

import com.dbn.connection.DatabaseType;
import com.dbn.data.grid.options.DataGridSettings;
import com.dbn.data.sorting.SortDirection;
import com.dbn.data.sorting.SortingInstruction;
import com.dbn.data.sorting.SortingState;
import com.dbn.database.DatabaseCompatibility;
import com.dbn.database.JdbcProperty;
import com.dbn.database.interfaces.DatabaseCompatibilityInterface;
import com.dbn.language.sql.SqlElementFactory;
import com.dbn.object.DBColumn;
import com.dbn.object.DBDataset;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;

import java.util.List;

import static com.dbn.common.dispose.Checks.isValid;

public class DatasetFilterUtil {

    public static void addOrderByClause(DBDataset dataset, StringBuilder buffer, SortingState sortingState) {
        DataGridSettings dataGridSettings = DataGridSettings.getInstance(dataset.getProject());
        boolean nullsFirst = dataGridSettings.getSortingSettings().isNullsFirst();
        List<SortingInstruction> sortingInstructions = sortingState.getInstructions();
        if (sortingInstructions.isEmpty()) return;

        buffer.append(" order by ");
        boolean instructionAdded = false;
        for (SortingInstruction sortingInstruction : sortingInstructions) {
            SortDirection sortDirection = sortingInstruction.getDirection();
            DBColumn column = dataset.getColumn(sortingInstruction.getColumnName());
            if (isValid(column) && !sortDirection.isIndefinite()) {
                DatabaseCompatibilityInterface compatibility = column.getCompatibilityInterface();
                String orderByClause = compatibility.getOrderByClause(column.getQuotedName(false), sortDirection, nullsFirst);
                buffer.append(instructionAdded ? ", " : "");
                buffer.append(orderByClause);
                instructionAdded = true;
            }
        }
    }

    public static void createSelectStatement(DBDataset dataset, StringBuilder buffer) {
        buffer.append("select ");
        int index = 0;
        for (DBColumn column : dataset.getColumns()) {
            if (index > 0) {
                buffer.append(", ");
            }
            buffer.append(column.getQuotedName(false));
            index++;
        }
        buffer.append(" from ");
        buffer.append(dataset.getSchema().getQuotedName(true));
        buffer.append(".");
        buffer.append(dataset.getQuotedName(true));

    }

    public static void createSimpleSelectStatement(DBDataset dataset, StringBuilder buffer) {
        DatabaseCompatibility compatibility = dataset.getConnection().getCompatibility();
        // TODO not implemented yet - returning always true at the moment
        boolean aliased = compatibility.isSupported(JdbcProperty.SQL_DATASET_ALIASING);

        String schemaName = dataset.getSchema().getQuotedName(true);
        String datasetName = dataset.getQuotedName(true);

        if (aliased) {
            // IMPORTANT oracle jdbc seems to create readonly result-set if dataset is not aliased
            buffer.append("select a.* from ");
            buffer.append(schemaName);
            buffer.append(".");
            buffer.append(datasetName);
            buffer.append(" a");
        } else {
            buffer.append("select * from ");
            buffer.append(schemaName);
            buffer.append(".");
            buffer.append(datasetName);
        }
    }

    public static String appendLimitIfLack(String sql, int pageNum, int pageSize, Project project, DatabaseType databaseType) {
        sql = sql.trim();
        if (sql.endsWith(";")) {
            sql = sql.substring(0, sql.length() - 1);
        }
        String finalSql = sql;
        String tmpSql = ReadAction.compute(() -> {
            PsiElement sqlElement = SqlElementFactory.createSqlElement(project, finalSql);
            return sqlElement.getText().toLowerCase();
        });

        if (databaseType == DatabaseType.MYSQL) {
            if (tmpSql.startsWith("select")) {
                boolean noLimit = !tmpSql.endsWith("limit");
                if (noLimit) {
                    String limit = DatasetFilterUtil.createMySqlLimit(pageNum, pageSize);
                    return sql + limit;
                }
            }
        }

        return sql;
    }

    public static String createLimit(DBDataset dataset, Integer pageNum, Integer pageSize) {
        if (dataset.getConnection().getDatabaseType() == DatabaseType.MYSQL) {
            return createMySqlLimit(pageNum, pageSize);
        }

        return null;
    }

    // TODO yudong 先只处理MySQL的分页参数
    private static String createMySqlLimit(int pageNum, int pageSize) {
        int start = pageNum * pageSize;
        return " limit " + start + "," + pageSize;
    }
}
