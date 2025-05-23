package com.yzgeneration.evc.docs.delivery;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.docs.RestDocsSupport;
import com.yzgeneration.evc.domain.delivery.controller.DeliveryController;
import com.yzgeneration.evc.domain.delivery.model.DeliveryView;
import com.yzgeneration.evc.domain.delivery.service.DeliveryService;
import com.yzgeneration.evc.fixture.DeliveryFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class DeliveryControllerDocsTest extends RestDocsSupport {

    private final DeliveryService deliveryService = mock(DeliveryService.class);

    @Override
    protected Object initController() {
        return new DeliveryController(deliveryService);
    }

    @Test
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
                .andExpect(jsonPath("$.receipt.status").value("COMPLETED"))
                .andDo(document("delivery-order",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("itemType").type(JsonFieldType.STRING)
                                        .description("물품 종류 e.g. USEDITEM, AUCTIONITEM"),
                                fieldWithPath("itemId").type(JsonFieldType.NUMBER)
                                        .description("물품 아이디"),
                                fieldWithPath("buyerId").type(JsonFieldType.NUMBER)
                                        .description("구매자 아이디"),
                                fieldWithPath("sellerId").type(JsonFieldType.NUMBER)
                                        .description("판매자 아이디")
                        ),
                        responseFields(
                                fieldWithPath("requestId").type(JsonFieldType.STRING)
                                        .description("요청 아이디"),
                                fieldWithPath("partnerOrderId").type(JsonFieldType.STRING).description("연동사 주문 아이디"),
                                fieldWithPath("receipt.orderId").type(JsonFieldType.STRING).description("주문 아이디"),
                                fieldWithPath("receipt.orderType").type(JsonFieldType.STRING).description("배송 방식"),
                                fieldWithPath("receipt.priceInfo.totalPrice").type(JsonFieldType.NUMBER).description("결제 금액"),
                                fieldWithPath("receipt.priceInfo.cancelFee").type(JsonFieldType.NUMBER).description("취소 금액"),
                                fieldWithPath("receipt.status").type(JsonFieldType.STRING).description("배송 상태")
                        )));

    }

    @Test
    @DisplayName("주문을 조회한다.")
    void get_order() throws Exception {

        given(deliveryService.get(any(), any()))
                .willReturn(DeliveryFixture.createGetKakaoMobilityOrder());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/deliveries/{orderId}", "orderId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.requestId").value("requestId"))
                .andExpect(jsonPath("$.partnerOrderId").value("partnerOrderId"))
                .andExpect(jsonPath("$.pickup.location.basicAddress").value("인천광역시 부평북로 431"))
                .andExpect(jsonPath("$.pickup.location.detailAddress").value("주공 미래타운 1단지"))
                .andExpect(jsonPath("$.pickup.location.latitude").value(0.0))
                .andExpect(jsonPath("$.pickup.location.longitude").value(0.0))
                .andExpect(jsonPath("$.pickup.wishTime").value("2025-04-24T20:29:26.184+09:00"))
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
                .andExpect(jsonPath("$.receipt.histories[0].updatedAt").value("2025-04-24T20:29:26.184+09:00"))
                .andDo(document("get-delivery-order",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("orderId").description("주문 아이디")
                        ),

                        responseFields(
                                fieldWithPath("requestId").type(JsonFieldType.STRING)
                                        .description("요청 아이디"),
                                fieldWithPath("partnerOrderId").type(JsonFieldType.STRING).description("연동사 주문 아이디"),
                                fieldWithPath("receipt.orderId").type(JsonFieldType.STRING).description("주문 아이디"),
                                fieldWithPath("receipt.orderType").type(JsonFieldType.STRING).description("배송 방식"),
                                fieldWithPath("pickup.location.basicAddress").type(JsonFieldType.STRING).description("픽업 주소"),
                                fieldWithPath("pickup.location.detailAddress").type(JsonFieldType.STRING).description("픽업 상세 주소"),
                                fieldWithPath("pickup.location.latitude").type(JsonFieldType.NUMBER).description("픽업 위도"),
                                fieldWithPath("pickup.location.longitude").type(JsonFieldType.NUMBER).description("픽업 경도"),
                                fieldWithPath("pickup.wishTime").type(JsonFieldType.STRING).description("픽업 요구 시간"),
                                fieldWithPath("pickup.contact.name").type(JsonFieldType.STRING).description("배송자 이름"),
                                fieldWithPath("pickup.contact.phone").type(JsonFieldType.STRING).description("배송자 연락처"),
                                fieldWithPath("pickup.note").type(JsonFieldType.STRING).description("배송자 메모"),

                                fieldWithPath("dropoff.location.basicAddress").type(JsonFieldType.STRING).description("배송지 주소"),
                                fieldWithPath("dropoff.location.detailAddress").type(JsonFieldType.STRING).description("배송지 상세 주소"),
                                fieldWithPath("dropoff.location.latitude").type(JsonFieldType.NUMBER).description("배송지 위도"),
                                fieldWithPath("dropoff.location.longitude").type(JsonFieldType.NUMBER).description("배송지 경도"),
                                fieldWithPath("dropoff.contact.name").type(JsonFieldType.STRING).description("배송 받는이 이름"),
                                fieldWithPath("dropoff.contact.phone").type(JsonFieldType.STRING).description("배송 받는이 연락처"),
                                fieldWithPath("dropoff.note").type(JsonFieldType.STRING).description("배송 받는이 메모"),


                                fieldWithPath("receipt.priceInfo.totalPrice").type(JsonFieldType.NUMBER).description("결제 금액"),
                                fieldWithPath("receipt.priceInfo.cancelFee").type(JsonFieldType.NUMBER).description("취소 금액"),
                                fieldWithPath("receipt.status").type(JsonFieldType.STRING).description("배송 상태"),
                                fieldWithPath("receipt.histories[0].status").type(JsonFieldType.STRING).description("배송 내역 - 상태"),
                                fieldWithPath("receipt.histories[0].updatedAt").type(JsonFieldType.STRING).description("배송 내역 - 업데이트 일자")
                        )));

    }

    @Test
    @DisplayName("나의 주문들을 조회한다.")
    void get_orders() throws Exception {
        List<DeliveryView> content = List.of(DeliveryView.builder().orderId("orderId").title("title").imageName("imageName").memberId(1L).createdAt(LocalDateTime.of(2025, 5, 10, 10, 10)).build());
        SliceResponse<DeliveryView> response = new SliceResponse<>(
                new SliceImpl<>(content, PageRequest.of(0,10), true), LocalDateTime.MIN);

        given(deliveryService.findList(any(), any()))
                .willReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/deliveries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("cursor", LocalDateTime.MIN.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.content[0].orderId").value("orderId"))
                .andExpect(jsonPath("$.content[0].title").value("title"))
                .andExpect(jsonPath("$.content[0].imageName").value("imageName"))
                .andExpect(jsonPath("$.content[0].createdAt").value("2025-05-10T10:10:00"))

                .andDo(document("get-delivery-orders",
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("cursor")
                                        .optional()
                                        .description("이전 메시지 조회를 위한 커서 (ISO-8601 형식: yyyy-MM-dd'T'HH:mm:ss)")
                        ),
                        responseFields(
                                fieldWithPath("content[].orderId").type(JsonFieldType.STRING)
                                        .description("주문 아이디"),
                                fieldWithPath("content[].title").type(JsonFieldType.STRING)
                                        .description("상품 제목"),
                                fieldWithPath("content[].imageName").type(JsonFieldType.STRING)
                                        .description("상대방 프로필 이미지 이름"),
                                fieldWithPath("content[].memberId").type(JsonFieldType.NUMBER)
                                        .description("상대방 아이디"),
                                fieldWithPath("content[].createdAt").type(JsonFieldType.STRING)
                                        .description("생성 시간"),
                                fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN)
                                        .description("다음 페이지 존재 여부"),
                                fieldWithPath("size").type(JsonFieldType.NUMBER)
                                        .description("한 페이지당 요소 개수"),
                                fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER)
                                        .description("현재 페이지의 요소 개수"),
                                fieldWithPath("cursor").type(JsonFieldType.STRING)
                                        .description("다음 페이지를 위한 커서 값")
                        )));

    }


}
