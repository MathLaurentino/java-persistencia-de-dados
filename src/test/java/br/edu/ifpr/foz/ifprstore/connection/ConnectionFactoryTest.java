package br.edu.ifpr.foz.ifprstore.connection;

import org.junit.Test;

import java.sql.Connection;

public class ConnectionFactoryTest {

    @Test
    public void deveRealizarConexaoSemExcecao() {

        Connection connection = ConnectionFactory.getConnection();

    }
}
