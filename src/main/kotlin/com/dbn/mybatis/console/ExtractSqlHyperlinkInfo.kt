package com.dbn.mybatis.console

import com.dbn.sql.BasicFormatter
import com.dbn.utils.NotifyUtil
import com.intellij.execution.filters.HyperlinkInfo
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.project.Project
import org.apache.commons.lang3.StringUtils
import com.dbn.mybatis.console.MyBatisSqlConsoleFilter.Companion.PARAM_KEY
import com.dbn.mybatis.console.MyBatisSqlConsoleFilter.Companion.PRE_KEY
import java.awt.datatransfer.StringSelection
import java.math.BigDecimal
import java.sql.Time
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import java.util.regex.Pattern

class ExtractSqlHyperlinkInfo(private val line: String, private val sqlLine: String) : HyperlinkInfo {

    override fun navigate(project: Project) {
        val params = parseParams(StringUtils.substringAfter(line, PARAM_KEY))
        val sql = StringUtils.substringAfter(sqlLine, PRE_KEY)

        val wholeSql = parseSql(sql, params)

        val formatSql = BasicFormatter.format(wholeSql)

        val copyPasteManager = CopyPasteManager.getInstance()
        copyPasteManager.setContents(StringSelection(formatSql))

        NotifyUtil.notifyDbToolWindowInfo(project, "Tip:完成提取并格式化SQL后复制到剪切板!")
    }

    companion object {
        private val MASK_PATTERN = Pattern.compile("\\?")
        private val NEED_BRACKETS: Set<String>

        init {
            val types: MutableSet<String> = HashSet(8)
            types.add(String::class.java.simpleName)
            types.add(Date::class.java.simpleName)
            types.add(Time::class.java.simpleName)
            types.add(LocalDate::class.java.simpleName)
            types.add(LocalTime::class.java.simpleName)
            types.add(LocalDateTime::class.java.simpleName)
            types.add(BigDecimal::class.java.simpleName)
            types.add(Timestamp::class.java.simpleName)
            NEED_BRACKETS = Collections.unmodifiableSet(types)
        }

        fun parseSql(sql: String, params: Queue<Map.Entry<String, String>>): String {
            return MASK_PATTERN.matcher(sql)
                .replaceAll {
                    val entry = params.poll()
                    if (NEED_BRACKETS.contains(entry.value)) {
                        String.format("'%s'", entry.key)
                    } else {
                        entry.key
                    }
                }
        }

        fun parseParams(line: String?): Queue<Map.Entry<String, String>> {
            val lineStr = StringUtils.removeEnd(line, "\n")

            val params = StringUtils.splitByWholeSeparator(lineStr, ", ")
            val queue: Queue<Map.Entry<String, String>> = ArrayDeque(params.size)

            for (param in params) {
                val value = StringUtils.substringBeforeLast(param, "(")
                val type = StringUtils.substringBetween(param, "(", ")")
                if (StringUtils.isEmpty(type)) {
                    queue.offer(AbstractMap.SimpleEntry<String, String>(value, null))
                } else {
                    queue.offer(AbstractMap.SimpleEntry(value, type))
                }
            }

            return queue
        }
    }
}
