package KBD.v1.filters;

import KBD.v1.Exceptions.FieldNotFoundException;
import KBD.v1.Exceptions.NoAuthenticationException;
import org.json.JSONObject;
import org.json.JSONException;

import javax.servlet.http.HttpServletResponse;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("/api/v1/*")
public class ExceptionHandler implements Filter {
    private FilterConfig filterConfig;

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletResponse response = (HttpServletResponse) resp;

        try {
            chain.doFilter(req, resp);
        } catch (FieldNotFoundException e) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            JSONObject json = new JSONObject();
            json.put("code", HttpServletResponse.SC_BAD_REQUEST);
            String message = "مورد نیاز است " + e.getMessage() + " فیلد";
            json.put("message", message);

            response.getWriter().print(json.toString());
        } catch (JSONException e) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            JSONObject json = new JSONObject();
            json.put("code", HttpServletResponse.SC_BAD_REQUEST);
            String message = "خطا در فرمت JSON ارسالی.";
            json.put("message", message);

            response.getWriter().print(json.toString());
        } catch (NoAuthenticationException e) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            JSONObject json = new JSONObject();
            json.put("code", HttpServletResponse.SC_UNAUTHORIZED);
            String message = "عدم احراز هویت";
            json.put("message", message);

            response.getWriter().print(json.toString());
        }
    }

    public void init(FilterConfig config) throws ServletException {
        this.filterConfig = config;
    }

}
