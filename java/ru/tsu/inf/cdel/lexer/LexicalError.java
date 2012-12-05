package ru.tsu.inf.cdel.lexer;


public class LexicalError extends Error {
    private Integer line, column;
    public LexicalError(String msg, int line, int column)
    {
        super(msg);
        this.line=line;
        this.column=column;
    }

    @Override
    public String toString() {
        return "Lexical Error: " + getMessage() + " at line " +line.toString() + ", column " + column.toString();
    }
}
