package com.foodkin.restaurante.service;

import com.foodkin.restaurante.dto.AuthRequestDTO;
import com.foodkin.restaurante.dto.AuthResponseDTO;
import com.foodkin.restaurante.exception.EmailAlreadyExist;
import com.foodkin.restaurante.exception.InvalidCredentialsException;
import com.foodkin.restaurante.model.User;
import com.foodkin.restaurante.repository.UserRepository;
import com.foodkin.restaurante.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private AuthService authService;

    private AuthRequestDTO dto;
    private User user;

    @BeforeEach
    void setUp() {
        dto = new AuthRequestDTO();
        dto.setName("Juan");
        dto.setEmail("juan@gmail.com");
        dto.setPassword("123456");

        user = new User();
        user.setId("1");
        user.setName("Juan");
        user.setEmail("juan@gmail.com");
        user.setPassword("hashedPassword");
        user.setRol("USER");
    }

    @Test
    void register_withNewEmail_shouldReturnToken() {
        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtUtil.generateToken(anyString())).thenReturn("token.jwt.mock");

        AuthResponseDTO response = authService.register(dto);

        assertThat(response.getToken()).isEqualTo("token.jwt.mock");
        assertThat(response.getEmail()).isEqualTo("juan@gmail.com");
        assertThat(response.getRol()).isEqualTo("USER");
    }

    @Test
    void register_withExistingEmail_shouldThrowException() {
        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(dto))
                .isInstanceOf(EmailAlreadyExist.class)
                .hasMessageContaining("juan@gmail.com");

    }

    @Test
    void login_withCorrectCredentials_shouldReturnToken() {
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(anyString())).thenReturn("token.jwt.mock");

        AuthResponseDTO response = authService.login(dto);

        assertThat(response.getToken()).isEqualTo("token.jwt.mock");
        assertThat(response.getEmail()).isEqualTo("juan@gmail.com");
    }

    @Test
    void login_withExistingEmail_shouldThrowException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(dto))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void login_withIncorrectPassword_shouldThrowException() {
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.getPassword(), user.getPassword())).thenReturn(false);
        assertThatThrownBy(() -> authService.login(dto))
                .isInstanceOf(InvalidCredentialsException.class);
    }
}
