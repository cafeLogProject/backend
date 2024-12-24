package cafeLogProject.cafeLog.auth.oauth2.provider;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class FacebookUser implements OAuth2UserResponse {

    private final Map<String, Object> attributes;

    @Override
    public String getProvider() {
        return "facebook";
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }
}
