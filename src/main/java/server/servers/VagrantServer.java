package server.servers;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class VagrantServer extends AbstractServer {
    private Path temp = Files.createTempDirectory("safeNativeCode-VM-");

    /**
     * Create a slave that runs inside a vagrant virtual machine
     *
     * @param useAgent     true to use a java agent to capture all classes, false to pass in classloaders below
     * @param classLoaders a list of classloaders to supply classes to the slave, if useAgent is false
     */
    public VagrantServer(boolean useAgent, ClassLoader... classLoaders) throws IOException, InterruptedException {
        super(useAgent, true, classLoaders);
        //Create a vagrant config based on a template
        String vagrantConfig = String.join("\n", IOUtils.readLines(VagrantServer.class.getResourceAsStream("/Vagrantfile"), "utf-8"));
        vagrantConfig = vagrantConfig.replaceAll("<registryPort>", registryPort + "");
        vagrantConfig = vagrantConfig.replaceAll("<slavePort>", slavePort + "");
        vagrantConfig = vagrantConfig.replaceAll("<source>", getJar().getAbsolutePath());
        vagrantConfig = vagrantConfig.replaceAll("<javaCommand>", String.join(" ", getJavaCommandArgs("java", false, true)));
        Files.write(temp.resolve("Vagrantfile"), vagrantConfig.getBytes());
        new ProcessBuilder("vagrant", "up").directory(temp.toFile()).inheritIO().start();
        setupRegistry();
    }

    public boolean isAlive() throws IOException, InterruptedException {
        //If a cleanup has already happened, its safe to assume the process is dead.
        if (!temp.toFile().exists()) return false;
        Process process = new ProcessBuilder("vagrant", "status").directory(temp.toFile()).start();
        process.waitFor();
        return String.join("\n", IOUtils.readLines(process.getInputStream(), "utf-8")).contains("running");
    }

    @Override
    public void terminate() {
        //If a cleanup has already happened, its safe to assume the process is dead.
        if (!temp.toFile().exists()) return;
        try {
            new ProcessBuilder("vagrant", "destroy", "-f").directory(temp.toFile()).start().waitFor();
            FileUtils.deleteDirectory(temp.toFile());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void waitForExit() throws InterruptedException, IOException {
        //There isn't really a nice way to do this....
        while (isAlive()) {
            Thread.sleep(100);
        }
    }
}