package br.edu.ifpr.foz.ifprstore.repositories;

import br.edu.ifpr.foz.ifprstore.connection.ConnectionFactory;
import br.edu.ifpr.foz.ifprstore.exceptions.DatabaseException;
import br.edu.ifpr.foz.ifprstore.models.Department;
import br.edu.ifpr.foz.ifprstore.models.Seller;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class SellerRepository {

    private Connection connection;

    public SellerRepository(){
        connection = ConnectionFactory.getConnection();
    }

    public List<Seller> getSellers(){

        List<Seller> sellers = new ArrayList<>();

        try {

            Statement statement = connection.createStatement();
            String sql = "SELECT seller.*,department.Name as DepName " +
                    "FROM seller " +
                    "INNER JOIN department " +
                    "ON seller.DepartmentId = department.Id";
            ResultSet result = statement.executeQuery(sql);

            while (result.next()){
                Seller seller;
                seller = getSeller(result);
                sellers.add(seller);
            }

            result.close();


        } catch (SQLException e) {

            throw new RuntimeException(e);

        } 

        return sellers;
    }

    public Seller insert(Seller seller){

        String sql = "INSERT INTO seller (Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                "VALUES(?, ?, ?, ?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, seller.getName());
            statement.setString(2, seller.getEmail());
            statement.setDate(3, Date.valueOf(seller.getBirthDate()));
            statement.setDouble(4 ,seller.getBaseSalary());
            statement.setInt(5, 1);

            Integer rowsInserted = statement.executeUpdate();

            if(rowsInserted > 0){

                ResultSet id = statement.getGeneratedKeys();

                id.next();

                Integer sellerId = id.getInt(1);

                System.out.println("Rows inserted: " + rowsInserted);
                System.out.println("Id: " + sellerId);

                seller.setId(sellerId);

            }

        } catch (Exception e){
            throw new DatabaseException(e.getMessage());
        }

        return seller;
    }

    public void updateSalary(Integer departmentId, Double bonus){

        String sql = "UPDATE seller SET BaseSalary = BaseSalary + ? WHERE DepartmentId = ?";

        try {

            PreparedStatement statement = connection.prepareStatement(sql);//crl+alt+v
            statement.setDouble(1, bonus);
            statement.setInt(2, departmentId);

            Integer rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0){
                System.out.println("Rows updated: " + rowsUpdated);
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } 

    }

    public void delete(Integer id){

        String sql = "DELETE FROM seller WHERE Id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, id);
            Integer rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0){
                System.out.println("Rows deleted: " + rowsDeleted);
            }

        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        } 
    }

    public Seller getById(Integer id) {

        Seller seller;

        String sql = "SELECT seller.*,department.Name as DepName " +
                "FROM seller " +
                "INNER JOIN department " +
                "ON seller.DepartmentId = department.Id " +
                "WHERE seller.Id = ?";

        try {

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();
            if (result.next()){
                seller = getSeller(result);
            } else {
                throw new DatabaseException("Vendedor não encontrado");
            }

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }

        return seller;
    }

    public void findByDepatment(Integer id) {
        Seller seller;
        Department department;

        String sql = "SELECT seller.*,department.Name as DepName " +
                "FROM seller INNER JOIN department " +
                "ON seller.DepartmentId = department.Id " +
                "WHERE DepartmentId = ? " +
                "ORDER BY Name";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();


        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    private static Seller getSeller(ResultSet result) throws SQLException {
        Department department;
        Seller seller = new Seller();

        seller.setId(result.getInt("Id"));
        seller.setName(result.getString("Name"));
        seller.setEmail(result.getString("Email"));
        seller.setBirthDate(result.getDate("BirthDate").toLocalDate());
        seller.setBaseSalary(result.getDouble("BaseSalary"));

        department = extracted(result);

        seller.setDepartment(department);

        return seller;
    }

    private static Department extracted(ResultSet result) throws SQLException {
        Department department = new Department();
        department.setId(result.getInt("DepartmentId"));
        department.setName(result.getString("DepName"));
        return department;
    }

}