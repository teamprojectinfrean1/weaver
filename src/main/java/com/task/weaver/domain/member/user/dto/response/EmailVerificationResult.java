package com.task.weaver.domain.member.user.dto.response;

public class EmailVerificationResult {
    private final boolean verificationStatus;

    private EmailVerificationResult(boolean verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public static EmailVerificationResult of(boolean verificationStatus) {
        return new EmailVerificationResult(verificationStatus);
    }

    public boolean isVerificationSuccessful() {
        return verificationStatus;
    }
}
