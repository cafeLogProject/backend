package cafeLogProject.cafeLog.common.auth.oauth2.provider;

public interface OAuth2UserResponse {

    String getProvider();
    String getProviderId();
    String getEmail();
}
