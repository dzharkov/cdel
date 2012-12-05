
src_full_path=../java/ru/tsu/inf/cdel

all: clean GeneratedLexer.java parser
	

parser: gramm.cup
	cup -parser GeneratedParser -symbols LexemeType -package ru.tsu.inf.cdel.parser gramm.cup
	cp GeneratedParser.java $(src_full_path)/parser
	cp LexemeType.java $(src_full_path)/parser

GeneratedLexer.java: gramm.flex parser
	jflex gramm.flex
	cp GeneratedLexer.java $(src_full_path)/lexer	
clean:
	rm -rf *.java
	rm -rf *.java~
