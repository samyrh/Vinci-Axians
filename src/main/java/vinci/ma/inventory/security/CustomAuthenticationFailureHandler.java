package vinci.ma.inventory.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        // Add parameters to the redirect URL for error messages
        Map<String, String> errorParams = new HashMap<>();
        if (exception.getMessage().contains("Username")) {
            errorParams.put("usernameError", "Invalid username.");
        }
        if (exception.getMessage().contains("Password")) {
            errorParams.put("passwordError", "Invalid password.");
        }
        errorParams.put("error", "Invalid username or password.");

        StringBuilder redirectUrl = new StringBuilder("/login?");
        errorParams.forEach((key, value) -> redirectUrl.append(key).append("=").append(value).append("&"));

        // Remove the trailing "&" if it exists
        String finalUrl = redirectUrl.toString().replaceAll("&$", "");
        response.sendRedirect(finalUrl);
    }
}
