all: compiler

compiler: Parse/Sym.java Parse/Parser.java classfiles

classfiles:
	javac -cp ./cup:.:$(CLASSPATH) Main/Main.java

Parse/Parser.java Parse/Sym.java: Parse/java14.cup
	cd Parse && \
        java -cp ../cup:$(CLASSPATH) java_cup.Main -parser Parser -symbols Sym < java14.cup

%.class: %.java
	javac -cp ../cup:.:$(CLASSPATH) $<

clean:
	rm -f Main/*.class Lex/*.class Parse/*.class Parse/Sym.java Parse/Parser.java
