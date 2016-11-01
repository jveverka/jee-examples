This EJB remote access demo within same instance of JEE AS (WildFly 10.0.0).
This is gradle multi-project, it consists of following sub-projects 
  ejb-common  - contains only definition of remote data service interface (java project)
  ejb-service - contains implementation of remote and local data service interface, uses local data service (dynamic web project)
  ejb-client  - uses remote implementation of data service from ejb-service module (dynamic web project)

Notes:
- ejb-service has to be deployed in order to ejb-client work
- ejb-client contains example of two possibilities of JNDI lookup
  1. injection: @Resource(lookup = "java:global/ejb-service/SingletonDataService!ite.examples.ejbservice.services.DataServiceRemote")
  2. programatically: ds = (DataServiceRemote) new InitialContext().lookup("java:global/ejb-service/SingletonDataService!ite.examples.ejbservice.services.DataServiceRemote");
  