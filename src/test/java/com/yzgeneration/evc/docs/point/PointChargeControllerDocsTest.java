package com.yzgeneration.evc.docs.point;

import com.yzgeneration.evc.docs.RestDocsSupport;
import com.yzgeneration.evc.domain.point.controller.PointChargeController;
import com.yzgeneration.evc.domain.point.service.PointChargeService;
import com.yzgeneration.evc.fixture.PointChargeFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PointChargeControllerDocsTest extends RestDocsSupport {

    private final PointChargeService pointChargeService = mock(PointChargeService.class);

    @Override
    protected Object initController() {
        return new PointChargeController(pointChargeService);
    }

    @Test
    @DisplayName("결제 주문을 생성할 수 있다.")
    void createOrder() throws Exception {

        given(pointChargeService.createOrder(any()))
                .willReturn(PointChargeFixture.createPointCharge("orderId", 1L, 5000));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/point")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(PointChargeFixture.pointChargeOrderRequest())))
                .andExpect(jsonPath("$.orderId").value("orderId"))
                .andExpect(jsonPath("$.price").value(5000))
                .andDo(document("point-order",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("price").type(JsonFieldType.NUMBER)
                                        .description("포인트 충전 금액")
                        ),
                        responseFields(
                                fieldWithPath("orderId").type(JsonFieldType.STRING).description("주문 아이디"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("주문 금액")
                        )

                ));
    }

    @Test
    @DisplayName("결제를 승인할 수 있다.")
    void confirm() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/point/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(PointChargeFixture.pointChargeConfirmRequest("orderId", "paymentKey", 5000))))
                .andExpect(status().isOk())
                .andDo(document("point-confirm",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("orderId").type(JsonFieldType.STRING).description("주문 아이디"),
                                fieldWithPath("paymentKey").type(JsonFieldType.STRING).description("결제 키"),
                                fieldWithPath("amount").type(JsonFieldType.NUMBER).description("주문 수량")
                        )
                ));
    }
}
