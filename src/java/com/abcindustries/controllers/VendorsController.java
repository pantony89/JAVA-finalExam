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
import com.abcindustries.entities.Vendor;
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
public class VendorsController {
    // TODO: I guess this will be similar to the ProductsController, right?
    
    List<Vendor>vendorList;

    public VendorsController() {        
        vendorList = new ArrayList<>();
        try {
            Connection conn = Database.getConnection();
            String sql = "SELECT * FROM nendors";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
               vendorList.add(new Vendor(
                        rs.getInt("vendorId"),
                        rs.getString("name"),
                        rs.getString("contactNumber"),
                        rs.getString("PhoneNumber")
                ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public List<Vendor> getAll(){
    return vendorList;
    }
    public JsonArray toJson() {
        // TODO: Build a JsonArray object from the List
        JsonArrayBuilder json = Json.createArrayBuilder();
        vendorList.stream().forEach((p) -> {
            json.add(p.toJson());
        });
        return json.build();
    }
    
    public Vendor getById(int id){
    
    Vendor result = null;
        for (int i = 0; i < vendorList.size() && result == null; i++) {
            if (vendorList.get(i).getVendorId() == id) {
                result = vendorList.get(i);
            }
        }
        return result;
    }
    
     public void add(Vendor V) throws SQLException {
        Connection conn = Database.getConnection();
        String sql = "INSERT INTO vendors (Name, ContactName, PhoneNumber) VALUES (?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, V.getName());
        pstmt.setString(2, V.getContactName());
        pstmt.setString(3, V.getPhoneNumber());
        int result = pstmt.executeUpdate();
        sql = "SELECT vendorId FROM vendors WHERE Name = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, V.getName());
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        V.setVendorId(rs.getInt("VendorId"));
        if (result == 1) {
            vendorList.add(V);
        }
    }
     public void set(int id, Vendor V) throws SQLException {
        Connection conn = Database.getConnection();
        String sql = "UPDATE vendors SET name = ?, contactName = ?, phoneNumber = ?,  WHERE vendorId =  ";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, V.getName());
        stmt.setString(2, V.getContactName());
        stmt.setString(3, V.getPhoneNumber());
        int result = stmt.executeUpdate();
        sql = "SELECT vendorId FROM vendors WHERE Name = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, V.getName());
        ResultSet rs = stmt.executeQuery();
        rs.next();
        V.setVendorId(rs.getInt("VendorId"));
        if (result == 1) {
            Vendor res = getById(id);
            res.setName(V.getName());
            res.setContactName(V.getContactName());
            res.setPhoneNumber(V.getPhoneNumber());
        }
        
    }

      public void delete(int id) throws SQLException {
        Connection conn = Database.getConnection();
        String sql = "DELETE FROM Vendors WHERE vendorId =  ";
        PreparedStatement stmt = conn.prepareStatement(sql);
        int result = stmt.executeUpdate();
        
        if (result== 1) {
            Vendor res= getById(id);
            vendorList.remove(res);
        }
    }
     
     
}
