package KBD.v1.filters;

import KBD.v1.services.JSONService;
import org.json.JSONObject;
import org.json.JSONArray;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@WebFilter("/api/v1/*")
public class FieldsFilter implements Filter {

    public class CharResponseWrapper extends HttpServletResponseWrapper {
        private CharArrayWriter output;
        public String toString() {
            return output.toString();
        }
        public PrintWriter getWriter() {
            return new PrintWriter(output);
        }
        public CharResponseWrapper(HttpServletResponse response) {
            super(response);
            output = new CharArrayWriter();
        }
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpServletRequest request = (HttpServletRequest) req;

        String fields = request.getParameter("fields");
        if(fields == null) {
            chain.doFilter(req, response);
        } else {
            try {
                CharResponseWrapper responseWrapper = new CharResponseWrapper(response);
                chain.doFilter(req, responseWrapper);

                List<String> items = Arrays.asList(fields.split("\\s*,\\s*"));

                JSONObject realResponse = new JSONObject(responseWrapper.toString());
                JSONObject targetResponse = new JSONObject();

                targetResponse.put("code", realResponse.get("code"));
                if(realResponse.has("message"))
                    targetResponse.put("message", realResponse.get("message"));

                if(realResponse.has("data")) {
                    Object data = realResponse.get("data");
                    if(data instanceof JSONObject) {
                        targetResponse.put("data", JSONService.keepAllowedKeys((JSONObject) data, items));
                    } else {
                        JSONArray realArray = (JSONArray) data;
                        JSONArray targetArray = new JSONArray();
                        for (int i = 0; i < realArray.length(); i++) {
                            JSONObject realItem = realArray.getJSONObject(i);
                            JSONObject filteredItem = JSONService.keepAllowedKeys(realItem, items);
//
                            targetArray.put(filteredItem);
                        }
                        targetResponse.put("data", targetArray);
                    }
                }

                response.getWriter().write(targetResponse.toString());
            } catch (Exception e) {
                JSONObject data = new JSONObject();
                data.put("code", HttpServletResponse.SC_NOT_IMPLEMENTED);
                data.put("message", "خطای سیستمی: این نوع پارامترها پشتیبانی نمی شوند.");

                response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
                response.getWriter().write(data.toString());
            }
        }
    }

    public void init(FilterConfig config) throws ServletException {}
    public void destroy() {}
}