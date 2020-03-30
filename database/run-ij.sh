DERBY_HOME=/c/java/db-derby-10.14.2.0-bin
export DERBY_HOME
CLASSPATH=$DERBY_HOME/lib/derby.jar:$DERBY_HOME/lib/derbytools.jar
export CLASSPATH
java -Dderby.ui.codeset=UTF8 org.apache.derby.tools.ij
