package ericminio;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class SocketTest {

    private int port = 5001;
    private ServerSocket server;
    private Thread thread;

    @Before
    public void startServer() throws IOException {
        server = new ServerSocket(port);
    }

    @After
    public void stopServer() throws IOException, InterruptedException {
        server.close();
        assertThat(thread.isAlive(), equalTo(false));
    }

    @Test(timeout = 200)
    public void canReceiveDataFromServer() throws IOException {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = server.accept();
                    PrintStream output = new PrintStream(socket.getOutputStream());
                    output.write(42);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        Socket socket = new Socket("localhost", port);
        DataInputStream input = new DataInputStream(socket.getInputStream());
        int data = input.read();

        assertThat(data, equalTo(42));
    }
}
