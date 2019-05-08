# queryengine
``` 
mvn install
mvn spring-boot:run
```
http://localhost:8080/query?sql=select%20%22state_province%22,%20count(*)%20as%20c%20from%20%22foodmart%22%20where%20%22timestamp%22%20%3E=%20%271900-01-011T1:00:00.000Z%27%20and%20%22timestamp%22%20%3C%20%272019-01-01T12:00:00.000Z%27%20and%20%22product_name%22%20=%20%27High%20Top%20Dried%20Mushrooms%27%20group%20by%20%22state_province%22
```
http://localhost:8080/query?sql=select%20count(*)%20from%20%22foodmart%22
```
http://localhost:8080/mysql?sql=select%20count(*)%20from%20%22foodmart%22
```
