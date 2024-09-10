package com.elidaniel92.app.product;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elidaniel92.app.database.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ProductDAO {
    private final DatabaseConnection dbConn;
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    public ProductDAO(DatabaseConnection dbConn) {
        this.dbConn = dbConn;
    }

    public ProductEntity createProduct(CreateUpdateProductDTO product) throws SQLException {
        String query = "INSERT INTO products (name, buy_price, quantity_in_stock) VALUES (?, ?, ?)";
        try (PreparedStatement ps = this.dbConn.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, product.getName());
            ps.setInt(2, product.getPrice());
            ps.setInt(3, product.getQuantityInStock());
            ps.executeUpdate();

            // Retrieve the auto-generated ID
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return new ProductEntity(rs.getInt(1), product.getName(), product.getPrice(), product.getQuantityInStock());
            }
            return null; // If no ID is generated, return null
        } catch (SQLException e) {
            log.error("Failed to execute the query: " + query, e);
            throw e;
        }
    }

    public ProductEntity getProductById(int id) throws SQLException {
        String query = "SELECT id, name, buy_price, quantity_in_stock FROM products WHERE id = ?";
        try (PreparedStatement ps = this.dbConn.getConnection().prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                int price = rs.getInt("buy_price");
                int quantityInStock = rs.getInt("quantity_in_stock");
                return new ProductEntity(id, name, price, quantityInStock);
            }
            return null; // Product not found
        } catch (SQLException e) {
            log.error("Failed to execute the query: " + query, e);
            throw e;
        }
    }

    public List<ProductEntity> getAllProducts() throws SQLException {
        List<ProductEntity> products = new ArrayList<>();
        String query = "SELECT id, name, buy_price, quantity_in_stock FROM products";
        try (PreparedStatement ps = this.dbConn.getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int price = rs.getInt("buy_price");
                int quantityInStock = rs.getInt("quantity_in_stock");
                ProductEntity product = new ProductEntity(id, name, price, quantityInStock);
                products.add(product);
            }
            return products;
        } catch (SQLException e) {
            log.error("Failed to execute the query: " + query, e);
            throw e;
        }
    }

    public boolean updateProduct(CreateUpdateProductDTO product, int id) throws SQLException {
        String query = "UPDATE products SET name = ?, buy_price = ?, quantity_in_stock = ? WHERE id = ?";
        try (PreparedStatement ps = this.dbConn.getConnection().prepareStatement(query)) {
            ps.setString(1, product.getName());
            ps.setInt(2, product.getPrice());
            ps.setInt(3, product.getQuantityInStock());
            ps.setInt(4, id);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            log.error("Failed to execute the query: " + query, e);
            throw e;
        }
    }

    public boolean deleteProduct(int id) throws SQLException {
        String query = "DELETE FROM products WHERE id = ?";
        try (PreparedStatement ps = this.dbConn.getConnection().prepareStatement(query)) {
            ps.setInt(1, id);
            int rowsDeleted = ps.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            log.error("Failed to execute the query: " + query, e);
            throw e;
        }
    }
}
