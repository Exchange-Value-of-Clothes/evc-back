package com.yzgeneration.evc.point.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yzgeneration.evc.domain.point.controller.PointChargeController;
import com.yzgeneration.evc.domain.point.service.PointChargeService;
import com.yzgeneration.evc.fixture.PointChargeFixture;
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
import org.springframework.web.filter.OncePerRequestFilter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PointChargeController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = OncePerRequestFilter.class),
        })
class PointChargeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PointChargeService pointChargeService;

    @Test
    @WithFakeUser
    @DisplayName("결제 주문을 생성할 수 있다.")
    void createOrder() throws Exception {

        given(pointChargeService.createOrder(any()))
                .willReturn(PointChargeFixture.createPointCharge("orderId", 1L, 5000));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/point")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(PointChargeFixture.pointChargeOrderRequest()))
                        .with(csrf()))
                .andExpect(jsonPath("$.orderId").value("orderId"))
                .andExpect(jsonPath("$.price").value(5000));
    }

    @Test
    @WithFakeUser
    @DisplayName("결제를 승인할 수 있다.")
    void confirm() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/point/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(PointChargeFixture.pointChargeConfirmRequest("orderId", "paymentKey", 5000)))
                        .with(csrf()))
                .andExpect(status().isOk());
    }
}
