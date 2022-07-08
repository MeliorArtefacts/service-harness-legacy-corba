/* __  __    _ _      
  |  \/  |  | (_)       
  | \  / | ___| |_  ___  _ __ 
  | |\/| |/ _ \ | |/ _ \| '__|
  | |  | |  __/ | | (_) | |   
  |_|  |_|\___|_|_|\___/|_|   
    Service Harness
*/
package org.melior.client.corba;
import java.lang.reflect.Method;
import org.melior.client.core.ClientConfig;
import org.melior.client.exception.RemotingException;
import org.melior.client.pool.ConnectionPool;
import org.omg.CORBA.ORB;
import org.omg.CORBA.UserException;
import org.omg.CORBA.portable.IDLEntity;
import org.omg.Messaging.RELATIVE_RT_TIMEOUT_POLICY_TYPE;

/**
 * TODO
 * @author Melior
 * @since 2.3
 */
public class Connection<T extends IDLEntity> extends org.melior.client.core.Connection<ClientConfig, Connection<T>, T>{
    private ORB objectRequestBroker;

    private CorbaStub<T> stubBuilder;

  /**
   * Constructor.
   * @param configuration The client configuration
   * @param connectionPool The connection pool
   * @param objectRequestBroker The object request broker
   * @param stubBuilder The stub builder
   * @throws RemotingException if an error occurs during the construction
   */
  public Connection(
    final ClientConfig configuration,
    final ConnectionPool<ClientConfig, Connection<T>, T> connectionPool,
    final ORB objectRequestBroker,
    final CorbaStub<T> stubBuilder) throws RemotingException{
        super(configuration, connectionPool);

        this.objectRequestBroker = objectRequestBroker;

        this.stubBuilder = stubBuilder;
  }

  /**
   * Check whether connection is still valid.
   * @param fullValidation The full validation indicator
   * @return true if the connection is still valid, false otherwise
   */
  public boolean isValid(
    final boolean fullValidation){

        if (lastException != null){
      return lastException instanceof UserException;
    }

    return true;
  }

  /**
   * Open raw connection.
   * @return The raw connection
   * @throws Exception if unable to open the raw connection
   */
  protected T openConnection() throws Exception{
        org.omg.CORBA.Object localObject;
    org.omg.CORBA.Any timeoutAny;
    org.omg.CORBA.Policy[] policies;
    T connection;

        localObject = objectRequestBroker.string_to_object(configuration.getUrl());

    if (localObject == null){
      throw new Exception("Could not open connection.  URL may be invalid.");
    }

        timeoutAny = objectRequestBroker.create_any();
    timeoutAny.insert_ulonglong(configuration.getRequestTimeout() * 10000);
    policies = new org.omg.CORBA.Policy[] {objectRequestBroker.create_policy(RELATIVE_RT_TIMEOUT_POLICY_TYPE.value, timeoutAny)};
    localObject = localObject._set_policy_override(policies, org.omg.CORBA.SetOverrideType.SET_OVERRIDE);

        connection = stubBuilder.build(localObject);

    if (connection == null){
      throw new Exception("Could not open connection.  URL may be invalid.");
    }

    return connection;
  }

  /**
   * Close raw connection.
   * @param connection The raw connection
   * @throws Exception if unable to close the raw connection
   */
  protected void closeConnection(
    final T connection) throws Exception{
        ((org.omg.CORBA.Object) connection)._release();
  }

  /**
   * Handle proxy invocation.
   * @param object The object on which the method was invoked
   * @param method The method to invoke
   * @param args The arguments to invoke with
   * @return The result of the invocation
   * @throws Throwable when the invocation fails
   */
  public Object invoke(
    final Object object,
    final Method method,
    final Object[] args) throws Throwable{
        String methodName;
    Object invocationResult;

        methodName = method.getName();

        if (methodName.equals("_release") == true){
            releaseConnection(this);

            invocationResult = null;
    }
    else{
            invocationResult = invoke(method, args);
    }

    return invocationResult;
  }

}
