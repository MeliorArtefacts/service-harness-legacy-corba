/* __  __      _ _            
  |  \/  |    | (_)           
  | \  / | ___| |_  ___  _ __ 
  | |\/| |/ _ \ | |/ _ \| '__|
  | |  | |  __/ | | (_) | |   
  |_|  |_|\___|_|_|\___/|_|   
        Service Harness
*/
package org.melior.client.corba;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.melior.client.core.ClientConfig;
import org.melior.client.exception.RemotingException;
import org.melior.logging.core.Logger;
import org.melior.logging.core.LoggerFactory;
import org.melior.service.exception.ExceptionType;
import org.melior.util.time.Timer;
import org.omg.CORBA.ORB;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.UserException;
import org.omg.CORBA.portable.IDLEntity;
import org.springframework.util.StringUtils;

/**
 * Implements an easy to use, auto-configuring CORBA client with connection
 * pooling and configurable backoff strategy.
 * <p>
 * The client writes timing details to the logs while dispatching CORBA messages
 * to the remote CORBA object.  The client converts any exception that occurs
 * during communication with the remote CORBA object, using the provided exception
 * translator.
 * @author Melior
 * @since 2.3
 */
public class CorbaClient<T extends IDLEntity, E extends UserException> extends ClientConfig {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private CorbaStub<T> stubBuilder;

    private CorbaException<E> exceptionTranslator;

    private ConnectionManager<T> connectionManager;

    /**
     * Constructor.
     * @param stubBuilder The stub builder
     * @param exceptionTranslator The exception translator
     */
    CorbaClient(
        final CorbaStub<T> stubBuilder,
        final CorbaException<E> exceptionTranslator) {

        super();

        this.stubBuilder = stubBuilder;

        this.exceptionTranslator = exceptionTranslator;
    }

    /**
     * Configure client.
     * @param clientConfig The new client configuration parameters
     * @return The LDAP client
     */
    public CorbaClient<T, E> configure(
        final ClientConfig clientConfig) {
        super.configure(clientConfig);

        return this;
    }

    /**
     * Initialize client.
     * @throws RemotingException if unable to initialize the client
     */
    private void initialize() throws RemotingException {

        Properties properties;
        ORB objectRequestBroker;

        if (connectionManager != null) {
            return;
        }

        if (StringUtils.hasLength(getUrl()) == false) {
            throw new RemotingException(ExceptionType.LOCAL_APPLICATION, "URL must be configured.");
        }

        properties = new Properties();
        properties.setProperty("org.omg.CORBA.ORBClass", "org.jacorb.orb.ORB");
        properties.setProperty("org.omg.CORBA.ORBSingletonClass", "org.jacorb.orb.ORBSingleton");
        properties.setProperty("jacorb.config.log.verbosity", "2");
        properties.setProperty("jacorb.log.default.verbosity", "0");
        properties.setProperty("jacorb.dns.enable", "off");
        properties.setProperty("jacorb.compactTypecodes", "0");
        properties.setProperty("jacorb.interop.indirection_encoding_disable", "on");
        properties.setProperty("jacorb.interop.comet", "on");
        properties.setProperty("jacorb.interop.lax_boolean_encoding", "on");
        properties.setProperty("jacorb.debug.dump_outgoing_messages", "off");
        properties.setProperty("jacorb.debug.dump_incoming_messages", "off");
        objectRequestBroker = ORB.init(new String[] {}, properties);

        connectionManager = new ConnectionManager<T>(this, new ConnectionFactory<T>(this, objectRequestBroker, stubBuilder));
    }

    /**
     * Execute method on remote object.
     * @param method The method to execute.
     * @throws RemotingException if unable to execute the method
     */
    @SuppressWarnings("unchecked")
    public void execute(
        final CorbaMethod<T> method) throws RemotingException {

        String methodName = "execute";
        Timer timer;
        T connection;
        long duration;
        RemotingException remotingException;

        initialize();

        logger.debug(methodName, "Execute method on remote object.");

        timer = Timer.ofNanos().start();

        connection = connectionManager.getConnection();

        try {

            method.execute(connection);

            duration = timer.elapsedTime(TimeUnit.MILLISECONDS);

            logger.debug(methodName, "Method executed successfully.  Duration = ", duration, " ms.");
        }
        catch (UserException exception) {

            duration = timer.elapsedTime(TimeUnit.MILLISECONDS);

            logger.debug(methodName, "Method execution failed.  Duration = ", duration, " ms.");

            if (exceptionTranslator == null) {

                throw new RemotingException(exception.toString() + " " + exception.getMessage(), exception);
            }
            else {

                remotingException = exceptionTranslator.translate((E) exception);
                throw new RemotingException(remotingException.getCode(), remotingException.getMessage(), exception);
            }

        }
        catch (SystemException exception) {

            duration = timer.elapsedTime(TimeUnit.MILLISECONDS);

            logger.debug(methodName, "Method execution failed.  Duration = ", duration, " ms.");

            throw new RemotingException(ExceptionType.REMOTING_COMMUNICATION, exception.toString() + " " + exception.getMessage(), exception);
        }
        finally {

            ((org.omg.CORBA.Object) connection)._release();
        }

    }

}
