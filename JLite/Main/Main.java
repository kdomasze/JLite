package Main;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileInputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import Parse.*;

public class Main
{
    /**
     * Main method for the compiler.
     */

    public static void main(String args[]) throws Exception
    {
        boolean dumpnodes = false;
        boolean dumpast = false;

        Vector<String> files = new Vector<String>();
        for (int i = 0; i < args.length; i++)
        {
            String option = args[i];

            if (option.equals("-help"))
            {
                System.out.println("-help -- print out help");
                System.out.println("-dump -- dump Parse Tree");
                System.out.println("-dumpAST -- dump AST tree");
                System.out.println("-mainclass -- this class cointains the main method");
                System.exit(0);
            }
            else if (option.equals("-dump"))
            {
                dumpnodes = true;
            }
            else if (option.equals("-dumpAST"))
            {
                dumpast = true;
            }
            else if (option.equals("-mainclass"))
            {
                String mainclass = args[++i];
            }
            else
            {
                files.add(option);
            }
        }

        for (int i = 0; i < files.size(); i++)
        {
            String arg = files.get(i);
            try
            {
                ParseNode pn = readSourceFile(arg);
                if (dumpnodes)
                {
                    System.out.println(pn.PPrint(2, true));
                }
/*
                if (dumpast)
                {

                }
                */
            } catch (Exception e)
            {
                System.out.println("Error in sourcefile:" + arg);
                e.printStackTrace();
                System.exit(-1);
            } catch (Error e)
            {
                System.out.println("Error in sourcefile:" + arg);
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }

    /**
     * Reads in a source file and returns the parse tree.
     */

    public static ParseNode readSourceFile(String sourcefile)
    {
        try
        {
            Reader fr = new BufferedReader(new FileReader(sourcefile));
            Lex.Lexer l = new Lex.Lexer(fr);
            java_cup.runtime.lr_parser g;
            g = new Parse.Parser(l);
            ParseNode p = null;
            try
            {
                p = (ParseNode) g./*debug_*/ parse().value;
            } catch (Exception e)
            {
                System.err.println("Error parsing file:" + sourcefile);
                e.printStackTrace();
                System.exit(-1);
            }
            if (l.numErrors() != 0)
            {
                System.out.println("Error parsing " + sourcefile);
                System.exit(l.numErrors());
            }
            return p;

        } catch (Exception e)
        {
            throw new Error(e);
        }
    }
}
