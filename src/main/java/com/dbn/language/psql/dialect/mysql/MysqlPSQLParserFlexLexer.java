/* The following code was generated by JFlex 1.7.0 tweaked for IntelliJ platform */

package com.dbn.language.psql.dialect.mysql;

import com.dbn.language.common.SharedTokenTypeBundle;
import com.dbn.language.common.TokenTypeBundle;
import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.7.0
 * from the specification file <tt>mysql_psql_parser.flex</tt>
 */
public final class MysqlPSQLParserFlexLexer implements FlexLexer {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;
  public static final int DIV = 2;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0,  0,  0, 0
  };

  /** 
   * Translates characters to character classes
   * Chosen bits are [12, 6, 3]
   * Total runtime size is 18432 bytes
   */
  public static int ZZ_CMAP(int ch) {
    return ZZ_CMAP_A[(ZZ_CMAP_Y[(ZZ_CMAP_Z[ch>>9]<<6)|((ch>>3)&0x3f)]<<3)|(ch&0x7)];
  }

  /* The ZZ_CMAP_Z table has 2176 entries */
  static final char ZZ_CMAP_Z[] = zzUnpackCMap(
    "\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1\15\1\16\1\17\1"+
    "\20\5\21\1\22\1\23\1\24\1\21\14\25\1\26\50\25\1\27\2\25\1\30\1\31\1\32\1\33"+
    "\25\25\1\34\20\21\1\35\1\36\1\37\1\40\1\41\1\42\1\43\1\44\1\45\1\46\1\47\1"+
    "\50\1\51\1\52\1\53\1\54\1\55\1\56\1\57\1\60\1\25\1\61\1\62\5\21\2\25\1\63"+
    "\7\21\1\25\1\64\20\21\1\25\1\65\1\21\1\66\13\25\1\67\2\25\1\70\21\21\1\71"+
    "\1\72\4\21\1\73\11\21\1\74\1\75\1\76\1\77\1\21\1\100\2\21\1\101\1\102\2\21"+
    "\1\103\1\21\1\104\1\105\5\21\1\106\2\21\123\25\1\107\7\25\1\110\1\111\12\25"+
    "\1\112\15\25\1\113\6\21\1\25\1\114\2\21\11\25\1\115\u0576\21\1\116\u017f\21");

  /* The ZZ_CMAP_Y table has 5056 entries */
  static final char ZZ_CMAP_Y[] = zzUnpackCMap(
    "\1\0\1\1\1\0\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\10\1\11\1\14\1"+
    "\15\3\0\1\16\1\17\1\20\1\21\2\11\1\22\3\11\1\22\71\11\1\23\1\11\1\24\1\25"+
    "\1\26\1\27\2\25\16\0\1\30\1\31\1\32\1\33\2\11\1\34\11\11\1\35\21\11\1\36\1"+
    "\37\24\11\1\40\3\11\1\22\1\41\5\11\1\42\1\43\4\0\1\44\1\45\1\25\3\11\1\46"+
    "\1\47\1\25\1\50\1\51\1\0\1\52\5\11\1\53\3\0\1\54\1\55\13\11\1\56\1\44\1\57"+
    "\1\60\1\0\1\61\1\25\1\62\1\63\3\11\3\0\1\64\12\11\1\65\1\0\1\66\1\25\1\0\1"+
    "\67\3\11\1\53\1\70\1\71\2\11\1\65\1\72\1\73\1\74\2\25\3\11\1\75\1\11\1\47"+
    "\6\25\2\11\1\30\2\11\1\25\1\76\5\0\1\77\6\11\1\100\2\0\1\101\1\11\1\102\1"+
    "\0\1\40\1\11\1\103\1\104\1\105\2\11\1\106\1\107\1\110\1\111\1\112\1\62\1\113"+
    "\1\102\1\0\1\114\1\115\1\116\1\46\1\105\2\11\1\106\1\117\1\120\1\121\1\122"+
    "\1\123\1\124\1\125\1\0\1\126\1\25\1\116\1\35\1\34\2\11\1\106\1\127\1\110\1"+
    "\44\1\130\1\131\1\25\1\102\1\0\1\41\1\132\1\116\1\104\1\105\2\11\1\106\1\127"+
    "\1\110\1\111\1\122\1\133\1\113\1\102\1\0\1\41\1\25\1\134\1\135\1\136\1\137"+
    "\1\140\1\135\1\11\1\141\1\142\1\143\1\144\1\25\1\125\1\0\1\25\1\41\1\145\1"+
    "\30\1\106\2\11\1\106\1\11\1\146\1\147\1\143\1\150\1\47\1\102\1\0\2\25\1\103"+
    "\1\30\1\106\2\11\1\106\1\151\1\110\1\147\1\143\1\150\1\32\1\102\1\0\1\152"+
    "\1\25\1\77\1\30\1\106\4\11\1\153\1\147\1\154\1\155\1\156\1\102\1\0\1\25\1"+
    "\37\1\116\1\11\1\22\1\37\2\11\1\34\1\157\1\22\1\160\1\161\1\0\1\125\1\0\1"+
    "\162\1\25\1\40\5\11\1\163\1\164\1\165\1\166\1\0\1\167\4\25\1\137\1\170\2\11"+
    "\1\171\1\11\1\163\1\172\1\173\1\50\1\0\1\174\4\25\1\131\2\25\1\167\1\0\1\167"+
    "\1\175\1\176\1\11\1\40\3\11\1\26\1\43\1\0\1\147\1\177\1\0\1\43\3\0\1\200\1"+
    "\201\7\25\5\11\1\53\1\0\1\202\1\0\1\167\1\65\1\203\1\204\1\205\1\206\1\11"+
    "\1\207\1\210\1\0\1\50\4\11\1\35\1\20\5\11\1\170\51\11\1\136\1\22\1\136\5\11"+
    "\1\136\4\11\1\136\1\22\1\136\1\11\1\22\7\11\1\136\10\11\1\211\4\25\2\11\2"+
    "\25\12\11\2\212\1\40\114\11\1\104\2\11\1\40\2\11\1\47\11\11\1\135\1\11\1\131"+
    "\1\11\1\30\1\213\1\25\2\11\1\213\1\25\2\11\1\214\1\25\1\11\1\30\1\215\1\25"+
    "\6\11\1\216\3\0\1\217\1\220\1\0\1\167\3\25\1\221\1\0\1\167\13\11\1\131\1\222"+
    "\4\11\1\223\10\11\1\212\1\25\3\11\1\22\1\0\1\2\1\0\1\2\1\125\1\0\3\11\1\212"+
    "\1\26\1\25\5\11\1\114\3\11\1\24\1\0\1\167\4\25\2\11\1\165\1\2\6\11\1\177\1"+
    "\166\3\0\1\111\1\0\1\167\1\0\1\167\1\156\1\25\1\0\1\44\1\224\7\25\1\145\5"+
    "\11\1\216\1\0\1\145\1\114\1\0\1\167\1\25\1\76\1\2\1\25\1\225\3\11\1\101\1"+
    "\205\1\0\1\67\4\11\1\65\1\0\1\2\1\25\4\11\1\216\2\0\1\25\1\0\1\226\1\0\1\67"+
    "\3\11\1\212\1\11\1\131\5\11\1\227\2\25\1\230\2\0\1\231\1\232\1\233\30\11\7"+
    "\0\1\234\42\11\2\212\4\11\2\212\1\11\1\235\3\11\1\212\6\11\1\30\1\173\1\236"+
    "\1\26\1\237\1\114\1\11\1\26\1\236\1\26\1\25\1\76\3\25\1\240\1\25\1\156\1\131"+
    "\1\25\1\241\1\25\1\147\1\0\1\41\1\156\2\25\1\11\1\26\4\11\2\25\1\0\1\200\1"+
    "\242\1\0\1\224\1\25\1\243\1\37\1\157\1\244\1\27\1\245\1\11\1\246\1\247\1\250"+
    "\2\25\5\11\1\131\116\25\5\11\1\22\5\11\1\22\20\11\1\26\1\251\1\252\1\25\4"+
    "\11\1\35\1\20\7\11\1\156\1\25\1\62\2\11\1\22\1\25\10\22\4\0\5\25\1\156\72"+
    "\25\1\247\3\25\1\40\1\207\1\244\1\26\1\40\11\11\1\22\1\253\1\40\12\11\1\170"+
    "\1\247\5\11\1\40\12\11\1\22\2\25\4\11\6\25\172\11\10\25\77\11\1\26\21\11\1"+
    "\26\10\25\5\11\1\212\41\11\1\26\2\11\1\0\1\252\2\25\5\11\1\165\1\254\1\255"+
    "\3\11\1\65\12\11\1\167\3\25\1\156\1\11\1\37\14\11\1\105\6\11\1\37\1\47\4\25"+
    "\1\247\1\11\1\256\1\257\2\11\1\53\1\260\1\25\1\131\6\11\1\114\1\25\1\67\5"+
    "\11\1\216\1\0\1\50\1\25\1\0\1\167\2\0\1\67\1\261\1\0\1\67\2\11\1\65\1\50\2"+
    "\11\1\165\1\0\1\2\1\25\3\11\1\26\1\77\5\11\1\53\1\0\1\224\1\156\1\0\1\167"+
    "\1\262\1\11\1\0\1\263\5\11\1\101\1\166\1\25\1\257\1\264\1\0\1\167\2\11\1\22"+
    "\1\265\6\11\1\204\1\266\1\223\2\25\1\267\1\11\1\53\1\270\1\25\3\271\1\25\2"+
    "\22\5\11\1\170\1\11\1\24\16\11\1\53\1\272\1\0\1\167\64\11\1\114\1\25\2\11"+
    "\1\22\1\273\5\11\1\114\40\25\55\11\1\212\15\11\1\24\4\25\1\22\1\25\1\273\1"+
    "\274\1\11\1\106\1\22\1\173\1\275\15\11\1\24\3\25\1\273\54\11\1\212\2\25\10"+
    "\11\1\37\6\11\5\25\1\11\1\26\2\0\2\25\2\0\1\140\2\25\1\247\3\25\1\41\1\30"+
    "\20\11\1\276\1\241\1\25\1\0\1\167\1\40\2\11\1\46\1\40\2\11\1\47\1\277\12\11"+
    "\1\22\3\37\1\300\1\301\2\25\1\302\1\11\1\151\2\11\1\22\2\11\1\303\1\11\1\212"+
    "\1\11\1\212\4\25\17\11\1\47\10\25\6\11\1\26\20\25\1\304\20\25\3\11\1\26\6"+
    "\11\1\131\1\25\1\224\3\25\4\11\1\25\1\247\3\11\1\47\4\11\1\65\1\305\3\11\1"+
    "\212\4\11\1\114\1\11\1\244\5\25\23\11\1\212\1\0\1\167\4\11\1\114\4\11\1\114"+
    "\5\11\1\25\6\11\1\114\23\25\46\11\1\22\1\25\2\11\1\212\1\25\1\11\23\25\1\212"+
    "\1\106\4\11\1\35\1\306\2\11\1\212\1\25\2\11\1\22\1\25\3\11\1\22\10\25\2\11"+
    "\1\307\1\25\2\11\1\212\1\25\3\11\1\24\10\25\7\11\1\277\10\25\1\310\1\254\1"+
    "\151\1\40\2\11\1\212\1\121\4\25\3\11\1\26\3\11\1\26\4\25\1\11\1\40\2\11\1"+
    "\311\3\25\6\11\1\212\1\25\2\11\1\212\1\25\2\11\1\47\1\25\2\11\1\24\15\25\11"+
    "\11\1\131\6\25\6\11\1\47\1\25\6\11\1\47\1\25\4\11\1\216\1\25\1\0\1\167\50"+
    "\25\5\11\1\312\1\24\11\25\3\11\1\26\1\156\1\25\2\11\1\65\1\0\1\224\13\25\2"+
    "\11\1\26\3\25\2\11\1\22\1\25\1\225\6\11\1\0\1\166\3\25\1\125\1\0\1\25\1\62"+
    "\1\225\5\11\1\0\1\313\1\25\1\304\3\11\1\131\1\0\1\167\1\225\3\11\1\165\1\0"+
    "\1\147\1\0\1\314\1\25\4\11\1\315\1\25\1\225\5\11\1\53\1\0\1\316\1\317\1\0"+
    "\1\320\4\25\2\11\1\34\2\11\1\216\1\0\1\201\10\25\1\22\1\245\1\11\1\35\1\11"+
    "\1\131\5\11\1\165\1\0\1\305\1\0\1\167\1\321\1\104\1\105\2\11\1\106\1\127\1"+
    "\322\1\111\1\122\1\144\1\247\1\102\2\200\21\25\6\11\1\177\1\0\1\202\1\47\1"+
    "\0\1\323\1\24\3\25\6\11\2\0\1\324\1\25\1\0\1\167\24\25\5\11\1\165\1\50\1\0"+
    "\1\224\2\25\1\264\4\25\6\11\2\0\1\325\1\25\1\0\1\167\4\25\5\11\1\53\1\0\1"+
    "\131\1\0\1\167\6\25\3\11\1\211\1\0\1\2\1\0\1\167\30\25\5\11\1\216\1\0\1\305"+
    "\14\25\10\11\1\0\1\167\1\25\1\156\1\22\1\326\1\327\3\11\1\44\1\330\1\331\1"+
    "\25\1\0\1\167\10\25\1\11\1\37\4\11\1\101\1\332\1\333\3\25\1\101\1\225\4\11"+
    "\1\53\1\334\1\62\1\25\1\101\1\77\5\11\1\207\1\0\1\335\4\25\7\11\1\131\40\25"+
    "\1\11\1\106\3\11\1\165\1\166\1\0\1\131\1\25\1\0\1\167\2\25\1\37\3\11\1\332"+
    "\2\0\1\43\1\166\11\25\1\22\1\34\4\11\1\336\1\337\1\210\1\25\1\0\1\167\1\35"+
    "\1\106\3\11\1\340\1\234\1\131\1\0\1\167\46\25\2\11\1\341\27\25\1\131\4\25"+
    "\1\247\1\131\3\25\63\11\1\24\14\25\15\11\1\22\2\25\30\11\1\114\27\25\5\11"+
    "\1\22\1\0\1\224\70\25\10\11\1\22\67\25\7\11\1\131\3\11\1\22\1\0\1\167\14\25"+
    "\3\11\1\212\1\200\1\25\6\11\1\166\1\25\1\114\1\25\1\0\1\167\1\273\2\11\1\247"+
    "\2\11\26\25\10\11\20\25\11\11\1\342\1\101\6\0\1\62\1\225\1\11\10\25\1\343"+
    "\1\25\1\167\1\25\77\11\1\25\32\11\1\212\5\25\1\11\1\131\36\25\43\11\1\22\6"+
    "\25\1\47\1\25\1\344\1\25\61\11\1\114\40\25\15\11\1\47\1\11\1\26\1\11\1\131"+
    "\1\11\1\345\1\2\127\25\1\133\1\346\2\0\1\347\1\2\3\25\1\350\22\25\1\351\67"+
    "\25\12\11\1\30\10\11\1\30\1\352\1\353\1\11\1\354\1\151\7\11\1\35\1\227\2\30"+
    "\3\11\1\355\1\173\1\37\1\106\51\11\1\212\3\11\1\106\2\11\1\170\3\11\1\170"+
    "\2\11\1\30\3\11\1\30\2\11\1\22\3\11\1\22\3\11\1\106\3\11\1\106\2\11\1\170"+
    "\1\356\14\0\1\166\1\76\5\0\1\200\1\304\1\25\1\260\2\25\1\76\1\43\1\0\52\25"+
    "\1\166\2\0\1\357\1\360\1\305\32\25\5\11\1\26\1\202\1\212\1\0\1\361\56\25\5"+
    "\11\1\216\1\0\1\362\40\25\30\11\1\26\1\25\1\166\5\25\10\11\1\216\1\363\1\0"+
    "\1\167\52\25\1\131\51\25\1\151\3\11\1\364\1\40\1\170\1\365\1\243\1\366\1\364"+
    "\1\235\1\364\2\170\1\124\1\11\1\34\1\11\1\114\1\367\1\34\1\11\1\114\146\25"+
    "\1\0\1\167\33\11\1\212\4\25\106\11\1\26\1\25\33\11\1\212\120\11\1\24\1\25"+
    "\146\11\1\131\3\25\3\11\1\212\74\25\51\11\1\47\26\25\1\123\3\25\14\0\20\25"+
    "\36\0\2\25");

  /* The ZZ_CMAP_A table has 1984 entries */
  static final char ZZ_CMAP_A[] = zzUnpackCMap(
    "\11\7\1\2\1\1\1\41\1\2\1\1\6\7\4\0\1\2\1\25\1\11\1\10\1\6\1\34\1\0\1\12\1"+
    "\33\1\37\1\3\1\14\1\30\1\5\1\17\1\4\12\15\1\26\1\40\1\23\1\21\1\24\1\0\1\27"+
    "\3\6\1\20\1\16\1\20\7\6\1\13\14\6\1\31\1\0\1\35\1\0\1\6\1\0\3\6\1\20\1\16"+
    "\1\20\4\6\1\32\1\22\1\36\1\0\6\7\1\42\2\7\2\0\4\6\4\0\1\6\2\0\1\7\7\0\1\6"+
    "\4\0\1\6\5\0\7\6\1\0\2\6\4\0\4\6\16\0\5\6\7\0\1\6\1\0\1\6\1\0\5\6\1\0\2\6"+
    "\2\0\4\6\1\0\1\6\6\0\1\6\1\0\3\6\1\0\1\6\1\0\4\6\1\0\13\6\1\0\3\6\1\0\5\7"+
    "\2\0\6\6\1\0\7\6\1\0\1\6\6\0\1\6\6\0\1\6\1\0\15\7\1\0\1\7\1\0\2\7\1\0\2\7"+
    "\1\0\1\7\3\6\4\0\4\6\5\0\6\7\5\0\1\6\4\0\3\7\1\0\1\7\3\0\3\6\7\7\4\0\2\6\1"+
    "\7\13\6\1\0\1\6\7\7\2\6\2\7\1\0\4\7\2\6\2\7\3\6\2\0\1\6\7\0\1\7\1\6\1\7\6"+
    "\6\3\7\2\0\11\6\3\7\1\6\6\0\2\7\6\6\4\7\2\6\4\0\1\6\2\0\1\7\2\6\2\7\1\6\11"+
    "\7\1\6\3\7\1\6\5\7\2\0\1\6\3\7\7\0\11\7\6\6\3\7\1\6\2\7\1\6\7\7\2\6\2\7\2"+
    "\0\2\7\1\6\3\7\1\0\10\6\2\0\2\6\2\0\6\6\1\0\7\6\1\0\1\6\3\0\4\6\2\0\1\7\1"+
    "\6\7\7\2\0\2\7\2\0\3\7\1\6\5\0\2\6\1\0\5\6\7\0\2\6\1\0\1\7\2\0\3\7\1\0\4\6"+
    "\1\0\2\6\1\0\2\6\1\0\2\6\2\0\1\7\1\0\5\7\4\0\2\7\2\0\3\7\3\0\1\7\7\0\4\6\1"+
    "\0\1\6\7\0\4\7\3\6\1\7\2\0\1\6\1\0\2\6\1\0\3\6\2\7\1\0\3\7\2\0\1\6\10\0\1"+
    "\6\6\7\5\0\3\7\2\0\1\7\1\6\1\0\6\6\3\0\3\6\1\0\4\6\3\0\2\6\1\0\1\6\1\0\2\6"+
    "\3\0\2\6\3\0\2\6\4\0\5\7\3\0\3\7\1\0\4\7\2\0\1\6\6\0\6\7\5\6\3\0\1\6\7\7\1"+
    "\0\2\7\5\0\2\7\1\0\4\6\1\0\3\6\1\0\2\6\5\0\3\6\2\7\1\6\3\7\1\0\4\7\1\6\5\0"+
    "\3\6\1\7\7\0\5\6\1\0\1\6\4\0\1\7\4\0\6\7\1\0\1\7\3\0\2\7\4\0\1\6\1\7\2\6\7"+
    "\7\4\0\10\6\10\7\1\0\2\7\6\0\3\6\1\0\10\6\1\0\1\6\1\0\1\6\5\7\1\6\2\0\5\6"+
    "\1\0\1\6\1\0\2\7\2\0\4\6\5\0\1\7\1\0\1\7\1\0\1\7\4\0\2\7\5\6\10\7\11\0\1\7"+
    "\1\0\7\7\1\6\2\7\4\6\3\7\1\6\3\7\2\6\7\7\3\6\4\7\5\6\14\7\1\6\1\7\3\6\2\0"+
    "\3\7\6\6\2\0\2\6\3\7\3\0\2\6\2\7\4\0\1\6\1\0\2\7\4\0\4\6\10\7\3\0\1\6\3\0"+
    "\2\6\1\7\5\0\4\7\1\0\5\6\2\7\2\6\1\7\1\6\5\0\1\7\7\0\3\7\5\6\2\7\3\0\6\6\2"+
    "\0\3\6\3\7\1\0\5\7\4\6\1\7\6\6\1\7\2\6\3\7\1\6\5\0\2\7\1\0\5\7\1\0\1\6\1\0"+
    "\1\6\1\0\1\6\1\0\1\6\2\0\3\6\1\0\6\6\2\0\2\6\2\41\5\7\5\0\1\6\4\0\1\7\3\0"+
    "\3\7\2\0\1\6\4\0\1\6\1\0\5\6\2\0\1\6\1\0\4\6\1\0\3\6\2\0\4\6\5\0\5\6\4\0\1"+
    "\6\4\0\4\6\3\7\2\6\5\0\2\7\2\0\3\6\4\0\12\7\1\0\3\6\1\7\3\6\1\7\4\6\1\7\4"+
    "\6\4\0\1\7\6\0\1\6\1\0\2\6\1\7\5\6\1\7\2\6\2\7\5\6\1\0\4\6\2\7\4\0\1\6\3\7"+
    "\2\6\1\7\5\6\2\7\3\0\3\6\4\0\3\6\2\7\2\0\6\6\1\0\3\7\1\0\2\7\5\0\5\6\5\0\1"+
    "\6\1\7\3\6\1\0\2\6\1\0\7\6\2\0\1\7\6\0\2\6\2\0\3\6\3\0\2\6\3\0\2\6\2\0\3\7"+
    "\4\0\3\6\1\0\2\6\1\0\1\6\5\0\1\7\2\0\3\7\5\0\1\6\3\0\1\6\2\0\4\6\1\0\2\6\2"+
    "\0\1\6\3\7\1\0\2\7\1\0\5\6\2\7\1\0\2\6\1\0\2\7\3\0\3\7\2\0\1\7\6\0\1\6\2\7"+
    "\4\6\1\7\2\0\1\6\1\0\1\7\4\6\4\0\4\7\1\0\4\7\1\6\1\0\1\6\3\0\4\7\1\0\5\6\1"+
    "\0\2\7\1\6\4\7\4\0\1\7\1\6\4\7\2\6\1\0\1\6\1\7\3\0\1\6\4\0\1\6\2\0\10\6\1"+
    "\0\2\6\1\0\1\7\2\0\4\7\1\6\1\7\1\6\2\7\6\0\7\7\1\6\1\0\1\6\1\7\3\0\2\7\1\6"+
    "\4\7\1\0\2\7\3\0\1\6\2\0\1\6\6\7\3\0\1\7\1\0\2\7\1\0\1\7\2\6\5\7\1\0\3\6\4"+
    "\7\1\0\3\6\4\0\1\7\2\6\1\0\1\6\1\7\7\0\6\6\3\0\2\7\1\0\2\7\3\0\6\7\2\0\3\7"+
    "\2\0\4\7\4\0\3\7\5\0\1\6\2\0\2\6\2\0\4\6\1\0\4\6\1\0\1\6\1\0\5\6\1\0\4\6\1"+
    "\0\4\6\2\0\3\7\2\0\7\7\1\0\2\7\1\0\4\7\4\0\1\6\1\0\2\7\5\0\1\6\3\7\1\6\5\0"+
    "\2\6\1\0\1\6\2\0\1\6\1\0\1\6\1\0\1\6\5\0\1\6\1\0\1\6\1\0\3\6\1\0\3\6\1\0\3"+
    "\6");

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\1\0\1\1\1\2\1\3\1\4\1\5\1\1\1\6"+
    "\1\7\1\10\1\1\1\11\1\12\1\13\1\14\1\15"+
    "\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25"+
    "\1\26\1\27\1\30\1\31\1\32\1\33\1\34\1\35"+
    "\1\7\1\10\2\0\1\36\1\37\1\0\1\40\1\41"+
    "\1\0\1\42\1\0\1\43\1\44\1\0\1\45\1\0"+
    "\1\46\1\0\1\47\1\50\1\34\1\0\1\12\4\0"+
    "\2\36\1\34";

  private static int [] zzUnpackAction() {
    int [] result = new int[63];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\43\0\106\0\43\0\151\0\214\0\257\0\43"+
    "\0\322\0\365\0\u0118\0\43\0\u013b\0\u015e\0\u0181\0\u01a4"+
    "\0\u01c7\0\u01ea\0\u020d\0\u0230\0\43\0\43\0\43\0\43"+
    "\0\43\0\43\0\43\0\43\0\43\0\43\0\u0253\0\u0276"+
    "\0\43\0\u0299\0\u02bc\0\u02df\0\u0302\0\43\0\u0181\0\43"+
    "\0\43\0\u01a4\0\43\0\u01c7\0\43\0\43\0\u01ea\0\43"+
    "\0\u020d\0\43\0\u0325\0\43\0\43\0\u0348\0\u036b\0\u038e"+
    "\0\u03b1\0\u03d4\0\u03f7\0\u041a\0\u041a\0\u03d4\0\43";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[63];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\2\2\3\1\4\1\5\1\6\1\7\1\2\1\10"+
    "\1\11\1\12\1\13\1\14\1\15\1\7\1\16\1\7"+
    "\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26"+
    "\1\27\1\30\1\31\1\32\1\33\1\34\1\35\1\36"+
    "\46\0\2\3\43\0\1\37\44\0\1\40\43\0\3\7"+
    "\2\0\1\7\1\0\2\7\1\0\1\7\21\0\1\7"+
    "\11\11\1\41\31\11\12\12\1\42\30\12\6\0\3\7"+
    "\1\0\1\12\1\7\1\0\2\7\1\0\1\7\21\0"+
    "\1\7\15\0\1\15\1\43\1\44\40\0\1\45\1\0"+
    "\1\46\24\0\2\47\16\0\1\50\2\0\1\51\17\0"+
    "\2\52\17\0\1\53\21\0\2\54\16\0\1\55\2\0"+
    "\1\56\17\0\2\57\16\0\1\60\22\0\2\61\16\0"+
    "\1\62\22\0\2\63\16\0\1\64\4\0\1\65\14\0"+
    "\43\66\1\40\1\0\41\40\12\0\1\12\35\0\1\67"+
    "\6\0\1\67\1\70\42\0\1\45\42\0\1\45\1\71"+
    "\1\0\1\72\23\0\2\63\16\0\1\64\21\0\3\66"+
    "\1\73\37\66\15\0\1\70\42\0\1\70\1\0\1\44"+
    "\30\0\1\74\6\0\1\74\1\75\26\0\2\76\40\0"+
    "\3\66\1\73\1\77\36\66\15\0\1\75\25\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[1085];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String[] ZZ_ERROR_MSG = {
    "Unknown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\1\0\1\11\1\1\1\11\3\1\1\11\3\1\1\11"+
    "\10\1\12\11\2\1\1\11\1\1\2\0\1\1\1\11"+
    "\1\0\2\11\1\0\1\11\1\0\2\11\1\0\1\11"+
    "\1\0\1\11\1\0\2\11\1\1\1\0\1\1\4\0"+
    "\2\1\1\11";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[63];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private CharSequence zzBuffer = "";

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /**
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;

  /* user code: */
    private TokenTypeBundle tt;
    private SharedTokenTypeBundle stt;
    public MysqlPSQLParserFlexLexer(TokenTypeBundle tt) {
        this.tt = tt;
        this.stt = tt.getSharedTokenTypes();
    }


  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public MysqlPSQLParserFlexLexer(java.io.Reader in) {
    this.zzReader = in;
  }


  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    int size = 0;
    for (int i = 0, length = packed.length(); i < length; i += 2) {
      size += packed.charAt(i);
    }
    char[] map = new char[size];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < packed.length()) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }

  public final int getTokenStart() {
    return zzStartRead;
  }

  public final int getTokenEnd() {
    return getTokenStart() + yylength();
  }

  public void reset(CharSequence buffer, int start, int end, int initialState) {
    zzBuffer = buffer;
    zzCurrentPos = zzMarkedPos = zzStartRead = start;
    zzAtEOF  = false;
    zzAtBOL = true;
    zzEndRead = end;
    yybegin(initialState);
  }

  /**
   * Refills the input buffer.
   *
   * @return      {@code false}, iff there was new input.
   *
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {
    return true;
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final CharSequence yytext() {
    return zzBuffer.subSequence(zzStartRead, zzMarkedPos);
  }


  /**
   * Returns the character at position {@code pos} from the
   * matched text.
   *
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch.
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer.charAt(zzStartRead+pos);
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occurred while scanning.
   *
   * In a wellformed scanner (no or only correct usage of
   * yypushback(int) and a match-all fallback rule) this method
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  }


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() {
    if (!zzEOFDone) {
      zzEOFDone = true;
    
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public IElementType advance() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    CharSequence zzBufferL = zzBuffer;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;

      zzState = ZZ_LEXSTATE[zzLexicalState];

      // set up zzAction for empty match case:
      int zzAttributes = zzAttrL[zzState];
      if ( (zzAttributes & 1) == 1 ) {
        zzAction = zzState;
      }


      zzForAction: {
        while (true) {

          if (zzCurrentPosL < zzEndReadL) {
            zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL/*, zzEndReadL*/);
            zzCurrentPosL += Character.charCount(zzInput);
          }
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL/*, zzEndReadL*/);
              zzCurrentPosL += Character.charCount(zzInput);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + ZZ_CMAP(zzInput) ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
        zzAtEOF = true;
        zzDoEOF();
        return null;
      }
      else {
        switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
          case 1: 
            { return stt.getIdentifier();
            } 
            // fall through
          case 41: break;
          case 2: 
            { return stt.getWhiteSpace();
            } 
            // fall through
          case 42: break;
          case 3: 
            { return tt.getCharacterTokenType(20);
            } 
            // fall through
          case 43: break;
          case 4: 
            { return tt.getCharacterTokenType(19);
            } 
            // fall through
          case 44: break;
          case 5: 
            { return tt.getCharacterTokenType(12);
            } 
            // fall through
          case 45: break;
          case 6: 
            { return tt.getCharacterTokenType(7);
            } 
            // fall through
          case 46: break;
          case 7: 
            { return stt.getQuotedIdentifier();
            } 
            // fall through
          case 47: break;
          case 8: 
            { return stt.getString();
            } 
            // fall through
          case 48: break;
          case 9: 
            { return tt.getCharacterTokenType(14);
            } 
            // fall through
          case 49: break;
          case 10: 
            { return stt.getInteger();
            } 
            // fall through
          case 50: break;
          case 11: 
            { return tt.getCharacterTokenType(3);
            } 
            // fall through
          case 51: break;
          case 12: 
            { return tt.getCharacterTokenType(4);
            } 
            // fall through
          case 52: break;
          case 13: 
            { return tt.getCharacterTokenType(21);
            } 
            // fall through
          case 53: break;
          case 14: 
            { return tt.getCharacterTokenType(11);
            } 
            // fall through
          case 54: break;
          case 15: 
            { return tt.getCharacterTokenType(6);
            } 
            // fall through
          case 55: break;
          case 16: 
            { return tt.getCharacterTokenType(5);
            } 
            // fall through
          case 56: break;
          case 17: 
            { return tt.getCharacterTokenType(1);
            } 
            // fall through
          case 57: break;
          case 18: 
            { return tt.getCharacterTokenType(0);
            } 
            // fall through
          case 58: break;
          case 19: 
            { return tt.getCharacterTokenType(2);
            } 
            // fall through
          case 59: break;
          case 20: 
            { return tt.getCharacterTokenType(8);
            } 
            // fall through
          case 60: break;
          case 21: 
            { return tt.getCharacterTokenType(9);
            } 
            // fall through
          case 61: break;
          case 22: 
            { return tt.getCharacterTokenType(10);
            } 
            // fall through
          case 62: break;
          case 23: 
            { return tt.getCharacterTokenType(13);
            } 
            // fall through
          case 63: break;
          case 24: 
            { return tt.getCharacterTokenType(15);
            } 
            // fall through
          case 64: break;
          case 25: 
            { return tt.getCharacterTokenType(16);
            } 
            // fall through
          case 65: break;
          case 26: 
            { return tt.getCharacterTokenType(17);
            } 
            // fall through
          case 66: break;
          case 27: 
            { return tt.getCharacterTokenType(18);
            } 
            // fall through
          case 67: break;
          case 28: 
            { return stt.getBlockComment();
            } 
            // fall through
          case 68: break;
          case 29: 
            { return stt.getLineComment();
            } 
            // fall through
          case 69: break;
          case 30: 
            { return stt.getNumber();
            } 
            // fall through
          case 70: break;
          case 31: 
            { return tt.getOperatorTokenType(8);
            } 
            // fall through
          case 71: break;
          case 32: 
            { return tt.getOperatorTokenType(0);
            } 
            // fall through
          case 72: break;
          case 33: 
            { return tt.getOperatorTokenType(7);
            } 
            // fall through
          case 73: break;
          case 34: 
            { return tt.getOperatorTokenType(1);
            } 
            // fall through
          case 74: break;
          case 35: 
            { return tt.getOperatorTokenType(2);
            } 
            // fall through
          case 75: break;
          case 36: 
            { return tt.getOperatorTokenType(4);
            } 
            // fall through
          case 76: break;
          case 37: 
            { return tt.getOperatorTokenType(3);
            } 
            // fall through
          case 77: break;
          case 38: 
            { return tt.getOperatorTokenType(5);
            } 
            // fall through
          case 78: break;
          case 39: 
            { return tt.getOperatorTokenType(6);
            } 
            // fall through
          case 79: break;
          case 40: 
            { return tt.getOperatorTokenType(9);
            } 
            // fall through
          case 80: break;
          default:
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}