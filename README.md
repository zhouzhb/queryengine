# queryengine

## how to run
```
git clone https://github.com/valtroffuture/hive.git
cd hive
git checkout thriftfix
mvn install -DskipTests
cd ..
git clone https://github.com/zhouzhb/queryengine.git
cd queryengine 
mvn install -DskipTests
mvn spring-boot:run
```


## tables
```
MySQL:
"mysql"."test"

Druid:
"druid"."test"
```

# sample queries
MySQL
http://localhost:8080/query?sql=select%20count(*)%20from%20%22mysql%22.%22test%22

Druid
http://localhost:8080/query?sql=select%20count(*)%20from%20%22druid%22.%22test%22

MySQL-to-Druid Join
http://localhost:8080/query?sql=select*from%22mysql%22.%22test%22as%22t1%22join%22druid%22.%22test%22as%22t2%22on%22t1%22.%22strInc1%22=%22t2%22.%22strInc1%22and%22t2%22.%22__time%22%20%3E=%20%272019-01-01%2000:00:00%20UTC%27%20and%20%22t2%22.%22__time%22%20%3C%20%272020-01-01%2000:01:00%20UTC%27
