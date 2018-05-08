package KBD.v1.filters;

import KBD.v1.Exceptions.FieldNotFoundException;
import KBD.v1.Exceptions.NoAuthenticationException;
import org.json.JSONException;

import javax.servlet.http.HttpServletResponse;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("/api/v1/*")
public class ExceptionHandler extends BaseFilter {
    private FilterConfig filterConfig;

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletResponse response = (HttpServletResponse) resp;

        try {
            chain.doFilter(req, resp);
        } catch (FieldNotFoundException e) {
            String message = "مورد نیاز است " + e.getMessage() + " فیلد";
            errorResponse(response, HttpServletResponse.SC_BAD_REQUEST, message);
        } catch (JSONException e) {
            String message = "خطا در فرمت JSON ارسالی.";
            errorResponse(response, HttpServletResponse.SC_BAD_REQUEST, message);
        } catch (NoAuthenticationException e) {
            String message = "عدم احراز هویت";
            errorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, message);
        }
    }

    public void init(FilterConfig config) throws ServletException {
        this.filterConfig = config;
    }

}
