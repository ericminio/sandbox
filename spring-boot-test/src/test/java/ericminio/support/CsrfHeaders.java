package ericminio.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CsrfHeaders {

    @Autowired
    BasicHeaders basic;

    @Autowired
    CsrfTokenRepository csrfTokenRepository;

    public Map<String, String> headers() {
        Map<String, String> values = basic.headers();
        CsrfToken csrfToken = csrfTokenRepository.generateToken(null);
        values.put(csrfToken.getHeaderName(), csrfToken.getToken());
        values.put("Cookie", "XSRF-TOKEN=" + csrfToken.getToken());

        return values;
    }
}
