package io.github.SENCOINSN.service;


import io.github.SENCOINSN.exception.OTPException;
import io.github.SENCOINSN.model.Duration;
import io.github.SENCOINSN.model.OTP;
import io.github.SENCOINSN.model.TypeCanal;
import io.github.SENCOINSN.model.TypeOTP;
import io.github.SENCOINSN.model.*;
import io.github.SENCOINSN.utils.OtpGeneration;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Adama SEYE
 */


 public class OTPManager implements OTPConfiguration {
   public static Map<String, OTP> otpStore=new HashMap<>();
    /**
     * generation du code selon different critere
     * NUMERIC,ALPHANUMERIC OR ALPHABET
     * Duration
     * Longueur de la chaine
     * Génerer le code et le sauvegarder dans un map
     * @param  type String : type d'OTP NUMBER,ALPHABET,ALPHA_NUMERIC
     * @param typeDuration String : MINUTE,SECONDS,HOUR
     * @param duration long : valeur de durée de validité
     * @param len int : longueur du code généré
     */

    @Override
    public String generateCodeOtp(String type, String typeDuration, long duration, int len) {
        OTP otp = new OTP();
        /*otp.setTypeOTP(TypeOTP.valueOf(type));
        String code = OtpGeneration.generateCode(type,len);
        otp.setAlreadyValidated(false);
        long timer = 0;
        if(StringUtils.equalsIgnoreCase(Duration.MINUTE.name(),typeDuration)){
             timer = (new Date()).getTime() + (duration * 60000);
        }
        if(StringUtils.equalsIgnoreCase(Duration.HOUR.name(),typeDuration)){
            timer = (new Date()).getTime() + (duration * 60*60*1000);
        }
        if(StringUtils.equalsIgnoreCase(Duration.SECONDS.name(),typeDuration)){
            timer = (new Date()).getTime() + (duration*1000);
        }
        otp.setDurationValidity(timer);*/
        String code = OtpGeneration.generateCode(type,len);
        setUpOTPInfos(otp,TypeOTP.valueOf(type),Duration.valueOf(typeDuration),duration,len,null,null);
        otpStore.put(code,otp);
        return code;
    }

     /**
      * vérifier le code OTP
      * @param code
      * @return boolean
      */

    @Override
    public boolean verifyCode(String code) throws OTPException {
        if(otpStore.containsKey(code)){
            OTP otp = otpStore.get(code);
            if(hasNoExpiration(otp.getDurationValidity())){
                removeOtpOnMap(code);
                return true;
            }else{
                throw new OTPException("OTP is not valid or expired !");
            }
        }
        return false;
    }

    /**
     * generation du code selon different critere
     * NUMERIC,ALPHANUMERIC OR ALPHABET
     * Duration
     * Longueur de la chaine
     * Génerer le code et le sauvegarder dans un map
     * @param  typeOTP TypeOTP : type d'OTP NUMBER,ALPHABET,ALPHA_NUMERIC
     * @param duration Duration : MINUTE,SECONDS,HOUR
     * @param time long : valeur de durée de validité
     * @param len int : longueur du code généré
     * @Param serviceName String: nom du service appelant
     * @Param typeCanal TypeCanal: le canal de communication appelant l'OTP
     */
    @Override
    public Map<String, String> generateCodeOtpV2(TypeOTP typeOTP, Duration duration, long time, int len, String serviceName, TypeCanal typeCanal) {
        OTP otp = new OTP();
        setUpOTPInfos(otp,typeOTP,duration,time,len,serviceName,typeCanal);
        String code = OtpGeneration.generateCode(typeOTP.name(),len);
        String trace = "OTP generated with Code :  "+ code+ " for service :" + serviceName + " and TypeCanal "+ typeCanal.name();
        /**
         * cette map sert de tracabilité de la generation otp sur le canal
         et le service executant l'otp
         */
        Map<String, String> storeTmp = new HashMap<>();

        storeTmp.put("code",code);
        storeTmp.put("trace",trace);
        otpStore.put(code,otp);

        return storeTmp;
    }

    private void setUpOTPInfos(OTP otp,TypeOTP typeOTP, Duration duration, long time, int len, String serviceName, TypeCanal typeCanal){
        otp.setTypeOTP(typeOTP);
        otp.setAlreadyValidated(false);
        long timer = 0;
        if(StringUtils.equalsIgnoreCase(Duration.MINUTE.name(),duration.name())){
            timer = (new Date()).getTime() + (time * 60000);
        }
        if(StringUtils.equalsIgnoreCase(Duration.HOUR.name(),duration.name())){
            timer = (new Date()).getTime() + (time * 60*60*1000);
        }
        if(StringUtils.equalsIgnoreCase(Duration.SECONDS.name(),duration.name())){
            timer = (new Date()).getTime() + (time*1000);
        }
        otp.setDurationValidity(timer);

        if(serviceName !=null && !StringUtils.isBlank(serviceName)){
            otp.setServiceName(serviceName);
        }
        if(typeCanal !=null){
            otp.setTypeCanal(typeCanal);
        }

    }
    @Override
    public OTPResponse generateCodeOtp(TypeOTP typeOTP, Duration duration, long time, int len, String serviceName, TypeCanal typeCanal) {
        OTP otp = new OTP();
        setUpOTPInfos(otp,typeOTP,duration,time,len,serviceName,typeCanal);
        String code = OtpGeneration.generateCode(typeOTP.name(),len);
        OTPResponse otpResponse = new OTPResponse(
                serviceName,
                code,
                typeCanal.name()
        );
        otpStore.put(code,otp);
        return otpResponse;
    }

    @Override
    public boolean verifyCodeAndService(String code, String service) throws OTPException {
       if(otpStore.containsKey(code)){
           OTP otp = otpStore.get(code);
           assert service != null;
           if(hasNoExpiration(otp.getDurationValidity()) && service.equals(otp.getServiceName())){
               removeOtpOnMap(code);
               return true;
           }else{
               throw new OTPException("OTP is not valid or expired or service calling not matches !");
           }
       }
       return false;
    }

    @Override
    public boolean verifyCodeAndServiceAndCanal(String code, String service, TypeCanal typeCanal) throws OTPException {
        if(otpStore.containsKey(code)){
            OTP otp = otpStore.get(code);
            assert service != null;
            if(hasNoExpiration(otp.getDurationValidity()) &&
                    service.equals(otp.getServiceName())
            && otp.getTypeCanal().equals(typeCanal)){
                removeOtpOnMap(code);
                return true;
            }else{
                throw new OTPException("OTP is not valid or expired or service call not matche or canal not matches !");
            }
        }
        return false;
    }


    /**
      * Supprimer le code présent au niveau du map
      * cette méthode est invoquée après validatation de l'otp
      * @param code
      */
    private void removeOtpOnMap(String code){
        otpStore.remove(code);
    }

     /**
      * vérifier si la durée de validité est expirée
      * @param time
      * @return
      */
    private boolean hasNoExpiration(long time){
        return ((new Date()).getTime() - time) < 0L;
    }
}
