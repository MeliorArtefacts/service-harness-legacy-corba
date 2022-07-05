# Melior Service Harness :: Legacy : CORBA
<div style="display: inline-block;">
<img src="https://img.shields.io/badge/version-2.3-yellow?style=for-the-badge"/>
<img src="https://img.shields.io/badge/development-busy-yellow?style=for-the-badge"/>
<img src="https://img.shields.io/badge/compatibility-spring_boot_2.4.5-yellow?style=for-the-badge"/>
</div>

## Artefact
Get the artefact and the POM file in the *artefact* folder.
```
<dependency>
    <groupId>org.melior</groupId>
    <artifactId>melior-harness-legacy-corba</artifactId>
    <version>2.3</version>
</dependency>
```

## Client
Create a bean to instantiate the CORBA client.  The CORBA client uses connection pooling to improve performance.
```
@Bean("myclient")
@ConfigurationProperties("myclient")
public CorbaClient<_MyInterfaceStub, MyException> client() {
    return CorbaClientBuilder.<_MyInterfaceStub, MyException>create()
        .stub(c -> (_MyInterfaceStub) MyInterfaceHelper.narrow(c))
        .exception(e -> new RemotingException(e.myErrId, e.myErrText))
        .build();
}
```

The CORBA client is auto-configured from the application properties.
```
myclient.url=corbaloc::some.service:12345/some.name
myclient.request-timeout=30
myclient.inactivity-timeout=300
```

Wire in and use the CORBA client.
```
@Autowired
@Qualifier("myclient")
private CorbaClient<_MyInterfaceStub, MyException> client;

public void foo(Person person) throws RemotingException {
    client.execute((c) -> c.addPerson(person));
}
```

The CORBA client may be configured using these application properties.

|Name|Default|Description|
|:---|:---|:---|
|`url`||The URL of the CORBA service|
|`minimum-connections`|0|The minimum number of connections to open to the CORBA service|
|`maximum-connections`|1000|The maximum number of connections to open to the CORBA service|
|`connection-timeout`|30 s|The amount of time to allow for a new connection to open to the CORBA service|
|`validate-on-borrow`|false|Indicates if a connection must be validated when it is borrowed from the JDBC connection pool|
|`validation-timeout`|5 s|The amount of time to allow for a connection to be validated|
|`request-timeout`|60 s|The amount of time to allow for a request to the CORBA service to complete|
|`backoff-period`|1 s|The amount of time to back off when the circuit breaker trips|
|`backoff-multiplier`|1|The factor with which to increase the backoff period when the circuit breaker trips repeatedly|
|`backoff-limit`||The maximum amount of time to back off when the circuit breaker trips repeatedly|
|`inactivity-timeout`|300 s|The amount of time to allow before surplus connections to the CORBA service are pruned|
|`maximum-lifetime`|unlimited|The maximum lifetime of a connection to the CORBA service|
|`prune-interval`|60 s|The interval at which surplus connections to the CORBA service are pruned|

&nbsp;  
## References
Refer to the [**Melior Service Harness :: Core**](https://github.com/MeliorArtefacts/service-harness-core) module for detail on the Melior logging system and available utilities.
