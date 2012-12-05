/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.tsu.inf.cdel.utils;

import ru.tsu.inf.cdel.parser.LexemeType;


public class LexerUtils {
        public static String getLexemeTypeName(int id) {
        switch(id) {
            case LexemeType.EOF: { return "EOF"; }
            case LexemeType.VAR: { return "VAR"; }
            case LexemeType.PROCEDURE: { return "PROCEDURE"; }
            case LexemeType.FUNCTION: { return "FUNCTION"; }
            case LexemeType.ARRAY: { return "ARRAY"; }
            case LexemeType.WHILE: { return "WHILE"; }
            case LexemeType.FOR: { return "FOR"; }
            case LexemeType.IF: { return "IF"; }
            case LexemeType.ELSE: { return "ELSE"; }
            case LexemeType.OR: { return "OR"; }
            case LexemeType.DIV: { return "DIV"; }
            case LexemeType.MOD: { return "MOD"; }
            case LexemeType.AND: { return "AND"; }
            case LexemeType.MAIN: { return "MAIN"; }
            case LexemeType.LEFTPAR: { return "LEFTPAR"; }
            case LexemeType.RIGTHPAR: { return "RIGTHPAR"; }
            case LexemeType.SEMICOLON: { return "SEMICOLON"; }
            case LexemeType.COMMA: { return "COMMA"; }
            case LexemeType.EQUALS: { return "EQUALS"; }
            case LexemeType.COLON: { return "COLON"; }
            case LexemeType.DOTDOT: { return "DOTDOT"; }
            case LexemeType.ASSIGN: { return "ASSIGN"; }
            case LexemeType.NOT: { return "NOT"; }
            case LexemeType.NOTEQUAL: { return "NOTEQUAL"; }
            case LexemeType.LESS: { return "LESS"; }
            case LexemeType.LESSOREQUAL: { return "LESSOREQUAL"; }
            case LexemeType.MORE: { return "MORE"; }
            case LexemeType.MOREOREQUAL: { return "MOREOREQUAL"; }
            case LexemeType.PLUS: { return "PLUS"; }
            case LexemeType.MINUS: { return "MINUS"; }
            case LexemeType.MULTIPLY: { return "MULTIPLY"; }
            case LexemeType.DIVIDE: { return "DIVIDE"; }
            case LexemeType.LEFTBRACE: { return "LEFTBRACE"; }
            case LexemeType.RIGHTBRACE: { return "RIGHTBRACE"; }
            case LexemeType.LEFTBRACKET: { return "LEFTBRACKET"; }
            case LexemeType.RIGHTBRACKET: { return "RIGHTBRACKET"; }
            case LexemeType.DOUBLE_LITERAL: { return "DOUBLE_LITERAL"; }
            case LexemeType.STRING_LITERAL: { return "STRING_LITERAL"; }
            case LexemeType.INTEGER_LITERAL: { return "INTEGER_LITERAL"; }
            case LexemeType.IDENT: { return "IDENT"; }
        }
        return "<UNKNOWN>(" + Integer.toString(id) + ")";
    }
}
