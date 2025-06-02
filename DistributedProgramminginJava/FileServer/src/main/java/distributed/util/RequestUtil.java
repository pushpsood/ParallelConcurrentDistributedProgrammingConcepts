package distributed.util;

import distributed.PCDPFilesystem;
import distributed.PCDPPath;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Utility class for handling HTTP requests and responses in a file server.
 */
public class RequestUtil {

    /**
     * Reads an HTTP GET request from the provided socket, extracts the requested file path,
     * and retrieves the file content from the given filesystem.
     *
     * @param fs The filesystem to read the file from.
     * @param s The socket from which the HTTP request is read.
     * @return The content of the requested file, or null if the file does not exist.
     * @throws IOException If an I/O error occurs while reading the request.
     */
    public static String readRequest(final PCDPFilesystem fs, final Socket s) throws IOException {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(s.getInputStream()));

        // Read the first line of the HTTP request
        String line = br.readLine();
        assert line != null; // Ensure the request is not null
        assert line.startsWith("GET"); // Ensure the request is a GET request

        // Extract the file path from the request
        final String path = line.split(" ")[1];

        // Convert the path to a PCDPPath and read the file content
        PCDPPath pcdpPath = new PCDPPath(path);
        return fs.readFile(pcdpPath);
    }

    /**
     * Creates a PrintWriter for sending an HTTP response to the client through the provided socket.
     * The response includes headers and the file content if the file exists, or a 404 error if it does not.
     *
     * @param s The socket to send the HTTP response through.
     * @param fileContent The content of the file to include in the response, or null if the file does not exist.
     * @return A PrintWriter object for the response.
     * @throws IOException If an I/O error occurs while creating the PrintWriter.
     */
    public static java.io.PrintWriter getPrintWriter(Socket s, String fileContent) throws IOException {
        OutputStream out = s.getOutputStream();
        java.io.PrintWriter pw = new java.io.PrintWriter(out);

        if (fileContent != null) {
            // Send a 200 OK response with the file content
            pw.write("HTTP/1.0 200 OK\r\n");
            pw.write("Server: FileServer\r\n");
            pw.write("\r\n");
            pw.write(fileContent + "\r\n");
        } else {
            // Send a 404 Not Found response
            pw.write("HTTP/1.0 404 Not Found\r\n");
            pw.write("Server: FileServer\r\n");
            pw.write("\r\n");
        }
        return pw;
    }
}