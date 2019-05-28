package com.huawei.cloudsop.us.queryengine;

import com.huawei.cloudsop.us.queryengine.Connection.QueryResult;
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
    @Test public void testMySqlScheme() {
        QueryController controller = new QueryController();
        String query = "select count(*) from \"mysql\".\"test\"";
        QueryResult res = controller.query(query);
        System.out.println(">>>>> result: " + res.getContent());
    }

    /**
     * Tests local or VM Druid schema. VM Druid should be set up using the following steps:
     *
     * Create a directory and named it to represent a VM you plan on creating, eg, "Druid" for VM with Druid installed on it.
     * Create a file in that directory named Vagrantfile with this content:
     *
     * Vagrant.configure("2") do |config|
     *   config.vm.box = "ubuntu/trusty64"
     *   config.vm.network "forwarded_port", guest: 8081, host: 8081
     *   config.vm.network "forwarded_port", guest: 8082, host: 8082
     *   config.vm.network "forwarded_port", guest: 8888, host: 8888
     *   config.vm.network "forwarded_port", guest: 8890, host: 8890
     * end
     *
     * Run command "vagrant up" in the same directory.
     * This will create a VirtualBox image of Ubuntu Server 14.
     * Run "vagrant ssh" to SSH to the VM.
     *
     * Install Git, JDK:
     *  Git:
     *   sudo apt-get update
     *   sudo apt-get install software-properties-common python-software-properties
     *   sudo add-apt-repository ppa:git-core/ppa
     *   sudo apt-get update
     *   sudo apt-get install git
     *  JDK:
     *   sudo apt-get purge openjdk-\*
     *   sudo apt-get autoremove
     *   sudo add-apt-repository ppa:openjdk-r/ppa
     *   sudo apt-get update
     *   sudo apt-get install openjdk-8-jdk
     *
     * Install Druid:
     *  sudo apt-get install curl
     *  sudo chown vagrant /var/cache/apt/archives/
     *  curl http://archive.apache.org/dist/incubator/druid/0.14.0-incubating/apache-druid-0.14.0-incubating-bin.tar.gz -o /var/cache/apt/archives/druid-0.14.0-incubating-bin.tar.gz
     *  tar -xzf /var/cache/apt/archives/druid-0.14.0-incubating-bin.tar.gz
     *  cd apache-druid-0.14.0-incubating
     *
     * Install ZooKeeper:
     *  curl http://archive.apache.org/dist/zookeeper/zookeeper-3.4.11/zookeeper-3.4.11.tar.gz -o /var/cache/apt/archives/zookeeper-3.4.11.tar.gz
     *  tar -xzf /var/cache/apt/archives/zookeeper-3.4.11.tar.gz
     *  mv zookeeper-3.4.11 zk
     *
     * Start Druid
     *  bin/supervise -c quickstart/tutorial/conf/tutorial-cluster.conf
     *
     * Ingest test data:
     *  SSH to teh VM in another window and run these commands:
     *   cd apache-druid-0.14.0-incubating
     *   vi quickstart/tutorial/test-index.json
     *   paste the following text into the file: https://github.com/valtroffuture/scripts/blob/master/druid-schema.json
     *   vi quickstart/tutorial/test.json
     *   paste the following text into the file: https://github.com/valtroffuture/scripts/blob/master/druid-index.json
     *   bin/post-index-task --file quickstart/tutorial/test-index.json
     *  verify that data is there:
     *   vi quickstart/tutorial/test-query.json
     *
     *   paste the following text into the file:
     *
     * {
     *   "queryType" : "topN",
     *   "dataSource" : "test",
     *   "intervals" : ["2015-09-12/2115-09-13"],
     *   "granularity" : "all",
     *   "metric" : "count",
     *   "dimension" : "timestamp",
     *   "threshold" : 10,
     *   "aggregations" : [
     *     {
     *       "type" : "count",
     *       "name" : "count"
     *     }
     *   ]
     * }
     *
     *   curl -X 'POST' -H 'Content-Type:application/json' -d @quickstart/tutorial/test-query.json http://localhost:8082/druid/v2?pretty
     *   You can run the above command from host machine to verify that you can access Druid - you should be able to do it because of port forwarding it will redirect localhost Druid requests to VM, just make sure you point to the host location of test.json file.
     */
    @Test public void testDruidScheme() {
        QueryController controller = new QueryController();
        String query = "select count(*) from \"druid\".\"test\"";
        QueryResult res = controller.query(query);
        System.out.println(">>>>> result: " + res.getContent());
    }
}
