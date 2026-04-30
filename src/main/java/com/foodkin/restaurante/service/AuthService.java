package com.foodkin.restaurante.service;

import com.foodkin.restaurante.dto.AuthRequestDTO;
import com.foodkin.restaurante.dto.AuthResponseDTO;
import com.foodkin.restaurante.exception.EmailAlreadyExist;
import com.foodkin.restaurante.exception.InvalidCredentialsException;
import com.foodkin.restaurante.model.User;
import com.foodkin.restaurante.repository.UserRepository;
import com.foodkin.restaurante.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.CredentialException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponseDTO register(AuthRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExist(dto.getEmail());
        }
        User user = new User();
        user.setName((dto.getName()));
        user.setEmail(dto.getEmail());
        user.setPassword((passwordEncoder.encode(dto.getPassword())));
        user.setRol("USER");

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponseDTO(token, user.getName(), user.getEmail(), user.getRol());
    }

    public AuthResponseDTO login(AuthRequestDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponseDTO(token, user.getName(),dto.getEmail(), user.getRol());
    }
}
