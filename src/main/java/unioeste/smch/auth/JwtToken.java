package unioeste.smch.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JwtToken {

    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @JsonProperty
    public String getToken() {
        return token;
    }

}
