package com.example.java_news.servlet;
import com.example.java_news.dao.DictDao;
import com.example.java_news.model.Dict;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "DictServlet", urlPatterns = "/dict")
public class DictServlet extends HttpServlet {
    private Connection connection;
    private DictDao dictDao;

    @Override
    public void init() throws ServletException {
        // 在初始化阶段创建数据库连接并实例化DictDao对象
        String jdbcUrl = "jdbc:mysql://localhost:3306/java_web";
        String username = "root";
        String password = "136617";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            dictDao = new DictDao(connection);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new ServletException("Unable to initialize database connection.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        // 解析Json
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(body);
        String method = node.get("method").asText();
        if(method.equals("add")){
            String word = node.get("word").asText();
            int id = dictDao.addWord(word);
            // 返回新添加的单词的id
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", id);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(jsonObject);


        }else if(method.equals("delete")){
            int id = node.get("id").asInt();
            dictDao.deleteWord(id);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Dict> words = dictDao.getAllWords();
        JSONArray jsonArray = new JSONArray();
        for (Dict word : words) {
            JSONObject jsonWord = new JSONObject();
            jsonWord.put("id", word.getId());
            jsonWord.put("word", word.getWord());
            jsonWord.put("created_at", word.getCreated_at());
            jsonArray.put(jsonWord);
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(jsonArray);
    }


    @Override
    public void destroy() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}