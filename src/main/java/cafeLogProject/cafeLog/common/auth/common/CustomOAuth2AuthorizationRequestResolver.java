package cafeLogProject.cafeLog.common.auth.common;

import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

public class CustomOAuth2AuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

    private final OAuth2AuthorizationRequestResolver defaultResolver;

    public CustomOAuth2AuthorizationRequestResolver(OAuth2AuthorizationRequestResolver defaultResolver) {
        this.defaultResolver = defaultResolver;
    }

    @Override
    @Nullable
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        OAuth2AuthorizationRequest defaultRequest = this.defaultResolver.resolve(request);
        return customizeRequestIfNeeded(request, defaultRequest);
    }

    @Override
    @Nullable
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        OAuth2AuthorizationRequest defaultRequest = this.defaultResolver.resolve(request, clientRegistrationId);
        return customizeRequestIfNeeded(request, defaultRequest);
    }

    private OAuth2AuthorizationRequest customizeRequestIfNeeded(HttpServletRequest request, OAuth2AuthorizationRequest defaultRequest) {
        if (defaultRequest == null) {
            return null;
        }

        String mockEmail = request.getParameter("mock_email");
        if (mockEmail != null) {
            Map<String, Object> extraParams = new LinkedHashMap<>(defaultRequest.getAdditionalParameters());
            extraParams.put("mock_email", mockEmail);

            return OAuth2AuthorizationRequest.from(defaultRequest)
                    .additionalParameters(extraParams)
                    .build();
        }

        return defaultRequest;
    }
}
