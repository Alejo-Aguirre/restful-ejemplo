package co.edu.uniquindio.ingesis.security;

import java.security.Principal;
import jakarta.ws.rs.core.SecurityContext;

public class AuthSecurityContext implements SecurityContext {
    private final String userEmail;
    private final boolean isSecure;

    public AuthSecurityContext(String userEmail, boolean isSecure) {
        this.userEmail = userEmail;
        this.isSecure = isSecure;
    }

    @Override
    public Principal getUserPrincipal() {
        return () -> userEmail; // Retorna el usuario autenticado
    }

    @Override
    public boolean isUserInRole(String role) {
        return false; // Aquí podrías manejar roles si los tienes
    }

    @Override
    public boolean isSecure() {
        return isSecure;
    }

    @Override
    public String getAuthenticationScheme() {
        return "Bearer";
    }
}
