package com.example.java_news.dao;
import com.example.java_news.model.News;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
public class NewsDao {
     private Connection connection;

    public NewsDao(Connection connection) {
        this.connection = connection;
    }

    public boolean addNews(News news) {
        String query = "INSERT INTO news (title, content, created_at, updated_at, pic, link) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, news.getTitle());
            statement.setString(2, news.getContent());
            statement.setTimestamp(3, new Timestamp(news.getCreatedAt().getTime()));
            statement.setTimestamp(4, new Timestamp(news.getUpdatedAt().getTime()));
            statement.setString(5, news.getPic());
            statement.setString(6, news.getLink());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    news.setId(generatedKeys.getLong(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteNews(long id) {
        String query = "DELETE FROM news WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);

            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateNews(News news) {
        String query = "UPDATE news SET title = ?, content = ?, updated_at = ?, pic = ?, link = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, news.getTitle());
            statement.setString(2, news.getContent());
            statement.setTimestamp(3, new Timestamp(news.getUpdatedAt().getTime()));
            statement.setString(4, news.getPic());
            statement.setString(5, news.getLink());
            statement.setLong(6, news.getId());

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<News> getAllNews() {
        List<News> newsList = new ArrayList<>();
        String query = "SELECT * FROM news";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String title = resultSet.getString("title");
                String content = resultSet.getString("content");
                Date createdAt = resultSet.getTimestamp("created_at");
                Date updatedAt = resultSet.getTimestamp("updated_at");
                String pic = resultSet.getString("pic");
                String link = resultSet.getString("link");

                News news = new News(id, title, content, createdAt, updatedAt, pic, link);
                newsList.add(news);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newsList;
    }
}
