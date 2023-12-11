package br.com.sptech.modelo.banco.jdbc.dao;

import br.com.sptech.modelo.banco.jdbc.conexao.Conexao;
import br.com.sptech.modelo.banco.jdbc.modelo.ModelMemoria;
import br.com.sptech.modelo.banco.jdbc.modelo.ModelUsuario;
import org.springframework.jdbc.core.JdbcTemplate;

public class MemoriaDao {
    private Integer idInfo;

    public void salvarCapturaFixa(ModelMemoria novaCapturaRam, Integer idMaquina) {
        if (idMaquina != null) {
            Conexao conexao = new Conexao();
            JdbcTemplate con = conexao.getConexaoDoBanco();

            con.update("INSERT INTO info_componente (total)  VALUES (?)", novaCapturaRam.getRamTotal());

            idInfo = con.queryForObject("SELECT SCOPE_IDENTITY()" , Integer.class);

            con.update("INSERT INTO componente (nome, parametro, fk_info, fk_maquina) VALUES (?, ?, ?, ?)", "memoria", "bytes", idInfo, idMaquina);

            con.update("INSERT INTO parametro_componente (valor_minimo, valor_maximo, fk_componente) VALUES ( ?, ?, ?)", 30, 80, idInfo);

        } else {
            throw new RuntimeException("Precisa existir uma máquina no banco primeiro.");
        }
    }

    public void salvarCapturaDinamica(ModelMemoria novaCapturaRam) {
        if (idInfo != null) {
            Conexao conexao = new Conexao();
            JdbcTemplate con = conexao.getConexaoDoBanco();
            con.update("INSERT INTO dados_captura (uso, data_captura, fk_componente, desligada) VALUES (?, ?, ?, ?)",
                    novaCapturaRam.getMemoriaUso(),
                    novaCapturaRam.getDataCaptura(),
                    idInfo,
                    novaCapturaRam.getDesligada()
            );
        } else {
            throw new RuntimeException("ID não foi capturado. Execute salvarCapturaFixa() primeiro.");
        }
    }

    public void buscarDadosFixo(Integer idMaquina) {

        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();

        String sql = "SELECT c.id_componente\n" +
                "FROM componente c\n" +
                "JOIN info_componente i ON c.fk_info = i.id_info\n" +
                "JOIN maquina m ON c.fk_maquina = m.id_maquina\n" +
                "WHERE c.nome = ? AND m.id_maquina = ?";
        idInfo = con.queryForObject(sql, Integer.class, "memoria", idMaquina);
    }
}


