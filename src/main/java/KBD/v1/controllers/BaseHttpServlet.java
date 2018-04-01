package KBD.v1.controllers;

import KBD.v1.Exceptions.FieldNotFoundException;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static KBD.Config.ARRAY_TYPE_DEFAULT_NAME;

public class BaseHttpServlet extends HttpServlet {

    protected JSONObject parseJsonData(HttpServletRequest request) throws IOException {
        String jsonString = IOUtils.toString(request.getInputStream());
        return new JSONObject(jsonString);
    }

    protected void jsonValidation(JSONObject jsonData, List<String> fields) throws IOException, FileNotFoundException {
        for (String field : fields)
            if (!jsonData.has(field))
                throw new FieldNotFoundException(field);
    }

    protected void sendJsonResponse(HttpServletResponse response, JSONObject json) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "localhost:3000");

        JSONObject finalResponse = new JSONObject();
        JSONObject data = new JSONObject();
        for (Iterator<String> it = json.keys(); it.hasNext(); ) {
            String key = it.next();

            if(key.equals("code"))
                finalResponse.put("code", json.get("code"));
            else if(key.equals("message"))
                finalResponse.put("message", json.get("message"));
            else
                data.put(key, json.get(key));

        }

        if(!finalResponse.has("code"))
            finalResponse.put("code", HttpServletResponse.SC_OK);

        if(data.length() != 0)
            finalResponse.put("data", data);

        response.getWriter().print(finalResponse.toString());
    }

    protected void sendJsonResponse(HttpServletResponse response, JSONArray json) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JSONObject finalResponse = new JSONObject();
        finalResponse.put("code", HttpServletResponse.SC_OK);
        finalResponse.put("data", json);
        response.getWriter().print(finalResponse.toString());
    }

    protected void successResponse(HttpServletResponse response, String message) throws IOException {
        JSONObject data = new JSONObject();
        data.put("code", 200);
        data.put("message", message);

        sendJsonResponse(response, data);
    }

    protected void errorResponse(HttpServletResponse response, int code, String message) throws IOException {
        JSONObject data = new JSONObject();
        data.put("code", code);
        data.put("message", message);

        response.setStatus(code);
        sendJsonResponse(response, data);
    }
}
