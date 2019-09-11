# PaymentTracker
Program keeps a record of payments.

## Assumptions
1. If the user enters “quit“, exit program.  
2. If program starting up, check input from a file.  
3. If the user enters invalid file path, display an error message.  
4. File must have .txt format.  
5. Check every line of file, if the line consist invalid input, display an error message.  
6. If the user enters invalid input, display an error message.  
7. Input must consist currency, space and amount.  
8. Currency may be any uppercase 3 letter code.  
9. Once per minute, the output showing the net amounts of each currency.  
10. If the net amount is 0, that currency should not be displayed.  
11. If currency have the exchange rate, write the USD equivalent amount next to it.  

## Run program (project directory)
-	mvn package  
-	java -jar target/PaymentTracker-1.0.jar  
__OR__  
-	mvn spring-boot:run  
