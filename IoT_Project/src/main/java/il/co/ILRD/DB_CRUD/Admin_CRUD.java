package il.co.ILRD.DB_CRUD;

import org.json.JSONObject;

import java.sql.SQLException;
import java.sql.*;

public class Admin_CRUD {
    private final String mySqlUrl;
    private final String username;
    private final String password;
    private DB_CRUD company;
    private DB_CRUD product;

    public static final String DBName = "Admin";
    public static final String typeBIGINT_UNSIGNED = "BIGINT UNSIGNED";
    public static final String type_VARCHAR = "VARCHAR(255)";
    public static final String type_VARCHAR_3 = "VARCHAR(3)";
    public static final String type_VARCHAR_32 = "VARCHAR(32)";
    public static final String type_VARCHAR_10 = "VARCHAR(10)";
    public static final String type_TEXT = "TEXT";

    public Admin_CRUD(String mySqlUrl, String username, String password) throws SQLException {
        this.mySqlUrl = mySqlUrl;
        this.username = username;
        this.password = password;

        this.company = new companyCRUDImp();
        this.product = new productCRUDImp();
        this.setup();
    }

    public JSONObject registerCompanyCRUD(JSONObject data){
        return company.create(data);
    }
    public JSONObject registerProductCRUD(JSONObject data){
        return product.create(data);
    }
    public void setup() throws SQLException {
        if (!this.isDBExists(DBName)) {
            createAdminDatabase();

            addTable_Companies();
            addTable_CreditCardDetails();
            addTable_Products();
        }
    }

    // ---------------------- privet methods ----------------------------------
    private boolean isDBExists(String DBName) throws SQLException {
        try (Connection connection = DriverManager.getConnection(this.mySqlUrl, username, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SHOW DATABASES")) {

            while (resultSet.next()){
                String name = resultSet.getString("Database");
                if (name.equals(DBName)){
                    return true;
                }
            }

        } catch (SQLException e) {
            throw new SQLException("isDBExists failed: " + e.getMessage());
        }
        return false;
    }
    private void createAdminDatabase() throws SQLException {
        String queryNewDB = "CREATE DATABASE IF NOT EXISTS Admin;";

        try (Connection connection = DriverManager.getConnection(mySqlUrl, username, password);
             Statement statement = connection.createStatement()) {

            statement.execute(queryNewDB);

        } catch (SQLException e) {
            throw new SQLException("create database Admin failed: " + e.getMessage());
        }
        //todo remove
//        System.out.println("Database: " + DBName + " was created");
    }
    private void addTable_Companies() throws SQLException {
        String tableName = "Companies";
        createNewTable(tableName, "company_name", type_VARCHAR_32, false);

        addField(tableName, "company_address", type_VARCHAR, true);
        addField(tableName, "contact_name", type_VARCHAR_32, true);
        addField(tableName, "contact_phone", type_VARCHAR_32, true);
        addField(tableName, "contact_email", type_VARCHAR, true);
        addField(tableName, "service_fee", typeBIGINT_UNSIGNED, true);
    }
    private void addTable_CreditCardDetails() throws SQLException {
        String tableName = "CreditCardDetails";
        createNewTable(tableName, "card_number", type_VARCHAR_32,false);

        addField(tableName, "company_name", type_VARCHAR_32, true);
        addField(tableName, "card_holder_name", type_VARCHAR_32, true);
        addField(tableName, "ex_date", type_VARCHAR_10, true);
        addField(tableName, "CVV", type_VARCHAR_3, true);

        addForeignKey(tableName, "company_name", "Companies");
    }
    private void addTable_Products() throws SQLException {
        String tableName = "Products";
        createNewTable(tableName, "product_id", typeBIGINT_UNSIGNED, true);

        addField(tableName, "company_name", type_VARCHAR_32, true);
        addField(tableName, "product_name", type_VARCHAR_32, true);
        addField(tableName, "product_description", type_TEXT, true);

        addForeignKey(tableName, "company_name", "Companies");
    }
    private void createNewTable(String tableName, String uniqueKeyName,
                                String uniqueKeyValue, boolean autoIncrement) throws SQLException {
        String urlToDB = String.format("%s/%s", this.mySqlUrl, DBName);

        String autoInc = autoIncrement ? "AUTO_INCREMENT" : "";
        String createNewTable = String.format("CREATE TABLE IF NOT EXISTS %s(%s %s %s PRIMARY KEY);",
                tableName, uniqueKeyName, uniqueKeyValue, autoInc);

        try (Connection connection = DriverManager.getConnection(urlToDB, username, password);
             Statement statement = connection.createStatement()) {

            statement.execute(createNewTable);
        } catch (SQLException e) {
            throw new SQLException(String.format("create table %s failed: ", tableName) + e.getMessage());
        }
    }
    private void addField(String tableName, String fieldName, String fieldType, boolean isNotNull) throws SQLException {
        addField(tableName, fieldName, fieldType, isNotNull, null);
    }
    private void addField(String tableName, String fieldName,
                          String fieldType, boolean isNotNull, String defaultValue) throws SQLException {
        String urlToDB = String.format("%s/%s", this.mySqlUrl, DBName);

        String notNull = isNotNull ? "NOT NULL" : "";
        String defValue = defaultValue == null ? "" : String.format("DEFAULT (%s)", defaultValue);

        String createNewTable = String.format("ALTER TABLE %s add %s %s %s %s;",
                tableName, fieldName, fieldType, notNull, defValue);

        try (Connection connection = DriverManager.getConnection(urlToDB, username, password);
             Statement statement = connection.createStatement()) {

            statement.execute(createNewTable);
        } catch (SQLException e) {
            throw new SQLException(String.format("adding field %s failed: ", fieldName) + e.getMessage());
        }
    }
    private void addForeignKey(String tableName, String fieldName, String otherTableName) throws SQLException {
        String urlToDB = String.format("%s/%s", this.mySqlUrl, DBName);

        String foreignKeyName = String.format("%s_%s_foreign", tableName, fieldName);
        String foreignKey = String.format("ALTER TABLE %s ADD CONSTRAINT %s FOREIGN KEY(%s) REFERENCES %s(%s);",
                tableName, foreignKeyName, fieldName, otherTableName, fieldName);

        try (Connection connection = DriverManager.getConnection(urlToDB, username, password);
             Statement statement = connection.createStatement()) {

            statement.execute(foreignKey);
        } catch (SQLException e) {
            throw new SQLException(String.format("adding foreign key: %s reference from %s to %s failed: ",
                    fieldName, otherTableName, tableName) + e.getMessage());
        }
    }





    private class companyCRUDImp implements DB_CRUD {

        @Override
        public JSONObject create(JSONObject data) {
            String company_name = data.getJSONObject("body").getString("company_name");
            String company_address = data.getJSONObject("body").getString("company_address");
            String contact_name = data.getJSONObject("body").getString("contact_name");
            String contact_phone = data.getJSONObject("body").getString("contact_phone");
            String contact_email = data.getJSONObject("body").getString("contact_email");
            String service_fee = data.getJSONObject("body").getString("service_fee");

            String card_number = data.getJSONObject("body").getString("card_number");
            String card_holder_name = data.getJSONObject("body").getString("card_holder_name");
            String ex_date = data.getJSONObject("body").getString("ex_date");
            String cvv = data.getJSONObject("body").getString("cvv");

            // company_name, company_address, contact_name, contact_phone, contact_email, service_fee
            String companyRecordValues = String.format("'%s', '%s', '%s', '%s', '%s', '%s'",
                    company_name, company_address, contact_name, contact_phone, contact_email, service_fee);
            String companyFields = "company_name, company_address, contact_name, " +
                    "contact_phone, contact_email, service_fee";

            // card_number, company_name, card_holder_name, ex_date, cvv
            String cardRecordValues = String.format("'%s', '%s', '%s', '%s', '%s'",
                    card_number, company_name, card_holder_name, ex_date, cvv);
            String paymentFields = "card_number, company_name, card_holder_name, ex_date, CVV";

            String urlToDB = String.format("%s/%s", Admin_CRUD.this.mySqlUrl, DBName);
            String companyRecord = String.format("INSERT INTO Companies (%s) VALUES (%s);", companyFields, companyRecordValues);
            String paymentRecord = String.format("INSERT INTO CreditCardDetails (%s) VALUES (%s);", paymentFields, cardRecordValues);

            try (Connection connection = DriverManager.getConnection(urlToDB, username, password);
                 Statement statement = connection.createStatement()) {

                statement.executeUpdate(companyRecord);
                statement.executeUpdate(paymentRecord);
            } catch (SQLException e) {
                data.getJSONObject("start_line").remove("method");
                data.getJSONObject("start_line").put("status_code", "404");
                data.getJSONObject("body").put("error", e.getMessage());
                return data;
            }

            data.getJSONObject("start_line").remove("method");
            data.getJSONObject("start_line").put("status_code", "200");
            return data;
        }

        @Override
        public JSONObject read(JSONObject data) {
            return null;
        }

        @Override
        public JSONObject update(JSONObject data) {
            return null;
        }

        @Override
        public JSONObject delete(JSONObject data) {
            return null;
        }
    }

    private class productCRUDImp implements DB_CRUD {

        @Override
        public JSONObject create(JSONObject data) {
            String company_name = data.getJSONObject("body").getString("company_name");
            String product_name = data.getJSONObject("body").getString("product_name");
            String product_description = data.getJSONObject("body").getString("product_description");

            String productsFields = "company_name, product_name, product_description";
            String productValues = String.format("'%s', '%s', '%s'",
                    company_name, product_name, product_description);

            String urlToDB = String.format("%s/%s", Admin_CRUD.this.mySqlUrl, DBName);
            String productRecord = String.format("INSERT INTO Products (%s) VALUES (%s);", productsFields, productValues);

            try (Connection connection = DriverManager.getConnection(urlToDB, username, password);
                 Statement statement = connection.createStatement()) {

                statement.executeUpdate(productRecord);
            } catch (SQLException e) {
//                throw new SQLException(e); todo
                data.getJSONObject("start_line").remove("method");
                data.getJSONObject("start_line").put("status_code", "404");
                data.getJSONObject("body").put("error", e.getMessage());
                return data;
            }

            data.getJSONObject("start_line").remove("method");
            data.getJSONObject("start_line").put("status_code", "200");
            return data;
        }

        @Override
        public JSONObject read(JSONObject data) {
            return null;
        }

        @Override
        public JSONObject update(JSONObject data) {
            return null;
        }

        @Override
        public JSONObject delete(JSONObject data) {
            return null;
        }
    }


}
