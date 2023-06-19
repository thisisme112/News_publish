package com.example.java_news.servlet;
import com.example.java_news.util.DatabaseConnector;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 解析json，获取用户名和密码
        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        // 解析Json
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(body);
        String username = node.get("username").asText();
        String password = node.get("password").asText();
        // 验证用户名和密码
        boolean isValidUser = DatabaseConnector.validateUser(username, password);
        if (isValidUser) {
        // 用户名和密码正确，执行相应操作
        response.setStatus(200);
        } else {
        // 用户名或密码错误，返回错误信息
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Invalid username or password");
    }
    }
}
