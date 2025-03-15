package com.dbn.sql.reference;

import com.dbn.sql.psi.SqlColumnName;
import com.dbn.sql.psi.SqlInsertStmt;
import com.dbn.sql.psi.SqlJoinClause;
import com.dbn.sql.psi.SqlRoot;
import com.dbn.sql.psi.SqlSelectStmt;
import com.dbn.sql.psi.SqlStatement;
import com.dbn.sql.psi.SqlTableAlias;
import com.dbn.sql.psi.SqlTableName;
import com.dbn.sql.psi.SqlUpdateStmtLimited;
import com.dbn.utils.SqlUtils;
import com.intellij.openapi.util.TextRange;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.PsiReferenceRegistrar;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yudong
 */
public class SqlReferenceContributor extends PsiReferenceContributor {

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(SqlStatement.class), new SqlPsiReferenceProvider());
    }

    public static class SqlPsiReferenceProvider extends PsiReferenceProvider {

        @Override
        public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                               @NotNull ProcessingContext context) {
            SqlStatement sqlStatement = (SqlStatement) element;

            return createSqlReferences(sqlStatement);
        }


        public static PsiReference[] createSqlReferences(SqlStatement sqlStatement) {
            SqlRoot sqlRoot = PsiTreeUtil.getParentOfType(sqlStatement, SqlRoot.class);
            if (sqlRoot == null) {
                return PsiReference.EMPTY_ARRAY;
            }

            Collection<SqlJoinClause> sqlJoinClauses = PsiTreeUtil.findChildrenOfType(sqlStatement, SqlJoinClause.class);

            Collection<SqlTableName> sqlTableNames = PsiTreeUtil.findChildrenOfType(sqlStatement, SqlTableName.class);

            Collection<SqlColumnName> sqlColumnNames = PsiTreeUtil.findChildrenOfType(sqlStatement, SqlColumnName.class);

            int startOffsetInParent = calOffset(sqlRoot);

            Map<String, List<SqlTableAlias>> aliasMap = SqlUtils.getAliasMap(sqlJoinClauses);

            List<ColumnTableAliasPsiReference> columnTableAliasReferences = createColumnTableAliasReferences(sqlStatement,
                    aliasMap, sqlTableNames, startOffsetInParent);

            List<SqlTableName> sqlTableNameList = Collections.emptyList();
            List<TableOrColumnPsiReference> tableOrColumnPsiReferences = Collections.emptyList();

            if (sqlStatement.getCompoundSelectStmt() != null) {
                sqlTableNameList = SqlUtils.getSqlTableNames(sqlJoinClauses);

                tableOrColumnPsiReferences = createTableNameReferences(sqlStatement, sqlTableNameList, startOffsetInParent);
            } else if (sqlStatement.getDeleteStmtLimited() != null
                    || sqlStatement.getUpdateStmtLimited() != null
                    || sqlStatement.getInsertStmt() != null) {
                sqlTableNameList = new ArrayList<>(PsiTreeUtil.findChildrenOfType(sqlStatement, SqlTableName.class));

                tableOrColumnPsiReferences = createTableNameReferences(sqlStatement, sqlTableNameList, startOffsetInParent);
            }

            if (sqlTableNameList.isEmpty()) {
                return PsiReference.EMPTY_ARRAY;
            }

            List<TableOrColumnPsiReference> columnReferences = createColumnNameReferences(sqlStatement, sqlColumnNames,
                    aliasMap, startOffsetInParent);

            List<PsiReference> references = Lists.newArrayList();
            references.addAll(columnTableAliasReferences);
            references.addAll(tableOrColumnPsiReferences);
            references.addAll(columnReferences);

            return references.toArray(PsiReference[]::new);
        }

        private static List<TableOrColumnPsiReference> createColumnNameReferences(SqlStatement sqlStatement,
                                                                                  Collection<SqlColumnName> sqlColumnNames,
                                                                                  Map<String, List<SqlTableAlias>> aliasMap,
                                                                                  int startOffsetInParent) {
            return sqlColumnNames.stream()
                    .map(sqlColumnName -> {
                        TextRange textRange = sqlColumnName.getTextRange().shiftLeft(startOffsetInParent);

                        SqlSelectStmt sqlSelectStmt = PsiTreeUtil.getParentOfType(sqlColumnName, SqlSelectStmt.class);
                        if (sqlSelectStmt != null) {
                            SqlTableName columnTableAliasName = SqlUtils.getTableAliasNameOfColumn(sqlColumnName);

                            if (columnTableAliasName != null) {
                                return createColumnTableReference(sqlColumnName, columnTableAliasName, sqlStatement,
                                        aliasMap, textRange);
                            } else {
                                List<SqlJoinClause> sqlJoinClauses = PsiTreeUtil.getChildrenOfTypeAsList(sqlSelectStmt, SqlJoinClause.class);
                                List<SqlTableName> tableNames = SqlUtils.getSqlTableNames(sqlJoinClauses);
                                return new TableOrColumnPsiReference(sqlStatement, tableNames, sqlColumnName, textRange);
                            }
                        }

                        SqlInsertStmt sqlInsertStmt = PsiTreeUtil.getParentOfType(sqlColumnName, SqlInsertStmt.class);
                        if (sqlInsertStmt != null) {
                            SqlTableName tableName = sqlInsertStmt.getTableName();
                            return new TableOrColumnPsiReference(sqlStatement, List.of(tableName), sqlColumnName, textRange);
                        }

                        SqlUpdateStmtLimited updateStmtLimited = PsiTreeUtil.getParentOfType(sqlColumnName, SqlUpdateStmtLimited.class);
                        if (updateStmtLimited != null) {
                            SqlTableName tableName = updateStmtLimited.getQualifiedTableName().getTableName();
                            return new TableOrColumnPsiReference(sqlStatement, List.of(tableName), sqlColumnName, textRange);
                        }

                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        @Nullable
        private static TableOrColumnPsiReference createColumnTableReference(SqlColumnName sqlColumnName,
                                                                            SqlTableName columnTableAliasName,
                                                                            SqlStatement sqlStatement,
                                                                            Map<String, List<SqlTableAlias>> aliasMap,
                                                                            TextRange textRange) {
            SqlTableName sqlTableName = SqlUtils.getTableNameOfAlias(aliasMap, columnTableAliasName);
            if (sqlTableName == null) {
                return null;
            }

            return new TableOrColumnPsiReference(sqlStatement, List.of(sqlTableName), sqlColumnName, textRange);
        }

        private static List<TableOrColumnPsiReference> createTableNameReferences(SqlStatement sqlStatement,
                                                                                 List<SqlTableName> sqlTableNameList,
                                                                                 int startOffsetInParent) {
            return sqlTableNameList.stream()
                    .map(sqlTableName -> {
                        TextRange textRange = sqlTableName.getTextRange().shiftLeft(startOffsetInParent);
                        return new TableOrColumnPsiReference(sqlStatement, List.of(sqlTableName), null, textRange);
                    })
                    .collect(Collectors.toList());
        }

        private static List<ColumnTableAliasPsiReference> createColumnTableAliasReferences(SqlStatement sqlStatement,
                                                                                           Map<String, List<SqlTableAlias>> aliasMap,
                                                                                           Collection<SqlTableName> sqlTableNames,
                                                                                           int startOffsetInParent) {
            if (aliasMap.isEmpty()) {
                return Lists.newArrayList();
            }

            return sqlTableNames.stream()
                    .filter(SqlUtils::isColumnTableAlias)
                    .map(columnTableAlias -> {
                        TextRange textRange = columnTableAlias.getTextRange().shiftLeft(startOffsetInParent);

                        SqlTableName sqlTableName = SqlUtils.getTableNameOfAlias(aliasMap, columnTableAlias);
                        if (sqlTableName == null) {
                            return new ColumnTableAliasPsiReference(sqlStatement, textRange, null);
                        }

                        SqlTableAlias sqlTableAlias = SqlUtils.getTableAliasOfColumn(aliasMap, columnTableAlias);
                        if (sqlTableAlias == null) {
                            return new ColumnTableAliasPsiReference(sqlStatement, textRange, null);
                        }

                        return new ColumnTableAliasPsiReference(sqlStatement, textRange, sqlTableAlias);
                    })
                    .collect(Collectors.toList());
        }

        private static int calOffset(PsiElement psiElement) {
            PsiElement prevSibling = psiElement.getPrevSibling();
            if (prevSibling != null) {
                return prevSibling.getTextLength() + calOffset(prevSibling);
            } else {
                return 0;
            }
        }
    }

}