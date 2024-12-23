package cafeLogProject.cafeLog.oauth2;

import cafeLogProject.cafeLog.jwt.JWTUserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {


    private final OAuth2UserDTO oAuth2UserDTO;
    private Map<String, Object> attributes;
    private final JWTUserDTO jwtUserDTO;
    public CustomOAuth2User(OAuth2UserDTO user, Map<String, Object> attributes) {
        this.oAuth2UserDTO = user;
        this.attributes = attributes;
        this.jwtUserDTO = null;
    }

    public CustomOAuth2User(JWTUserDTO jwtUserDTO) {
        this.jwtUserDTO = jwtUserDTO;
        this.oAuth2UserDTO = null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(() -> String.valueOf(oAuth2UserDTO.getRole()));
    }

    @Override
    public String getName() {
        return oAuth2UserDTO.getUsername();
    }
}
