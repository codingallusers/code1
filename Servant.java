import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class Servant extends UnicastRemoteObject implements ServerInterface {

    public Servant() throws RemoteException {
        super();
    }

    public String concat(String a, String b) throws RemoteException {
        System.out.println("Handling request from: " + Thread.currentThread().getName());
        return a + b;
    }
}