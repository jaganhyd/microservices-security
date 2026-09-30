package com.example.gatewayserver.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {


    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {

        Map<String, Object> realmAccess = (Map<String, Object>) source.getClaims().get("realm_access");
        if (realmAccess == null || realmAccess.isEmpty()) {
            return new ArrayList<>();
        }

        Collection<GrantedAuthority> returnValue = ((List<String>) realmAccess
                .get("roles")).stream()
                .map(roleName -> "ROLE_" + roleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return returnValue;
    }
}


/* JWT TOKEN
{
        "exp": 1726071397,
        "iat": 1726071337,
        "jti": "2a305164-f75e-455c-a8db-56ed02a038e2",
        "iss": "http://localhost:7080/realms/master",
        "aud": "account",
        "sub": "1c177baf-2e0f-4877-9094-fadf0530865f",
        "typ": "Bearer",
        "azp": "jbmicroservice-callcenter-cc",
        "acr": "1",
        "allowed-origins": [
        "/*"
        ],
        "realm_access": {
            "roles": [
                "default-roles-master",
                "ACCOUNTS",
                "offline_access",
                "uma_authorization"
            ]
        },
        "resource_access": {
            "account": {
                "roles": [
                        "manage-account",
                        "manage-account-links",
                        "view-profile"
                    ]
            }
        },
        "scope": "openid email profile",
        "email_verified": false,
        "clientHost": "172.17.0.1",
        "preferred_username": "service-account-jbmicroservice-callcenter-cc",
        "clientAddress": "172.17.0.1",
        "client_id": "jbmicroservice-callcenter-cc"
}*/
