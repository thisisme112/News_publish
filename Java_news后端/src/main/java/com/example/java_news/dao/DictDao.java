package com.example.java_news.dao;
import com.example.java_news.model.Dict;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DictDao {
    private Connection connection;

    public DictDao(Connection connection) {
        this.connection = connection;
    }

    public List<Dict> getAllWords() {
        List<Dict> words = new ArrayList<>();
        String query = "SELECT * FROM `sensitive words`";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String word = resultSet.getString("word");
                Timestamp createdAt = resultSet.getTimestamp("created_at");
                Dict dict = new Dict(id, word, createdAt);
                words.add(dict);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return words;
    }

    public int addWord(String word) {
        String query = "INSERT INTO `sensitive words` (word) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, word);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    generatedKeys.getInt(1);
                }
                // 获得id
                System.out.println(generatedKeys.getInt(1));
                return generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void deleteWord(int id) {
        String query = "DELETE FROM `sensitive words` WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
