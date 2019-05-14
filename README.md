# queryengine
``` 
mvn install
mvn spring-boot:run
```


#schemas
MySQL:
"foodmart-mysql"."foodmart"
"persons"."persons"

Druid:
"foodmart"."foodmart"


# sample queries
Druid
http://localhost:8080/query?sql=select%20count(*)%20from%20%22foodmart%22.%22foodmart%22

MySQL
http://localhost:8080/query?sql=select%20count(*)%20from%20%22foodmart-mysql%22.%22foodmart%22

MySQL joid Druid
http://localhost:8080/query?sql=select%22t2%22.%22product_name%22%20from%22persons%22.%22persons%22as%22t1%22join%22foodmart%22.%22foodmart%22as%22t2%22on%22t1%22.%22person_id%22=%22t2%22.%22customer_id%22and%22t2%22.%22timestamp%22%20%3E=%20%271997-02-12%2000:00:00%20UTC%27%20and%20%22t2%22.%22timestamp%22%20%3C%20%271997-02-12%2000:01:00%20UTC%27
