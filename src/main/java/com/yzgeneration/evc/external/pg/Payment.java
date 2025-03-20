package com.yzgeneration.evc.external.pg;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class Payment {
    private String version;
    private String paymentKey; // 결제 식별 키
    private String type; // NORMAL(일반결제), BILLING(자동결제), BRANDPAY(브랜드페이)
    private String orderId;
    private String orderName;
    private String mId;
    private String currency;
    private String method; // 결제수단
    private int totalAmount;
    private int balanceAmount;
    private String status;
    private OffsetDateTime requestedAt;
    private OffsetDateTime approvedAt;
    private boolean useEscrow;
    private String lastTransactionKey;
    private int suppliedAmount;
    private int vat;
    private boolean cultureExpense;
    private int taxFreeAmount;
    private int taxExemptionAmount;
    private List<Cancel> cancels;
    private boolean isPartialCancelable;
    private Card card;
    private VirtualAccount virtualAccount;
    private String secret;
    private MobilePhone mobilePhone;
    private GiftCertificate giftCertificate;
    private Transfer transfer;
    private Object metadata;
    private Receipt receipt;
    private Checkout checkout;
    private EasyPay easyPay;
    private String country;
    private TossErrorV1Response failure;
    private CashReceipt cashReceipt;
    private List<CashReceipts> cashReceipts;
    private Discount discount;

    @Getter
    @NoArgsConstructor
    private static class Cancel {
        private int canceledAmount;
        private String canceledReason;
        private int taxFreeAmount;
        private int taxExemptionAmount;
        private int refundableAmount;
        private int transferDiscountAmount;
        private int easyPayDiscountAmount;
        private OffsetDateTime canceledAt;
        private String transactionKey;
        private String receiptKey;
        private String cancelStatus;
        private String cancelRequestId;
    }

    @Getter
    @NoArgsConstructor
    private static class Card {
        private int amount;
        private String issuerCode;
        private String acquirerCode;
        private String number;
        private int installmentPlanMonths;
        private String approveNo;
        private boolean useCardPoint;
        private String cardType;
        private String ownerType;
        private String acquireStatus;
        private boolean isInterestFree;
        private String interestPayer;
    }

    @Getter
    @NoArgsConstructor
    private static class VirtualAccount {
        private String accountType;
        private String accountNumber;
        private String bankCode;
        private String customerName;
        private OffsetDateTime dueDate;
        private String refundStatus;
        private boolean expired;
        private String settlementStatus;
        private RefundReceiveAccount refundReceiveAccount;

        @Getter
        @NoArgsConstructor
        private static class RefundReceiveAccount {
            private String bankCode;
            private String accountNumber;
            private String holderName;
        }
    }

    @Getter
    @NoArgsConstructor
    private static class MobilePhone {
        private String customerMobilePhone;
        private String settlementStatus;
        private String receiptUrl;
    }

    @Getter
    @NoArgsConstructor
    private static class GiftCertificate {
        private String approveNo;
        private String settlementStatus;
    }

    @Getter
    @NoArgsConstructor
    private static class Transfer {
        private String bankCode;
        private String settlementStatus;
    }

    @Getter
    @NoArgsConstructor
    private static class Receipt {
        private String url;
    }

    @Getter
    @NoArgsConstructor
    private static class Checkout {
        private String url;
    }

    @Getter
    @NoArgsConstructor
    private static class EasyPay {
        private String provider;
        private int amount;
        private int discountAmount;
    }

    @Getter
    @NoArgsConstructor
    private static class CashReceipt {
        private String type;
        private String receiptKey;
        private String issueNumber;
        private int amount;
        private int taxFreeAmount;
    }

    @Getter
    @NoArgsConstructor
    private static class CashReceipts {
        private String receiptKey;
        private String orderId;
        private String orderName;
        private String type;
        private String issueNumber;
        private String receiptUrl;
        private String businessNumber;
        private String transactionType;
        private int amount;
        private int taxFreeAmount;
        private String issueStatus;
        private TossErrorV1Response failure;
        private String customerIdentityNumber;
        private OffsetDateTime requestedAt;
    }

    @Getter
    @NoArgsConstructor
    private static class Discount {
        private int amount;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "version='" + version + '\'' +
                ", paymentKey='" + paymentKey + '\'' +
                ", type='" + type + '\'' +
                ", orderId='" + orderId + '\'' +
                ", orderName='" + orderName + '\'' +
                ", mId='" + mId + '\'' +
                ", currency='" + currency + '\'' +
                ", method='" + method + '\'' +
                ", totalAmount=" + totalAmount +
                ", balanceAmount=" + balanceAmount +
                ", status='" + status + '\'' +
                ", requestedAt=" + requestedAt +
                ", approvedAt=" + approvedAt +
                ", useEscrow=" + useEscrow +
                ", lastTransactionKey='" + lastTransactionKey + '\'' +
                ", suppliedAmount=" + suppliedAmount +
                ", vat=" + vat +
                ", cultureExpense=" + cultureExpense +
                ", taxFreeAmount=" + taxFreeAmount +
                ", taxExemptionAmount=" + taxExemptionAmount +
                ", cancels=" + cancels +
                ", isPartialCancelable=" + isPartialCancelable +
                ", card=" + card +
                ", virtualAccount=" + virtualAccount +
                ", secret='" + secret + '\'' +
                ", mobilePhone=" + mobilePhone +
                ", giftCertificate=" + giftCertificate +
                ", transfer=" + transfer +
                ", metadata=" + metadata +
                ", receipt=" + receipt +
                ", checkout=" + checkout +
                ", easyPay=" + easyPay +
                ", country='" + country + '\'' +
                ", failure=" + failure +
                ", cashReceipt=" + cashReceipt +
                ", cashReceipts=" + cashReceipts +
                ", discount=" + discount +
                '}';
    }
}
