/* JFlex example: part of Java language lexer specification */
package ru.tsu.inf.cdel.lexer;

import java_cup.runtime.*;
import ru.tsu.inf.cdel.parser.LexemeType;
import ru.tsu.inf.cdel.ast.TerminalNode;
/**
 * This class is a simple example lexer.
 */
%%
%class GeneratedLexer
%unicode
%cup
%line
%column
%caseless
%{
  StringBuffer string = new StringBuffer();
  int stringBeginLine,stringBeginColumn;

  private Symbol symbol(int type) {
    return symbol(type, yytext());
  }
  private Symbol symbol(int type, Object value) {
    TerminalNode node = new TerminalNode(value, type, yyline+1, yycolumn+1, yylength());
    Symbol s = new Symbol(type, yyline+1, yycolumn+1, node);
    return s;
  }

  public int getLine() {
        return yyline+1;
  }
  public int getColumn() {
        return yycolumn+1;
  }
%}
%eofval{ 
return symbol(LexemeType.EOF);
%eofval}
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]

Identifier = [:jletter:] [:jletterdigit:]*

Sign = [+-]

DecIntegerLiteral = (0 | [1-9][0-9]*)
DecDoubleLiteral = (0 | [1-9][0-9]*)  "." [0-9]+

%state STRING

%%
 /* keywords */
<YYINITIAL> {

"var"              { return symbol(LexemeType.VAR); }
"procedure"              { return symbol(LexemeType.PROCEDURE); }
"function"              { return symbol(LexemeType.FUNCTION); }
"array"              { return symbol(LexemeType.ARRAY); }
"while"              { return symbol(LexemeType.WHILE); }
"for"              { return symbol(LexemeType.FOR); }
"if"              { return symbol(LexemeType.IF); }
"else"              { return symbol(LexemeType.ELSE); }
"or"              { return symbol(LexemeType.OR); }
"div"              { return symbol(LexemeType.DIV); }
"mod"              { return symbol(LexemeType.MOD); }
"and"              { return symbol(LexemeType.AND); }
"main"              { return symbol(LexemeType.MAIN); }
"of"                { return symbol(LexemeType.OF); }
"("              { return symbol(LexemeType.LEFTPAR); }
")"              { return symbol(LexemeType.RIGTHPAR); }
";"              { return symbol(LexemeType.SEMICOLON); }
","              { return symbol(LexemeType.COMMA); }
"=="              { return symbol(LexemeType.EQUALS); }
":"              { return symbol(LexemeType.COLON); }
".."              { return symbol(LexemeType.DOTDOT); }
":="              { return symbol(LexemeType.ASSIGN); }
"!"              { return symbol(LexemeType.NOT); }
"!="              { return symbol(LexemeType.NOTEQUAL); }
"<"              { return symbol(LexemeType.LESS); }
"<="              { return symbol(LexemeType.LESSOREQUAL); }
">"              { return symbol(LexemeType.MORE); }
">="              { return symbol(LexemeType.MOREOREQUAL); }
"+"              { return symbol(LexemeType.PLUS); }
"-"              { return symbol(LexemeType.MINUS); }
"*"              { return symbol(LexemeType.MULTIPLY); }
"/"              { return symbol(LexemeType.DIVIDE); }
"{"              { return symbol(LexemeType.LEFTBRACE); }
"}"              { return symbol(LexemeType.RIGHTBRACE); }
"["              { return symbol(LexemeType.LEFTBRACKET); }
"]"              { return symbol(LexemeType.RIGHTBRACKET); }


{DecIntegerLiteral} { return symbol(LexemeType.INTEGER_LITERAL, Integer.parseInt(yytext())); }
{DecDoubleLiteral}  { return symbol(LexemeType.DOUBLE_LITERAL, Double.parseDouble(yytext())); }

{Identifier}        { return symbol(LexemeType.IDENT); }

{WhiteSpace}                   { /* ignore */ }
'                             { string.setLength(0); 
                                stringBeginLine=yyline+1;
                                stringBeginColumn=yycolumn+1; 
                                yybegin(STRING); 
                            }
}
<STRING> {
'                            { yybegin(YYINITIAL); 
                                    TerminalNode node = new TerminalNode(string.toString(), 
                                                LexemeType.STRING_LITERAL, stringBeginLine,
                                                stringBeginColumn, yyline, yycolumn
                                                );
                                   Symbol s =  new Symbol(LexemeType.STRING_LITERAL, 
                                   stringBeginLine,
                                   stringBeginColumn,
                                   node);
                                   return s;
                             }
  [^'\\]+                   { string.append( yytext() ); }
  \\t                            { string.append('\t'); }
  \\n                            { string.append('\n'); }

  \\r                            { string.append('\r'); }
  \\\'                           { string.append('\''); }
  \\                             { string.append('\\'); }
<<EOF>>                     { throw new LexicalError("Expected end of string literial", yyline+1, yycolumn+1); }
}
#.*                         {}
 /* error fallback */
.|\n                             { throw new LexicalError("Illegal character <"+
                                                    yytext()+">", yyline+1, yycolumn+1); }
