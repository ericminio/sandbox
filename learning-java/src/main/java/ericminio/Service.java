package ericminio;

public class Service {

    public Domain domain;

    public void please(String request) {
        domain.handle(request);
    }

    public void hey(String request) {
        domain.handle("wake-up");
        domain.handle(request);
    }
}
