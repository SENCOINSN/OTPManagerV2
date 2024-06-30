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
        otp.setTypeOTP(TypeOTP.valueOf(type));
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
        otp.setDurationValidity(timer);
        //store this on hasmap
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
        //verifier l'ensemble des elements de vérification
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
        OTP otpv2 = new OTP();
        otpv2.setTypeOTP(typeOTP);
        String code = OtpGeneration.generateCode(typeOTP.name(),len);
        otpv2.setAlreadyValidated(false);

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
        otpv2.setDurationValidity(timer);

        if(serviceName !=null && !StringUtils.isBlank(serviceName)){
            otpv2.setServiceName(serviceName);
        }
        otpv2.setTypeCanal(typeCanal);

        String trace = "OTP generated with Code :  "+ code+ " for service :" + serviceName + " and TypeCanal "+ typeCanal.name();

        /*cette map sert de tracabilité de la generation otp sur le canal
         et le service executant l'otp*/
        Map<String, String> storeTmp = new HashMap<>();

        storeTmp.put("code",code);
        storeTmp.put("trace",trace);
        otpStore.put(code,otpv2);

        return storeTmp;
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
