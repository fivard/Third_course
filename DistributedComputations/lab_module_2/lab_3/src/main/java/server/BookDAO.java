package server;

import connection.CustomConnection;
import dto.BookDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    public static BookDTO findById(long id) {
        try (Connection connection = CustomConnection.getConnection();) {
            String sql = "SELECT * FROM book WHERE id = ?";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            BookDTO book = null;
            if (resultSet.next()) {
                book = new BookDTO();
                book.setBookId(resultSet.getLong(1));
                book.setName(resultSet.getString(2));
                book.setReleaseYear(resultSet.getInt(3));
                book.setAuthorId(resultSet.getLong(4));
            }
            preparedStatement.close();
            return book;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BookDTO findByName(String name) {
        try (Connection connection = CustomConnection.getConnection();) {
            String sql = "SELECT * FROM book WHERE name = ?";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            BookDTO book = null;
            if (resultSet.next()) {
                book = new BookDTO();
                book.setBookId(resultSet.getLong(1));
                book.setName(resultSet.getString(2));
                book.setReleaseYear(resultSet.getInt(3));
                book.setAuthorId(resultSet.getLong(4));
            }
            preparedStatement.close();
            return book;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean update(BookDTO book) {
        try (Connection connection = CustomConnection.getConnection();) {
            String sql = "UPDATE book SET name = ?, release_year = ?, author_id = ? WHERE id = ?";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, book.getName());
            preparedStatement.setInt(2, book.getReleaseYear());
            preparedStatement.setLong(3, book.getAuthorId());
            preparedStatement.setLong(4, book.getBookId());
            int result = preparedStatement.executeUpdate();
            preparedStatement.close();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean insert(BookDTO book) {
        try (Connection connection = CustomConnection.getConnection();) {
            String sql = "INSERT INTO book (name, release_year, author_id) VALUES (?,?,?) RETURNING id";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, book.getName());
            preparedStatement.setInt(2, book.getReleaseYear());
            preparedStatement.setLong(3, book.getAuthorId());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                book.setBookId(resultSet.getLong(1));
            } else
                return false;
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean delete(BookDTO book) {
        try (Connection connection = CustomConnection.getConnection();) {
            String sql = "DELETE FROM book WHERE id = ?";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, book.getBookId());
            int result = preparedStatement.executeUpdate();
            preparedStatement.close();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<BookDTO> findAll() {
        try (Connection connection = CustomConnection.getConnection()) {
            String sql = "SELECT * FROM book";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<BookDTO> list = new ArrayList<>();
            while (resultSet.next()) {
                BookDTO book = new BookDTO();
                book.setBookId(resultSet.getLong(1));
                book.setName(resultSet.getString(2));
                book.setReleaseYear(resultSet.getInt(3));
                book.setAuthorId(resultSet.getLong(4));
                list.add(book);
            }
            preparedStatement.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<BookDTO> findByAuthorId(Long id) {
        try (Connection connection = CustomConnection.getConnection()) {
            String sql = "SELECT * FROM book WHERE author_id = ?";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<BookDTO> list = new ArrayList<>();
            while (resultSet.next()) {
                BookDTO book = new BookDTO();
                book.setBookId(resultSet.getLong(1));
                book.setName(resultSet.getString(2));
                book.setReleaseYear(resultSet.getInt(3));
                book.setAuthorId(resultSet.getLong(4));
                list.add(book);
            }
            preparedStatement.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

