package softuni.bg.finalPJ.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import softuni.bg.finalPJ.models.DTOs.MessageDTO;
import softuni.bg.finalPJ.models.entities.UserEntity;
import softuni.bg.finalPJ.service.MessageService;
import softuni.bg.finalPJ.service.UserService;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @MockBean
    private UserService userService;

    private UserEntity testUser;
    private MessageDTO testMessageDTO;

    @BeforeEach
    void setup() {
        testUser = new UserEntity();
        testUser.setId(1L);
        testUser.setEmail("john.doe@example.com");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setPassword("password");

        testMessageDTO = new MessageDTO();
        testMessageDTO.setSenderName("testName");
        testMessageDTO.setSenderEmail("test@mail");
        testMessageDTO.setSenderPhone("0899999999");
        testMessageDTO.setContent("test");


        when(userService.findById(1L)).thenReturn(testUser);
        when(userService.findUserByEmail("john.doe@example.com")).thenReturn(testUser);
    }

    @Test
    @WithMockUser(username = "john.doe@example.com")
    public void testShowContactForm() throws Exception {

        mockMvc.perform(get("/profile/1/contactForm"))
                .andExpect(status().isOk())
                .andExpect(view().name("contactForm"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", testUser));
    }

    @Test
    @WithMockUser(username = "john.doe@example.com", roles = {"USER"})
    public void testSubmitContactForm() throws Exception {

        doNothing().when(messageService).saveMessage(any(MessageDTO.class), anyLong());

        mockMvc.perform(post("/profile/1/submitContactForm")
                        .flashAttr("messageDTO", testMessageDTO)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile/1"));
    }

}

