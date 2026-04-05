package com.workintech.s18d1;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.workintech.s18d1.controller.BurgerController;
import com.workintech.s18d1.dao.BurgerDao;
import com.workintech.s18d1.entity.BreadType;
import com.workintech.s18d1.entity.Burger;
import com.workintech.s18d1.exceptions.BurgerException;
import com.workintech.s18d1.exceptions.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {BurgerController.class, GlobalExceptionHandler.class})
@ExtendWith(ResultAnalyzer.class)
@ExtendWith(ResultAnalyzer2.class)
class ApplicationPropertiesAndControllerTest {

    @Autowired
    private Environment env;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BurgerDao burgerDao;

    private Burger sampleBurger;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        sampleBurger = new Burger();
        sampleBurger.setId(1); // Integer uyumu
        sampleBurger.setName("Classic Burger");
        sampleBurger.setPrice(7.99);
        sampleBurger.setVegan(false); // Lombok boolean kuralı: setVegan
        sampleBurger.setBreadType(BreadType.BURGER);
        sampleBurger.setContents("Beef, Lettuce, Tomato, Cheese");
    }

    @Test
    @DisplayName("application properties istenilenler eklendi mi?")
    void serverPortIsSetTo9000() {
        String serverPort = env.getProperty("server.port");
        assertThat(serverPort).isEqualTo("9000");

        assertNotNull(env.getProperty("spring.datasource.url"));
        assertNotNull(env.getProperty("spring.datasource.username"));
        assertNotNull(env.getProperty("spring.datasource.password"));
        assertNotNull(env.getProperty("spring.jpa.hibernate.ddl-auto"));
        assertNotNull(env.getProperty("logging.level.org.hibernate.SQL"));
        assertNotNull(env.getProperty("logging.level.org.hibernate.jdbc.bind"));
    }

    @Test
    @DisplayName("Burger not found exception test")
    void testBurgerNotFoundException() throws Exception {
        given(burgerDao.findById(anyInt())).willThrow(new BurgerException("Burger not found", HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/workintech/burgers/{id}", 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Burger not found"));
    }

    @Test
    @DisplayName("Generic exception test")
    void testGenericException() throws Exception {
        given(burgerDao.findById(anyInt())).willThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/workintech/burgers/{id}", 2))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Unexpected error"));
    }

    @Test
    @DisplayName("Save burger test")
    void testSaveBurger() throws Exception {
        given(burgerDao.save(any())).willReturn(sampleBurger);

        mockMvc.perform(post("/workintech/burgers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleBurger)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(sampleBurger.getName())));
    }

    @Test
    @DisplayName("Find all burgers test")
    void testFindAllBurgers() throws Exception {
        List<Burger> burgers = Arrays.asList(sampleBurger);
        given(burgerDao.findAll()).willReturn(burgers);

        mockMvc.perform(get("/workintech/burgers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(sampleBurger.getName())));
    }

    @Test
    @DisplayName("Find burger by id test")
    void testGetBurgerById() throws Exception {
        given(burgerDao.findById(sampleBurger.getId())).willReturn(sampleBurger);

        mockMvc.perform(get("/workintech/burgers/{id}", sampleBurger.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(sampleBurger.getName())));
    }

    @Test
    @DisplayName("Update burger test")
    void testUpdateBurger() throws Exception {
        Burger updatedBurger = new Burger();
        updatedBurger.setId(1);
        updatedBurger.setName("Updated Classic Burger");
        updatedBurger.setPrice(9.99); // Eksik olan fiyat eklendi
        updatedBurger.setVegan(false);
        updatedBurger.setBreadType(BreadType.BURGER);
        updatedBurger.setContents("Updated Contents"); // Eksik olan içerik eklendi

        given(burgerDao.update(any())).willReturn(updatedBurger);

        mockMvc.perform(put("/workintech/burgers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBurger)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(updatedBurger.getName())));
    }

    @Test
    @DisplayName("Remove burger test")
    void testRemoveBurger() throws Exception {
        given(burgerDao.remove(sampleBurger.getId())).willReturn(sampleBurger);

        mockMvc.perform(delete("/workintech/burgers/{id}", sampleBurger.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Find by bread type test")
    void testFindByBreadType() throws Exception {
        List<Burger> burgers = Arrays.asList(sampleBurger);
        given(burgerDao.findByBreadType(any())).willReturn(burgers);

        mockMvc.perform(get("/workintech/burgers/findByBreadType")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleBurger.getBreadType())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("Find by price test")
    void testFindByPrice() throws Exception {
        List<Burger> burgers = Arrays.asList(sampleBurger);
        // Mockito'ya Double geleceğini açıkça söyleyelim
        given(burgerDao.findByPrice(any(Double.class))).willReturn(burgers);

        mockMvc.perform(get("/workintech/burgers/findByPrice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("7.99")) // Noktalı (Double) bir fiyat gönderiyoruz
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("Find by content test")
    void testFindByContent() throws Exception {
        List<Burger> burgers = Arrays.asList(sampleBurger);
        given(burgerDao.findByContent(any())).willReturn(burgers);

        mockMvc.perform(get("/workintech/burgers/findByContent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("Cheese")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}