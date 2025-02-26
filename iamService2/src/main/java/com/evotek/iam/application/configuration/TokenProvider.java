package com.evotek.iam.application.configuration;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.encrypt.KeyStoreKeyFactory;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Getter
@Component
public class TokenProvider implements InitializingBean {
    @Value("${jwt.key-store}")
    private String keyStore;
    @Value("${jwt.key-store-password}")
    private String keyStorePassword;
    @Value("${jwt.key-alias}")
    private String keyAlias;

    private KeyPair keyPair;

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            this.keyPair = keyPair(
                    keyStore,
                    keyStorePassword,
                    keyAlias
            );
        } catch (Exception e) {
            System.err.println("Lỗi tạo KeyPair: " + e.getMessage());
            throw e;
        }
    }

    private KeyPair keyPair(String keyStore, String password, String alias) {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(
                new ClassPathResource(keyStore),
                password.toCharArray()
        );
        return keyStoreKeyFactory.getKeyPair(alias);
    }

    public JWKSet jwkSet() {
        RSAKey.Builder builder = new RSAKey.Builder((RSAPublicKey) this.keyPair.getPublic()).keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS256)
                .keyID(UUID.randomUUID().toString());
        return new JWKSet(builder.build());
    }
}
