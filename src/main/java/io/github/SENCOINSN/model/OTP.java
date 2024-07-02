package io.github.SENCOINSN.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Adama SEYE :
 * classe de génération et de validation de l'otp généré avec les différentes regles de validité
 */

public class OTP {
    private long durationValidity;
    private boolean alreadyValidated;
    private TypeOTP typeOTP;
    private Duration duration;
    private String serviceName;
    private TypeCanal typeCanal;

    public OTP() {
    }

    public OTP(long durationValidity, boolean alreadyValidated, TypeOTP typeOTP, Duration duration, String serviceName, TypeCanal typeCanal) {
        this.durationValidity = durationValidity;
        this.alreadyValidated = alreadyValidated;
        this.typeOTP = typeOTP;
        this.duration = duration;
        this.serviceName = serviceName;
        this.typeCanal = typeCanal;
    }


    @Override
    public String toString() {
        return "OTP{" +
                "durationValidity=" + durationValidity +
                ", alreadyValidated=" + alreadyValidated +
                ", typeOTP=" + typeOTP +
                ", duration=" + duration +
                ", serviceName='" + serviceName + '\'' +
                ", typeCanal=" + typeCanal +
                '}';
    }

    public long getDurationValidity() {
        return durationValidity;
    }

    public void setDurationValidity(long durationValidity) {
        this.durationValidity = durationValidity;
    }

    public boolean isAlreadyValidated() {
        return alreadyValidated;
    }

    public void setAlreadyValidated(boolean alreadyValidated) {
        this.alreadyValidated = alreadyValidated;
    }

    public TypeOTP getTypeOTP() {
        return typeOTP;
    }

    public void setTypeOTP(TypeOTP typeOTP) {
        this.typeOTP = typeOTP;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public TypeCanal getTypeCanal() {
        return typeCanal;
    }

    public void setTypeCanal(TypeCanal typeCanal) {
        this.typeCanal = typeCanal;
    }
}
