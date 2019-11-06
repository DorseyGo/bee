bee-data-weaver:
	目标：
		数据的接入、数据存储以及数据分发
	功能模块：
		1. 数据的接入：
			1.1 将数据从客户端接入到Kafka，并根据配置是否对接入的数据进行治理，包括数据预处理模块
			1.2 数据的接入主要实现Source，目前接收用户的批次写入以及单挑数据写入
		2. 数据存储：
			2.1 将接入的数据以AVRO的格式存放到Kafka
		3. 数据分发：
			3.1 从Kafka集群中读取数据，并对数据进行后处理，包括后处理链
			3.2 数据读取后，被分发到不同的存储介质，目前只支持JDBC、HDFS
	数据接入配置：
		- JDBC 公告表
			-- connector配置样本：
				{
					"name": "kafka-jdbc-affiche-source-retry05",
					"config": {
				        "connector.class": "com.leatop.bee.data.weaver.connector.jdbc.JDBCSourceAfficheConnector",
				        "tasks.max": "2",
				        "table.whitelist": "ETC_PASS_LIST_SOURCE",
				        "value.converter.schema.registry.url": "http://128.8.7.176:9090",
				        "topic.prefix": "",
				        "connection.user": "exchange",
				        "connection.password": "exchange",
				        "affiche.table.name": "FI_PROSDBOARD",
				        "value.converter": "com.leatop.bee.data.weaver.converter.AvroConverter",
				        "connection.url": "jdbc:oracle:thin:@128.8.7.174:1521:cjintdb",
				        "key.converter": "org.apache.kafka.connect.storage.StringConverter",
				        "table.relation.name": "FI_TABLE_REL"
				    }
				}
			-- notice
				table.relation.name, 定义主表与关系表的关系，默认为FI_TABLE_REL
				affiche.table.name, 公告表
	数据分发配置：
		- JDBC
			-- connector配置样本：
				{
					"name": "kafka-jdbc-oracle-connector-retry01",
					"config": {
						"connector.class": "com.leatop.bee.data.weaver.sink.jdbc.JDBCSinkConnector",
						"tasks.max": 2,
						"table.name.format": "ETCPASSLIST{yyyy}",
						"key.converter": "org.apache.kafka.connect.storage.StringConverter",
						"value.converter": "com.leatop.bee.data.weaver.converter.AvroConverter",
						"metrix.reporter.url": "",
						"topics": "etc-pass-list",
						"value.converter.schema.registry.url": "http://128.8.7.176:9090",
						"max.retries": 3,
						"connection.url": "jdbc:oracle:thin:@128.8.7.174:1521:cjintdb",
						"connection.user": "lyjc",
						"connection.password": "lyjc",
						"pivot.field": "opTime",
						"table.name.generator.class": "com.leatop.bee.data.weaver.sink.jdbc.generator.TimeBasedTableNameGenerator"
					}
				}
			
		- HDFS
			-- 坏境准备：
				1) 在Kafka目录下，新建一个ext目录（$KAFKA_HOME/ext），加入Kafka目录为/opt/kafka-2.12_1.0.0，则
				新建的ext目录的全路径为：	/opt/kafka-2.12_1.0.0/ext
				2) 将Hadoop相关的依赖包拷贝到新建的external目录，jar包主要包括：
					1.1) hadoop-common-${version}.jar ($HADOOP_HOME/share/hadoop/common)
					1.2) hadoop-hdfs-${version}.jar ($HADOOP_HOME/share/hadoop/hdfs)
					1.3) hadoop-hdfs-client-${version}.jar ($HADOOP_HOME/share/hadoop/hdfs)
					1.4) $HADOOP_HOME/share/hadoop/common/lib目录下全部的jar包
					
					注意：
					a) $HADOOP_HOME为Hadoop的安装目录，例如/opt/hadoop-2.9.2
					b) ${version}为Hadoop的版本，假如选取版本2.9.2，则common包为hadoop-common-2.9.2.jar
		    -- shell脚本
		    	修改kafka目录下的kafka-run-class.sh脚本，在shopt -u nullglob命令前加入以下代码：
			    	#: external for support
					for file in "$base_dir"/ext/*;
					do
					  if should_include_file "$file"; then
					    CLASSPATH="$CLASSPATH":"$file"
					  fi
					done
					
		    	(注意，可将本项目scripts目录下的kafka-run-class.sh脚本拷贝到$KAFKA_HOME/bin目录下，进行覆盖)
			-- connector配置样本：
				{
					"name": "kafka-hdfs-connector-retry06",
					"config": {
						"connector.class": "com.leatop.bee.data.weaver.sink.hdfs.HDFSSinkConnector",
						"tasks.max": 2,
						"key.converter": "org.apache.kafka.connect.storage.StringConverter",
						"value.converter": "com.leatop.bee.data.weaver.converter.AvroConverter",
						"topics": "hdfs-test",
						"value.converter.schema.registry.url": "http://128.8.7.176:9090",
						"max.retries": 3,
						"hadoop.home": "/opt/hadoop-2.9.2",
						"hadoop.conf.dir": "/opt/hadoop-2.9.2/etc/hadoop/",
						"hdfs.url": "hdfs://128.8.7.191:9001",
						"topics.folder": "/topics",
						"storage.class": "com.leatop.bee.data.weaver.sink.hdfs.storage.HDFSStorage",
						"formatter.class": "com.leatop.bee.data.weaver.sink.hdfs.storage.format.formatters.AvroFormatter"
					}
				}