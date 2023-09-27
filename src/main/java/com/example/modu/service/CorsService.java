package com.example.modu.service;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Slf4j(topic = "Custom CORS Service")
@Service
public class CorsService {

    private final String[] passUrls = {"http://localhost"};

    public boolean checkCors(HttpServletRequest request) {
        String path = request.getRemoteAddr();
        for (String passUrl : passUrls) {
            if (path.startsWith(passUrl)) {
                return true;
            }
        }
        return false;
    }


    public void validateUrl(HttpServletRequest request)
    {
        log.info("---> Url : " + request.getRequestURI()  + " / " + request.getMethod() + " / Protocol : " + request.getProtocol() + " / Origin : " + request.getHeader("Origin")
                + "\n ContextPath : " + request.getContextPath() + " / User : " + request.getRemoteUser() + " | ServletPath : "  + request.getServletPath() + " | Host : " + request.getRemoteHost()
                + " / PORT  Local: " + request.getLocalPort() + " / Remote : " + request.getRemotePort() + " / Server : " + request.getServerPort());

        //request.getLocalPort() / request.getHeader("Origin")
        if (false)
        {
          throw new BadCredentialsException("유효하지 않은 접근 : Header.Origin : " + request.getHeader("Origin"));
        }

    }
}
