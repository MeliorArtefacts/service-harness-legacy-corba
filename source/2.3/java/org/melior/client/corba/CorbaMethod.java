/* __  __      _ _            
|  \/  |    | (_)           
| \  / | ___| |_  ___  _ __ 
| |\/| |/ _ \ | |/ _ \| '__|
| |  | |  __/ | | (_) | |   
|_|  |_|\___|_|_|\___/|_|   
      Service Harness
*/
package org.melior.client.corba;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.UserException;
import org.omg.CORBA.portable.ObjectImpl;

/**
* TODO
* @author Melior
* @since 2.3
*/
@FunctionalInterface
public interface CorbaMethod<T extends ObjectImpl> {

	/**
	 * Execute a method on the client stub.  The client stub serializes the payload
	 * and sends it to the remote CORBA object, then receives the response from the
	 * remote CORBA object and deserializes it so that the client code may receive
	 * the response.
	 * @param stub The client stub
	 * @throws UserException if the IDL defined a custom user exception for the
	 * method and the remote CORBA object raised the custom user exception
	 * @throws SystemException if the client stub is not abke to communicate with
	 * the remote CORBA object
	 */
	public abstract void execute(
		final T stub) throws UserException, SystemException;

}
