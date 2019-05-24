package com.huawei.cloudsop.us.queryengine;

import com.huawei.cloudsop.us.queryengine.Controller.QueryController;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class SchemaTest {

    /**
     * Tests local or VM MySQL schema. VM MySQL should be set up using the following steps:
     *
     * Run these commands:
     *
     * vagrant init ubuntu/trusty64
     * add this line to the Vagrantfile:
     *  config.vm.network "forwarded_port", guest: 3306, host: 3306
     * vagrant up
     * vagrant ssh
     *
     * sudo apt-get update
     * sudo apt-get install mysql-server
     * sudo ufw allow mysql
     * sudo service mysql restart
     *
     * sudo vi /etc/mysql/my.cnf
     *  comment out this line:
     *   skip-external-locking
     *  set this line to be:
     *   bind-address = 0.0.0.0
     * :x
     * sudo service mysql restart
     *
     * sudo mysql -u root -p -e "grant all privileges on *.* to 'root'@'%' identified by 'pass' with grant option";
     * sudo service mysql restart
     *
     * You can now connect to the VM MySQL as if it was your local MySQL installation using the "mysql -u root -p" command. For example, to ingest test data into MySQL, run this command:
     *  mysql -u root -p < mysql.sql
     * where mysql.sql file is generated using this script: https://github.com/valtroffuture/scripts/blob/master/mysql.scala
     */
    @Test public void testLocalMySqlScheme() {
        QueryController controller = new QueryController();
        String query = "select count(*) from \"test\".\"test\"";
        controller.query(query);
    }
}
