package co.netguru.android.inbbbox.model.api;

import com.google.gson.annotations.SerializedName;

public class Token {

    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("token_type")
    private String tokenType;
    @SerializedName("scope")
    private String scope;

    public Token(String accessToken, String tokenType, String scope) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.scope = scope;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        return accessToken + " " + scope + " " + tokenType;
    }
}