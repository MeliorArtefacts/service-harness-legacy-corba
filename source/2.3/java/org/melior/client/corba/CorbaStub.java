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
import org.omg.CORBA.portable.IDLEntity;

/**
* Build a client stub for the local CORBA object.  The client code requires
* the stub to be able to execute methods on the remote CORBA object.
* @author Melior
* @since 2.3
*/
@FunctionalInterface
public interface CorbaStub<T extends IDLEntity> {

    /**
     * Build a client stub for the local CORBA object.
     * @param localObject The local CORBA object
     * @return The client stub
     * @throws SystemException if unable to build the client stub
     */
    public abstract T build(
        final org.omg.CORBA.Object localObject) throws SystemException;

}
