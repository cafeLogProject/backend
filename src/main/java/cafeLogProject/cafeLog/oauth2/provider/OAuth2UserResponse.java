package cafeLogProject.cafeLog.oauth2.provider;

public interface OAuth2UserResponse {

    String getProvider();
    String getProviderId();
    String getEmail();
}
