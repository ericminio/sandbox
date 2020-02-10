package ericminio.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.stereotype.Component;

@Component
public class Csrf {

    @Autowired
    CsrfTokenRepository csrfTokenRepository;

    public HttpHeaders headers() {
        CsrfToken csrfToken = csrfTokenRepository.generateToken(null);
        HttpHeaders headers = new HttpHeaders();

        headers.add(csrfToken.getHeaderName(), csrfToken.getToken());
        headers.add("Cookie", "XSRF-TOKEN=" + csrfToken.getToken());

        return headers;
    }
}
