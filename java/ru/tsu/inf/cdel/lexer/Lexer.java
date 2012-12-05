package ru.tsu.inf.cdel.lexer;
import java.io.*;
import java_cup.runtime.*;
import ru.tsu.inf.cdel.utils.LexerUtils;


public class Lexer implements java_cup.runtime.Scanner {
    public static String getLexemeTypeName(int id) {
        return LexerUtils.getLexemeTypeName(id);
    }
    
    private GeneratedLexer generatedLexer;
    public Lexer(Reader in) {
        generatedLexer = new GeneratedLexer(in);
    }
    public Lexer(InputStream in) {
        this(new java.io.InputStreamReader(in));
    }
    
    public Integer getLine()
    {
        return generatedLexer.getLine();
    }
    public Integer getColumn()
    {
        return generatedLexer.getColumn();
    }
    
    @Override
    public Symbol next_token() throws java.io.IOException {
        try {
            Symbol token = generatedLexer.next_token();
            return token;
        } catch (NumberFormatException e) {
            throw new LexicalError(generatedLexer.yytext() + " can't be parsed as number", getLine(), getColumn());
        }
    }
    
}
