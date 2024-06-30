package io.github.SENCOINSN;

import io.github.SENCOINSN.exception.OTPException;
import io.github.SENCOINSN.model.Duration;
import io.github.SENCOINSN.model.TypeCanal;
import io.github.SENCOINSN.model.TypeOTP;
import io.github.SENCOINSN.service.OTPManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Adama SEYE
 */
@ExtendWith(MockitoExtension.class)
class OTPManagerTest {

    @InjectMocks
    private OTPManager otpManager;

    @Test
    void should_generate_otp(){
        String result = otpManager.generateCodeOtp("NUMBER","SECONDS",1,4);
        assertNotNull(result);
        assertEquals(4,result.length());
    }

    @Test
    void should_generate_otp_v2(){
        Map<String,String> result = otpManager.generateCodeOtpV2(TypeOTP.NUMBER, Duration.SECONDS,1,4,"user-service", TypeCanal.EMAIl);
        assertNotNull(result);
        assertNotNull(result.get("trace"));
        assertEquals(4,result.get("code").length());

    }

    @Test
    void should_generate_otp_v2_service_Null(){
        Map<String,String> result = otpManager.generateCodeOtpV2(TypeOTP.NUMBER, Duration.SECONDS,1,4,"", TypeCanal.EMAIl);
        assertNotNull(result);
        assertNotNull(result.get("trace"));
        assertEquals(4,result.get("code").length());

    }

    @Test
    void should_verify_otp_v2() throws OTPException {
        Map<String,String> map = otpManager.generateCodeOtpV2(TypeOTP.NUMBER, Duration.SECONDS,1,4,"user-service", TypeCanal.EMAIl);
        boolean result = otpManager.verifyCode(map.get("code"));
        assertTrue(result);

    }

    //verify code
    @Test
    void should_verify_code() throws OTPException {
        String code = otpManager.generateCodeOtp("NUMBER","SECONDS",1,4);
        boolean result = otpManager.verifyCode(code);
        assertTrue(result);
    }

    @Test
    void should_hasExpiration(){
        boolean expiration = hasNoExpiration(1695727225263L);
        assertFalse(expiration);
    }

    private boolean hasNoExpiration(long time){
        return ((new Date()).getTime() - time) < 0L;
    }


}
