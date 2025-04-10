package com.yzgeneration.evc.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yzgeneration.evc.domain.member.controller.MemberProfileController;
import com.yzgeneration.evc.domain.member.dto.ProfileResponse;
import com.yzgeneration.evc.domain.member.dto.UpdateProfileResponse;
import com.yzgeneration.evc.domain.member.service.MemberProfileService;

import com.yzgeneration.evc.fixture.MemberFixture;
import com.yzgeneration.evc.mock.WithFakeUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.filter.OncePerRequestFilter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MemberProfileController.class,
        excludeFilters = {
            @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = OncePerRequestFilter.class),
        })
class MemberProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MemberProfileService memberProfileService;

    @Test
    @WithFakeUser
    @DisplayName("프로필을 조회한다.")
    void get() throws Exception {
        given(memberProfileService.get(any()))
                .willReturn(new ProfileResponse("imageName", "imageUrl", "nickname", 1000, true));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/members/profile/me")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.imageName").value("imageName"))
                .andExpect(jsonPath("$.imageUrl").value("imageUrl"))
                .andExpect(jsonPath("$.nickname").value("nickname"))
                .andExpect(jsonPath("$.point").value(1000))
                .andExpect(jsonPath("$.isSocialProfileVisible").value(true));

    }

    @Test
    @WithFakeUser
    @DisplayName("프로필을 업데이트한다..")
    void update() throws Exception {
        given(memberProfileService.update(any(), any()))
                .willReturn(new UpdateProfileResponse("imageName", "imageUrl", "nickname", 1000, true));
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/members/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(MemberFixture.fixtureUpdateProfileRequest("imageName", "imageUrl")))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.imageName").value("imageName"))
                .andExpect(jsonPath("$.imageUrl").value("imageUrl"))
                .andExpect(jsonPath("$.nickname").value("nickname"))
                .andExpect(jsonPath("$.point").value(1000))
                .andExpect(jsonPath("$.isSocialProfileVisible").value(true));

    }

}