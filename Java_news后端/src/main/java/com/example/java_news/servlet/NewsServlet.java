package com.example.java_news.servlet;
import com.example.java_news.dao.NewsDao;
import com.example.java_news.model.News;
import java.io.*;
import com.example.java_news.util.DatabaseConnector;
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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/news")
public class NewsServlet extends HttpServlet {
    private NewsDao newsDao;
    @Override
    public void init() throws ServletException {
        super.init();
        // 创建数据库连接对象
        new DatabaseConnector();
        // 获取数据库连接
        Connection connection = DatabaseConnector.getConnection();
        // 创建 NewsDao 对象
        newsDao = new NewsDao(connection);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("get!");
        // 处理查看新闻功能
        List<News> newsList = newsDao.getAllNews();
         // 将新闻列表转换为 JSON 字符串
        JSONArray jsonArray = new JSONArray();
        for (News news : newsList) {
            // 创建 JSON 对象表示新闻
            JSONObject jsonNews = new JSONObject();
            jsonNews.put("id", news.getId());
            jsonNews.put("title", news.getTitle());
            jsonNews.put("content", news.getContent());
            jsonNews.put("created_at", news.getCreatedAt());
            jsonNews.put("updated_at", news.getUpdatedAt());
            jsonNews.put("pic", news.getPic());
            jsonNews.put("link", news.getLink());

            // 将新闻对象添加到 JSON 数组中
            jsonArray.put(jsonNews);
        }
        System.out.println("GET!");
        // 设置响应类型为 JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // 向客户端发送 JSON 数据
        PrintWriter writer = response.getWriter();
        writer.print(jsonArray.toString());
        writer.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 处理发布新闻功能
        // 获取请求体中的 JSON 字符串
        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        // 解析 JSON 字符串
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(body);
        String method = node.get("method").asText();
        if(method.equals("delete")){
            doDelete(node);
            return;
        }
        if(method.equals("put")){
            doPut(node);
            return;
        }
        String title = node.get("title").asText();
        String content = node.get("content").asText();
        String pic = node.get("pic").asText();
        String link = node.get("link").asText();
        // 创建 News 对象
        Date time = new Date();
        News news = new News(title, content,time,time,pic,link);
        // 将新闻插入到数据库中
        newsDao.addNews(news);
        // 回传插入新闻的 ID 和状态
        // 创建 JSON 对象
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", news.getId());  // 回传ID，否则前端无法删除
        jsonObject.put("status", "ok");
        // 设置响应类型为 JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // 向客户端发送 JSON 数据
        PrintWriter writer = response.getWriter();
        writer.print(jsonObject.toString());
        writer.flush();
    }

    protected void doPut(JsonNode node)  {
        System.out.println("put!");
        // 处理修改新闻功能
        String id = node.get("id").asText();
        String title = node.get("title").asText();
        String content = node.get("content").asText();
        String pic = node.get("pic").asText();
        String link = node.get("link").asText();
        // 创建 News 对象
        Date time = new Date();
        News news = new News(Long.parseLong(id),title, content,time,time,pic,link);
        // 将新闻插入到数据库中
        newsDao.updateNews(news);
    }

     void doDelete(JsonNode node){
        System.out.println("delete!");
        // 处理删除新闻功能
        String id = node.get("id").asText();
        System.out.println(id);
        newsDao.deleteNews(Long.parseLong(id));
    }
}