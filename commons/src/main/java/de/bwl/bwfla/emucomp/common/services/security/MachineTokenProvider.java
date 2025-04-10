package de.bwl.bwfla.emucomp.common.services.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import java.time.Duration;
import java.util.Date;
import java.util.function.Function;


public class MachineTokenProvider {
    private static String apiSecret;

    static {
        apiSecret = ConfigProvider.getConfig().getValue("ws.apiSecret", String.class);
        if(apiSecret != null && (apiSecret.isEmpty() || apiSecret.equals("null")))
            apiSecret = null;
    }

    static String getApiSecret()
    {
        return apiSecret;
    }

    public static String getApiKey()
    {
       if(apiSecret == null)
            return null;

       final var lifetime = MachineTokenProvider.getDefaultLifetime();
       try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            String token = JWT.create()
                    .withIssuer("eaasi")
                    .withExpiresAt(new Date(MachineTokenProvider.time() + lifetime.toMillis()))
                    .sign(algorithm);
           // System.out.println("Token:"  + token);
            return token;
        } catch (JWTCreationException exception){
            exception.printStackTrace();
            return null;
        }
    }

    public static String getJwt(String secret)
    {
        return MachineTokenProvider.getBearerToken(secret, MachineTokenProvider.getDefaultLifetime());
    }

    private static String getBearerToken(String secret, Duration lifetime)
    {
        if(secret == null)
            return null;

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("com/openslx/eaas")
                    .withExpiresAt(new Date(MachineTokenProvider.time() + lifetime.toMillis()))
                    .sign(algorithm);
            // System.out.println("Token:"  + token);
            return "Bearer " + token;
        } catch (JWTCreationException exception){
            exception.printStackTrace();
            return null;
        }
    }

    public static MachineToken getInternalToken()
    {
        return MachineTokenProvider.getInternalToken(MachineTokenProvider.getDefaultLifetime());
    }

    public static MachineToken getInternalToken(Duration lifetime)
    {
        final Function<Duration, String> refresher = (time) -> {
            final var secret = ConfigProvider.getConfig().getValue("rest.internalApiSecret", String.class);

            return MachineTokenProvider.getBearerToken(secret, time);
        };

        return new MachineToken(lifetime, refresher);
    }

    public static MachineToken getAccessToken(String secret)
    {
        return MachineTokenProvider.getAccessToken(secret, MachineTokenProvider.getDefaultLifetime());
    }

    public static MachineToken getAccessToken(String secret, Duration lifetime)
    {
        final Function<Duration, String> refresher = (time) -> {
            return MachineTokenProvider.getBearerToken(secret, time);
        };

        return new MachineToken(lifetime, refresher);
    }

    public static Duration getDefaultLifetime()
    {
        return Duration.ofHours(2L);
    }

    public static long time()
    {
        return System.currentTimeMillis();
    }
}
