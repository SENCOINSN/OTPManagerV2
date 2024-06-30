package io.github.SENCOINSN.model;

public record OTPResponse(
        String service,
        String codeOTP,
        String typeCanal
) {
}
