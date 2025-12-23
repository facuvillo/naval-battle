package org.batallanaval.backend.security.auth;

import lombok.RequiredArgsConstructor;
import org.batallanaval.backend.persistence.dao.UserDAO;
import org.batallanaval.backend.persistence.dao.impl.UserDAOImpl;
import org.batallanaval.backend.persistence.entity.Role;
import org.batallanaval.backend.persistence.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserDAOImpl userDAO;
    private final PasswordEncoder passwordEncoder;

    // TODO Cambiar cuando se implemente JWT
    public boolean register(RegisterRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userDAO.save(user);
        return true;
    }

    // TODO Implementar el AuthenticationManager para mejorar la seguridad
    // TODO Implementar los JWT
    public LoginResponse login(LoginRequest request) {
        User user = userDAO.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        LoginResponse loginResponse = LoginResponse.builder()
                .userId(user.getId().toString())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();

        return loginResponse;
    }

}
