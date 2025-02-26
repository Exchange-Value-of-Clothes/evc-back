package com.yzgeneration.evc.mock;

import com.yzgeneration.evc.domain.member.model.Member;
import com.yzgeneration.evc.fixture.MemberFixture;
import com.yzgeneration.evc.security.MemberPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithFakeUserSecurityContextFactory implements WithSecurityContextFactory<WithFakeUser> {
    @Override
    public SecurityContext createSecurityContext(WithFakeUser annotation) {
        Member member = MemberFixture.withFakeUser();
        MemberPrincipal memberPrincipal = new MemberPrincipal(member);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                memberPrincipal,
                memberPrincipal.getMember().getId(),
                memberPrincipal.getAuthorities());
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);
        return context;
    }
}
