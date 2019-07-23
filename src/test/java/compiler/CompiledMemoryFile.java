package compiler;

import javax.tools.SimpleJavaFileObject;
import java.io.*;
import java.net.URI;

class CompiledMemoryFile extends SimpleJavaFileObject {

    /**
     * Byte code created by the compiler will be stored in this
     * ByteArrayOutputStream so that we can later get the
     * byte array out of it
     * and put it in the memory as an instance of our class.
     */
    protected ByteArrayOutputStream bos =
            new ByteArrayOutputStream();

    /**
     * Registers the compiled class object under URI
     * containing the class full name
     *
     * @param name
     *            Full name of the compiled class
     * @param kind
     *            Kind of the data. It will be CLASS in our case
     */
    public CompiledMemoryFile(String name, Kind kind) {
        super(URI.create("string:///" + name.replace('.', '/')
                + kind.extension), kind);
    }
    @Override
    public String getCharContent(boolean b) {
        return new String(bos.toByteArray());
    }

    /**
     * Will be used by our file manager to get the byte code that
     * can be put into memory to instantiate our class
     *
     * @return compiled byte code
     */
    public byte[] getBytes() {
        return bos.toByteArray();
    }

    /**
     * Will provide the compiler with an output stream that leads
     * to our byte array. This way the compiler will write everything
     * into the byte array that we will instantiate later
     */
    @Override
    public OutputStream openOutputStream() throws IOException {
        return bos;
    }

    /**
     * Provices the compiler with an input stream that is read from our byte array.
     * @return The current code as an input stream
     * @throws IOException there was an error
     */
    public InputStream openInputStream() throws IOException {
        return new ByteArrayInputStream(bos.toByteArray());
    }
}