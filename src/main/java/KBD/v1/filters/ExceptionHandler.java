package KBD.v1.filters;

import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.FileNotFoundException;
import java.io.IOException;

@WebFilter("/*")
public class ExceptionHandler implements Filter {
    private FilterConfig filterConfig;

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletResponse response = (HttpServletResponse) resp;

        try {
            chain.doFilter(req, resp);
        } catch (FileNotFoundException e) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            JSONObject json = new JSONObject();
            json.put("code", HttpServletResponse.SC_BAD_REQUEST);
            String message = "مورد نیاز است " + e.getMessage() + " فیلد";
            json.put("message", message);

            response.getWriter().print(json.toString());
        }
    }

    public void init(FilterConfig config) throws ServletException {
        this.filterConfig = config;
    }

}
