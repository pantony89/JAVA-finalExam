/*
 * Copyright 2015 Len Payne <len.payne@lambtoncollege.ca>.
 * Updated 2015 Mark Russell <mark.russell@lambtoncollege.ca>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.abcindustries.controllers;

import com.abcindustries.entities.Product;
import com.abcindustries.utilities.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;

/**
 *
 * @author Len Payne <len.payne@lambtoncollege.ca>
 */
@ApplicationScoped
public class ProductsController {

    List<Product> productList;

    public ProductsController() {        
        productList = new ArrayList<>();
        try {
            Connection conn = Database.getConnection();
            String sql = "SELECT * FROM Products";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                productList.add(new Product(
                        rs.getInt("productId"),
                        rs.getString("name"),
                        rs.getInt("vendorId")
                ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Product> getAll() {
        return productList;
    }

    public JsonArray toJson() {
        // TODO: Build a JsonArray object from the List
        JsonArrayBuilder json = Json.createArrayBuilder();
        productList.stream().forEach((p) -> {
            json.add(p.toJson());
        });
        return json.build();
    }

    public Product getById(int id) {
        Product result = null;
        for (int i = 0; i < productList.size() && result == null; i++) {
            if (productList.get(i).getProductId() == id) {
                result = productList.get(i);
            }
        }
        return result;
    }

    public void add(Product p) throws SQLException {
        Connection conn = Database.getConnection();
        String sql = "INSERT INTO Products (Name, VendorId) VALUES (?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, p.getName());
        pstmt.setInt(2, p.getVendorId());
        int result = pstmt.executeUpdate();
        
        // Name is Unique and ProductId is auto-incremented, so we can find
        // the actual ProductId based on the Name
        // -- Vendors does not have this issue --
        sql = "SELECT ProductId FROM Products WHERE Name = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, p.getName());
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        p.setProductId(rs.getInt("ProductId"));
        
        if (result == 1) {
            productList.add(p);
        }
    }

    public void set(int id, Product p) throws SQLException {
        // TODO: Similar to the add() method
        Connection conn = Database.getConnection();
        String sql = "UPDATE Products SET name = ?, vendorId = ? WHERE productId =? ";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, p.getName());
        stmt.setInt(2, p.getVendorId());
        int result = stmt.executeUpdate();  
         sql = "SELECT ProductId FROM Products WHERE Name = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, p.getName());
        ResultSet rs = stmt.executeQuery();
        rs.next();
        p.setProductId(rs.getInt("ProductId"));
        
        if (result == 1) {
           Product res=getById(id);
            res.setName(p.getName());
           res.setVendorId(p.getVendorId());
        }
    }

    public void delete(int id) throws SQLException {
        // TODO: Remember to delete from both the List and the DB
           Connection conn = Database.getConnection();
           String sql="DELETE products WHERE productid=?";
           PreparedStatement stmt=conn.prepareStatement(sql);
           int result=stmt.executeUpdate();
           if(result==0){
               Product res=getById(id);
           productList.remove(res);
           }
    }
}
