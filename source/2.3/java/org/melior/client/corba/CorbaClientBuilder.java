/* __  __    _ _      
  |  \/  |  | (_)       
  | \  / | ___| |_  ___  _ __ 
  | |\/| |/ _ \ | |/ _ \| '__|
  | |  | |  __/ | | (_) | |   
  |_|  |_|\___|_|_|\___/|_|   
    Service Harness
*/
package org.melior.client.corba;
import org.omg.CORBA.UserException;
import org.omg.CORBA.portable.IDLEntity;

/**
 * TODO
 * @author Melior
 * @since 2.3
 */
public class CorbaClientBuilder<T extends IDLEntity, E extends UserException>{
    private CorbaStub<T> stubBuilder;

    private CorbaException<E> exceptionTranslator;

  /**
   * Constructor.
   */
  private CorbaClientBuilder(){
        super();
  }

  /**
   * Create CORBA client builder.
   * @return The CORBA client builder
   */
  public static <T extends IDLEntity, E extends UserException> CorbaClientBuilder<T, E> create(){
        return new CorbaClientBuilder<T, E>();
  }

  /**
   * Build CORBA client.
   * @return The CORBA client
   */
  public CorbaClient<T, E> build(){

        if (stubBuilder == null){
      throw new RuntimeException( "Stub builder must be provided.");
    }

        return new CorbaClient<T, E>(stubBuilder, exceptionTranslator);
  }

  /**
   * Set stub builder.
   * @param stubBuilder The stub builder
   * @return The CORBA client builder
   */
  public CorbaClientBuilder<T, E> stub(
    final CorbaStub<T> stubBuilder){
        this.stubBuilder = stubBuilder;

    return this;
  }

  /**
   * Set exception translator.
   * @param exceptionTranslator The exception translator
   * @return The CORBA client builder
   */
  public CorbaClientBuilder<T, E> exception(
    final CorbaException<E> exceptionTranslator){
        this.exceptionTranslator = exceptionTranslator;

    return this;
  }

}
