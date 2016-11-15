package ericminio;

import org.hamcrest.core.IsEqual;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class ServerSocketTest {

    private int port = 5001;
    private ServerSocket server;

    @Before
    public void startServer() throws IOException {
        server = new ServerSocket(port);
    }

    @After
    public void stopServer() throws IOException, InterruptedException {
        server.close();
    }

    @Test(timeout = 200)
    public void canAcceptOneConnection() throws Exception {
        CompletableFuture<Void> listening = CompletableFuture.runAsync(() -> {
            try {
                server.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        new Socket("localhost", port);

        listening.get();
    }

    @Test(timeout = 200)
    public void canAcceptTwoConnections() throws Exception {
        CompletableFuture<Void> listening = CompletableFuture.runAsync(() -> {
            int count = 0;
            try {
                do {
                    server.accept();
                    count ++;
                } while(count <2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        new Socket("localhost", port);
        new Socket("localhost", port);

        listening.get();
    }

    @Test(timeout = 200)
    public void canSendDataToTheClient() throws Exception {
        CompletableFuture<Void> listening = CompletableFuture.runAsync(() -> {
            try {
                Socket socket = server.accept();
                PrintStream output = new PrintStream(socket.getOutputStream());
                output.write(42);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Socket client = new Socket("localhost", port);
        listening.get();
        DataInputStream input = new DataInputStream(client.getInputStream());
        int data = input.read();

        assertThat(data, IsEqual.equalTo(42));
    }
}