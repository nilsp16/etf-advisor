package de.dhbwravensburg.etfadvisor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AuthControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void register_shouldReturn200WithToken() throws Exception{
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"registertest\",\"password\":\"test123\",\"role\":\"USER\"}"))
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void login_shouldReturn200WithToken() throws Exception{
        // Erst registrieren
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"loginuser\",\"password\":\"test123\",\"role\":\"USER\"}"));

        // Dann einloggen
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"loginuser\",\"password\":\"test123\"}"))
                .andExpect(jsonPath("$.token").exists());
    }
}
