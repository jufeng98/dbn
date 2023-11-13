package com.dbn.language.psql.dialect.oracle;

import com.dbn.language.common.TokenTypeBundle;
import com.intellij.psi.tree.IElementType;

%%

%class OraclePLSQLParserFlexLexer
%extends DBLanguageLexerBase
%final
%unicode
%ignorecase
%function advance
%type IElementType
%eof{ return;
%eof}

%{
    public OraclePLSQLParserFlexLexer(TokenTypeBundle tt) {
      super(tt);
  }
%}

%include ../../../common/lexer/shared_elements.flext
%include ../../../common/lexer/shared_elements_oracle.flext

VARIABLE = ":"{INTEGER}
SQLP_VARIABLE = "&""&"?{IDENTIFIER}

%state WRAPPED
%%

<WRAPPED> {
    {WHITE_SPACE}   { return stt.getWhiteSpace(); }
    .*              { return stt.getLineComment(); }
    .               { return stt.getLineComment(); }
}

{BLOCK_COMMENT}  { return stt.getBlockComment(); }
{LINE_COMMENT}   { return stt.getLineComment(); }

"wrapped"        { yybegin(WRAPPED); return tt.getTokenType("KW_WRAPPED");}

{VARIABLE}       {return stt.getVariable(); }
{SQLP_VARIABLE}  {return stt.getVariable(); }


{INTEGER}     { return stt.getInteger(); }
{NUMBER}      { return stt.getNumber(); }
{STRING}      { return stt.getString(); }

"("{wso}"+"{wso}")"  {return tt.getTokenType("CT_OUTER_JOIN");}

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




"varchar2" {return tt.getDataTypeTokenType(0);}
"bfile" {return tt.getDataTypeTokenType(1);}
"binary_double" {return tt.getDataTypeTokenType(2);}
"binary_float" {return tt.getDataTypeTokenType(3);}
"binary_integer" {return tt.getDataTypeTokenType(4);}
"blob" {return tt.getDataTypeTokenType(5);}
"boolean" {return tt.getDataTypeTokenType(6);}
"byte" {return tt.getDataTypeTokenType(7);}
"char" {return tt.getDataTypeTokenType(8);}
"character" {return tt.getDataTypeTokenType(9);}
"character"{ws}"varying" {return tt.getDataTypeTokenType(10);}
"clob" {return tt.getDataTypeTokenType(11);}
"date" {return tt.getDataTypeTokenType(12);}
"decimal" {return tt.getDataTypeTokenType(13);}
"double"{ws}"precision" {return tt.getDataTypeTokenType(14);}
"float" {return tt.getDataTypeTokenType(15);}
"int" {return tt.getDataTypeTokenType(16);}
"integer" {return tt.getDataTypeTokenType(17);}
"long" {return tt.getDataTypeTokenType(18);}
"long"{ws}"raw" {return tt.getDataTypeTokenType(19);}
"long"{ws}"varchar" {return tt.getDataTypeTokenType(20);}
"national"{ws}"char" {return tt.getDataTypeTokenType(21);}
"national"{ws}"char"{ws}"varying" {return tt.getDataTypeTokenType(22);}
"national"{ws}"character" {return tt.getDataTypeTokenType(23);}
"national"{ws}"character"{ws}"varying" {return tt.getDataTypeTokenType(24);}
"nchar" {return tt.getDataTypeTokenType(25);}
"nchar"{ws}"varying" {return tt.getDataTypeTokenType(26);}
"nclob" {return tt.getDataTypeTokenType(27);}
"number" {return tt.getDataTypeTokenType(28);}
"numeric" {return tt.getDataTypeTokenType(29);}
"nvarchar2" {return tt.getDataTypeTokenType(30);}
"pls_integer" {return tt.getDataTypeTokenType(31);}
"raw" {return tt.getDataTypeTokenType(32);}
"real" {return tt.getDataTypeTokenType(33);}
"rowid" {return tt.getDataTypeTokenType(34);}
"smallint" {return tt.getDataTypeTokenType(35);}
"string" {return tt.getDataTypeTokenType(36);}
"timestamp" {return tt.getDataTypeTokenType(37);}
"urowid" {return tt.getDataTypeTokenType(38);}
"varchar" {return tt.getDataTypeTokenType(39);}
"with"{ws}"local"{ws}"time"{ws}"zone" {return tt.getDataTypeTokenType(40);}
"with"{ws}"time"{ws}"zone" {return tt.getDataTypeTokenType(41);}





"a set" {return tt.getKeywordTokenType(0);}
"absent" {return tt.getKeywordTokenType(1);}
"accessible" {return tt.getKeywordTokenType(2);}
"after" {return tt.getKeywordTokenType(3);}
"agent" {return tt.getKeywordTokenType(4);}
"aggregate" {return tt.getKeywordTokenType(5);}
"all" {return tt.getKeywordTokenType(6);}
"alter" {return tt.getKeywordTokenType(7);}
"analyze" {return tt.getKeywordTokenType(8);}
"and" {return tt.getKeywordTokenType(9);}
"any" {return tt.getKeywordTokenType(10);}
"apply" {return tt.getKeywordTokenType(11);}
"array" {return tt.getKeywordTokenType(12);}
"as" {return tt.getKeywordTokenType(13);}
"asc" {return tt.getKeywordTokenType(14);}
"associate" {return tt.getKeywordTokenType(15);}
"at" {return tt.getKeywordTokenType(16);}
"audit" {return tt.getKeywordTokenType(17);}
"authid" {return tt.getKeywordTokenType(18);}
"automatic" {return tt.getKeywordTokenType(19);}
"autonomous_transaction" {return tt.getKeywordTokenType(20);}
"before" {return tt.getKeywordTokenType(21);}
"begin" {return tt.getKeywordTokenType(22);}
"between" {return tt.getKeywordTokenType(23);}
"block" {return tt.getKeywordTokenType(24);}
"body" {return tt.getKeywordTokenType(25);}
"both" {return tt.getKeywordTokenType(26);}
"bulk" {return tt.getKeywordTokenType(27);}
"bulk_exceptions" {return tt.getKeywordTokenType(28);}
"bulk_rowcount" {return tt.getKeywordTokenType(29);}
"by" {return tt.getKeywordTokenType(30);}
"c" {return tt.getKeywordTokenType(31);}
"call" {return tt.getKeywordTokenType(32);}
"canonical" {return tt.getKeywordTokenType(33);}
"case" {return tt.getKeywordTokenType(34);}
"char_base" {return tt.getKeywordTokenType(35);}
"char_cs" {return tt.getKeywordTokenType(36);}
"charsetform" {return tt.getKeywordTokenType(37);}
"charsetid" {return tt.getKeywordTokenType(38);}
"check" {return tt.getKeywordTokenType(39);}
"chisq_df" {return tt.getKeywordTokenType(40);}
"chisq_obs" {return tt.getKeywordTokenType(41);}
"chisq_sig" {return tt.getKeywordTokenType(42);}
"clone" {return tt.getKeywordTokenType(43);}
"close" {return tt.getKeywordTokenType(44);}
"cluster" {return tt.getKeywordTokenType(45);}
"coalesce" {return tt.getKeywordTokenType(46);}
"coefficient" {return tt.getKeywordTokenType(47);}
"cohens_k" {return tt.getKeywordTokenType(48);}
"collation" {return tt.getKeywordTokenType(49);}
"collect" {return tt.getKeywordTokenType(50);}
"columns" {return tt.getKeywordTokenType(51);}
"comment" {return tt.getKeywordTokenType(52);}
"commit" {return tt.getKeywordTokenType(53);}
"committed" {return tt.getKeywordTokenType(54);}
"compatibility" {return tt.getKeywordTokenType(55);}
"compound" {return tt.getKeywordTokenType(56);}
"compress" {return tt.getKeywordTokenType(57);}
"conditional" {return tt.getKeywordTokenType(58);}
"connect" {return tt.getKeywordTokenType(59);}
"constant" {return tt.getKeywordTokenType(60);}
"constraint" {return tt.getKeywordTokenType(61);}
"constructor" {return tt.getKeywordTokenType(62);}
"cont_coefficient" {return tt.getKeywordTokenType(63);}
"container" {return tt.getKeywordTokenType(64);}
"content" {return tt.getKeywordTokenType(65);}
"context" {return tt.getKeywordTokenType(66);}
"conversion" {return tt.getKeywordTokenType(67);}
"count" {return tt.getKeywordTokenType(68);}
"cramers_v" {return tt.getKeywordTokenType(69);}
"create" {return tt.getKeywordTokenType(70);}
"cross" {return tt.getKeywordTokenType(71);}
"crossedition" {return tt.getKeywordTokenType(72);}
"cube" {return tt.getKeywordTokenType(73);}
"current" {return tt.getKeywordTokenType(74);}
"current_user" {return tt.getKeywordTokenType(75);}
"currval" {return tt.getKeywordTokenType(76);}
"cursor" {return tt.getKeywordTokenType(77);}
"database" {return tt.getKeywordTokenType(78);}
"day" {return tt.getKeywordTokenType(79);}
"db_role_change" {return tt.getKeywordTokenType(80);}
"ddl" {return tt.getKeywordTokenType(81);}
"declare" {return tt.getKeywordTokenType(82);}
"decrement" {return tt.getKeywordTokenType(83);}
"default" {return tt.getKeywordTokenType(84);}
"defaults" {return tt.getKeywordTokenType(85);}
"definer" {return tt.getKeywordTokenType(86);}
"delete" {return tt.getKeywordTokenType(87);}
"deleting" {return tt.getKeywordTokenType(88);}
"dense_rank" {return tt.getKeywordTokenType(89);}
"deprecate" {return tt.getKeywordTokenType(90);}
"desc" {return tt.getKeywordTokenType(91);}
"deterministic" {return tt.getKeywordTokenType(92);}
"df" {return tt.getKeywordTokenType(93);}
"df_between" {return tt.getKeywordTokenType(94);}
"df_den" {return tt.getKeywordTokenType(95);}
"df_num" {return tt.getKeywordTokenType(96);}
"df_within" {return tt.getKeywordTokenType(97);}
"dimension" {return tt.getKeywordTokenType(98);}
"disable" {return tt.getKeywordTokenType(99);}
"disassociate" {return tt.getKeywordTokenType(100);}
"distinct" {return tt.getKeywordTokenType(101);}
"do" {return tt.getKeywordTokenType(102);}
"document" {return tt.getKeywordTokenType(103);}
"drop" {return tt.getKeywordTokenType(104);}
"dump" {return tt.getKeywordTokenType(105);}
"duration" {return tt.getKeywordTokenType(106);}
"each" {return tt.getKeywordTokenType(107);}
"editionable" {return tt.getKeywordTokenType(108);}
"else" {return tt.getKeywordTokenType(109);}
"elsif" {return tt.getKeywordTokenType(110);}
"empty" {return tt.getKeywordTokenType(111);}
"enable" {return tt.getKeywordTokenType(112);}
"encoding" {return tt.getKeywordTokenType(113);}
"end" {return tt.getKeywordTokenType(114);}
"entityescaping" {return tt.getKeywordTokenType(115);}
"equals_path" {return tt.getKeywordTokenType(116);}
"error" {return tt.getKeywordTokenType(117);}
"error_code" {return tt.getKeywordTokenType(118);}
"error_index" {return tt.getKeywordTokenType(119);}
"errors" {return tt.getKeywordTokenType(120);}
"escape" {return tt.getKeywordTokenType(121);}
"evalname" {return tt.getKeywordTokenType(122);}
"exact_prob" {return tt.getKeywordTokenType(123);}
"except" {return tt.getKeywordTokenType(124);}
"exception" {return tt.getKeywordTokenType(125);}
"exception_init" {return tt.getKeywordTokenType(126);}
"exceptions" {return tt.getKeywordTokenType(127);}
"exclude" {return tt.getKeywordTokenType(128);}
"exclusive" {return tt.getKeywordTokenType(129);}
"execute" {return tt.getKeywordTokenType(130);}
"exists" {return tt.getKeywordTokenType(131);}
"exit" {return tt.getKeywordTokenType(132);}
"extend" {return tt.getKeywordTokenType(133);}
"extends" {return tt.getKeywordTokenType(134);}
"external" {return tt.getKeywordTokenType(135);}
"f_ratio" {return tt.getKeywordTokenType(136);}
"fetch" {return tt.getKeywordTokenType(137);}
"final" {return tt.getKeywordTokenType(138);}
"first" {return tt.getKeywordTokenType(139);}
"following" {return tt.getKeywordTokenType(140);}
"follows" {return tt.getKeywordTokenType(141);}
"for" {return tt.getKeywordTokenType(142);}
"forall" {return tt.getKeywordTokenType(143);}
"force" {return tt.getKeywordTokenType(144);}
"forward" {return tt.getKeywordTokenType(145);}
"found" {return tt.getKeywordTokenType(146);}
"from" {return tt.getKeywordTokenType(147);}
"format" {return tt.getKeywordTokenType(148);}
"full" {return tt.getKeywordTokenType(149);}
"function" {return tt.getKeywordTokenType(150);}
"goto" {return tt.getKeywordTokenType(151);}
"grant" {return tt.getKeywordTokenType(152);}
"group" {return tt.getKeywordTokenType(153);}
"hash" {return tt.getKeywordTokenType(154);}
"having" {return tt.getKeywordTokenType(155);}
"heap" {return tt.getKeywordTokenType(156);}
"hide" {return tt.getKeywordTokenType(157);}
"hour" {return tt.getKeywordTokenType(158);}
"if" {return tt.getKeywordTokenType(159);}
"ignore" {return tt.getKeywordTokenType(160);}
"immediate" {return tt.getKeywordTokenType(161);}
"in" {return tt.getKeywordTokenType(162);}
"include" {return tt.getKeywordTokenType(163);}
"increment" {return tt.getKeywordTokenType(164);}
"indent" {return tt.getKeywordTokenType(165);}
"index" {return tt.getKeywordTokenType(166);}
"indicator" {return tt.getKeywordTokenType(167);}
"indices" {return tt.getKeywordTokenType(168);}
"infinite" {return tt.getKeywordTokenType(169);}
"inline" {return tt.getKeywordTokenType(170);}
"inner" {return tt.getKeywordTokenType(171);}
"insert" {return tt.getKeywordTokenType(172);}
"inserting" {return tt.getKeywordTokenType(173);}
"instantiable" {return tt.getKeywordTokenType(174);}
"instead" {return tt.getKeywordTokenType(175);}
"interface" {return tt.getKeywordTokenType(176);}
"intersect" {return tt.getKeywordTokenType(177);}
"interval" {return tt.getKeywordTokenType(178);}
"into" {return tt.getKeywordTokenType(179);}
"is" {return tt.getKeywordTokenType(180);}
"isolation" {return tt.getKeywordTokenType(181);}
"isopen" {return tt.getKeywordTokenType(182);}
"iterate" {return tt.getKeywordTokenType(183);}
"java" {return tt.getKeywordTokenType(184);}
"join" {return tt.getKeywordTokenType(185);}
"json" {return tt.getKeywordTokenType(186);}
"keep" {return tt.getKeywordTokenType(187);}
"key" {return tt.getKeywordTokenType(188);}
"keys" {return tt.getKeywordTokenType(189);}
"language" {return tt.getKeywordTokenType(190);}
"last" {return tt.getKeywordTokenType(191);}
"leading" {return tt.getKeywordTokenType(192);}
"left" {return tt.getKeywordTokenType(193);}
"level" {return tt.getKeywordTokenType(194);}
"library" {return tt.getKeywordTokenType(195);}
"like" {return tt.getKeywordTokenType(196);}
"like2" {return tt.getKeywordTokenType(197);}
"like4" {return tt.getKeywordTokenType(198);}
"likec" {return tt.getKeywordTokenType(199);}
"limit" {return tt.getKeywordTokenType(200);}
"limited" {return tt.getKeywordTokenType(201);}
"local" {return tt.getKeywordTokenType(202);}
"lock" {return tt.getKeywordTokenType(203);}
"locked" {return tt.getKeywordTokenType(204);}
"log" {return tt.getKeywordTokenType(205);}
"logoff" {return tt.getKeywordTokenType(206);}
"logon" {return tt.getKeywordTokenType(207);}
"loop" {return tt.getKeywordTokenType(208);}
"main" {return tt.getKeywordTokenType(209);}
"map" {return tt.getKeywordTokenType(210);}
"matched" {return tt.getKeywordTokenType(211);}
"maxlen" {return tt.getKeywordTokenType(212);}
"maxvalue" {return tt.getKeywordTokenType(213);}
"mean_squares_between" {return tt.getKeywordTokenType(214);}
"mean_squares_within" {return tt.getKeywordTokenType(215);}
"measures" {return tt.getKeywordTokenType(216);}
"member" {return tt.getKeywordTokenType(217);}
"merge" {return tt.getKeywordTokenType(218);}
"metadata" {return tt.getKeywordTokenType(219);}
"minus" {return tt.getKeywordTokenType(220);}
"minute" {return tt.getKeywordTokenType(221);}
"minvalue" {return tt.getKeywordTokenType(222);}
"mismatch" {return tt.getKeywordTokenType(223);}
"mlslabel" {return tt.getKeywordTokenType(224);}
"mode" {return tt.getKeywordTokenType(225);}
"model" {return tt.getKeywordTokenType(226);}
"month" {return tt.getKeywordTokenType(227);}
"multiset" {return tt.getKeywordTokenType(228);}
"name" {return tt.getKeywordTokenType(229);}
"nan" {return tt.getKeywordTokenType(230);}
"natural" {return tt.getKeywordTokenType(231);}
"naturaln" {return tt.getKeywordTokenType(232);}
"nav" {return tt.getKeywordTokenType(233);}
"nchar_cs" {return tt.getKeywordTokenType(234);}
"nested" {return tt.getKeywordTokenType(235);}
"new" {return tt.getKeywordTokenType(236);}
"next" {return tt.getKeywordTokenType(237);}
"nextval" {return tt.getKeywordTokenType(238);}
"no" {return tt.getKeywordTokenType(239);}
"noaudit" {return tt.getKeywordTokenType(240);}
"nocopy" {return tt.getKeywordTokenType(241);}
"nocycle" {return tt.getKeywordTokenType(242);}
"none" {return tt.getKeywordTokenType(243);}
"noentityescaping" {return tt.getKeywordTokenType(244);}
"noneditionable" {return tt.getKeywordTokenType(245);}
"noschemacheck" {return tt.getKeywordTokenType(246);}
"not" {return tt.getKeywordTokenType(247);}
"notfound" {return tt.getKeywordTokenType(248);}
"nowait" {return tt.getKeywordTokenType(249);}
"null" {return tt.getKeywordTokenType(250);}
"nulls" {return tt.getKeywordTokenType(251);}
"number_base" {return tt.getKeywordTokenType(252);}
"object" {return tt.getKeywordTokenType(253);}
"ocirowid" {return tt.getKeywordTokenType(254);}
"of" {return tt.getKeywordTokenType(255);}
"offset" {return tt.getKeywordTokenType(256);}
"oid" {return tt.getKeywordTokenType(257);}
"old" {return tt.getKeywordTokenType(258);}
"on" {return tt.getKeywordTokenType(259);}
"one_sided_prob_or_less" {return tt.getKeywordTokenType(260);}
"one_sided_prob_or_more" {return tt.getKeywordTokenType(261);}
"one_sided_sig" {return tt.getKeywordTokenType(262);}
"only" {return tt.getKeywordTokenType(263);}
"opaque" {return tt.getKeywordTokenType(264);}
"open" {return tt.getKeywordTokenType(265);}
"operator" {return tt.getKeywordTokenType(266);}
"option" {return tt.getKeywordTokenType(267);}
"or" {return tt.getKeywordTokenType(268);}
"order" {return tt.getKeywordTokenType(269);}
"ordinality" {return tt.getKeywordTokenType(270);}
"organization" {return tt.getKeywordTokenType(271);}
"others" {return tt.getKeywordTokenType(272);}
"out" {return tt.getKeywordTokenType(273);}
"outer" {return tt.getKeywordTokenType(274);}
"over" {return tt.getKeywordTokenType(275);}
"overflow" {return tt.getKeywordTokenType(276);}
"overlaps" {return tt.getKeywordTokenType(277);}
"overriding" {return tt.getKeywordTokenType(278);}
"package" {return tt.getKeywordTokenType(279);}
"parallel_enable" {return tt.getKeywordTokenType(280);}
"parameters" {return tt.getKeywordTokenType(281);}
"parent" {return tt.getKeywordTokenType(282);}
"partition" {return tt.getKeywordTokenType(283);}
"passing" {return tt.getKeywordTokenType(284);}
"path" {return tt.getKeywordTokenType(285);}
"pctfree" {return tt.getKeywordTokenType(286);}
"percent" {return tt.getKeywordTokenType(287);}
"phi_coefficient" {return tt.getKeywordTokenType(288);}
"pipe" {return tt.getKeywordTokenType(289);}
"pipelined" {return tt.getKeywordTokenType(290);}
"pivot" {return tt.getKeywordTokenType(291);}
"pluggable" {return tt.getKeywordTokenType(292);}
"positive" {return tt.getKeywordTokenType(293);}
"positiven" {return tt.getKeywordTokenType(294);}
"power" {return tt.getKeywordTokenType(295);}
"pragma" {return tt.getKeywordTokenType(296);}
"preceding" {return tt.getKeywordTokenType(297);}
"precedes" {return tt.getKeywordTokenType(298);}
"present" {return tt.getKeywordTokenType(299);}
"pretty" {return tt.getKeywordTokenType(300);}
"prior" {return tt.getKeywordTokenType(301);}
"private" {return tt.getKeywordTokenType(302);}
"procedure" {return tt.getKeywordTokenType(303);}
"public" {return tt.getKeywordTokenType(304);}
"raise" {return tt.getKeywordTokenType(305);}
"range" {return tt.getKeywordTokenType(306);}
"read" {return tt.getKeywordTokenType(307);}
"record" {return tt.getKeywordTokenType(308);}
"ref" {return tt.getKeywordTokenType(309);}
"reference" {return tt.getKeywordTokenType(310);}
"referencing" {return tt.getKeywordTokenType(311);}
"regexp_like" {return tt.getKeywordTokenType(312);}
"reject" {return tt.getKeywordTokenType(313);}
"release" {return tt.getKeywordTokenType(314);}
"relies_on" {return tt.getKeywordTokenType(315);}
"remainder" {return tt.getKeywordTokenType(316);}
"rename" {return tt.getKeywordTokenType(317);}
"replace" {return tt.getKeywordTokenType(318);}
"restrict_references" {return tt.getKeywordTokenType(319);}
"result" {return tt.getKeywordTokenType(320);}
"result_cache" {return tt.getKeywordTokenType(321);}
"return" {return tt.getKeywordTokenType(322);}
"returning" {return tt.getKeywordTokenType(323);}
"reverse" {return tt.getKeywordTokenType(324);}
"revoke" {return tt.getKeywordTokenType(325);}
"right" {return tt.getKeywordTokenType(326);}
"rnds" {return tt.getKeywordTokenType(327);}
"rnps" {return tt.getKeywordTokenType(328);}
"rollback" {return tt.getKeywordTokenType(329);}
"rollup" {return tt.getKeywordTokenType(330);}
"row" {return tt.getKeywordTokenType(331);}
"rowcount" {return tt.getKeywordTokenType(332);}
"rownum" {return tt.getKeywordTokenType(333);}
"rows" {return tt.getKeywordTokenType(334);}
"rowtype" {return tt.getKeywordTokenType(335);}
"rules" {return tt.getKeywordTokenType(336);}
"sample" {return tt.getKeywordTokenType(337);}
"save" {return tt.getKeywordTokenType(338);}
"savepoint" {return tt.getKeywordTokenType(339);}
"schema" {return tt.getKeywordTokenType(340);}
"schemacheck" {return tt.getKeywordTokenType(341);}
"scn" {return tt.getKeywordTokenType(342);}
"second" {return tt.getKeywordTokenType(343);}
"seed" {return tt.getKeywordTokenType(344);}
"segment" {return tt.getKeywordTokenType(345);}
"select" {return tt.getKeywordTokenType(346);}
"self" {return tt.getKeywordTokenType(347);}
"separate" {return tt.getKeywordTokenType(348);}
"sequential" {return tt.getKeywordTokenType(349);}
"serializable" {return tt.getKeywordTokenType(350);}
"serially_reusable" {return tt.getKeywordTokenType(351);}
"servererror" {return tt.getKeywordTokenType(352);}
"set" {return tt.getKeywordTokenType(353);}
"sets" {return tt.getKeywordTokenType(354);}
"share" {return tt.getKeywordTokenType(355);}
"sharing" {return tt.getKeywordTokenType(356);}
"show" {return tt.getKeywordTokenType(357);}
"shutdown" {return tt.getKeywordTokenType(358);}
"siblings" {return tt.getKeywordTokenType(359);}
"sig" {return tt.getKeywordTokenType(360);}
"single" {return tt.getKeywordTokenType(361);}
"size" {return tt.getKeywordTokenType(362);}
"skip" {return tt.getKeywordTokenType(363);}
"some" {return tt.getKeywordTokenType(364);}
"space" {return tt.getKeywordTokenType(365);}
"sql" {return tt.getKeywordTokenType(366);}
"sqlcode" {return tt.getKeywordTokenType(367);}
"sqlerrm" {return tt.getKeywordTokenType(368);}
"standalone" {return tt.getKeywordTokenType(369);}
"start" {return tt.getKeywordTokenType(370);}
"startup" {return tt.getKeywordTokenType(371);}
"statement" {return tt.getKeywordTokenType(372);}
"static" {return tt.getKeywordTokenType(373);}
"statistic" {return tt.getKeywordTokenType(374);}
"statistics" {return tt.getKeywordTokenType(375);}
"strict" {return tt.getKeywordTokenType(376);}
"struct" {return tt.getKeywordTokenType(377);}
"submultiset" {return tt.getKeywordTokenType(378);}
"subpartition" {return tt.getKeywordTokenType(379);}
"subtype" {return tt.getKeywordTokenType(380);}
"successful" {return tt.getKeywordTokenType(381);}
"sum_squares_between" {return tt.getKeywordTokenType(382);}
"sum_squares_within" {return tt.getKeywordTokenType(383);}
"suspend" {return tt.getKeywordTokenType(384);}
"synonym" {return tt.getKeywordTokenType(385);}
"table" {return tt.getKeywordTokenType(386);}
"tdo" {return tt.getKeywordTokenType(387);}
"then" {return tt.getKeywordTokenType(388);}
"ties" {return tt.getKeywordTokenType(389);}
"time" {return tt.getKeywordTokenType(390);}
"timezone_abbr" {return tt.getKeywordTokenType(391);}
"timezone_hour" {return tt.getKeywordTokenType(392);}
"timezone_minute" {return tt.getKeywordTokenType(393);}
"timezone_region" {return tt.getKeywordTokenType(394);}
"to" {return tt.getKeywordTokenType(395);}
"trailing" {return tt.getKeywordTokenType(396);}
"transaction" {return tt.getKeywordTokenType(397);}
"trigger" {return tt.getKeywordTokenType(398);}
"truncate" {return tt.getKeywordTokenType(399);}
"trust" {return tt.getKeywordTokenType(400);}
"two_sided_prob" {return tt.getKeywordTokenType(401);}
"two_sided_sig" {return tt.getKeywordTokenType(402);}
"type" {return tt.getKeywordTokenType(403);}
"u_statistic" {return tt.getKeywordTokenType(404);}
"unbounded" {return tt.getKeywordTokenType(405);}
"unconditional" {return tt.getKeywordTokenType(406);}
"under" {return tt.getKeywordTokenType(407);}
"under_path" {return tt.getKeywordTokenType(408);}
"union" {return tt.getKeywordTokenType(409);}
"unique" {return tt.getKeywordTokenType(410);}
"unlimited" {return tt.getKeywordTokenType(411);}
"unpivot" {return tt.getKeywordTokenType(412);}
"unplug" {return tt.getKeywordTokenType(413);}
"until" {return tt.getKeywordTokenType(414);}
"update" {return tt.getKeywordTokenType(415);}
"updated" {return tt.getKeywordTokenType(416);}
"updating" {return tt.getKeywordTokenType(417);}
"upsert" {return tt.getKeywordTokenType(418);}
"use" {return tt.getKeywordTokenType(419);}
"user" {return tt.getKeywordTokenType(420);}
"using" {return tt.getKeywordTokenType(421);}
"validate" {return tt.getKeywordTokenType(422);}
"value" {return tt.getKeywordTokenType(423);}
"values" {return tt.getKeywordTokenType(424);}
"variable" {return tt.getKeywordTokenType(425);}
"varray" {return tt.getKeywordTokenType(426);}
"varying" {return tt.getKeywordTokenType(427);}
"version" {return tt.getKeywordTokenType(428);}
"versions" {return tt.getKeywordTokenType(429);}
"view" {return tt.getKeywordTokenType(430);}
"wait" {return tt.getKeywordTokenType(431);}
"wellformed" {return tt.getKeywordTokenType(432);}
"when" {return tt.getKeywordTokenType(433);}
"whenever" {return tt.getKeywordTokenType(434);}
"where" {return tt.getKeywordTokenType(435);}
"while" {return tt.getKeywordTokenType(436);}
"with" {return tt.getKeywordTokenType(437);}
"within" {return tt.getKeywordTokenType(438);}
"without" {return tt.getKeywordTokenType(439);}
"wnds" {return tt.getKeywordTokenType(440);}
"wnps" {return tt.getKeywordTokenType(441);}
"work" {return tt.getKeywordTokenType(442);}
"write" {return tt.getKeywordTokenType(443);}
"wrapped" {return tt.getKeywordTokenType(444);}
"wrapper" {return tt.getKeywordTokenType(445);}
"xml" {return tt.getKeywordTokenType(446);}
"xmlnamespaces" {return tt.getKeywordTokenType(447);}
"year" {return tt.getKeywordTokenType(448);}
"yes" {return tt.getKeywordTokenType(449);}
"zone" {return tt.getKeywordTokenType(450);}
"false" {return tt.getKeywordTokenType(451);}
"true" {return tt.getKeywordTokenType(452);}








"abs" {return tt.getFunctionTokenType(0);}
"acos" {return tt.getFunctionTokenType(1);}
"add_months" {return tt.getFunctionTokenType(2);}
"appendchildxml" {return tt.getFunctionTokenType(3);}
"ascii" {return tt.getFunctionTokenType(4);}
"asciistr" {return tt.getFunctionTokenType(5);}
"asin" {return tt.getFunctionTokenType(6);}
"atan" {return tt.getFunctionTokenType(7);}
"atan2" {return tt.getFunctionTokenType(8);}
"avg" {return tt.getFunctionTokenType(9);}
"bfilename" {return tt.getFunctionTokenType(10);}
"bin_to_num" {return tt.getFunctionTokenType(11);}
"bitand" {return tt.getFunctionTokenType(12);}
"cardinality" {return tt.getFunctionTokenType(13);}
"cast" {return tt.getFunctionTokenType(14);}
"ceil" {return tt.getFunctionTokenType(15);}
"chartorowid" {return tt.getFunctionTokenType(16);}
"chr" {return tt.getFunctionTokenType(17);}
"compose" {return tt.getFunctionTokenType(18);}
"concat" {return tt.getFunctionTokenType(19);}
"convert" {return tt.getFunctionTokenType(20);}
"corr" {return tt.getFunctionTokenType(21);}
"corr_k" {return tt.getFunctionTokenType(22);}
"corr_s" {return tt.getFunctionTokenType(23);}
"cos" {return tt.getFunctionTokenType(24);}
"cosh" {return tt.getFunctionTokenType(25);}
"covar_pop" {return tt.getFunctionTokenType(26);}
"covar_samp" {return tt.getFunctionTokenType(27);}
"cume_dist" {return tt.getFunctionTokenType(28);}
"current_date" {return tt.getFunctionTokenType(29);}
"current_timestamp" {return tt.getFunctionTokenType(30);}
"cv" {return tt.getFunctionTokenType(31);}
"dbtimezone" {return tt.getFunctionTokenType(32);}
"dbtmezone" {return tt.getFunctionTokenType(33);}
"decode" {return tt.getFunctionTokenType(34);}
"decompose" {return tt.getFunctionTokenType(35);}
"deletexml" {return tt.getFunctionTokenType(36);}
"depth" {return tt.getFunctionTokenType(37);}
"deref" {return tt.getFunctionTokenType(38);}
"empty_blob" {return tt.getFunctionTokenType(39);}
"empty_clob" {return tt.getFunctionTokenType(40);}
"existsnode" {return tt.getFunctionTokenType(41);}
"exp" {return tt.getFunctionTokenType(42);}
"extract" {return tt.getFunctionTokenType(43);}
"extractvalue" {return tt.getFunctionTokenType(44);}
"first_value" {return tt.getFunctionTokenType(45);}
"floor" {return tt.getFunctionTokenType(46);}
"from_tz" {return tt.getFunctionTokenType(47);}
"greatest" {return tt.getFunctionTokenType(48);}
"group_id" {return tt.getFunctionTokenType(49);}
"grouping" {return tt.getFunctionTokenType(50);}
"grouping_id" {return tt.getFunctionTokenType(51);}
"hextoraw" {return tt.getFunctionTokenType(52);}
"initcap" {return tt.getFunctionTokenType(53);}
"insertchildxml" {return tt.getFunctionTokenType(54);}
"insertchildxmlafter" {return tt.getFunctionTokenType(55);}
"insertchildxmlbefore" {return tt.getFunctionTokenType(56);}
"insertxmlafter" {return tt.getFunctionTokenType(57);}
"insertxmlbefore" {return tt.getFunctionTokenType(58);}
"instr" {return tt.getFunctionTokenType(59);}
"instr2" {return tt.getFunctionTokenType(60);}
"instr4" {return tt.getFunctionTokenType(61);}
"instrb" {return tt.getFunctionTokenType(62);}
"instrc" {return tt.getFunctionTokenType(63);}
"iteration_number" {return tt.getFunctionTokenType(64);}
"json_array" {return tt.getFunctionTokenType(65);}
"json_arrayagg" {return tt.getFunctionTokenType(66);}
"json_dataguide" {return tt.getFunctionTokenType(67);}
"json_object" {return tt.getFunctionTokenType(68);}
"json_objectagg" {return tt.getFunctionTokenType(69);}
"json_query" {return tt.getFunctionTokenType(70);}
"json_table" {return tt.getFunctionTokenType(71);}
"json_value" {return tt.getFunctionTokenType(72);}
"lag" {return tt.getFunctionTokenType(73);}
"last_day" {return tt.getFunctionTokenType(74);}
"last_value" {return tt.getFunctionTokenType(75);}
"lateral" {return tt.getFunctionTokenType(76);}
"lead" {return tt.getFunctionTokenType(77);}
"least" {return tt.getFunctionTokenType(78);}
"length" {return tt.getFunctionTokenType(79);}
"length2" {return tt.getFunctionTokenType(80);}
"length4" {return tt.getFunctionTokenType(81);}
"lengthb" {return tt.getFunctionTokenType(82);}
"lengthc" {return tt.getFunctionTokenType(83);}
"listagg" {return tt.getFunctionTokenType(84);}
"ln" {return tt.getFunctionTokenType(85);}
"lnnvl" {return tt.getFunctionTokenType(86);}
"localtimestamp" {return tt.getFunctionTokenType(87);}
"lower" {return tt.getFunctionTokenType(88);}
"lpad" {return tt.getFunctionTokenType(89);}
"ltrim" {return tt.getFunctionTokenType(90);}
"make_ref" {return tt.getFunctionTokenType(91);}
"max" {return tt.getFunctionTokenType(92);}
"median" {return tt.getFunctionTokenType(93);}
"min" {return tt.getFunctionTokenType(94);}
"mod" {return tt.getFunctionTokenType(95);}
"months_between" {return tt.getFunctionTokenType(96);}
"nanvl" {return tt.getFunctionTokenType(97);}
"nchr" {return tt.getFunctionTokenType(98);}
"new_time" {return tt.getFunctionTokenType(99);}
"next_day" {return tt.getFunctionTokenType(100);}
"nls_charset_decl_len" {return tt.getFunctionTokenType(101);}
"nls_charset_id" {return tt.getFunctionTokenType(102);}
"nls_charset_name" {return tt.getFunctionTokenType(103);}
"nls_initcap" {return tt.getFunctionTokenType(104);}
"nls_lower" {return tt.getFunctionTokenType(105);}
"nls_upper" {return tt.getFunctionTokenType(106);}
"nlssort" {return tt.getFunctionTokenType(107);}
"ntile" {return tt.getFunctionTokenType(108);}
"nullif" {return tt.getFunctionTokenType(109);}
"numtodsinterval" {return tt.getFunctionTokenType(110);}
"numtoyminterval" {return tt.getFunctionTokenType(111);}
"nvl" {return tt.getFunctionTokenType(112);}
"nvl2" {return tt.getFunctionTokenType(113);}
"ora_hash" {return tt.getFunctionTokenType(114);}
"percent_rank" {return tt.getFunctionTokenType(115);}
"percentile_cont" {return tt.getFunctionTokenType(116);}
"percentile_disc" {return tt.getFunctionTokenType(117);}
"powermultiset" {return tt.getFunctionTokenType(118);}
"powermultiset_by_cardinality" {return tt.getFunctionTokenType(119);}
"presentnnv" {return tt.getFunctionTokenType(120);}
"presentv" {return tt.getFunctionTokenType(121);}
"previous" {return tt.getFunctionTokenType(122);}
"rank" {return tt.getFunctionTokenType(123);}
"ratio_to_report" {return tt.getFunctionTokenType(124);}
"rawtohex" {return tt.getFunctionTokenType(125);}
"rawtonhex" {return tt.getFunctionTokenType(126);}
"reftohex" {return tt.getFunctionTokenType(127);}
"regexp_instr" {return tt.getFunctionTokenType(128);}
"regexp_replace" {return tt.getFunctionTokenType(129);}
"regexp_substr" {return tt.getFunctionTokenType(130);}
"regr_avgx" {return tt.getFunctionTokenType(131);}
"regr_avgy" {return tt.getFunctionTokenType(132);}
"regr_count" {return tt.getFunctionTokenType(133);}
"regr_intercept" {return tt.getFunctionTokenType(134);}
"regr_r2" {return tt.getFunctionTokenType(135);}
"regr_slope" {return tt.getFunctionTokenType(136);}
"regr_sxx" {return tt.getFunctionTokenType(137);}
"regr_sxy" {return tt.getFunctionTokenType(138);}
"regr_syy" {return tt.getFunctionTokenType(139);}
"round" {return tt.getFunctionTokenType(140);}
"row_number" {return tt.getFunctionTokenType(141);}
"rowidtochar" {return tt.getFunctionTokenType(142);}
"rowidtonchar" {return tt.getFunctionTokenType(143);}
"rpad" {return tt.getFunctionTokenType(144);}
"rtrim" {return tt.getFunctionTokenType(145);}
"scn_to_timestamp" {return tt.getFunctionTokenType(146);}
"sessiontimezone" {return tt.getFunctionTokenType(147);}
"sign" {return tt.getFunctionTokenType(148);}
"sin" {return tt.getFunctionTokenType(149);}
"sinh" {return tt.getFunctionTokenType(150);}
"soundex" {return tt.getFunctionTokenType(151);}
"sqrt" {return tt.getFunctionTokenType(152);}
"stats_binomial_test" {return tt.getFunctionTokenType(153);}
"stats_crosstab" {return tt.getFunctionTokenType(154);}
"stats_f_test" {return tt.getFunctionTokenType(155);}
"stats_ks_test" {return tt.getFunctionTokenType(156);}
"stats_mode" {return tt.getFunctionTokenType(157);}
"stats_mw_test" {return tt.getFunctionTokenType(158);}
"stats_one_way_anova" {return tt.getFunctionTokenType(159);}
"stats_t_test_indep" {return tt.getFunctionTokenType(160);}
"stats_t_test_indepu" {return tt.getFunctionTokenType(161);}
"stats_t_test_one" {return tt.getFunctionTokenType(162);}
"stats_t_test_paired" {return tt.getFunctionTokenType(163);}
"stats_wsr_test" {return tt.getFunctionTokenType(164);}
"stddev" {return tt.getFunctionTokenType(165);}
"stddev_pop" {return tt.getFunctionTokenType(166);}
"stddev_samp" {return tt.getFunctionTokenType(167);}
"substr" {return tt.getFunctionTokenType(168);}
"substr2" {return tt.getFunctionTokenType(169);}
"substr4" {return tt.getFunctionTokenType(170);}
"substrb" {return tt.getFunctionTokenType(171);}
"substrc" {return tt.getFunctionTokenType(172);}
"sum" {return tt.getFunctionTokenType(173);}
"sys_connect_by_path" {return tt.getFunctionTokenType(174);}
"sys_context" {return tt.getFunctionTokenType(175);}
"sys_dburigen" {return tt.getFunctionTokenType(176);}
"sys_extract_utc" {return tt.getFunctionTokenType(177);}
"sys_guid" {return tt.getFunctionTokenType(178);}
"sys_typeid" {return tt.getFunctionTokenType(179);}
"sys_xmlagg" {return tt.getFunctionTokenType(180);}
"sys_xmlgen" {return tt.getFunctionTokenType(181);}
"sysdate" {return tt.getFunctionTokenType(182);}
"systimestamp" {return tt.getFunctionTokenType(183);}
"tan" {return tt.getFunctionTokenType(184);}
"tanh" {return tt.getFunctionTokenType(185);}
"timestamp_to_scn" {return tt.getFunctionTokenType(186);}
"to_binary_double" {return tt.getFunctionTokenType(187);}
"to_binary_float" {return tt.getFunctionTokenType(188);}
"to_char" {return tt.getFunctionTokenType(189);}
"to_clob" {return tt.getFunctionTokenType(190);}
"to_date" {return tt.getFunctionTokenType(191);}
"to_dsinterval" {return tt.getFunctionTokenType(192);}
"to_lob" {return tt.getFunctionTokenType(193);}
"to_multi_byte" {return tt.getFunctionTokenType(194);}
"to_nchar" {return tt.getFunctionTokenType(195);}
"to_nclob" {return tt.getFunctionTokenType(196);}
"to_number" {return tt.getFunctionTokenType(197);}
"to_single_byte" {return tt.getFunctionTokenType(198);}
"to_timestamp" {return tt.getFunctionTokenType(199);}
"to_timestamp_tz" {return tt.getFunctionTokenType(200);}
"to_yminterval" {return tt.getFunctionTokenType(201);}
"translate" {return tt.getFunctionTokenType(202);}
"treat" {return tt.getFunctionTokenType(203);}
"trim" {return tt.getFunctionTokenType(204);}
"trunc" {return tt.getFunctionTokenType(205);}
"tz_offset" {return tt.getFunctionTokenType(206);}
"uid" {return tt.getFunctionTokenType(207);}
"unistr" {return tt.getFunctionTokenType(208);}
"updatexml" {return tt.getFunctionTokenType(209);}
"upper" {return tt.getFunctionTokenType(210);}
"userenv" {return tt.getFunctionTokenType(211);}
"validate_conversion" {return tt.getFunctionTokenType(212);}
"var_pop" {return tt.getFunctionTokenType(213);}
"var_samp" {return tt.getFunctionTokenType(214);}
"variance" {return tt.getFunctionTokenType(215);}
"vsize" {return tt.getFunctionTokenType(216);}
"width_bucket" {return tt.getFunctionTokenType(217);}
"xmlagg" {return tt.getFunctionTokenType(218);}
"xmlattributes" {return tt.getFunctionTokenType(219);}
"xmlcast" {return tt.getFunctionTokenType(220);}
"xmlcdata" {return tt.getFunctionTokenType(221);}
"xmlcolattval" {return tt.getFunctionTokenType(222);}
"xmlcomment" {return tt.getFunctionTokenType(223);}
"xmlconcat" {return tt.getFunctionTokenType(224);}
"xmldiff" {return tt.getFunctionTokenType(225);}
"xmlelement" {return tt.getFunctionTokenType(226);}
"xmlforest" {return tt.getFunctionTokenType(227);}
"xmlisvalid" {return tt.getFunctionTokenType(228);}
"xmlparse" {return tt.getFunctionTokenType(229);}
"xmlpatch" {return tt.getFunctionTokenType(230);}
"xmlpi" {return tt.getFunctionTokenType(231);}
"xmlquery" {return tt.getFunctionTokenType(232);}
"xmlroot" {return tt.getFunctionTokenType(233);}
"xmlsequence" {return tt.getFunctionTokenType(234);}
"xmlserialize" {return tt.getFunctionTokenType(235);}
"xmltable" {return tt.getFunctionTokenType(236);}
"xmltransform" {return tt.getFunctionTokenType(237);}










"access_into_null" {return tt.getExceptionTokenType(0);}
"case_not_found" {return tt.getExceptionTokenType(1);}
"collection_is_null" {return tt.getExceptionTokenType(2);}
"cursor_already_open" {return tt.getExceptionTokenType(3);}
"dup_val_on_index" {return tt.getExceptionTokenType(4);}
"invalid_cursor" {return tt.getExceptionTokenType(5);}
"invalid_number" {return tt.getExceptionTokenType(6);}
"login_denied" {return tt.getExceptionTokenType(7);}
"no_data_found" {return tt.getExceptionTokenType(8);}
"not_logged_on" {return tt.getExceptionTokenType(9);}
"program_error" {return tt.getExceptionTokenType(10);}
"rowtype_mismatch" {return tt.getExceptionTokenType(11);}
"self_is_null" {return tt.getExceptionTokenType(12);}
"storage_error" {return tt.getExceptionTokenType(13);}
"subscript_beyond_count" {return tt.getExceptionTokenType(14);}
"subscript_outside_limit" {return tt.getExceptionTokenType(15);}
"sys_invalid_rowid" {return tt.getExceptionTokenType(16);}
"timeout_on_resource" {return tt.getExceptionTokenType(17);}
"too_many_rows" {return tt.getExceptionTokenType(18);}
"value_error" {return tt.getExceptionTokenType(19);}
"zero_divide" {return tt.getExceptionTokenType(20);}



{IDENTIFIER}           { return stt.getIdentifier(); }
{QUOTED_IDENTIFIER}    { return stt.getQuotedIdentifier(); }
{WHITE_SPACE}          { return stt.getWhiteSpace(); }
.                      { return stt.getIdentifier(); }


