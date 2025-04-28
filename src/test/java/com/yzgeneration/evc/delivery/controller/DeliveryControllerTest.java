package com.yzgeneration.evc.delivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yzgeneration.evc.domain.delivery.controller.DeliveryController;
import com.yzgeneration.evc.domain.delivery.service.DeliveryService;
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

@WebMvcTest(controllers = DeliveryController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = OncePerRequestFilter.class),
        })
class DeliveryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DeliveryService deliveryService;

    @Test
    @WithFakeUser
    @DisplayName("주문한다.")
    void order() throws Exception {

        given(deliveryService.order(any(), any(), any(), any(), any()))
                .willReturn(DeliveryFixture.createKakaoMobilityOrderResponse("orderId"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/deliveries")
                        .content(objectMapper.writeValueAsString(DeliveryFixture.createDeliveryPrepare()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.requestId").value("requestId"))
                .andExpect(jsonPath("$.partnerOrderId").value("partnerOrderId"))
                .andExpect(jsonPath("$.receipt.orderId").value("orderId"))
                .andExpect(jsonPath("$.receipt.orderType").value("QUICK"))
                .andExpect(jsonPath("$.receipt.priceInfo.totalPrice").value(1000))
                .andExpect(jsonPath("$.receipt.priceInfo.cancelFee").value(0))
                .andExpect(jsonPath("$.receipt.status").value("COMPLETED"));

    }

    @Test
    @WithFakeUser
    @DisplayName("주문을 조회한다.")
    void get_order() throws Exception {

        given(deliveryService.get(any(), any()))
                .willReturn(DeliveryFixture.createGetKakaoMobilityOrder());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/deliveries/{orderId}", "orderId")
                        .content(objectMapper.writeValueAsString(DeliveryFixture.createDeliveryPrepare()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.requestId").value("requestId"))
                .andExpect(jsonPath("$.partnerOrderId").value("partnerOrderId"))
                .andExpect(jsonPath("$.pickup.location.basicAddress").value("인천광역시 부평북로 431"))
                .andExpect(jsonPath("$.pickup.location.detailAddress").value("주공 미래타운 1단지"))
                .andExpect(jsonPath("$.pickup.location.latitude").value(0.0))
                .andExpect(jsonPath("$.pickup.location.longitude").value(0.0))
                .andExpect(jsonPath("$.pickup.contact.name").value("김보내"))
                .andExpect(jsonPath("$.pickup.contact.phone").value("01012345678"))
                .andExpect(jsonPath("$.pickup.note").value("잘 보내주세요"))
                .andExpect(jsonPath("$.dropoff.location.basicAddress").value("인천광역시 부평북로 432"))
                .andExpect(jsonPath("$.dropoff.location.detailAddress").value("주공 미래타운 2단지"))
                .andExpect(jsonPath("$.dropoff.location.latitude").value(0.0))
                .andExpect(jsonPath("$.dropoff.location.longitude").value(0.0))
                .andExpect(jsonPath("$.dropoff.contact.name").value("김받아"))
                .andExpect(jsonPath("$.dropoff.contact.phone").value("01033337778"))
                .andExpect(jsonPath("$.dropoff.note").value("문 앞에 놓아주세요"))
                .andExpect(jsonPath("$.receipt.orderId").value("OS782T"))
                .andExpect(jsonPath("$.receipt.orderType").value("QUICK"))
                .andExpect(jsonPath("$.receipt.priceInfo.totalPrice").value(1000))
                .andExpect(jsonPath("$.receipt.priceInfo.cancelFee").value(0))
                .andExpect(jsonPath("$.receipt.status").value("MATCHING_FAILED"))
                .andExpect(jsonPath("$.receipt.histories[0].status").value("MATCHING_FAILED"))
                .andExpect(jsonPath("$.receipt.histories[0].updatedAt").value("2025-04-24T20:29:26.184+09:00"));


    }
}
