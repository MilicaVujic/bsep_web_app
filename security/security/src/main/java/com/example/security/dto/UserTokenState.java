package com.example.security.dto;

public class UserTokenState {
    private String accesstoken;

    private String refreshtoken;
    private boolean mfaenabled;

    private String secretImageUri;
    private boolean isEmployee;

    public boolean isEmployee() {
        return isEmployee;
    }

    public void setEmployee(boolean employee) {
        isEmployee = employee;
    }

    public String getSecretImageUri() {
        return secretImageUri;
    }

    public void setSecretImageUri(String secretImageUri) {
        this.secretImageUri = secretImageUri;
    }

    public boolean isMfaenabled() {
        return mfaenabled;
    }

    public void setMfaenabled(boolean mfaenabled) {
        this.mfaenabled = mfaenabled;
    }

    public UserTokenState(){};

    public UserTokenState(String accesstoken, String refreshtoken,String secretImageUri) {
        this.accesstoken = accesstoken;
        this.refreshtoken = refreshtoken;
        this.secretImageUri=secretImageUri;
    }

    public String getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }

    public String getRefreshtoken() {
        return refreshtoken;
    }

    public void setRefreshtoken(String refreshtoken) {
        this.refreshtoken = refreshtoken;
    }

}
