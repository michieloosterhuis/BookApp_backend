package nl.oosterhuis.bookapp.controller;

import nl.oosterhuis.bookapp.filter.JwtRequestFilter;
import nl.oosterhuis.bookapp.service.BookService;
import nl.oosterhuis.bookapp.service.CustomUserDetailsService;
import nl.oosterhuis.bookapp.service.TransactionService;
import nl.oosterhuis.bookapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomUserDetailsService customUserDetailsService;

    @MockBean
    JwtRequestFilter jwtRequestFilter;

    @MockBean
    UserService userService;

    @MockBean
    BookService bookService;

    @MockBean
    TransactionService transactionService;

    @MockBean
    FileController fileController;

    @Test
    void ShouldReturnStatusOk() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/users/michiel")
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(requestBuilder).andDo(print()).andExpect(status().isOk());
    }
}