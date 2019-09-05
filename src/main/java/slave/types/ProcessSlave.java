package slave.types;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A ProcessSlave runs a slave in another process on the same machine that the host process is executed on.
 */
public class ProcessSlave extends AbstractSlave {
    private Process process;

    /**
     * Create a slave that runs inside another process
     *
     * @param classLoaders a list of classloaders to supply classes to the slave
     */
    public ProcessSlave(ClassLoader... classLoaders) throws IOException, InterruptedException {
        super(classLoaders);
        Path javaProcess = Paths.get(System.getProperty("java.home"), "bin", "java");
        process = new ProcessBuilder(getJavaCommandArgs(javaProcess.toString(), "")).inheritIO().start();
        //End the remoteSlave process if the parent process ends.
        Runtime.getRuntime().addShutdownHook(new Thread(process::destroy));
        setupRegistry();
    }

    @Override
    public void terminate() {
        try {
            process.destroyForcibly().waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void waitForExit() throws InterruptedException {
        process.waitFor();
    }

    @Override
    public boolean isAlive() {
        return process.isAlive();
    }
}
