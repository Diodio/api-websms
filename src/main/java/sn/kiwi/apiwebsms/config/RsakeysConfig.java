package sn.kiwi.apiwebsms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Objects;

//@ConfigurationPropertiesScan
//@ConstructorBinding
//@ConfigurationProperties(prefix = "rsa")
public final class RsakeysConfig {
    private final RSAPublicKey publicKey;
    private final RSAPrivateKey privateKey;

    RsakeysConfig(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public RSAPublicKey publicKey() {
        return publicKey;
    }

    public RSAPrivateKey privateKey() {
        return privateKey;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (RsakeysConfig) obj;
        return Objects.equals(this.publicKey, that.publicKey) &&
                Objects.equals(this.privateKey, that.privateKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publicKey, privateKey);
    }

    @Override
    public String toString() {
        return "RsakeysConfig[" +
                "publicKey=" + publicKey + ", " +
                "privateKey=" + privateKey + ']';
    }

}