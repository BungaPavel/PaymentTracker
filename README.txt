Clone repository from Github.
Run mvn clean install.
Copy PaymentTracker-1.0-SNAPSHOT.jar from target folder to your folder (home folder).
Copy initial file to your folder.
Execute statement bellow.

java -classpath PaymentTracker-1.0-SNAPSHOT.jar tb.paymentTracker.PaymentTracker a.txt

a.txt is name of initial file, which is locate in same foder as jar file.

Assumptions:
Application don´t end after wrong input. Error message is written to console.
Application don´t accepted file, which contains wrong data. Error message with number of line is written to console. 

Instruction for import files:
Write input in form *.txt (payments.txt, USD.txt,...). File with this name from home folder will be imported to application. 