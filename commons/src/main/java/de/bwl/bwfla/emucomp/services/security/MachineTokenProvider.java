package de.bwl.bwfla.emucomp.services.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.eclipse.microprofile.config.ConfigProvider;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;

public class MachineTokenProvider {
    private static String authProxy = ConfigProvider.getConfig().getValue("emucomp.image_proxy", String.class);
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

       try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            String token = JWT.create()
                    .withIssuer("eaasi")
                    .withExpiresAt(new Date(System.currentTimeMillis() + (2 * 60 * 60 * 1000))) // 2h
                    .sign(algorithm);
           // System.out.println("Token:"  + token);
            return token;
        } catch (JWTCreationException exception){
            exception.printStackTrace();
            return null;
        }
    }

    public static String getAuthProxy()
    {
        if(authProxy != null && (authProxy.isEmpty() || authProxy.equals("null")))
            return null;
        if(!authProxy.endsWith("/"))
            authProxy += "/";
        return authProxy;
    }

    public static String getAuthenticationProxy()
    {
        if( getApiKey() != null && getAuthProxy() != null)
            return "http://jwt:" + getApiKey() + "@" + getAuthProxy();
        else
            return null;
    }

    public static String getProxy()
    {
        if( getAuthProxy() != null)
            return "http://" + getAuthProxy();
        else
            return null;
    }

    public static String getProxyAuthenticationHeader()
    {
        if(getApiKey() != null)
            return "Basic " + new String(Base64.getEncoder().encode(("jwt:" + getApiKey()).getBytes()));
        return null;
    }
}
