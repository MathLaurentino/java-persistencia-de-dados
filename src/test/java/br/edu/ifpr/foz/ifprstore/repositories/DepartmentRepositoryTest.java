package br.edu.ifpr.foz.ifprstore.repositories;

import br.edu.ifpr.foz.ifprstore.connection.ConnectionFactory;
import br.edu.ifpr.foz.ifprstore.exceptions.DatabaseException;
import br.edu.ifpr.foz.ifprstore.models.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentRepositoryTest {

    @Test
    public void deveInserirUmRegistroNaTabelaDepartment() {
        DepartmentRepository repository = new DepartmentRepository();

        Department departmentFake = new Department();
        departmentFake.setName("Tecnologia");

        Department department = repository.insert(departmentFake);

        System.out.println(department);
    }

    @Test
    public void deveAtualizarUmDepartment() {
        DepartmentRepository repository = new DepartmentRepository();

        Department departmentToUpdate = new Department();
        departmentToUpdate.setId(1);
        departmentToUpdate.setName("Recursos Humanos");

        repository.update(departmentToUpdate);
    }

    @Test
    public void deveDeletarUmDepartment() {
        DepartmentRepository repository = new DepartmentRepository();

        repository.delete(5);
    }

    @Test
    public void deveRetornarTodosOsDepartments() {
        DepartmentRepository repository = new DepartmentRepository();

        List<Department> departments = repository.getAll();

        for (Department d : departments) {
            System.out.println(d);
        }
    }

    @Test
    public void deveRetornarUmDepartmentPeloId() {
        DepartmentRepository repository = new DepartmentRepository();

        Department department = repository.getById(1);

        System.out.println(department);
    }

    @Test
    public void deveRetornarDepartmentsPeloNome() {
        DepartmentRepository repository = new DepartmentRepository();

        List<Department> departments = repository.findByName("Tecnologia");

        for (Department d : departments) {
            System.out.println(d);
        }
    }

    @Test
    public void deveRetornarDepartmentsSemSellers() {
        DepartmentRepository repository = new DepartmentRepository();

        List<Department> departments = repository.getDepartmentsWithoutSellers();

        for (Department d : departments) {
            System.out.println(d);
        }
    }
}