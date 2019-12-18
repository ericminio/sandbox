package ericminio;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static ericminio.ListMatcher.equalList;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class ArgumentCaptorTest {

    @Test
    public void canCaptureOneCall() {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Service service = new Service();
        service.domain = mock(Domain.class);
        service.please("do-this");
        verify(service.domain).handle(captor.capture());
        String value = captor.getValue();

        assertThat(value, equalTo("do-this"));
    }

    @Test
    public void canCaptureSeveralCalls() {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Service service = new Service();
        service.domain = mock(Domain.class);
        service.hey("do-this");
        verify(service.domain, times(2)).handle(captor.capture());
        List<String> values = captor.getAllValues();

        assertThat(values, equalList(asList("wake-up", "do-this")));
    }
}
