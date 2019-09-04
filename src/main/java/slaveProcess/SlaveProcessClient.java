package slaveProcess;

import shared.*;
import shared.exceptions.UnknownObjectException;
import shared.exceptions.SlaveException;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * SlaveProcessClient is the Main class run by a slave
 */
public class SlaveProcessClient extends UnicastRemoteObject implements Slave {

    private transient Map<SlaveProcessObject, Object> localObjects = new HashMap<>();

    public SlaveProcessClient(int registryPort) throws IOException, InterruptedException, NotBoundException {
        Registry registry = LocateRegistry.createRegistry(registryPort);

        while (!Arrays.asList(registry.list()).contains("bytecodeLookup")) {
            Thread.sleep(10);
        }
        Retriever r = (Retriever) registry.lookup("bytecodeLookup");
        SlaveProcessClassloader.setLookup(r);
        registry.rebind("slaveProcess", this);
    }

    @Override
    public void run(SerializableRunnable lambda) {
        try {
            lambda.run();
        } catch (UnknownObjectException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new SlaveException(wrap(ex));
        }
    }

    @Override
    public <T> RemoteObject<T> call(SerializableSupplier<T> lambda) {
        try {
            return wrap(lambda.get());
        } catch (UnknownObjectException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new SlaveException(wrap(ex));
        }
    }

    @Override
    public <T> void call(RemoteObject<T> obj, SerializableConsumer<T> lambda) throws RemoteException {
        try {
            lambda.accept(get(obj));
        } catch (UnknownObjectException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new SlaveException(wrap(ex));
        }
    }

    @Override
    public <R, T> RemoteObject<R> call(RemoteObject<T> t, One<R, T> lambda) throws RemoteException {
        try {
            return wrap(lambda.accept(get(t)));
        } catch (UnknownObjectException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new SlaveException(wrap(ex));
        }
    }

    @Override
    public <R, T1, T2> RemoteObject<R> call(RemoteObject<T1> t1, RemoteObject<T2> t2, Two<R, T1, T2> lambda) throws RemoteException {
        try {
            return wrap(lambda.accept(get(t1), get(t2)));
        } catch (UnknownObjectException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new SlaveException(wrap(ex));
        }
    }

    @Override
    public <R, T1, T2, T3> RemoteObject<R> call(RemoteObject<T1> t1, RemoteObject<T2> t2, RemoteObject<T3> t3, Three<R, T1, T2, T3> lambda) throws RemoteException {
        try {
            return wrap(lambda.accept(get(t1), get(t2), get(t3)));
        } catch (UnknownObjectException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new SlaveException(wrap(ex));
        }
    }

    @Override
    public <R, T1, T2, T3, T4> RemoteObject<R> call(RemoteObject<T1> t1, RemoteObject<T2> t2, RemoteObject<T3> t3, RemoteObject<T4> t4, Four<R, T1, T2, T3, T4> lambda) throws RemoteException {

        try {
            return wrap(lambda.accept(get(t1), get(t2), get(t3), get(t4)));
        } catch (UnknownObjectException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new SlaveException(wrap(ex));
        }
    }

    @Override
    public <R, T1, T2, T3, T4, T5> RemoteObject<R> call(RemoteObject<T1> t1, RemoteObject<T2> t2, RemoteObject<T3> t3, RemoteObject<T4> t4, RemoteObject<T5> t5, Five<R, T1, T2, T3, T4, T5> lambda) throws RemoteException {

        try {
            return wrap(lambda.accept(get(t1), get(t2), get(t3), get(t4), get(t5)));
        } catch (UnknownObjectException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new SlaveException(wrap(ex));
        }
    }

    @Override
    public <R, T1, T2, T3, T4, T5, T6> RemoteObject<R> call(RemoteObject<T1> t1, RemoteObject<T2> t2, RemoteObject<T3> t3, RemoteObject<T4> t4, RemoteObject<T5> t5, RemoteObject<T6> t6, Six<R, T1, T2, T3, T4, T5, T6> lambda) throws RemoteException {

        try {
            return wrap(lambda.accept(get(t1), get(t2), get(t3), get(t4), get(t5), get(t6)));
        } catch (UnknownObjectException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new SlaveException(wrap(ex));
        }
    }

    @Override
    public <R, T1, T2, T3, T4, T5, T6, T7> RemoteObject<R> call(RemoteObject<T1> t1, RemoteObject<T2> t2, RemoteObject<T3> t3, RemoteObject<T4> t4, RemoteObject<T5> t5, RemoteObject<T6> t6, RemoteObject<T7> t7, Seven<R, T1, T2, T3, T4, T5, T6, T7> lambda) throws RemoteException {

        try {
            return wrap(lambda.accept(get(t1), get(t2), get(t3), get(t4), get(t5), get(t6), get(t7)));
        } catch (UnknownObjectException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new SlaveException(wrap(ex));
        }
    }

    @Override
    public <R, T1, T2, T3, T4, T5, T6, T7, T8> RemoteObject<R> call(RemoteObject<T1> t1, RemoteObject<T2> t2, RemoteObject<T3> t3, RemoteObject<T4> t4, RemoteObject<T5> t5, RemoteObject<T6> t6, RemoteObject<T7> t7, RemoteObject<T8> t8, Eight<R, T1, T2, T3, T4, T5, T6, T7, T8> lambda) throws RemoteException {

        try {
            return wrap(lambda.accept(get(t1), get(t2), get(t3), get(t4), get(t5), get(t6), get(t7), get(t8)));
        } catch (UnknownObjectException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new SlaveException(wrap(ex));
        }
    }

    @Override
    public <R, T1, T2, T3, T4, T5, T6, T7, T8, T9> RemoteObject<R> call(RemoteObject<T1> t1, RemoteObject<T2> t2, RemoteObject<T3> t3, RemoteObject<T4> t4, RemoteObject<T5> t5, RemoteObject<T6> t6, RemoteObject<T7> t7, RemoteObject<T8> t8, RemoteObject<T9> t9, Nine<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> lambda) throws RemoteException {

        try {
            return wrap(lambda.accept(get(t1), get(t2), get(t3), get(t4), get(t5), get(t6), get(t7), get(t8), get(t9)));
        } catch (UnknownObjectException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new SlaveException(wrap(ex));
        }
    }

    @Override
    public <R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> RemoteObject<R> call(RemoteObject<T1> t1, RemoteObject<T2> t2, RemoteObject<T3> t3, RemoteObject<T4> t4, RemoteObject<T5> t5, RemoteObject<T6> t6, RemoteObject<T7> t7, RemoteObject<T8> t8, RemoteObject<T9> t9, RemoteObject<T10> t10, Ten<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> lambda) throws RemoteException {

        try {
            return wrap(lambda.accept(get(t1), get(t2), get(t3), get(t4), get(t5), get(t6), get(t7), get(t8), get(t9), get(t10)));
        } catch (UnknownObjectException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new SlaveException(wrap(ex));
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(RemoteObject<T> obj) throws UnknownObjectException {
        if (!(obj instanceof SlaveProcessObject) || !localObjects.containsKey(obj)) {
            throw new UnknownObjectException();
        }
        return (T) localObjects.get(obj);
    }

    public void remove(RemoteObject obj) throws UnknownObjectException {
        if (!(obj instanceof SlaveProcessObject) || !localObjects.containsKey(obj)) {
            throw new UnknownObjectException();
        }
        localObjects.remove(obj);
    }

    @Override
    public <T> RemoteObject<T> copy(RemoteObject<T> object) throws RemoteException {
        return wrap(object.get());
    }

    private <T> RemoteObject<T> wrap(T object) {
        SlaveProcessObject<T> r = new SlaveProcessObject<>(this);
        localObjects.put(r, object);
        return r;
    }
}
