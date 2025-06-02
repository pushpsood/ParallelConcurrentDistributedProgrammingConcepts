package distributed.server;

import distributed.PCDPFilesystem;
import distributed.util.RequestUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A basic and very limited implementation of a file server that responds to GET
 * requests from HTTP clients.
 */
public final class MultiThreadedFileServer extends Server{
    final int nCores;

    /**
     * Constructs a MultiThreadedFileServer.
     *
     * @param nCores The number of cores that are available to your
     *               multi-threaded file server. Using this argument is entirely
     *               optional. You are free to use this information to change
     *               how you create your threads, or ignore it.
     */

    public MultiThreadedFileServer(int nCores) {
        this.nCores = nCores;
    }

    /**
     * {@inheritDoc}
     */
    public void run(final ServerSocket socket, final PCDPFilesystem fs) throws IOException {
        /*
         * Enter a spin loop for handling client requests to the provided
         * ServerSocket object.
         */
        while (true) {

            // Listens for a connection to be made to this socket and accepts it.
            // The method blocks until a connection is made.
            Socket s = socket.accept();

            Thread thread = new Thread(() -> {
                try {
                    String fileContent = RequestUtil.readRequest(fs, s);
                    PrintWriter pw = RequestUtil.getPrintWriter(s, fileContent);
                    pw.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            thread.start();
        }
    }
}