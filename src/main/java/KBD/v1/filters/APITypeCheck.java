package KBD.v1.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/api/v1/*")
public class APITypeCheck extends BaseFilter {
    public void destroy() {}

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String acceptType = httpRequest.getHeader("Accept");

        if(acceptType.equals("*/*") || acceptType.toLowerCase().indexOf("json") > 0)
            chain.doFilter(req, resp);
        else {
            String message = "در حال حاضر فقط فرمت JSON پشتیبانی می شود.";
            errorResponse(response, HttpServletResponse.SC_NOT_IMPLEMENTED, message);
        }
    }

    public void init(FilterConfig config) throws ServletException {}

}