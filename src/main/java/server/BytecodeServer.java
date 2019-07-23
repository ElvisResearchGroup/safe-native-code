package server;

import sun.security.util.IOUtils;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.HashMap;

import static spark.Spark.get;
import static spark.Spark.port;

/**
 * Start a web server hosting classes from a specific set of classloaders
 */
public class BytecodeServer implements ClassFileTransformer {
    /**
     * This HashMap stores all class files that are encountered
     */
    private static HashMap<String, byte[]> classFiles = new HashMap<>();
    public static void agentmain(String args, Instrumentation instrumentation) {
        String[] splitArgs = args.split(" ");
        //Add a transformer that simply stores all classes encountered to classFiles
        instrumentation.addTransformer(new BytecodeServer());
        port(Integer.parseInt(splitArgs[0]));
        //Start a webserver that serves any class from classFiles
        get("/*", (req, res) -> {
            if (classFiles.containsKey(req.pathInfo())) {
                return classFiles.get(req.pathInfo());
            }
            //Anything not detected was loaded before we initialized RemoteCodeManager. This should just be static classes however.
            String path = req.pathInfo().substring(1);
            return IOUtils.readFully(ClassLoader.getSystemClassLoader().getResourceAsStream(path),Integer.MAX_VALUE, false);
        });
    }


    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        //store this class file's content in classFiles, and structure its name so it is easily retrieved by rmi
        classFiles.put("/" + className + ".class", classfileBuffer);
        return classfileBuffer;
    }
}