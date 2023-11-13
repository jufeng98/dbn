package com.dbn.language.sql.dialect.mysql;

import com.dbn.language.common.SharedTokenTypeBundle;
import com.dbn.language.common.TokenTypeBundle;
import com.intellij.psi.tree.IElementType;

%%

%class MysqlSQLParserFlexLexer
%implements FlexLexer
%public
%final
%unicode
%ignorecase
%function advance
%type IElementType
%eof{ return;
%eof}

%{
    private TokenTypeBundle tt;
    private SharedTokenTypeBundle stt;
    public MysqlSQLParserFlexLexer(TokenTypeBundle tt) {
        this.tt = tt;
        this.stt = tt.getSharedTokenTypes();
    }
%}

WHITE_SPACE= {white_space_char}|{line_terminator}
line_terminator = \r|\n|\r\n
input_character = [^\r\n]
white_space = [ \t\f]
white_space_char= [ \n\r\t\f]
ws  = {WHITE_SPACE}+
wso = {WHITE_SPACE}*

comment_tail =([^"*"]*("*"+[^"*""/"])?)*("*"+"/")?
BLOCK_COMMENT=("/*"[^]{comment_tail})|"/*"
LINE_COMMENT = ("--"|"#"){input_character}*

IDENTIFIER = [:jletter:] [:jletterdigit:]*
QUOTED_IDENTIFIER = "`"[^\`]*"`"?

CHARSET ="armscii8"|"ascii"|"big5"|"binary"|"cp1250"|"cp1251"|"cp1256"|"cp1257"|"cp850"|"cp852"|"cp866"|"cp932"|"dec8"|"eucjpms"|"euckr"|"gb2312"|"gbk"|"geostd8"|"greek"|"hebrew"|"hp8"|"keybcs2"|"koi8r"|"koi8u"|"latin1"|"latin2"|"latin5"|"latin7"|"macce"|"macroman"|"sjis"|"swe7"|"tis620"|"ucs2"|"ujis"|"utf8"

string_simple_quoted      = "'"([^\']|"''"|{WHITE_SPACE})*"'"?
STRING = ("n"|"_"{CHARSET})?{wso}{string_simple_quoted}

sign = "+"|"-"
digit = [0-9]
INTEGER = {digit}+("e"{sign}?{digit}+)?
NUMBER = {INTEGER}?"."{digit}+(("e"{sign}?{digit}+)|(("f"|"d"){ws}))?

VARIABLE = ":"{wso}({IDENTIFIER}|{INTEGER})

%state DIV
%%

{WHITE_SPACE}+   { return stt.getWhiteSpace(); }

{BLOCK_COMMENT}  { return stt.getBlockComment(); }
{LINE_COMMENT}   { return stt.getLineComment(); }

{VARIABLE}       { return stt.getVariable(); }
{INTEGER}        { return stt.getInteger(); }
{NUMBER}         { return stt.getNumber(); }
{STRING}         { return stt.getString(); }

"="{wso}"=" {return tt.getOperatorTokenType(0);}
"|"{wso}"|" {return tt.getOperatorTokenType(1);}
"<"{wso}"=" {return tt.getOperatorTokenType(2);}
">"{wso}"=" {return tt.getOperatorTokenType(3);}
"<"{wso}">" {return tt.getOperatorTokenType(4);}
"!"{wso}"=" {return tt.getOperatorTokenType(5);}
":"{wso}"=" {return tt.getOperatorTokenType(6);}
"="{wso}">" {return tt.getOperatorTokenType(7);}
".."        {return tt.getOperatorTokenType(8);}
"::"        {return tt.getOperatorTokenType(9);}

"("{wso}"+"{wso}")"  {return tt.getTokenType("CT_OUTER_JOIN");}

"@" {return tt.getCharacterTokenType(0);}
":" {return tt.getCharacterTokenType(1);}
"," {return tt.getCharacterTokenType(2);}
"." {return tt.getCharacterTokenType(3);}
"=" {return tt.getCharacterTokenType(4);}
"!" {return tt.getCharacterTokenType(5);}
">" {return tt.getCharacterTokenType(6);}
"#" {return tt.getCharacterTokenType(7);}
"[" {return tt.getCharacterTokenType(8);}
"{" {return tt.getCharacterTokenType(9);}
"(" {return tt.getCharacterTokenType(10);}
"<" {return tt.getCharacterTokenType(11);}
"-" {return tt.getCharacterTokenType(12);}
"%" {return tt.getCharacterTokenType(13);}
"+" {return tt.getCharacterTokenType(14);}
"]" {return tt.getCharacterTokenType(15);}
"}" {return tt.getCharacterTokenType(16);}
")" {return tt.getCharacterTokenType(17);}
";" {return tt.getCharacterTokenType(18);}
"/" {return tt.getCharacterTokenType(19);}
"*" {return tt.getCharacterTokenType(20);}
"|" {return tt.getCharacterTokenType(21);}






"bit" {return tt.getDataTypeTokenType(0);}
"tinyint" {return tt.getDataTypeTokenType(1);}
"bool" {return tt.getDataTypeTokenType(2);}
"boolean" {return tt.getDataTypeTokenType(3);}
"smallint" {return tt.getDataTypeTokenType(4);}
"mediumint" {return tt.getDataTypeTokenType(5);}
"int" {return tt.getDataTypeTokenType(6);}
"integer" {return tt.getDataTypeTokenType(7);}
"bigint" {return tt.getDataTypeTokenType(8);}
"double"{ws}"precision" {return tt.getDataTypeTokenType(9);}
"double" {return tt.getDataTypeTokenType(10);}
"real" {return tt.getDataTypeTokenType(11);}
"float" {return tt.getDataTypeTokenType(12);}
"decimal" {return tt.getDataTypeTokenType(13);}
"dec" {return tt.getDataTypeTokenType(14);}
"numeric" {return tt.getDataTypeTokenType(15);}
"fixed" {return tt.getDataTypeTokenType(16);}
"date" {return tt.getDataTypeTokenType(17);}
"datetime" {return tt.getDataTypeTokenType(18);}
"timestamp" {return tt.getDataTypeTokenType(19);}
"time" {return tt.getDataTypeTokenType(20);}
"year" {return tt.getDataTypeTokenType(21);}
"varchar" {return tt.getDataTypeTokenType(22);}
"national"{ws}"varchar" {return tt.getDataTypeTokenType(23);}
"binary" {return tt.getDataTypeTokenType(24);}
"varbinary" {return tt.getDataTypeTokenType(25);}
"tinyblob" {return tt.getDataTypeTokenType(26);}
"tinytext" {return tt.getDataTypeTokenType(27);}
"blob" {return tt.getDataTypeTokenType(28);}
"text" {return tt.getDataTypeTokenType(29);}
"mediumblob" {return tt.getDataTypeTokenType(30);}
"mediumtext" {return tt.getDataTypeTokenType(31);}
"longblob" {return tt.getDataTypeTokenType(32);}
"longtext" {return tt.getDataTypeTokenType(33);}
"enum" {return tt.getDataTypeTokenType(34);}



"accessible" {return tt.getKeywordTokenType(0);}
"add" {return tt.getKeywordTokenType(1);}
"algorithm" {return tt.getKeywordTokenType(2);}
"all" {return tt.getKeywordTokenType(3);}
"alter" {return tt.getKeywordTokenType(4);}
"analyze" {return tt.getKeywordTokenType(5);}
"and" {return tt.getKeywordTokenType(6);}
"as" {return tt.getKeywordTokenType(7);}
"asc" {return tt.getKeywordTokenType(8);}
"asensitive" {return tt.getKeywordTokenType(9);}
"before" {return tt.getKeywordTokenType(10);}
"between" {return tt.getKeywordTokenType(11);}
"both" {return tt.getKeywordTokenType(12);}
"by" {return tt.getKeywordTokenType(13);}
"call" {return tt.getKeywordTokenType(14);}
"cascade" {return tt.getKeywordTokenType(15);}
"cascaded" {return tt.getKeywordTokenType(16);}
"case" {return tt.getKeywordTokenType(17);}
"change" {return tt.getKeywordTokenType(18);}
"character" {return tt.getKeywordTokenType(19);}
"check" {return tt.getKeywordTokenType(20);}
"close" {return tt.getKeywordTokenType(21);}
"collate" {return tt.getKeywordTokenType(22);}
"column" {return tt.getKeywordTokenType(23);}
"columns" {return tt.getKeywordTokenType(24);}
"concurrent" {return tt.getKeywordTokenType(25);}
"condition" {return tt.getKeywordTokenType(26);}
"constraint" {return tt.getKeywordTokenType(27);}
"continue" {return tt.getKeywordTokenType(28);}
"convert" {return tt.getKeywordTokenType(29);}
"create" {return tt.getKeywordTokenType(30);}
"cross" {return tt.getKeywordTokenType(31);}
"current_user" {return tt.getKeywordTokenType(32);}
"cursor" {return tt.getKeywordTokenType(33);}
"data" {return tt.getKeywordTokenType(34);}
"database" {return tt.getKeywordTokenType(35);}
"databases" {return tt.getKeywordTokenType(36);}
"declare" {return tt.getKeywordTokenType(37);}
"default" {return tt.getKeywordTokenType(38);}
"definer" {return tt.getKeywordTokenType(39);}
"delayed" {return tt.getKeywordTokenType(40);}
"delete" {return tt.getKeywordTokenType(41);}
"desc" {return tt.getKeywordTokenType(42);}
"describe" {return tt.getKeywordTokenType(43);}
"deterministic" {return tt.getKeywordTokenType(44);}
"distinct" {return tt.getKeywordTokenType(45);}
"distinctrow" {return tt.getKeywordTokenType(46);}
"div" {return tt.getKeywordTokenType(47);}
"do" {return tt.getKeywordTokenType(48);}
"drop" {return tt.getKeywordTokenType(49);}
"dual" {return tt.getKeywordTokenType(50);}
"dumpfile" {return tt.getKeywordTokenType(51);}
"duplicate" {return tt.getKeywordTokenType(52);}
"each" {return tt.getKeywordTokenType(53);}
"else" {return tt.getKeywordTokenType(54);}
"elseif" {return tt.getKeywordTokenType(55);}
"enclosed" {return tt.getKeywordTokenType(56);}
"end" {return tt.getKeywordTokenType(57);}
"escaped" {return tt.getKeywordTokenType(58);}
"exists" {return tt.getKeywordTokenType(59);}
"exit" {return tt.getKeywordTokenType(60);}
"expansion" {return tt.getKeywordTokenType(61);}
"explain" {return tt.getKeywordTokenType(62);}
"fetch" {return tt.getKeywordTokenType(63);}
"fields" {return tt.getKeywordTokenType(64);}
"first" {return tt.getKeywordTokenType(65);}
"float4" {return tt.getKeywordTokenType(66);}
"float8" {return tt.getKeywordTokenType(67);}
"for" {return tt.getKeywordTokenType(68);}
"force" {return tt.getKeywordTokenType(69);}
"foreign" {return tt.getKeywordTokenType(70);}
"from" {return tt.getKeywordTokenType(71);}
"fulltext" {return tt.getKeywordTokenType(72);}
"grant" {return tt.getKeywordTokenType(73);}
"group" {return tt.getKeywordTokenType(74);}
"handler" {return tt.getKeywordTokenType(75);}
"having" {return tt.getKeywordTokenType(76);}
"high_priority" {return tt.getKeywordTokenType(77);}
"if" {return tt.getKeywordTokenType(78);}
"ignore" {return tt.getKeywordTokenType(79);}
"in" {return tt.getKeywordTokenType(80);}
"index" {return tt.getKeywordTokenType(81);}
"infile" {return tt.getKeywordTokenType(82);}
"inner" {return tt.getKeywordTokenType(83);}
"inout" {return tt.getKeywordTokenType(84);}
"insensitive" {return tt.getKeywordTokenType(85);}
"insert" {return tt.getKeywordTokenType(86);}
"int1" {return tt.getKeywordTokenType(87);}
"int2" {return tt.getKeywordTokenType(88);}
"int3" {return tt.getKeywordTokenType(89);}
"int4" {return tt.getKeywordTokenType(90);}
"int8" {return tt.getKeywordTokenType(91);}
"interval" {return tt.getKeywordTokenType(92);}
"into" {return tt.getKeywordTokenType(93);}
"invoker" {return tt.getKeywordTokenType(94);}
"is" {return tt.getKeywordTokenType(95);}
"iterate" {return tt.getKeywordTokenType(96);}
"join" {return tt.getKeywordTokenType(97);}
"key" {return tt.getKeywordTokenType(98);}
"keys" {return tt.getKeywordTokenType(99);}
"kill" {return tt.getKeywordTokenType(100);}
"language" {return tt.getKeywordTokenType(101);}
"last" {return tt.getKeywordTokenType(102);}
"leading" {return tt.getKeywordTokenType(103);}
"leave" {return tt.getKeywordTokenType(104);}
"left" {return tt.getKeywordTokenType(105);}
"level" {return tt.getKeywordTokenType(106);}
"like" {return tt.getKeywordTokenType(107);}
"limit" {return tt.getKeywordTokenType(108);}
"linear" {return tt.getKeywordTokenType(109);}
"lines" {return tt.getKeywordTokenType(110);}
"load" {return tt.getKeywordTokenType(111);}
"local" {return tt.getKeywordTokenType(112);}
"lock" {return tt.getKeywordTokenType(113);}
"long" {return tt.getKeywordTokenType(114);}
"loop" {return tt.getKeywordTokenType(115);}
"low_ignore" {return tt.getKeywordTokenType(116);}
"low_priority" {return tt.getKeywordTokenType(117);}
"master_ssl_verify_server_cert" {return tt.getKeywordTokenType(118);}
"match" {return tt.getKeywordTokenType(119);}
"merge" {return tt.getKeywordTokenType(120);}
"microsecond" {return tt.getKeywordTokenType(121);}
"middleint" {return tt.getKeywordTokenType(122);}
"mod" {return tt.getKeywordTokenType(123);}
"mode" {return tt.getKeywordTokenType(124);}
"modifies" {return tt.getKeywordTokenType(125);}
"natural" {return tt.getKeywordTokenType(126);}
"next" {return tt.getKeywordTokenType(127);}
"no_write_to_binlog" {return tt.getKeywordTokenType(128);}
"not" {return tt.getKeywordTokenType(129);}
"null" {return tt.getKeywordTokenType(130);}
"offset" {return tt.getKeywordTokenType(131);}
"oj" {return tt.getKeywordTokenType(132);}
"on" {return tt.getKeywordTokenType(133);}
"open" {return tt.getKeywordTokenType(134);}
"optimize" {return tt.getKeywordTokenType(135);}
"option" {return tt.getKeywordTokenType(136);}
"optionally" {return tt.getKeywordTokenType(137);}
"or" {return tt.getKeywordTokenType(138);}
"order" {return tt.getKeywordTokenType(139);}
"out" {return tt.getKeywordTokenType(140);}
"outer" {return tt.getKeywordTokenType(141);}
"outfile" {return tt.getKeywordTokenType(142);}
"precision" {return tt.getKeywordTokenType(143);}
"prev" {return tt.getKeywordTokenType(144);}
"primary" {return tt.getKeywordTokenType(145);}
"procedure" {return tt.getKeywordTokenType(146);}
"purge" {return tt.getKeywordTokenType(147);}
"query" {return tt.getKeywordTokenType(148);}
"quick" {return tt.getKeywordTokenType(149);}
"range" {return tt.getKeywordTokenType(150);}
"read" {return tt.getKeywordTokenType(151);}
"read_only" {return tt.getKeywordTokenType(152);}
"read_write" {return tt.getKeywordTokenType(153);}
"reads" {return tt.getKeywordTokenType(154);}
"references" {return tt.getKeywordTokenType(155);}
"regexp" {return tt.getKeywordTokenType(156);}
"release" {return tt.getKeywordTokenType(157);}
"rename" {return tt.getKeywordTokenType(158);}
"repeat" {return tt.getKeywordTokenType(159);}
"replace" {return tt.getKeywordTokenType(160);}
"require" {return tt.getKeywordTokenType(161);}
"restrict" {return tt.getKeywordTokenType(162);}
"return" {return tt.getKeywordTokenType(163);}
"reverse" {return tt.getKeywordTokenType(164);}
"revoke" {return tt.getKeywordTokenType(165);}
"right" {return tt.getKeywordTokenType(166);}
"rlike" {return tt.getKeywordTokenType(167);}
"rollup" {return tt.getKeywordTokenType(168);}
"schema" {return tt.getKeywordTokenType(169);}
"schemas" {return tt.getKeywordTokenType(170);}
"security" {return tt.getKeywordTokenType(171);}
"select" {return tt.getKeywordTokenType(172);}
"sensitive" {return tt.getKeywordTokenType(173);}
"separator" {return tt.getKeywordTokenType(174);}
"set" {return tt.getKeywordTokenType(175);}
"share" {return tt.getKeywordTokenType(176);}
"show" {return tt.getKeywordTokenType(177);}
"spatial" {return tt.getKeywordTokenType(178);}
"specific" {return tt.getKeywordTokenType(179);}
"sql" {return tt.getKeywordTokenType(180);}
"sql_big_result" {return tt.getKeywordTokenType(181);}
"sql_buffer_result" {return tt.getKeywordTokenType(182);}
"sql_cache" {return tt.getKeywordTokenType(183);}
"sql_calc_found_rows" {return tt.getKeywordTokenType(184);}
"sql_no_cache" {return tt.getKeywordTokenType(185);}
"sql_small_result" {return tt.getKeywordTokenType(186);}
"sqlexception" {return tt.getKeywordTokenType(187);}
"sqlstate" {return tt.getKeywordTokenType(188);}
"sqlwarning" {return tt.getKeywordTokenType(189);}
"ssl" {return tt.getKeywordTokenType(190);}
"starting" {return tt.getKeywordTokenType(191);}
"straight_join" {return tt.getKeywordTokenType(192);}
"table" {return tt.getKeywordTokenType(193);}
"temptable" {return tt.getKeywordTokenType(194);}
"terminated" {return tt.getKeywordTokenType(195);}
"then" {return tt.getKeywordTokenType(196);}
"to" {return tt.getKeywordTokenType(197);}
"trailing" {return tt.getKeywordTokenType(198);}
"trigger" {return tt.getKeywordTokenType(199);}
"truncate" {return tt.getKeywordTokenType(200);}
"undefined" {return tt.getKeywordTokenType(201);}
"undo" {return tt.getKeywordTokenType(202);}
"union" {return tt.getKeywordTokenType(203);}
"unique" {return tt.getKeywordTokenType(204);}
"unlock" {return tt.getKeywordTokenType(205);}
"unsigned" {return tt.getKeywordTokenType(206);}
"update" {return tt.getKeywordTokenType(207);}
"usage" {return tt.getKeywordTokenType(208);}
"use" {return tt.getKeywordTokenType(209);}
"using" {return tt.getKeywordTokenType(210);}
"value" {return tt.getKeywordTokenType(211);}
"values" {return tt.getKeywordTokenType(212);}
"varcharacter" {return tt.getKeywordTokenType(213);}
"varying" {return tt.getKeywordTokenType(214);}
"view" {return tt.getKeywordTokenType(215);}
"when" {return tt.getKeywordTokenType(216);}
"where" {return tt.getKeywordTokenType(217);}
"while" {return tt.getKeywordTokenType(218);}
"with" {return tt.getKeywordTokenType(219);}
"write" {return tt.getKeywordTokenType(220);}
"xor" {return tt.getKeywordTokenType(221);}
"zerofill" {return tt.getKeywordTokenType(222);}
"false" {return tt.getKeywordTokenType(223);}
"true" {return tt.getKeywordTokenType(224);}





"abs" {return tt.getFunctionTokenType(0);}
"acos" {return tt.getFunctionTokenType(1);}
"adddate" {return tt.getFunctionTokenType(2);}
"addtime" {return tt.getFunctionTokenType(3);}
"aes_decrypt" {return tt.getFunctionTokenType(4);}
"aes_encrypt" {return tt.getFunctionTokenType(5);}
"against" {return tt.getFunctionTokenType(6);}
"ascii" {return tt.getFunctionTokenType(7);}
"asin" {return tt.getFunctionTokenType(8);}
"atan" {return tt.getFunctionTokenType(9);}
"atan2" {return tt.getFunctionTokenType(10);}
"avg" {return tt.getFunctionTokenType(11);}
"benchmark" {return tt.getFunctionTokenType(12);}
"bin" {return tt.getFunctionTokenType(13);}
"bit_and" {return tt.getFunctionTokenType(14);}
"bit_length" {return tt.getFunctionTokenType(15);}
"bit_or" {return tt.getFunctionTokenType(16);}
"bit_xor" {return tt.getFunctionTokenType(17);}
"ceil" {return tt.getFunctionTokenType(18);}
"ceiling" {return tt.getFunctionTokenType(19);}
"char" {return tt.getFunctionTokenType(20);}
"char_length" {return tt.getFunctionTokenType(21);}
"character_length" {return tt.getFunctionTokenType(22);}
"charset" {return tt.getFunctionTokenType(23);}
"coercibility" {return tt.getFunctionTokenType(24);}
"collation" {return tt.getFunctionTokenType(25);}
"compress" {return tt.getFunctionTokenType(26);}
"concat" {return tt.getFunctionTokenType(27);}
"concat_ws" {return tt.getFunctionTokenType(28);}
"connection_id" {return tt.getFunctionTokenType(29);}
"conv" {return tt.getFunctionTokenType(30);}
"convert_tz" {return tt.getFunctionTokenType(31);}
"cos" {return tt.getFunctionTokenType(32);}
"cot" {return tt.getFunctionTokenType(33);}
"count" {return tt.getFunctionTokenType(34);}
"crc32" {return tt.getFunctionTokenType(35);}
"curdate" {return tt.getFunctionTokenType(36);}
"current_date" {return tt.getFunctionTokenType(37);}
"current_time" {return tt.getFunctionTokenType(38);}
"current_timestamp" {return tt.getFunctionTokenType(39);}
"curtime" {return tt.getFunctionTokenType(40);}
"date_add" {return tt.getFunctionTokenType(41);}
"date_format" {return tt.getFunctionTokenType(42);}
"date_sub" {return tt.getFunctionTokenType(43);}
"datediff" {return tt.getFunctionTokenType(44);}
"day" {return tt.getFunctionTokenType(45);}
"day_hour" {return tt.getFunctionTokenType(46);}
"day_microsecond" {return tt.getFunctionTokenType(47);}
"day_minute" {return tt.getFunctionTokenType(48);}
"day_second" {return tt.getFunctionTokenType(49);}
"dayname" {return tt.getFunctionTokenType(50);}
"dayofmonth" {return tt.getFunctionTokenType(51);}
"dayofweek" {return tt.getFunctionTokenType(52);}
"dayofyear" {return tt.getFunctionTokenType(53);}
"decode" {return tt.getFunctionTokenType(54);}
"degrees" {return tt.getFunctionTokenType(55);}
"des_decrypt" {return tt.getFunctionTokenType(56);}
"des_encrypt" {return tt.getFunctionTokenType(57);}
"elt" {return tt.getFunctionTokenType(58);}
"encode" {return tt.getFunctionTokenType(59);}
"encrypt" {return tt.getFunctionTokenType(60);}
"exp" {return tt.getFunctionTokenType(61);}
"export_set" {return tt.getFunctionTokenType(62);}
"extract" {return tt.getFunctionTokenType(63);}
"field" {return tt.getFunctionTokenType(64);}
"find_in_set" {return tt.getFunctionTokenType(65);}
"floor" {return tt.getFunctionTokenType(66);}
"fn_second_microsecond" {return tt.getFunctionTokenType(67);}
"format" {return tt.getFunctionTokenType(68);}
"found_rows" {return tt.getFunctionTokenType(69);}
"from_days" {return tt.getFunctionTokenType(70);}
"from_unixtime" {return tt.getFunctionTokenType(71);}
"get_format" {return tt.getFunctionTokenType(72);}
"get_lock" {return tt.getFunctionTokenType(73);}
"group_concat" {return tt.getFunctionTokenType(74);}
"hex" {return tt.getFunctionTokenType(75);}
"hour" {return tt.getFunctionTokenType(76);}
"hour_microsecond" {return tt.getFunctionTokenType(77);}
"hour_minute" {return tt.getFunctionTokenType(78);}
"hour_second" {return tt.getFunctionTokenType(79);}
"ifnull" {return tt.getFunctionTokenType(80);}
"inet_aton" {return tt.getFunctionTokenType(81);}
"inet_ntoa" {return tt.getFunctionTokenType(82);}
"instr" {return tt.getFunctionTokenType(83);}
"is_free_lock" {return tt.getFunctionTokenType(84);}
"is_used_lock" {return tt.getFunctionTokenType(85);}
"last_day" {return tt.getFunctionTokenType(86);}
"last_insert_id" {return tt.getFunctionTokenType(87);}
"lcase" {return tt.getFunctionTokenType(88);}
"length" {return tt.getFunctionTokenType(89);}
"ln" {return tt.getFunctionTokenType(90);}
"load_file" {return tt.getFunctionTokenType(91);}
"localtime" {return tt.getFunctionTokenType(92);}
"localtimestamp" {return tt.getFunctionTokenType(93);}
"locate" {return tt.getFunctionTokenType(94);}
"log" {return tt.getFunctionTokenType(95);}
"log10" {return tt.getFunctionTokenType(96);}
"log2" {return tt.getFunctionTokenType(97);}
"lower" {return tt.getFunctionTokenType(98);}
"lpad" {return tt.getFunctionTokenType(99);}
"ltrim" {return tt.getFunctionTokenType(100);}
"make_set" {return tt.getFunctionTokenType(101);}
"makedate" {return tt.getFunctionTokenType(102);}
"maketime" {return tt.getFunctionTokenType(103);}
"master_pos_wait" {return tt.getFunctionTokenType(104);}
"max" {return tt.getFunctionTokenType(105);}
"md5" {return tt.getFunctionTokenType(106);}
"mid" {return tt.getFunctionTokenType(107);}
"min" {return tt.getFunctionTokenType(108);}
"minute" {return tt.getFunctionTokenType(109);}
"minute_microsecond" {return tt.getFunctionTokenType(110);}
"minute_second" {return tt.getFunctionTokenType(111);}
"month" {return tt.getFunctionTokenType(112);}
"monthname" {return tt.getFunctionTokenType(113);}
"name_const" {return tt.getFunctionTokenType(114);}
"not_like" {return tt.getFunctionTokenType(115);}
"not_regexp" {return tt.getFunctionTokenType(116);}
"now" {return tt.getFunctionTokenType(117);}
"nullif" {return tt.getFunctionTokenType(118);}
"oct" {return tt.getFunctionTokenType(119);}
"octet_length" {return tt.getFunctionTokenType(120);}
"old_password" {return tt.getFunctionTokenType(121);}
"ord" {return tt.getFunctionTokenType(122);}
"password" {return tt.getFunctionTokenType(123);}
"period_add" {return tt.getFunctionTokenType(124);}
"period_diff" {return tt.getFunctionTokenType(125);}
"pi" {return tt.getFunctionTokenType(126);}
"position" {return tt.getFunctionTokenType(127);}
"pow" {return tt.getFunctionTokenType(128);}
"power" {return tt.getFunctionTokenType(129);}
"quarter" {return tt.getFunctionTokenType(130);}
"quote" {return tt.getFunctionTokenType(131);}
"radians" {return tt.getFunctionTokenType(132);}
"rand" {return tt.getFunctionTokenType(133);}
"release_lock" {return tt.getFunctionTokenType(134);}
"round" {return tt.getFunctionTokenType(135);}
"row_count" {return tt.getFunctionTokenType(136);}
"rpad" {return tt.getFunctionTokenType(137);}
"rtrim" {return tt.getFunctionTokenType(138);}
"sec_to_time" {return tt.getFunctionTokenType(139);}
"second" {return tt.getFunctionTokenType(140);}
"second_microsecond" {return tt.getFunctionTokenType(141);}
"session_user" {return tt.getFunctionTokenType(142);}
"sha" {return tt.getFunctionTokenType(143);}
"sha1" {return tt.getFunctionTokenType(144);}
"sha2" {return tt.getFunctionTokenType(145);}
"sign" {return tt.getFunctionTokenType(146);}
"sin" {return tt.getFunctionTokenType(147);}
"sleep" {return tt.getFunctionTokenType(148);}
"soundex" {return tt.getFunctionTokenType(149);}
"sounds_like" {return tt.getFunctionTokenType(150);}
"space" {return tt.getFunctionTokenType(151);}
"sqrt" {return tt.getFunctionTokenType(152);}
"std" {return tt.getFunctionTokenType(153);}
"stddev" {return tt.getFunctionTokenType(154);}
"stddev_pop" {return tt.getFunctionTokenType(155);}
"stddev_samp" {return tt.getFunctionTokenType(156);}
"str_to_date" {return tt.getFunctionTokenType(157);}
"strcmp" {return tt.getFunctionTokenType(158);}
"subdate" {return tt.getFunctionTokenType(159);}
"substr" {return tt.getFunctionTokenType(160);}
"substring" {return tt.getFunctionTokenType(161);}
"substring_index" {return tt.getFunctionTokenType(162);}
"subtime" {return tt.getFunctionTokenType(163);}
"sum" {return tt.getFunctionTokenType(164);}
"sysdate" {return tt.getFunctionTokenType(165);}
"system_user" {return tt.getFunctionTokenType(166);}
"tan" {return tt.getFunctionTokenType(167);}
"time_format" {return tt.getFunctionTokenType(168);}
"time_to_sec" {return tt.getFunctionTokenType(169);}
"timediff" {return tt.getFunctionTokenType(170);}
"timestampadd" {return tt.getFunctionTokenType(171);}
"timestampdiff" {return tt.getFunctionTokenType(172);}
"to_days" {return tt.getFunctionTokenType(173);}
"trim" {return tt.getFunctionTokenType(174);}
"ucase" {return tt.getFunctionTokenType(175);}
"uncompress" {return tt.getFunctionTokenType(176);}
"uncompressed_length" {return tt.getFunctionTokenType(177);}
"unhex" {return tt.getFunctionTokenType(178);}
"unix_timestamp" {return tt.getFunctionTokenType(179);}
"upper" {return tt.getFunctionTokenType(180);}
"user" {return tt.getFunctionTokenType(181);}
"utc_date" {return tt.getFunctionTokenType(182);}
"utc_time" {return tt.getFunctionTokenType(183);}
"utc_timestamp" {return tt.getFunctionTokenType(184);}
"uuid" {return tt.getFunctionTokenType(185);}
"uuid_short" {return tt.getFunctionTokenType(186);}
"var_pop" {return tt.getFunctionTokenType(187);}
"var_samp" {return tt.getFunctionTokenType(188);}
"variance" {return tt.getFunctionTokenType(189);}
"version" {return tt.getFunctionTokenType(190);}
"week" {return tt.getFunctionTokenType(191);}
"weekday" {return tt.getFunctionTokenType(192);}
"weekofyear" {return tt.getFunctionTokenType(193);}
"weight_string" {return tt.getFunctionTokenType(194);}
"year_month" {return tt.getFunctionTokenType(195);}
"yearweek" {return tt.getFunctionTokenType(196);}




{IDENTIFIER}           { return stt.getIdentifier(); }
{QUOTED_IDENTIFIER}    { return stt.getQuotedIdentifier(); }
.                      { return stt.getIdentifier(); }
