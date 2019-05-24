# queryengine

## how to run
``` 
mvn install
mvn spring-boot:run
```


## tables
```
MySQL:
"foodmart-mysql"."foodmart"
"persons"."persons"

Druid:
"foodmart"."foodmart"
```

# sample queries
MySQL
http://localhost:8080/query?sql=select%20count(*)%20from%20%22test%22.%22test%22
