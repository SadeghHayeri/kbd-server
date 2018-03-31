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
            chain.doFilter(req, resp);
        } else {
            CharResponseWrapper responseWrapper = new CharResponseWrapper(response);
            chain.doFilter(req, responseWrapper);

            List<String> items = Arrays.asList(fields.split("\\s*,\\s*"));

            JSONObject realResponse = new JSONObject(responseWrapper.toString());
            JSONObject targetResponse = new JSONObject();
            if(realResponse.length() == 2) {    //code + array

                // get key!
                Iterator<String> keys = realResponse.keys();
                String key = keys.next();
                if(key.equals("code"))
                    key = keys.next();

                JSONArray realArray = realResponse.getJSONArray(key);

                JSONArray targetArray = new JSONArray();
                for (int i = 0; i < realArray.length(); i++) {
                    JSONObject realItem = realArray.getJSONObject(i);
                    JSONObject filteredItem = JSONService.keepAllowedKeys(realItem, items);
//
                    targetArray.put(filteredItem);
                }
                targetResponse.put(key, targetArray);
            } else {
                targetResponse = JSONService.keepAllowedKeys(realResponse, items);
            }

            response.getWriter().write(targetResponse.toString());
        }
    }

    public void init(FilterConfig config) throws ServletException {}
    public void destroy() {}
}