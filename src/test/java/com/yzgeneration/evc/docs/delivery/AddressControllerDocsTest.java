package com.yzgeneration.evc.docs.delivery;

import com.yzgeneration.evc.docs.RestDocsSupport;
import com.yzgeneration.evc.domain.delivery.controller.AddressController;
import com.yzgeneration.evc.domain.delivery.service.AddressService;
import com.yzgeneration.evc.fixture.DeliveryFixture;
import com.yzgeneration.evc.mock.WithFakeUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class AddressControllerDocsTest extends RestDocsSupport {

    private final AddressService addressService = mock(AddressService.class);

    @Override
    protected Object initController() {
        return new AddressController(addressService);
    }

    @Test
    @DisplayName("위, 경도를 찾는다.")
    void coordinate() throws Exception {
        given(addressService.searchCoordinate(any()))
                .willReturn(DeliveryFixture.createSearchCoordinateResponse());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/addresses/coordinate")
                        .content(objectMapper.writeValueAsString(DeliveryFixture.createSearchCoordinate("address")))
                        .contentType(MediaType.APPLICATION_JSON)
                        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.data[0].address_name").value("address_name"))
                .andExpect(jsonPath("$.data[0].x").value("x"))
                .andExpect(jsonPath("$.data[0].y").value("y"))
                .andDo(document("address-coordinate",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("addressName").type(JsonFieldType.STRING)
                                        .description("주소 명")
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.ARRAY)
                                        .description("주소 정보 리스트"),
                                fieldWithPath("data[].address_name").type(JsonFieldType.STRING).description("주소 명"),
                                fieldWithPath("data[].x").type(JsonFieldType.STRING).description("경도"),
                                fieldWithPath("data[].y").type(JsonFieldType.STRING).description("위도")
                        )));

    }

    @Test
    @DisplayName("주소를 저장한다.")
    void create() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/addresses")
                        .content(objectMapper.writeValueAsString(DeliveryFixture.createAddressCreate("basicAddress", "detailAddress", 0.0, 0.0)))
                        .contentType(MediaType.APPLICATION_JSON)
                        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(document("address-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("basicAddress").type(JsonFieldType.STRING).description("기본 주소"),
                                fieldWithPath("detailAddress").type(JsonFieldType.STRING).description("상세 주소"),
                                fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("위도"),
                                fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("경도")
                        ),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부")
                        )));

    }
}
