package KBD.v1.filters;

import KBD.models.IndividualUser;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("/api/v1/*")
public class AuthFilter implements Filter {
    public void destroy() {}

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        req.setAttribute("user", IndividualUser.find(1));
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {}

}
