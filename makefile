JFLAGS = -g
JC = javac
JVM= java
FILE=

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	bplusTree.java \
	TreeOperations.java

MAIN = bplusTree

ifndef INPUT
INPUT = inputfile.txt
endif

default: classes

classes: $(CLASSES:.java=.class)

run: $(MAIN).class
	$(JVM) $(MAIN) $(INPUT)


clean:
	$(RM) *.class