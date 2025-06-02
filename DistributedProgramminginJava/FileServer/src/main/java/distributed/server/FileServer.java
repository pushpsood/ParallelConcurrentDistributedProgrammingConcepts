package distributed.server;

import distributed.PCDPFilesystem;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static distributed.util.RequestUtil.getPrintWriter;
import static distributed.util.RequestUtil.readRequest;

/**
 * A basic and very limited implementation of a file server that responds to GET
 * requests from HTTP clients.
 */
public final class FileServer extends Server{
    /**
     * {@inheritDoc}
     */
    public void run(final ServerSocket socket, final PCDPFilesystem fs)
            throws IOException {
        /*
         * Enter a spin loop for handling client requests to the provided
         * ServerSocket object.
         */
        while (true) {
            Socket s = socket.accept();
            String fileContent = readRequest(fs, s);
            PrintWriter pw = getPrintWriter(s, fileContent);
            pw.close();
        }
    }
}