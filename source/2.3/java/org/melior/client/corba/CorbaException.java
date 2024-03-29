/* __  __      _ _            
|  \/  |    | (_)           
| \  / | ___| |_  ___  _ __ 
| |\/| |/ _ \ | |/ _ \| '__|
| |  | |  __/ | | (_) | |   
|_|  |_|\___|_|_|\___/|_|   
      Service Harness
*/
package org.melior.client.corba;
import org.melior.client.exception.RemotingException;
import org.omg.CORBA.UserException;

/**
* Translate a CORBA user exception to a standard remoting exception.
* The application code is expected to catch and handle the remoting
* exception.
* @author Melior
* @since 2.3
*/
@FunctionalInterface
public interface CorbaException<E extends UserException> {

    /**
     * Translate the CORBA user exception to a standard remoting exception.
     * @param exception The CORBA user exception
     * @return The remoting exception
     */
    public abstract RemotingException translate(
        final E exception);

}
