Clustered Application
=====================

This demo application demostrates clustering capabilities of WildFly AS.
Application is able to run on standalone WildFly server as well. 
WildFy 9.0.1 or later is required
Deploy and go to URL http://localhost:8080/ClusteredTestApp

Application utilizes following WildFly subsystems:
CDI, EJB, JPA, Servlets, WebSockets, Infinispan (JCache), JMS 

Standalone Mode
---------------
In order to run on WildFly in standalone mode, add snippets below into standalone-full.xml
subsystem:datasources
                <xa-datasource jndi-name="java:/datasources/PostgresDSXA" pool-name="PostgresDSXA" enabled="true" use-java-context="true">
                    <xa-datasource-property name="ServerName">
                        localhost
                    </xa-datasource-property>
                    <xa-datasource-property name="PortNumber">
                        5432
                    </xa-datasource-property>
                    <xa-datasource-property name="DatabaseName">
                        postgres
                    </xa-datasource-property>
                    <driver>postgres</driver>
                    <xa-pool>
                        <min-pool-size>5</min-pool-size>
                        <initial-pool-size>5</initial-pool-size>
                        <max-pool-size>100</max-pool-size>
                        <prefill>true</prefill>
                    </xa-pool>
                    <security>
                        <user-name>dbadmin</user-name>
                        <password>dbadmin</password>
                    </security>
                    <validation>
                        <valid-connection-checker class-name="org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLValidConnectionChecker"/>
                        <exception-sorter class-name="org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLExceptionSorter"/>
                    </validation>
                </xa-datasource>

subsystem:messaging/hornetq-server/jms-destinations
                    <jms-topic name="TestClusterTopic">
                        <entry name="java:/jboss/exported/jms/topic/TestClusterTopic"/>
                    </jms-topic>    
                    
subsystem:infinispan
            <cache-container name="jcacheClustered" default-cache="jcacheClustereddb">
                <local-cache name="jcacheClustereddb">
                    <transaction mode="NON_XA"/>
                </local-cache>
            </cache-container>
                                            
Clustered mode
--------------
In order to run on WildFly in cluster, add snippets into standalone-full-ha.xml mode

subsystem:datasources
                <xa-datasource jndi-name="java:/datasources/PostgresDSXA" pool-name="PostgresDSXA" enabled="true" use-java-context="true">
                    <xa-datasource-property name="ServerName">
                        localhost
                    </xa-datasource-property>
                    <xa-datasource-property name="PortNumber">
                        5432
                    </xa-datasource-property>
                    <xa-datasource-property name="DatabaseName">
                        postgres
                    </xa-datasource-property>
                    <driver>postgres</driver>
                    <xa-pool>
                        <min-pool-size>5</min-pool-size>
                        <initial-pool-size>5</initial-pool-size>
                        <max-pool-size>100</max-pool-size>
                        <prefill>true</prefill>
                    </xa-pool>
                    <security>
                        <user-name>dbadmin</user-name>
                        <password>dbadmin</password>
                    </security>
                    <validation>
                        <valid-connection-checker class-name="org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLValidConnectionChecker"/>
                        <exception-sorter class-name="org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLExceptionSorter"/>
                    </validation>
                </xa-datasource>

subsystem:messaging/hornetq-server/jms-destinations (JMS provider must be configured to run in cluster mode)
                    <jms-topic name="TestClusterTopic">
                        <entry name="java:/jboss/exported/jms/topic/TestClusterTopic"/>
                    </jms-topic>    
                    
subsystem:infinispan
            <cache-container name="jcacheClustered" default-cache="jcacheClustereddb">
                <transport lock-timeout="60000"/>
                <replicated-cache name="jcacheClustereddb" mode="SYNC">
                    <transaction mode="FULL_XA"/>
                </replicated-cache>
            </cache-container> 

