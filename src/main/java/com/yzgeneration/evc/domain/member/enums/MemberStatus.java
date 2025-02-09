package com.yzgeneration.evc.domain.member.enums;

import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum MemberStatus {
    PENDING {
        @Override
        public void checkStatus() {
            throw new CustomException(ErrorCode.INACTIVE_MEMBER);
        }
    }, ACTIVE {
        @Override
        public void checkStatus() {

        }
    }, DELETED {
        @Override
        public void checkStatus() {
            throw new CustomException(ErrorCode.INACTIVE_MEMBER);
        }
    }, BAN {
        @Override
        public void checkStatus() {
            throw new CustomException(ErrorCode.INACTIVE_MEMBER);
        }
    };

    public abstract void checkStatus();
}
