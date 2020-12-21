package ericminio.testing;

import ericminio.http.GetRequest;

public class WaitStrategyUsingGetEndpoint implements WaitStrategy {

    public static final int SLEEP = 100;
    private String endpoint;

    public WaitStrategyUsingGetEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    private String getUrl(int port) {
        return "http://localhost:" + port + endpoint;
    }

    @Override
    public void waitForStarted(int port) {
        debug("Waiting for server start using " + getUrl(port));
        boolean ready = false;
        while (! ready ) {
            try {
                GetRequest.get(getUrl(port));
                debug("Server started");
                ready = true;
            }
            catch (Exception e) {
                debug("Still waiting...");
                try {
                    Thread.sleep(SLEEP);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }
    }

    @Override
    public void waitForStopped(int port) {
        debug("Waiting for server stop using " + getUrl(port));
        boolean stopped = false;
        while (! stopped ) {
            try {
                GetRequest.get(getUrl(port));
                debug("Still waiting...");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
            catch (Exception e) {
                stopped = true;
                debug("Server stopped");
            }
        }
    }

    private void debug(String s) {
        System.out.println(s);
    }
}
