package io.github.SENCOINSN.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Adama SEYE :
 * classe de génération et de validation de l'otp généré avec les différentes regles de validité
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OTP {
    private long durationValidity;
    private boolean alreadyValidated;
    private TypeOTP typeOTP;
    private Duration duration;
    private String serviceName;
    private TypeCanal typeCanal;

}
