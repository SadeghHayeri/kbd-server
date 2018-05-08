package KBD.v1.filters;

import KBD.Config;
import KBD.models.IndividualUser;
import KBD.models.Logger;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@WebFilter("/api/v1/*")
public class AuthFilter implements Filter {
    public void destroy() {}

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String token = httpRequest.getHeader("Authorization");
        if(token != null) {
            try {
                Algorithm algorithm = Algorithm.HMAC256(Config.SECRET);
                JWTVerifier verifier = JWT.require(algorithm)
                        .withIssuer("auth0")
                        .build(); //Reusable verifier instance
                DecodedJWT jwt = verifier.verify(token);
                int userId = jwt.getClaim("userId").asInt();
                Logger.info("id " + userId);

                req.setAttribute("user", IndividualUser.find(userId));
                chain.doFilter(req, resp);
            } catch (UnsupportedEncodingException exception) {
                //UTF-8 encoding not supported
            } catch (TokenExpiredException e){
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);

                JSONObject json = new JSONObject();
                json.put("code", HttpServletResponse.SC_FORBIDDEN);
                json.put("message", "توکن منغضی شده است.");

                response.getWriter().print(json.toString());
            } catch (JWTVerificationException exception) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);

                JSONObject json = new JSONObject();
                json.put("code", HttpServletResponse.SC_FORBIDDEN);
                json.put("message", "توکن نامعتبر");

                response.getWriter().print(json.toString());
            }
        } else {
            req.setAttribute("user", null);
            chain.doFilter(req, resp);
        }
    }

    public void init(FilterConfig config) throws ServletException {}
}
