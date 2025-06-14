package distributed;

import distributed.server.MultiThreadedFileServer;
import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.URL;
import java.nio.channels.ClosedByInterruptException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MultiThreadedFileServerTest extends TestCase {
    private int port;
    private static final String rootDirName = "static";
    private static final File rootDir = new File(rootDirName);
    private static final Random rand = new Random();

    private static final Map<String, String> files = new HashMap<String, String>();

    private static int getNCores() {
        return Runtime.getRuntime().availableProcessors();
    }

    private static String getRandomFileContents(final int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(Integer.toString(rand.nextInt(10)));
        }
        return sb.toString();
    }

    static {
        files.put("/static/A.txt", getRandomFileContents(5));
        files.put("/static/B.txt", getRandomFileContents(10));
        files.put("/static/dir1/C.txt", getRandomFileContents(10));
        files.put("/static/dir3/dir4/E.txt", getRandomFileContents(10));
        files.put("/static/ABC.txt", getRandomFileContents(2048));
        files.put("/static/large.txt", getRandomFileContents(1048576));
    }

    private PCDPFilesystem getFilesystem() {
        PCDPFilesystem fs = new PCDPFilesystem();

        for (Map.Entry<String, String> entry : files.entrySet()) {
            PCDPPath path = new PCDPPath(entry.getKey());
            fs.addFile(path, entry.getValue());
        }

        return fs;
    }

    private HttpServer launchServer() throws IOException {
        System.err.println("\nLaunching server for " +
                Thread.currentThread().getStackTrace()[2].getMethodName());
        port = ThreadLocalRandom.current().nextInt(3000, 9000);

        final ServerSocket socket = new ServerSocket(port);
        socket.setReuseAddress(true);
        Thread thread = getThread(socket);

        return new HttpServer(thread, socket);
    }

    private Thread getThread(ServerSocket socket) {
        final PCDPFilesystem fs = getFilesystem();

        Runnable runner = () -> {
            try {
                MultiThreadedFileServer server = new MultiThreadedFileServer(getNCores());
                server.run(socket, fs);
            } catch (SocketException | ClosedByInterruptException s) {
                // Do nothing, assume killed by main thread
            } catch (IOException io) {
                throw new RuntimeException(io);
            }
        };

        Thread thread = new Thread(runner);

        thread.start();
        return thread;
    }

    private HttpResponse sendHttpRequest(final String path, final boolean print)
            throws IOException {
        assert !path.startsWith("/");

        if (print) {
            System.err.print("Requesting " + path + "... ");
        }

        URL obj = new URL("http://localhost:" + port + "/" + path);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(5000); // 5 seconds
        con.setReadTimeout(5000); // 5 seconds

        final int responseCode = con.getResponseCode();

        final String responseStr;
        if (responseCode != 404) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            responseStr = response.toString();
        } else {
            responseStr = "";
        }

        if (print) {
            System.err.println("reponse code is " + responseCode +
                    ", with content length " + responseStr.length());
        }

        return new HttpResponse(responseCode, responseStr);
    }


    public void testTermination() throws IOException, InterruptedException {
        final HttpServer server = launchServer();

        // Termination
        server.socket.close();
        server.thread.interrupt();
        server.thread.join();
    }

    public void testFileGet() throws IOException, InterruptedException {
        final HttpServer server = launchServer();

        HttpResponse response = sendHttpRequest(rootDirName + "/A.txt", true);
        assertEquals(200, response.code);
        assertEquals(response.body, files.get("/static/A.txt"));

        // Termination
        server.socket.close();
        server.thread.interrupt();
        server.thread.join();
    }

    public void testFileGets() throws IOException, InterruptedException {
        final HttpServer server = launchServer();

        HttpResponse response = sendHttpRequest(rootDirName + "/A.txt", true);
        assertEquals(200, response.code);
        assertEquals(response.body, files.get("/static/A.txt"));

        response = sendHttpRequest(rootDirName + "/B.txt", true);
        assertEquals(200, response.code);
        assertEquals(response.body, files.get("/static/B.txt"));

        // Termination
        server.socket.close();
        server.thread.interrupt();
        server.thread.join();
    }

    public void testNestedFileGet() throws IOException, InterruptedException {
        final HttpServer server = launchServer();

        HttpResponse response = sendHttpRequest(rootDirName + "/dir1/C.txt", true);
        assertEquals(200, response.code);
        assertEquals(response.body, files.get("/static/dir1/C.txt"));

        // Termination
        server.socket.close();
        server.thread.interrupt();
        server.thread.join();
    }

    public void testDoublyNestedFileGet() throws IOException, InterruptedException {
        final HttpServer server = launchServer();

        HttpResponse response = sendHttpRequest(rootDirName + "/dir3/dir4/E.txt", true);
        assertEquals(200, response.code);
        assertEquals(response.body, files.get("/static/dir3/dir4/E.txt"));

        // Termination
        server.socket.close();
        server.thread.interrupt();
        server.thread.join();
    }

    public void testLargeFileGet() throws IOException, InterruptedException {
        final HttpServer server = launchServer();

        HttpResponse response = sendHttpRequest(rootDirName + "/ABC.txt", true);
        assertEquals(200, response.code);
        assertEquals(response.body, files.get("/static/ABC.txt"));

        // Termination
        server.socket.close();
        server.thread.interrupt();
        server.thread.join();
    }

    public void testMissingFileGet() throws IOException, InterruptedException {
        final HttpServer server = launchServer();

        HttpResponse response = sendHttpRequest(rootDirName + "/missing.txt", true);
        assertEquals(404, response.code);

        // Termination
        server.socket.close();
        server.thread.interrupt();
        server.thread.join();
    }

    public void testMissingNestedFileGet() throws IOException, InterruptedException {
        final HttpServer server = launchServer();

        HttpResponse response = sendHttpRequest(rootDirName + "/missingdir/missing.txt", true);
        assertEquals(404, response.code);

        // Termination
        server.socket.close();
        server.thread.interrupt();
        server.thread.join();
    }

    private int runPerformanceTest(final int nDriverThreads)
            throws IOException, InterruptedException {
        final HttpServer server = launchServer();

        Thread[] driverThreads = new Thread[nDriverThreads];
        RequestDriver[] drivers = new RequestDriver[driverThreads.length];

        for (int i = 0; i < drivers.length; i++) {
            drivers[i] = new RequestDriver();
            drivers[i].addRequest(rootDirName + "/large.txt",
                    files.get("/static/large.txt"));
            driverThreads[i] = new Thread(drivers[i]);
        }

        for (Thread driverThread : driverThreads) {
            driverThread.start();
        }

        Thread.sleep(10000);

        server.socket.close();
        server.thread.interrupt();
        server.thread.join();

        int completedRequests = 0;
        for (RequestDriver driver : drivers) {
            completedRequests += driver.getNRequests();
        }
        return completedRequests;
    }

    public void testPerformance() throws IOException, InterruptedException {
        int nDriverThreads = getNCores();

        System.err.println("Testing performance of multi-threaded web server " +
                "using " + nDriverThreads + " request threads");
        final int seqRequests = runPerformanceTest(1);
        final int parallelRequests = runPerformanceTest(nDriverThreads);

        final double improvement = (double)parallelRequests /
            (double)seqRequests;

        System.err.println("Single-core execution completed " + seqRequests);
        System.err.println("Parallel execution completed " +
                parallelRequests + ", yielding an improvement of " +
                improvement + "x");
        System.err.println();

        /*
         * Expect some parallel improvement, though we don't expect it to scale
         * perfectly with threads.
         */
        final double expected;
        if (nDriverThreads == 2) {
            expected = 1.0;
        } else if (nDriverThreads == 4) {
            expected = 2.0;
        } else {
            expected = 0.3 * nDriverThreads;
        }
        final String msg = "Expected parallel threads to produce at least a " +
            expected + "x improvement, but only saw " + improvement + "x";
        assertTrue(msg, improvement > expected);
    }

    static class HttpResponse {
        public final int code;
        public final String body;

        public HttpResponse(final int setCode, final String setBody) {
            code = setCode;
            body = setBody;
        }
    }

    static class HttpServer {
        public final Thread thread;
        public final ServerSocket socket;

        HttpServer(final Thread setThread, final ServerSocket setSocket) {
            thread = setThread;
            socket = setSocket;
        }
    }

    class RequestDriver implements Runnable {
        private final Map<String, String> requests =
            new HashMap<String, String>();
        private int nRequests = 0;

        public void addRequest(final String filename, final String body) {
            requests.put(filename, body);
        }

        public int getNRequests() {
            return nRequests;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    for (Map.Entry<String, String> r : requests.entrySet()) {
                        HttpResponse response = sendHttpRequest(r.getKey(),
                                false);
                        assertEquals(response.body, r.getValue());
                    }

                    nRequests++;
                }
            } catch (SocketException s) {
                // Is fine, server shut down
            } catch (IOException io) {
                throw new RuntimeException(io);
            }
        }
    }
}
