package com.elidaniel92.app.product;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.elidaniel92.app.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProductDAOTest {

    @Mock
    private DatabaseConnection dbConn;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private ProductDAO productDAO;

    @BeforeEach
    public void setup() throws SQLException {
        MockitoAnnotations.openMocks(this);
        when(dbConn.getConnection()).thenReturn(connection);
    }

    @Test
    public void testCreateProduct() throws SQLException {
        // GIVEN
        final String name = "Keyboard";
        final int buyPrice = 100;
        final int quantityInStock = 123;
        CreateUpdateProductDTO dto = new CreateUpdateProductDTO(name, buyPrice, quantityInStock);

        // WHEN
        String query = "INSERT INTO products (name, buy_price, quantity_in_stock) VALUES (?, ?, ?)";

        when(connection.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS))
                .thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        final int idCreated = 148;
        when(resultSet.getInt(1)).thenReturn(idCreated);

        ProductEntity product = productDAO.createProduct(dto);

        // THEN

        // ArgumentCaptor to capture the arguments passed to PreparedStatement
        ArgumentCaptor<String> setStringCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> setIntCaptor = ArgumentCaptor.forClass(Integer.class);

        // Verify the exact method calls with the correct arguments
        verify(preparedStatement).setString(eq(1), setStringCaptor.capture()); // Name
        verify(preparedStatement).setInt(eq(2), setIntCaptor.capture()); // Buy price
        verify(preparedStatement).setInt(eq(3), setIntCaptor.capture()); // Quantity in stock

        // Assert the captured arguments
        assertEquals(name, setStringCaptor.getValue());
        assertEquals(buyPrice, (int) setIntCaptor.getAllValues().get(0)); // First setInt call
        assertEquals(quantityInStock, (int) setIntCaptor.getAllValues().get(1)); // Second setInt call

        assertNotNull(product);
        assertEquals(idCreated, product.getId());
        assertEquals(name, product.getName());
        assertEquals(buyPrice, product.getPrice());
        assertEquals(quantityInStock, product.getQuantityInStock());

        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testGetProductById() throws SQLException {
        int productId = 1;
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id")).thenReturn(productId);
        when(resultSet.getString("name")).thenReturn("Product1");
        when(resultSet.getInt("buy_price")).thenReturn(100);
        when(resultSet.getInt("quantity_in_stock")).thenReturn(10);

        ProductEntity product = productDAO.getProductById(productId);

        assertNotNull(product);
        assertEquals(productId, product.getId());
        assertEquals("Product1", product.getName());
        assertEquals(100, product.getPrice());
        assertEquals(10, product.getQuantityInStock());
        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    public void testGetAllProducts() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, false); // Simulate two products
        when(resultSet.getInt("id")).thenReturn(1, 2);
        when(resultSet.getString("name")).thenReturn("Product1", "Product2");
        when(resultSet.getInt("buy_price")).thenReturn(100, 200);
        when(resultSet.getInt("quantity_in_stock")).thenReturn(10, 20);

        List<ProductEntity> products = productDAO.getAllProducts();

        assertNotNull(products);
        assertEquals(2, products.size());

        ProductEntity product1 = products.get(0);
        ProductEntity product2 = products.get(1);

        assertEquals(1, product1.getId());
        assertEquals("Product1", product1.getName());
        assertEquals(100, product1.getPrice());
        assertEquals(10, product1.getQuantityInStock());

        assertEquals(2, product2.getId());
        assertEquals("Product2", product2.getName());
        assertEquals(200, product2.getPrice());
        assertEquals(20, product2.getQuantityInStock());

        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    public void testUpdateProduct() throws SQLException {
        // GIVEN
        final String name = "Keyboard";
        final int buyPrice = 100;
        final int quantityInStock = 123;
        CreateUpdateProductDTO dto = new CreateUpdateProductDTO(name, buyPrice, quantityInStock);
        int productId = 1;

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = productDAO.updateProduct(dto, productId);

        // THEN

        // ArgumentCaptor to capture the arguments passed to PreparedStatement
        ArgumentCaptor<String> setStringCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> setIntCaptor = ArgumentCaptor.forClass(Integer.class);

        // Verify the exact method calls with the correct arguments
        verify(preparedStatement).setString(eq(1), setStringCaptor.capture()); // Name
        verify(preparedStatement).setInt(eq(2), setIntCaptor.capture()); // Buy price
        verify(preparedStatement).setInt(eq(3), setIntCaptor.capture()); // Quantity in stock
        verify(preparedStatement).setInt(eq(4), setIntCaptor.capture()); // Id

        // Assert the captured arguments
        assertEquals(name, setStringCaptor.getValue());
        assertEquals(buyPrice, (int) setIntCaptor.getAllValues().get(0)); // First setInt call
        assertEquals(quantityInStock, (int) setIntCaptor.getAllValues().get(1)); // Second setInt call
        assertEquals(productId, (int) setIntCaptor.getAllValues().get(2)); // Third setInt call

        assertTrue(result);
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testDeleteProduct() throws SQLException {
        int productId = 1;
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = productDAO.deleteProduct(productId);

        assertTrue(result);
        verify(preparedStatement, times(1)).executeUpdate();
    }
}
