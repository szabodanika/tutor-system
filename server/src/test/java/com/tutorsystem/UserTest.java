package com.tutorsystem;

import com.tutorsystem.controller.MainController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(MainController.class)
public class UserTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MainController mainController;

    @Test
    public void register() {
//        RequestBuilder = MockMvcRequestBuilders.post(
//                "/api/register"
//        ).requestAttr("firstName")
//        User user = new User();
    }
}
