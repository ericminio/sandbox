package ericminio.katas.af;

public interface ApplicationFirewall {

    boolean isOpen(Object resource);

    void setConfiguration(AfConfiguration configuration);
}
