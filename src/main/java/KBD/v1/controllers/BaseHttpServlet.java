package KBD.v1.controllers;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
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
                throw new FileNotFoundException(field);
    }

    protected void sendJsonResponse(HttpServletResponse response, JSONObject json) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        if(!json.has("code"))
            json.put("code", HttpServletResponse.SC_OK);
        response.getWriter().print(json.toString());
    }

    protected void sendJsonResponse(HttpServletResponse response, JSONArray json, String name) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JSONObject finalResponse = new JSONObject();
        finalResponse.put(name, json);
        sendJsonResponse(response, finalResponse);
    }

    protected void sendJsonResponse(HttpServletResponse response, JSONArray json) throws IOException {
        sendJsonResponse(response, json, ARRAY_TYPE_DEFAULT_NAME);
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
