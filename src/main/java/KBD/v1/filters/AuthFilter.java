package KBD.v1.filters;

import KBD.Database;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {
    public void destroy() {}

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        req.setAttribute("user", Database.getUser(0));
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {}

}
