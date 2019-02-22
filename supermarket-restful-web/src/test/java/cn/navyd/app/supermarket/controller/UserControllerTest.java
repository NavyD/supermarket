package cn.navyd.app.supermarket.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import cn.navyd.app.supermarket.BaseTest;


public class UserControllerTest extends BaseTest {

    @Autowired
    private MockMvc mvc;

    @DisplayName("getAllUser 正常测试")
    @Test
    public void getAllUsersByPageTest() throws Exception {
        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.data.data").isArray())
                .andExpect(jsonPath("$.data.data[14]").isNotEmpty())
                .andExpect(jsonPath("$.data.data[0].username").isString())
                .andExpect(jsonPath("$.data.pageNumber").value(0))
                .andExpect(jsonPath("$.data.pageSize").value(15));

    }
    
    @DisplayName("getAllUser 参数正常测试")
    @Test
    void getAllUsersByPageTestWithParam() throws Exception {
        int pageNumber = 0, pageSize = 5;
        mvc.perform(get("/users")
                    .param("page_num", "" + pageNumber)
                    .param("page_size", "" + pageSize)
                    .param("last_id", "0")
                    )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.error").isEmpty())
            .andExpect(jsonPath("$.data.data").isArray())
            .andExpect(jsonPath("$.data.pageNumber").value(pageNumber))
            .andExpect(jsonPath("$.data.pageSize").value(pageSize));
    }
    
    @DisplayName("getAllUser 参数异常测试")
    @Test
    void getAllUsersByPageTestWithInvalidParam() throws Exception {
        int pageNumber = 110, pageSize = 1115;
        mvc.perform(get("/users")
                    .param("page_num", "" + pageNumber)
                    .param("page_size", "" + pageSize)
                    )
            .andExpect(status().is(422))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.data").isEmpty())
            .andExpect(jsonPath("$.error").isNotEmpty());
    }
    
    @DisplayName("getUser 正常测试")
    @Test
    void getUserByIdTest() throws Exception {
        int userId = 1;
        mvc.perform(get("/users/" + userId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.error").isEmpty())
            .andExpect(jsonPath("$.data").isNotEmpty());
    }
    
    @DisplayName("getUser 路径参数异常测试")
    @Test
    void getUserByIdTestWithInvalidParam() throws Exception {
        int userId = -1;
        mvc.perform(get("/users/" + userId))
            .andExpect(status().is(422))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.error").isNotEmpty())
            .andExpect(jsonPath("$.data").isEmpty());
        
        userId = Integer.MAX_VALUE;
        mvc.perform(get("/users/" + userId))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.error").isNotEmpty())
            .andExpect(jsonPath("$.data").isEmpty());
    }
}
