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
import softuni.bg.finalPJ.models.entities.UserEntity;
import softuni.bg.finalPJ.models.entities.UserRoleEntity;
import softuni.bg.finalPJ.models.enums.UserRoleEnum;
import softuni.bg.finalPJ.service.UserService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class UserSettingsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        testUser = new UserEntity();
        testUser.setId(1L);
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setCompanyName("Test Company");
        testUser.setEmail("john.doe@example.com");
        testUser.setPassword("password");

        UserRoleEntity role = new UserRoleEntity();
        role.setRole(UserRoleEnum.NORMAL);
        testUser.setRoles(Collections.singletonList(role));

        when(userService.findById(anyLong())).thenReturn(testUser);
        when(userService.findUserByEmail("john.doe@example.com")).thenReturn(testUser);
    }

    @Test
    @WithMockUser(username = "john.doe@example.com", roles = {"NORMAL"})
    public void showSettingsShouldReturnSettingsView() throws Exception {
        mockMvc.perform(get("/profile/1/settings"))
                .andExpect(status().isOk())
                .andExpect(view().name("settings"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", testUser));
    }

    @Test
    @WithMockUser(username = "john.doe@example.com", roles = {"NORMAL"})
    public void updateFirstName_ShouldRedirectToProfile() throws Exception {
        mockMvc.perform(post("/profile/1/settings/updateFirstName")
                        .param("firstName", "Jane")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile/1"));

    }

    @Test
    @WithMockUser(username = "john.doe@example.com", roles = {"USER"})
    public void updateLastName_ShouldRedirectToProfile() throws Exception {
        mockMvc.perform(post("/profile/1/settings/updateLastName")
                        .param("lastName", "Smith")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile/1"));


    }

    @Test
    @WithMockUser(username = "john.doe@example.com", roles = {"NORMAL"})
    public void updatePassword_ShouldRedirectToProfile_WhenPasswordsMatch() throws Exception {
        when(userService.checkIfNewAndCurrentPasswordMatches(testUser, "currentPassword")).thenReturn(true);
        when(userService.checkIfPasswordAndConfirmPasswordMatches("newPassword", "newPassword")).thenReturn(true);

        mockMvc.perform(post("/profile/1/settings/updatePassword")
                        .param("password", "newPassword")
                        .param("confirmPassword", "newPassword")
                        .param("currentPassword", "currentPassword")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile/1"));

    }

    @Test
    @WithMockUser(username = "john.doe@example.com", roles = {"NORMAL"})
    public void updatePassword_ShouldReturnError_WhenCurrentPasswordIsIncorrect() throws Exception {
        when(userService.checkIfNewAndCurrentPasswordMatches(testUser, "wrongCurrentPassword")).thenReturn(false);

        mockMvc.perform(post("/profile/1/settings/updatePassword")
                        .param("password", "newPassword")
                        .param("confirmPassword", "newPassword")
                        .param("currentPassword", "wrongCurrentPassword")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("settings"))
                .andExpect(model().attributeExists("currentPasswordError"));
    }

    @Test
    @WithMockUser(username = "john.doe@example.com", roles = {"USER"})
    public void updatePassword_ShouldReturnError_WhenPasswordsDoNotMatch() throws Exception {
        when(userService.checkIfNewAndCurrentPasswordMatches(testUser, "currentPassword")).thenReturn(true);
        when(userService.checkIfPasswordAndConfirmPasswordMatches("newPassword", "differentPassword")).thenReturn(false);

        mockMvc.perform(post("/profile/1/settings/updatePassword")
                        .param("password", "newPassword")
                        .param("confirmPassword", "differentPassword")
                        .param("currentPassword", "currentPassword")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("settings"))
                .andExpect(model().attributeExists("confirmPasswordError"));
    }
}