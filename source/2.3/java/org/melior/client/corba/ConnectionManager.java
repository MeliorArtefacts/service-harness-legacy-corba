/* __  __      _ _            
  |  \/  |    | (_)           
  | \  / | ___| |_  ___  _ __ 
  | |\/| |/ _ \ | |/ _ \| '__|
  | |  | |  __/ | | (_) | |   
  |_|  |_|\___|_|_|\___/|_|   
        Service Harness
*/
package org.melior.client.corba;
import org.melior.client.core.ClientConfig;
import org.melior.client.exception.RemotingException;
import org.omg.CORBA.portable.IDLEntity;

/**
 * Implements a manager for persistent CORBA {@code Connection} objects, for connections to
 * remote CORBA object. The manager writes statistics from the underlying connection pool to
 * the logs whenever a {@code Connection} is borrowed from the pool.
 * @author Melior
 * @since 2.3
 */
public class ConnectionManager<T extends IDLEntity> extends org.melior.client.pool.ConnectionManager<ClientConfig, Connection<T>, T> {

    /**
     * Constructor.
     * @param configuration The client configuration
     * @param connectionFactory The connection factory
     */
    public ConnectionManager(
        final ClientConfig configuration,
        final ConnectionFactory<T> connectionFactory) {

        super(configuration, connectionFactory);
    }

    /**
     * Get connection from pool.
     * @return The connection
     * @throws RemotingException if unable to get a connection
     */
    public T getConnection() throws RemotingException {
        return super.getConnection();
    }

}
