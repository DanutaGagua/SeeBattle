

/*
 * Game Server class
 *
 * realisation is as simple as can
* */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server extends Thread {
    private int port;

    AtomicBoolean works = new AtomicBoolean(true);

    public Server(int port)
    {
        this.port = port;
    }

    public void endWork()
    {
        works.set(false);
    }

    @Override
    public void run()
    {
        Executor threadsPool = Executors.newCachedThreadPool();

        try {
            ServerSocket serverSocket = new ServerSocket(port);

            while (works.get()) {
                // wait for connection
                Socket socket = serverSocket.accept();

                // create a worker to handle session
                threadsPool.execute(
                        new WorkerRunnable(socket)
                );
            }

            serverSocket.close();
            ((ExecutorService) threadsPool).shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
