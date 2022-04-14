JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Ambuteiaj.java \
	Autoturism.java \
	Autoutilitar.java \
	Autovehicul.java \
	Bicicleta.java \
	Camion.java \
	Harta.java \
	Main.java \
	Moped.java \
	Motocicleta.java \
	Vehicul.java \

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class

run:
	java Main.java

build:
	javac *.java