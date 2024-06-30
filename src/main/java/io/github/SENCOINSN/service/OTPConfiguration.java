package io.github.SENCOINSN.service;

import io.github.SENCOINSN.exception.OTPException;
import io.github.SENCOINSN.model.Duration;
import io.github.SENCOINSN.model.OTPResponse;
import io.github.SENCOINSN.model.TypeCanal;
import io.github.SENCOINSN.model.TypeOTP;

import java.util.Map;

/**
 * @author Adama SEYE :
 * Interface utilisée pour la génération du code OTP
 * et la vérification du code OTP
 */

public interface OTPConfiguration {
    String generateCodeOtp(String type, String typeDuration, long duration, int len);
    boolean verifyCode(String code) throws OTPException;

    Map<String,String> generateCodeOtpV2(TypeOTP typeOTP, Duration duration, long time, int len,
                                         String serviceName, TypeCanal typeCanal);

    OTPResponse generateCodeOtp(TypeOTP typeOTP, Duration duration, long time, int len,
                                  String serviceName, TypeCanal typeCanal);

    boolean verifyCodeAndService(String code,String service) throws OTPException;

    boolean verifyCodeAndServiceAndCanal(String code,String service,TypeCanal typeCanal) throws OTPException;

}
