package com.yzgeneration.evc.delivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yzgeneration.evc.domain.delivery.controller.AddressController;
import com.yzgeneration.evc.domain.delivery.service.AddressService;
import com.yzgeneration.evc.fixture.DeliveryFixture;
import com.yzgeneration.evc.mock.WithFakeUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.filter.OncePerRequestFilter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = AddressController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = OncePerRequestFilter.class),
        })
class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AddressService addressService;

    @Test
    @WithFakeUser
    @DisplayName("위, 경도를 찾는다.")
    void coordinate() throws Exception {
        given(addressService.searchCoordinate(any()))
                .willReturn(DeliveryFixture.createSearchCoordinateResponse());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/addresses/coordinate")
                .content(objectMapper.writeValueAsString(DeliveryFixture.createSearchCoordinate("address")))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.data[0].address_name").value("address_name"))
                .andExpect(jsonPath("$.data[0].x").value("x"))
                .andExpect(jsonPath("$.data[0].y").value("y"));

    }

    @Test
    @WithFakeUser
    @DisplayName("주소를 저장한다.")
    void create() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/addresses")
                        .content(objectMapper.writeValueAsString(DeliveryFixture.createAddressCreate("basicAddress", "detailAddress", 0.0, 0.0)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.success").value(true));

    }

}
