import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import my.pkg.*;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;

public class PrettyPrinter {
    private static String example1 = "a     := 0\n" +
                                     "    if    10 < 1\n" +
                                     "       do\n" +
                                     "a    :=     42      # Zuweisung des Wertes 42 an die Variable a\n" +
                                     "else do\n" +
                                     "        a :=      7\n" +
                                     "  end" +
                                     "       a := 10\n" +
                                     "b := 0\n" +
                                     "while    \na > 0       do\n" +
                                     "    a    :=     a - 1\n" +
                                     "    b :=\n b + 9\n" +
                                     "end";
    private static String example2 = "while 1 < 10 do" +
                                     " a := \"FGYUOIgiuweh89y4e0\"" +
                                     "while a < 10 do" +
                                     " 10 - 5 end end";
    private static String example3 = "   var1:=     10\n" +
                                     "if    var1>5     do\n" +
                                     "b  :=   3\n" +
                                     "      c    :=var1+ b*2\n" +
                                     "else do\n" +
                                     "      b:=     0\n" +
                                     "  c    :=   1\n" +
                                     "   end\n" +
                                     "while     c>0     do\n" +
                                     "     c:=c -   1\n" +
                                     "      if   c==0 do\n" +
                                     "      var1:=var1-1\n" +
                                     "  else do\n" +
                                     "         var1:= var1 +   2\n" +
                                     " end\n" +
                                     "end\n" +
                                     "   result := var1 + b + c\n";
    private static String[] examples = {example1, example2, example3};

    public static void main(String[] args) {
        for(String example : examples) {
            Blatt3GrammatikLexer lexer = new Blatt3GrammatikLexer(CharStreams.fromString(example));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            Blatt3GrammatikParser parser = new Blatt3GrammatikParser(tokens);

            ParseTree tree = parser.start();

            IO.println("\nUsed to be:\n\n" + example);

            IO.println("Cleaned:");
            prettyPrint(tree, parser, 0);
        }
        IO.println("\n\n\nAST");

        for(String example : examples) {
            Blatt3GrammatikLexer lexer = new Blatt3GrammatikLexer(CharStreams.fromString(example));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            Blatt3GrammatikParser parser = new Blatt3GrammatikParser(tokens);

            ParseTree tree = parser.start();
            printAST(tree, 0);
            IO.println();
        }
    }

    private static void prettyPrint(ParseTree tree, Blatt3GrammatikParser parser, int level) {
        //each statement has at least its own line
        if (tree.toStringTree(parser).startsWith("(statement")) {
            IO.println();
        }

        //handles while loop indentation
        if (tree.getParent() != null && tree.getParent().toStringTree(parser).startsWith("(loop")) {
            if (!tree.toStringTree(parser).equals("while")
                & !tree.toStringTree(parser).startsWith("(condition")
                & !tree.toStringTree(parser).equals("do")
                & !tree.toStringTree(parser).equals("end")) {
                IO.print("   ".repeat(level));
            }
            if (tree.toStringTree(parser).equals("end")) {
                IO.println();
                IO.print("   ".repeat(level));
            }
        }

        //handles if-Statement indentation
        if (tree.getParent() != null && tree.getParent().toStringTree(parser).startsWith("(conditional")) {
            if (!tree.toStringTree(parser).equals("if")
                & !tree.toStringTree(parser).startsWith("(condition")
                & !tree.toStringTree(parser).equals("do")
                & !tree.toStringTree(parser).equals("end")
                & !tree.toStringTree(parser).equals("else do")) {
                IO.print("   ".repeat(level));
            }
            if (tree.toStringTree(parser).equals("end")) {
                IO.println();
                IO.print("   ".repeat(level));
            }
            if (tree.toStringTree(parser).equals("else do")) {
                IO.println();
                IO.print("   ".repeat(level - 1));
            }
        }

        //each token is seperated by whitespace
        if (tree.getChildCount() == 0) {
            IO.print(tree.toStringTree() + " ");
        }

        //traverses entire tree
        for (int i = 0; i < tree.getChildCount(); i++) {
            if (tree.getChild(i).toStringTree(parser).equals("do")) {
                level++;
            }
            if (tree.getChild(i).toStringTree(parser).equals("end")) {
                level--;
            }
            prettyPrint(tree.getChild(i), parser, level);
        }
    }

    private static void printAST(ParseTree tree, int indent) {
        String rule = tree.getClass().getSimpleName().replace("Context", "");
        String pad = "   ".repeat(indent);
        if (rule.equals("Conditional")) {
            rule = "if";
            if (tree.getText().contains("else")) rule = "if-else";
        }
        if (!rule.equals("Term")
            & !rule.equals("Statement")
            & !rule.equals("Arithmetic")
            & !rule.equals("Condition")
            ) {
            System.out.println(pad + rule);
        }

        for (int i = 0; i < tree.getChildCount(); i++) {
            ParseTree child = tree.getChild(i);
            if (!(child instanceof TerminalNode)) {
                printAST(child, indent + 1);
            } else if (!child.getText().equals("do") & !child.getText().equals("end") & !child.getText().equals(":=") & !child.getText().equals("if") & !child.getText().equals("else do")) {
                System.out.println(pad + "   " + child.getText());
            }
        }
    }

}
