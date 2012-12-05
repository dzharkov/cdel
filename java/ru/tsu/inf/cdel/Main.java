package ru.tsu.inf.cdel;
import java.io.*;
import java_cup.runtime.Symbol;
import ru.tsu.inf.cdel.ast.ASTNode;
import ru.tsu.inf.cdel.lexer.Lexer;
import ru.tsu.inf.cdel.lexer.LexicalError;
import ru.tsu.inf.cdel.parser.GeneratedParser;
import ru.tsu.inf.cdel.parser.SyntaxFatalError;
import ru.tsu.inf.cdel.semantical.DeclarationsVisitor;
import ru.tsu.inf.cdel.semantical.SemanticalError;
import ru.tsu.inf.cdel.semantical.TypeCheckVisitor;
import ru.tsu.inf.cdel.semantical.function.Function;

public class Main {
    
    private static TypeCheckVisitor vis;
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        if (args.length < 1)
        {
            System.out.println("file is expected as first argument");
            System.exit(1);
        }
        
        InputStream in = new FileInputStream(args[0]);
        Lexer lexer = new Lexer(in);

        GeneratedParser parser = new GeneratedParser(lexer);
        Symbol s = null;
        try {
            s = parser.parse();
        } catch (LexicalError e) {
            System.err.println(e.toString());
            System.exit(1);
        } catch (SyntaxFatalError e) {
            
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        if (parser.getErrors().size() > 0) {
            for (String error: parser.getErrors()) {
                System.err.println(error);
            }
            System.exit(1);
        }
        
        ASTNode root = (ASTNode)(s.value);
        
        DeclarationsVisitor declars = new DeclarationsVisitor();
        
        root.accept(declars);
        
        if (declars.getErrors().size() > 0) {
            for (SemanticalError e : declars.getErrors()) {
                System.out.println("Semantical error: " + e.getMessage());
            }
            System.exit(1);
        }
        
        for (Function f : declars.getFuncMap().values()) {
            System.out.println(f);
        }
        
        System.out.println(declars.getVars());
        
        TypeCheckVisitor typeCheckVisitor = new TypeCheckVisitor(declars);
        
        root.accept(typeCheckVisitor);
        
        if (typeCheckVisitor.getErrors().size() > 0) {
            for (SemanticalError e : typeCheckVisitor.getErrors()) {
                System.out.println("Semantical error: " + e.getMessage());
            }
            System.exit(1);
        }
        vis = typeCheckVisitor;
        printASTTree(root, "");
        
    }
       
    private static void printASTTree(ASTNode root, String prefix) {
        String tmp = prefix + root.getNodeInfo();
        if (vis.getType(root) != null) {
            tmp += "[" + vis.getType(root) + "]";
        }
        System.out.println(tmp);
        for (ASTNode child : root.getChildren()) {
            printASTTree(child, prefix + "----");
        }
    }
}
