package com.dbn.utils;

import com.dbn.connection.DatabaseType;
import com.dbn.sql.psi.SqlColumnAlias;
import com.dbn.sql.psi.SqlColumnExpr;
import com.dbn.sql.psi.SqlColumnName;
import com.dbn.sql.psi.SqlCompoundSelectStmt;
import com.dbn.sql.psi.SqlGroupingTerm;
import com.dbn.sql.psi.SqlJoinClause;
import com.dbn.sql.psi.SqlOrderingTerm;
import com.dbn.sql.psi.SqlSelectStmt;
import com.dbn.sql.psi.SqlTableAlias;
import com.dbn.sql.psi.SqlTableName;
import com.dbn.sql.psi.SqlTableOrSubquery;
import com.dbn.sql.psi.SqlTypes;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SqlUtils {
    public static Set<IElementType> SQL_KEYWORDS = Set.of(
            SqlTypes.SELECT,
            SqlTypes.DELETE,
            SqlTypes.ADD,
            SqlTypes.UPDATE,
            SqlTypes.FROM,
            SqlTypes.INNER,
            SqlTypes.LEFT,
            SqlTypes.JOIN,
            SqlTypes.WHEN,
            SqlTypes.WHERE,
            SqlTypes.CASE,
            SqlTypes.IF,
            SqlTypes.AS,
            SqlTypes.ON,
            SqlTypes.AND,
            SqlTypes.IS,
            SqlTypes.NOT,
            SqlTypes.NULL,
            SqlTypes.CREATE,
            SqlTypes.EXISTS,
            SqlTypes.NO,
            SqlTypes.END,
            SqlTypes.FOR,
            SqlTypes.OR,
            SqlTypes.COLUMN,
            SqlTypes.COMMENT_WORD,
            SqlTypes.DEFAULT,
            SqlTypes.LIKE,
            SqlTypes.ELSE,
            SqlTypes.IN,
            SqlTypes.TO,
            SqlTypes.CAST,
            SqlTypes.LIMIT,
            SqlTypes.OFFSET,
            SqlTypes.OF,
            SqlTypes.TABLE,
            SqlTypes.INDEX,
            SqlTypes.ASC,
            SqlTypes.DESC,
            SqlTypes.BETWEEN,
            SqlTypes.BY,
            SqlTypes.ORDER,
            SqlTypes.VALUES,
            SqlTypes.UNIQUE,
            SqlTypes.UNION,
            SqlTypes.CURRENT_TIME,
            SqlTypes.CURRENT_DATE,
            SqlTypes.CURRENT_TIMESTAMP,
            SqlTypes.AUTO_INCREMENT,
            SqlTypes.MONTH,
            SqlTypes.DAY,
            SqlTypes.HOUR,
            SqlTypes.MINUTE,
            SqlTypes.ENGINE,
            SqlTypes.CHARSET,
            SqlTypes.SEPARATOR,
            SqlTypes.UNSIGNED,
            SqlTypes.INTERVAL,
            SqlTypes.TRUE,
            SqlTypes.FALSE,
            SqlTypes.SET,
            SqlTypes.PRIMARY,
            SqlTypes.KEY,
            SqlTypes.HAVING,
            SqlTypes.GROUP
    );

    public static boolean isKeyword(@Nullable IElementType tokenType) {
        if (tokenType == null) {
            return false;
        }

        return SqlUtils.SQL_KEYWORDS.contains(tokenType);
    }

    /**
     * 获取列名前的表别名
     */
    public static @Nullable SqlTableName getTableAliasNameOfColumn(SqlColumnName columnName) {
        return PsiTreeUtil.getPrevSiblingOfType(columnName, SqlTableName.class);
    }

    /**
     * 判断element是否属于列名前的表别名(只适用于select)
     */
    public static boolean isColumnTableAlias(SqlTableName element) {
        return PsiTreeUtil.getNextSiblingOfType(element, SqlColumnName.class) != null
                || getNextSiblingOfType(element, SqlTypes.MULTIPLY) != null;
    }

    public static boolean isInOrderGroupBy(SqlColumnName sqlColumnName) {
        SqlOrderingTerm sqlOrderingTerm = PsiTreeUtil.getParentOfType(sqlColumnName, SqlOrderingTerm.class);
        SqlGroupingTerm sqlGroupingTerm = PsiTreeUtil.getParentOfType(sqlColumnName, SqlGroupingTerm.class);
        return sqlGroupingTerm != null || sqlOrderingTerm != null;
    }

    public static Collection<SqlColumnAlias> getSqlColumnAliases(SqlColumnName sqlColumnName) {
        SqlCompoundSelectStmt sqlCompoundSelectStmt = PsiTreeUtil.getParentOfType(sqlColumnName, SqlCompoundSelectStmt.class);
        return PsiTreeUtil.findChildrenOfType(sqlCompoundSelectStmt, SqlColumnAlias.class);
    }

    /**
     * 若sqlColumnName位于order by或group by里,则尝试找到其对应的列别名
     */
    public static @Nullable SqlColumnAlias getColumnAliasIfInOrderGroupBy(SqlColumnName sqlColumnName, DatabaseType databaseType) {
        if (!isInOrderGroupBy(sqlColumnName)) {
            return null;
        }

        Collection<SqlColumnAlias> columnAliases = getSqlColumnAliases(sqlColumnName);

        String name = SqlUtils.convertName(sqlColumnName.getName(), databaseType);
        Optional<SqlColumnAlias> optionalSqlColumnAlias = columnAliases.stream()
                .filter(it -> name.equals(SqlUtils.convertName(it.getName(), databaseType)))
                .findFirst();

        return optionalSqlColumnAlias.orElse(null);
    }


    public static PsiElement getNextSiblingOfType(@Nullable PsiElement sibling, IElementType elementType) {
        if (sibling == null) {
            return null;
        }

        for (PsiElement nextSibling = sibling.getNextSibling(); nextSibling != null; nextSibling = nextSibling.getNextSibling()) {
            if (PsiUtil.getElementType(nextSibling) == elementType) {
                return nextSibling;
            }
        }

        return null;
    }

    /**
     * 获取列名前的表别名对应的表别名元素
     */
    public static @Nullable SqlTableAlias getTableAliasOfColumn(Map<String, List<SqlTableAlias>> aliasMap, SqlTableName columnTableAliasName) {
        String name = columnTableAliasName.getName();
        List<SqlTableAlias> sqlTableAliases = aliasMap.get(name);
        if (sqlTableAliases == null) {
            return null;
        }

        SqlTableAlias sqlTableAlias = sqlTableAliases.get(0);

        if (sqlTableAliases.size() > 1) {
            SqlSelectStmt sqlSelectStmt = PsiTreeUtil.getParentOfType(columnTableAliasName, SqlSelectStmt.class);
            // 存在同样别名的表,需要判断下哪个是真正的表
            for (SqlTableAlias sqlTableAliasTmp : sqlTableAliases) {
                SqlSelectStmt sqlSelectStmtTmp = PsiTreeUtil.getParentOfType(sqlTableAliasTmp, SqlSelectStmt.class);
                if (sqlSelectStmtTmp == sqlSelectStmt) {
                    sqlTableAlias = sqlTableAliasTmp;
                    break;
                }
            }
        }

        return sqlTableAlias;
    }

    /**
     * 获取列名前表别名对应的的表名元素
     */
    public static @Nullable SqlTableName getTableNameOfAlias(Map<String, List<SqlTableAlias>> aliasMap, SqlTableName columnTableAliasName) {
        SqlTableAlias sqlTableAlias = getTableAliasOfColumn(aliasMap, columnTableAliasName);
        if (sqlTableAlias == null) {
            return null;
        }

        return getTableNameOfAlias(sqlTableAlias);
    }

    public static @Nullable SqlTableName getTableNameOfAlias(SqlTableAlias sqlTableAlias) {
        return PsiTreeUtil.getPrevSiblingOfType(sqlTableAlias, SqlTableName.class);
    }

    public static @Nullable SqlColumnName getSqlColumnNameOfAlias(SqlColumnAlias sqlColumnAlias) {
        SqlColumnExpr sqlColumnExpr = PsiTreeUtil.getPrevSiblingOfType(sqlColumnAlias, SqlColumnExpr.class);
        return PsiTreeUtil.getChildOfType(sqlColumnExpr, SqlColumnName.class);
    }

    public static List<SqlTableName> getSqlTableNames(Collection<SqlJoinClause> sqlJoinClauses) {
        return sqlJoinClauses.stream()
                .map(sqlJoinClause -> {
                    List<SqlTableOrSubquery> tableOrSubqueryList = sqlJoinClause.getTableOrSubqueryList();
                    return tableOrSubqueryList.stream()
                            .map(SqlTableOrSubquery::getTableName)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public static Map<String, List<SqlTableAlias>> getAliasMap(Collection<SqlJoinClause> sqlJoinClauses) {
        return sqlJoinClauses.stream()
                .map(sqlJoinClause -> {
                    List<SqlTableOrSubquery> tableOrSubqueryList = sqlJoinClause.getTableOrSubqueryList();
                    return tableOrSubqueryList.stream()
                            .map(SqlTableOrSubquery::getTableAlias)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                })
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(SqlTableAlias::getName));
    }

    public static PsiElement getLastChildElement(PsiElement psiElement) {
        PsiElement lastChild = psiElement.getLastChild();
        if (lastChild != null) {
            return getLastChildElement(lastChild);
        }

        return psiElement;
    }

    public static String convertName(String name, DatabaseType dataBaseType) {
        if (dataBaseType == DatabaseType.MYSQL) {
            return name.toLowerCase();
        }

        return name;
    }
}
