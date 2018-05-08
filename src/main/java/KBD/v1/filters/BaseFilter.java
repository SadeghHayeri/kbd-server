package KBD.v1.filters;

import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class BaseFilter implements Filter {

    protected void errorResponse(HttpServletResponse response, int code, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(code);

        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("message", message);

        response.getWriter().print(json.toString());
    }

}
