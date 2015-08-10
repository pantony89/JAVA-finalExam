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
package com.abcindustries.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Len Payne <len.payne@lambtoncollege.ca>
 */
public class Database {
    public static Connection getConnection() {        
        Connection result = null;
        try {
            // TODO: Make sure that you change the x's in the credentials to
            //       match your own student number
            Class.forName("com.mysql.jdbc.Driver");
            String server = "ipro.lambton.on.ca";
            String database = "c0633176-java";
            String user = "c0633176-java";
            String pass = "c0633176";
            String jdbc = "jdbc:mysql://" + server + ":3306/" + database;
            result = DriverManager.getConnection(jdbc, user, pass);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);            
        }
        return result;
    }
}
