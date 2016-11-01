WildFly 10.1.0 demo: how to connect messaging client using AMQP or MQTT protocols: 

1. enable remote JMS acceptor in wildfly configuration
Edit bin/standalone.conf
add JAVA_OPTS="$JAVA_OPTS -Djboss.server.default.config=standalone-full.xml"
edit standalone/configuration/standalone-full.xml
in section<subsystem xmlns="urn:jboss:domain:messaging-activemq:1.0"> add
                <remote-acceptor name="netty-acceptor-amqp" socket-binding="messaging-amqp">  
                  <param name="protocols" value="AMQP,CORE"/>  
                </remote-acceptor>                
                <remote-acceptor name="netty-acceptor-mqtt" socket-binding="messaging-mqtt">  
                  <param name="protocols" value="MQTT"/>  
                </remote-acceptor>                
in section <socket-binding-group name="standard-sockets" default-interface="public" port-offset="${jboss.socket.binding.port-offset:0}"> add
        <socket-binding name="messaging-amqp" port="7999"/>
        <socket-binding name="messaging-mqtt" port="7998"/>

2. run clients:

