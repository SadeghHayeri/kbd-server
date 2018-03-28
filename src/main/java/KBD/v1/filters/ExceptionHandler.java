package KBD.v1.filters;

import KBD.v1.Exceptions.NotFoundException;
import KBD.models.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("/*")
public class ExceptionHandler implements Filter {
    private FilterConfig filterConfig;

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        try {
            chain.doFilter(req, resp);
        } catch (NotFoundException e) {
            Logger.error(e.getMessage());

            req.setAttribute("message", "صفحه مورد نظر یافت نشد.");

            filterConfig.getServletContext().getRequestDispatcher("/404.jsp").forward(req, resp);
        }
    }

    public void init(FilterConfig config) throws ServletException {
        this.filterConfig = config;
    }

}
