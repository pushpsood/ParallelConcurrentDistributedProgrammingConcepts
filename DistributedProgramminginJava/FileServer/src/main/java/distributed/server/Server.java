package distributed.server;

import distributed.PCDPFilesystem;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Abstract class representing a server that handles requests using a filesystem.
 * Subclasses must implement the `run` method to define server behavior.
 */
abstract class Server {

    /**
     * Runs the server using the provided socket and filesystem.
     * This method must be implemented by subclasses to handle incoming requests.
     *
     * @param socket The `ServerSocket` used to accept client connections.
     * @param fs The `PCDPFilesystem` used to retrieve file contents.
     * @throws IOException If an I/O error occurs while handling requests.
     */
    public abstract void run(final ServerSocket socket, final PCDPFilesystem fs) throws IOException;
}