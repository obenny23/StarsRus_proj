JFLAGS = -d classes
JC = javac

SQLPATH = .:sqlite-jdbc-3.34.0.jar

.SUFFIXES: .java .class

CLASSES = \
	net/main/Main.java \
	net/main/Connect.java \
	net/main/Populate.java \

.PHONY: classes

classes:
	$(JC) $(JFLAGS) $(CLASSES)

run: 
	java -cp "$(SQLPATH)":classes starsrus.Main run

setup:
	java -cp "$(SQLPATH)":classes starsrus.Main setup

reset:
	java -cp "$(SQLPATH)":classes starsrus.Main reset

clean:
	rm -r ./classes/*