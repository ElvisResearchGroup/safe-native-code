package slave.process;

import slave.RemoteObject;
import slave.Slave;
import slave.exceptions.UnknownObjectException;

import java.rmi.RemoteException;

public interface Process extends Slave {

    <T> T get(RemoteObject<T> obj) throws RemoteException, UnknownObjectException;

    void remove(RemoteObject obj) throws RemoteException, UnknownObjectException;
}