package com.rh.rh_capsule.auth.service;


import com.rh.rh_capsule.auth.domain.Provider;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Getter
@Component
@ConfigurationProperties(prefix = "oauth2")
public class OAuthProviderProperties {
    private final Map<Provider, OAuthProviderProperty> provider = new EnumMap<>(Provider.class);

    public OAuthProviderProperty getProviderProperties(Provider provider) {
        return this.provider.get(provider);
    }

    @Getter
    @Setter
    public static class OAuthProviderProperty {
        private String id;
        private String secret;
        private String tokenUrl;
        private String infoUrl;
        private String loginUrl;
        private String scope;
    }
}
