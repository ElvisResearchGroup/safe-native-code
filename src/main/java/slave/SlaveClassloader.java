package slave;

import org.apache.commons.io.IOUtils;
import shared.BytecodeLookup;

import java.io.IOException;
import java.rmi.RemoteException;
import java.security.SecureClassLoader;

/**
 * SlaveClassloader facilitates loading classes from the main process, using RMI. We retrieve a BytecodeLookup from the host, and use it to load classes.
 */
public class SlaveClassloader extends SecureClassLoader {
    private static BytecodeLookup lookup;

    public SlaveClassloader(ClassLoader parent) {
        super(parent);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        // We need to make sure the slave main and slave object are loaded using the correct classloader.
        // This is due to the fact that anything executed from the slave will use the parent's classloader.
        // And we need everything to go through this class loader, so that we can proxy calls to the main jvm when required.
        if (name.endsWith("slave.SlaveMain") || name.endsWith("slave.SlaveObject")) {
            // Essentially, we just reload these specific classes using this classloader instead of the app classloader.
            try {
                byte[] b = IOUtils.toByteArray(getResourceAsStream(name.replace(".","/")+".class"));
                return super.defineClass(name, b, 0, b.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // If we don't have a reference to the main JVM yet, it's too early to need to proxy classes through it.
        // We also can not proxy core java classes, as java specifically blocks redefining anything inside the java package.
        if (lookup == null || name.startsWith("java")) return super.loadClass(name);
        try {
            byte[] b = lookup.getByteCode(name);
            if (b == null) return super.loadClass(name);
            return super.defineClass(name, b, 0, b.length);
        } catch (RemoteException e) {
            // If we loose connection to the main JVM, just throw a class not found exception.
           throw new ClassNotFoundException(e.getMessage(), e);
        }
    }

    public static void setLookup(BytecodeLookup lookup) {
        SlaveClassloader.lookup = lookup;
    }
}
