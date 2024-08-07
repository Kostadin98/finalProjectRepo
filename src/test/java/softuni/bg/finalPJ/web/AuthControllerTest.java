package softuni.bg.finalPJ.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import softuni.bg.finalPJ.models.DTOs.UserRegistrationDTO;
import softuni.bg.finalPJ.service.UserService;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserRegistrationDTO userRegistrationDTO;

    @BeforeEach
    public void setup() {
        userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setEmail("gosho@mail");
        userRegistrationDTO.setFirstName("Gosho");
        userRegistrationDTO.setLastName("Goshov");
        userRegistrationDTO.setPassword("password");
        userRegistrationDTO.setConfirmPassword("password");
        userRegistrationDTO.setCompanyName("TestCompany");

    }

    @Test
    public void testRegisterGet() throws Exception {
        mockMvc.perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth-register"))
                .andExpect(model().attributeExists("userRegistrationDTO"));
    }

    @Test
    public void testLoginGet() throws Exception {
        mockMvc.perform(get("/users/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth-login"));
    }

    @Test
    void testPostRegister() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users/register")
                                .param("email", userRegistrationDTO.getEmail())
                                .param("firstName", userRegistrationDTO.getFirstName())
                                .param("lastName", userRegistrationDTO.getLastName())
                                .param("password", userRegistrationDTO.getPassword())
                                .param("confirmPassword", userRegistrationDTO.getConfirmPassword())
                                .param("companyName", userRegistrationDTO.getCompanyName())
                                .with(csrf()))
                .andExpect(status().is3xxRedirection());

    }

    @Test
    public void testRegisterPost_Failure() throws Exception {
        userRegistrationDTO.setConfirmPassword("wrongpassword");

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", userRegistrationDTO.getEmail())
                        .param("firstName", userRegistrationDTO.getFirstName())
                        .param("lastName", userRegistrationDTO.getLastName())
                        .param("password", userRegistrationDTO.getPassword())
                        .param("confirmPassword", userRegistrationDTO.getConfirmPassword())
                        .param("companyName", userRegistrationDTO.getCompanyName()))
                .andExpect(status().isOk())
                .andExpect(view().name("auth-register"))
                .andExpect(model().attributeHasFieldErrors("userRegistrationDTO", "confirmPassword"));
    }

    @Test
    @WithMockUser(username = "john.doe@example.com")
    public void testLoginError() throws Exception {

        mockMvc.perform(post("/users/login-error")
                        .param("email", "john.doe@example.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth-login"))
                .andExpect(model().attributeExists("email"))
                .andExpect(model().attributeExists("bad_credentials"));
    }
}