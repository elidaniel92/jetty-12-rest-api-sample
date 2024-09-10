package com.elidaniel92.app.product;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elidaniel92.app.ioc.Root;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ProductController extends HttpServlet {

    private final ProductDAO productDAO;
    private final ObjectMapper objectMapper = new ObjectMapper();
    protected final Logger log = LoggerFactory.getLogger(getClass());

    public ProductController() {
        super();
        this.productDAO = Root.getInjector().getInstance(ProductDAO.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            try {
                List<ProductEntity> products = productDAO.getAllProducts();
                String jsonResponse = objectMapper.writeValueAsString(products); // Use Jackson
                resp.setContentType("application/json");
                resp.getWriter().write(jsonResponse);
            } catch (Exception e) {
                log.error("Error fetching all products", e);
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"error\":\"Unable to fetch products\"}");
            }
        } else {
            String[] pathParts = pathInfo.split("/");
            if (pathParts.length > 1) {
                try {
                    int productId = Integer.parseInt(pathParts[1]);
                    ProductEntity product = productDAO.getProductById(productId);
                    if (product != null) {
                        String jsonResponse = objectMapper.writeValueAsString(product); // Use Jackson
                        resp.setContentType("application/json");
                        resp.getWriter().write(jsonResponse);
                    } else {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        resp.getWriter().write("{\"error\":\"Product not found\"}");
                    }
                } catch (Exception e) {
                    log.error("Error fetching product by ID: {}", pathParts[1], e);
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    resp.getWriter().write("{\"error\":\"Error fetching product\"}");
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            CreateUpdateProductDTO productDTO = objectMapper.readValue(req.getReader(), CreateUpdateProductDTO.class); 
            ProductEntity product = productDAO.createProduct(productDTO);
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(objectMapper.writeValueAsString(product));
        } catch (Exception e) {
            log.error("Error creating product", e);
            log.error("Error creating product: " + e.getMessage(), e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Error creating product\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            String[] pathParts = pathInfo.split("/");
            if (pathParts.length > 1) {
                try {
                    int productId = Integer.parseInt(pathParts[1]);
                    CreateUpdateProductDTO productDTO = objectMapper.readValue(req.getReader(),
                            CreateUpdateProductDTO.class); // Deserialize using Jackson
                    boolean success = productDAO.updateProduct(productDTO, productId);
                    if (success) {
                        resp.setStatus(HttpServletResponse.SC_OK);
                        resp.getWriter().write("{\"message\":\"Product updated successfully\"}");
                    } else {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        resp.getWriter().write("{\"error\":\"Product not found\"}");
                    }
                } catch (Exception e) {
                    log.error("Error updating product with ID: {}", pathParts[1], e);
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    resp.getWriter().write("{\"error\":\"Error updating product\"}");
                }
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\":\"Product not found\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            String[] pathParts = pathInfo.split("/");
            if (pathParts.length > 1) {
                try {
                    int productId = Integer.parseInt(pathParts[1]);
                    boolean success = productDAO.deleteProduct(productId);
                    if (success) {
                        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    } else {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        resp.getWriter().write("{\"error\":\"Product not found\"}");
                    }
                } catch (Exception e) {
                    log.error("Error deleting product with ID: {}", pathParts[1], e);
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    resp.getWriter().write("{\"error\":\"Error deleting product\"}");
                }
            }
        }
    }
}