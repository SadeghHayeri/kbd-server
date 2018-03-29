package KBD.v1.filters;

import org.json.JSONObject;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/api/v1/*")
public class APITypeCheck implements Filter {
    public void destroy() {}

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String acceptType = httpRequest.getHeader("Accept");
//        response.getWriter().print(acceptType);

        if(acceptType.equals("*/*") || acceptType.toLowerCase().indexOf("json") > 0)
            chain.doFilter(req, resp);
        else {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            JSONObject json = new JSONObject();
            json.put("code", HttpServletResponse.SC_BAD_REQUEST);
            String message = "در حال حاضر فقط فرمت JSON پشتیبانی می شود.";
            json.put("message", message);

            response.getWriter().print(json.toString());
        }
    }

    public void init(FilterConfig config) throws ServletException {}

}