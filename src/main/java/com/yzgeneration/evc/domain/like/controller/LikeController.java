package com.yzgeneration.evc.domain.like.controller;

import com.yzgeneration.evc.domain.item.enums.ItemType;
import com.yzgeneration.evc.domain.like.dto.LikeResponse;
import com.yzgeneration.evc.domain.like.service.LikeService;
import com.yzgeneration.evc.security.MemberPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/{itemId}")
    public ResponseEntity<LikeResponse> like(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @PathVariable Long itemId, @RequestParam ItemType itemType) {
        return ResponseEntity.ok(likeService.toggleLike(memberPrincipal.getId(), itemId, itemType));
    }

    @GetMapping("/my")
    public void getLikes(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {

    }
}
