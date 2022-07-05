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
import org.melior.client.pool.ConnectionPool;
import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.ObjectImpl;

/**
 * TODO
 * @author Melior
 * @since 2.3
 */
public class ConnectionFactory<T extends ObjectImpl> implements org.melior.client.core.ConnectionFactory<ClientConfig, Connection<T>, T> {
		private ORB objectRequestBroker;

		private CorbaStub<T> stubBuilder;

	/**
	 * Constructor.
	 * @param configuration The client configuration
	 * @para] objectRequestBroker The object request broker
	 * @param stubBuilder The stub builder
	 * @throws RemotingException if unable to initialize the connection factory
	 */
	public ConnectionFactory(
		final ClientConfig configuration,
		final ORB objectRequestBroker,
		final CorbaStub<T> stubBuilder) throws RemotingException {
				super();

				this.objectRequestBroker = objectRequestBroker;

				this.stubBuilder = stubBuilder;
	}

	/**
	 * Create a new connection.
	 * @param configuration The client configuration
	 * @param connectionPool The connection pool
	 * @return The new connection
	 * @throws RemotingException if unable to create a new connection
	 */
	public Connection createConnection(
		final ClientConfig configuration,
		final ConnectionPool<ClientConfig, Connection<T>, T> connectionPool) throws RemotingException {
				Connection connection;

				connection = new Connection<T>(configuration, connectionPool, objectRequestBroker, stubBuilder);
		connection.open();

		return connection;
	}

	/**
	 * Destroy the connection.
	 * @param connection The connection
	 */
	public void destroyConnection(
		final Connection connection) {
				connection.close();
	}

}
