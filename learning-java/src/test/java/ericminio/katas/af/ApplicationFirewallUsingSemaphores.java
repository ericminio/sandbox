package ericminio.katas.af;

import java.util.concurrent.ConcurrentHashMap;

public class ApplicationFirewallUsingSemaphores implements ApplicationFirewall {

    private AfConfiguration configuration;
    private ConcurrentHashMap<Object, Entry> traffic;

    public ApplicationFirewallUsingSemaphores() {
        this.traffic = new ConcurrentHashMap<>();
    }

    @Override
    public void setConfiguration(AfConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public boolean isOpen(Object resource) {
        Entry entry = getEntryFor(resource);
        return entry.isOpen();
    }

    private Entry getEntryFor(Object resource) {
        Entry entry = traffic.get(resource);
        if (entry == null) {
            entry = new EntryUsingSemaphore(configuration);
            traffic.put(resource, entry);
        }
        return entry;
    }
}
