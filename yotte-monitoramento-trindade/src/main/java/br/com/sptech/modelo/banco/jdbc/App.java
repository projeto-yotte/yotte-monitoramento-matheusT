package br.com.sptech.modelo.banco.jdbc;
import br.com.sptech.modelo.banco.jdbc.conexao.Conexao;
import br.com.sptech.modelo.banco.jdbc.dao.*;
import br.com.sptech.modelo.banco.jdbc.modelo.*;
import br.com.sptech.modelo.banco.jdbc.servico.Maquina;
import com.github.britooo.looca.api.group.discos.Disco;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.janelas.Janela;
import com.github.britooo.looca.api.group.janelas.JanelaGrupo;
import com.github.britooo.looca.api.group.memoria.Memoria;
import com.github.britooo.looca.api.core.Looca;
import br.com.sptech.modelo.banco.jdbc.validacoes.ValidacoesUsuario;
import com.github.britooo.looca.api.group.processador.Processador;
import com.github.britooo.looca.api.group.processos.Processo;
import com.github.britooo.looca.api.group.processos.ProcessoGrupo;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class App {
    private static String logUserName = "";

    public static void setLogUserName(String userName) {
        logUserName = userName;
    }
    private static String getLogFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(new Date());
        return dateStr + "-" + logUserName + "-.txt";
    }

    public static void log(String message) {
        try {
            String logFileName = getLogFileName();
            FileWriter fw = new FileWriter(logFileName, true);
            BufferedWriter bw = new BufferedWriter(fw);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = sdf.format(new Date());
            String logMessage = timestamp + " - " + message;

            bw.write(logMessage);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logError(String errorMessage, Exception exception) {
        try {
            String logFileName = getLogFileName();
            FileWriter fw = new FileWriter(logFileName, true);
            BufferedWriter bw = new BufferedWriter(fw);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = sdf.format(new Date());
            String logMessage = timestamp + " - " + errorMessage + ": " + exception.getMessage();

            bw.write(logMessage);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        // Crie um agendador de tarefas
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Maquina maquina01 = new Maquina();

        ModelUsuario novoUsuario = new ModelUsuario();
        UsuarioDao usuarioDao = new UsuarioDao();

        FuncionarioDao funcionarioDao = new FuncionarioDao();
        ModelFuncionario novoFunc = new ModelFuncionario();

        ModelAdm loginAdm = new ModelAdm();
        AdmDao admDao = new AdmDao();

        Looca looca = new Looca();

        Scanner leitor = new Scanner(System.in);
        Scanner leitorTexto = new Scanner(System.in);
        Boolean logado = false;
        ModelUsuario usuario = new ModelUsuario();

        ValidacoesUsuario validacoesUsuario = new ValidacoesUsuario();


        // TESTES DE SEGUNDO PLANO ======================
        ActiveWindowDetector windowDetector = new ActiveWindowDetector();
        List<ActiveWindowDetector.WindowInfo> windowsInfo = windowDetector.getActiveWindowInfo();

        for (ActiveWindowDetector.WindowInfo ws : windowsInfo) {
            if (ws != null) {
                System.out.println("Window Name: " + ws.getWindowName());
                System.out.println("PID: " + ws.getProcessId());
                System.out.println("Is in Foreground: " + ws.isInForeground());
            }
        }

        // ===============================================
        String[] opcoes = {"Não", "Sim", "Sair"};

        int escolha = JOptionPane.showOptionDialog(
                null,
                "Já tem cadastro:",
                "Yotte Trindade",
                JOptionPane.YES_NO_CANCEL_OPTION, // Tipo de opções
                JOptionPane.QUESTION_MESSAGE,    // Tipo de ícone
                null,                           // Ícone personalizado (use null para o padrão)
                opcoes,                         // Conjunto de opções
                opcoes[2]                       // Opção padrão selecionada
        );

        if (escolha == 0) {
            System.out.println("Opção 1 selecionada");
        } else if (escolha == 1) {
            System.out.println("Opção 2 selecionada");
        } else {
            System.out.println("Opção Cancelar ou fechar");
        }

//        Integer opcao = leitor.nextInt();

        if (escolha == 0) {
            // Cadastro de novo usuário
            String nome;
            String email;
            String senha;
            String area;
            String cargo;
            String empresa;
            String ip;
            String modelo;
            String so;
            String matricula;
            Boolean todasValidacoes = false;


            do {
                try {
                    System.out.println("Fazer cadastro...");

                    nome = JOptionPane.showInputDialog(
                            null,
                            "Digite seu nome:",
                            "Yotte Trindade",
                            JOptionPane.PLAIN_MESSAGE
                    );

                    email = JOptionPane.showInputDialog(
                            null,
                            "Digite seu e-mail:",
                            "Yotte Trindade",
                            JOptionPane.PLAIN_MESSAGE
                    );

                    senha = JOptionPane.showInputDialog(
                            null,
                            "Digite sua senha:",
                            "Yotte Trindade",
                            JOptionPane.PLAIN_MESSAGE
                    );

                    area = JOptionPane.showInputDialog(
                            null,
                            "Digite sua área:",
                            "Yotte Trindade",
                            JOptionPane.PLAIN_MESSAGE
                    );

                    cargo = JOptionPane.showInputDialog(
                            null,
                            "Digite seu cargo:",
                            "Yotte Trindade",
                            JOptionPane.PLAIN_MESSAGE
                    );

                    empresa = JOptionPane.showInputDialog(
                            null,
                            "Digite sua empresa:",

                            "Yotte Trindade",
                            JOptionPane.PLAIN_MESSAGE
                    );

                    ip = JOptionPane.showInputDialog(
                            null,
                            "Digite seu IP:",
                            "Yotte Trindade",
                            JOptionPane.PLAIN_MESSAGE
                    );

                    modelo = JOptionPane.showInputDialog(
                            null,
                            "Digite o modelo do notebook:",
                            "Yotte Trindade",
                            JOptionPane.PLAIN_MESSAGE
                    );

                    so = JOptionPane.showInputDialog(
                            null,
                            "Digite seu SO:",
                            "Yotte Trindade",
                            JOptionPane.PLAIN_MESSAGE
                    );

                    matricula = JOptionPane.showInputDialog(
                            null,
                            "Digite seu token:",
                            "Yotte Trindade",
                            JOptionPane.PLAIN_MESSAGE
                    );

                    Boolean isSenhaValida = validacoesUsuario.isSenhaValida(senha);
                    Boolean isSenhaComplexa = validacoesUsuario.isSenhaComplexa(senha);
                    Boolean emailNaoTemEspacos = validacoesUsuario.naoTemEspacos(email);
                    Boolean isEmailValido = validacoesUsuario.isEmailValido(email);

                    if (isSenhaValida && emailNaoTemEspacos && isEmailValido && isSenhaComplexa) {
                        todasValidacoes = true;

                        try {
                            if (usuarioDao.isTokenValido(matricula)) {
                                if (usuarioDao.buscarEmpresaPorNome(empresa) != null) {
                                    Conexao con = new Conexao();
                                    JdbcTemplate conMySQL = con.getConexaoDoBancoMySQL();
                                    JdbcTemplate conSql = con.getConexaoDoBancoSQLServer();

                                    Integer fkEmpresa = usuarioDao.buscarEmpresaPorNome(empresa);

                                    novoUsuario.setNome(nome);
                                    novoUsuario.setEmail(email);
                                    novoUsuario.setSenha(senha);
                                    novoUsuario.setArea(area);
                                    novoUsuario.setCargo(cargo);
                                    novoUsuario.setFkEmpresa(fkEmpresa);
                                    novoUsuario.setFkTipoUsuario(3);
                                    logUserName = nome;

                                    MaquinaDao maquinaDao = new MaquinaDao();
                                    ModelMaquina novaMaquina = new ModelMaquina();
                                    novaMaquina.setIp(ip);
                                    novaMaquina.setSo(so);
                                    novaMaquina.setModelo(modelo);

                                    usuarioDao.salvarUsuario(novoUsuario);
                                    maquinaDao.salvarMaquina(novaMaquina, usuarioDao.buscarIdUsuario(novoUsuario), usuarioDao.buscarIdToken(matricula), conMySQL, conSql);
                                    maquina01.buscarIdMaquina(usuarioDao.buscarIdUsuario(novoUsuario));

                                    logado = true;
                                    log("Cadastro bem-sucedido para o usuário " + nome);
                                    log("Área de atuação " + area);
                                    log("Sistema Operacional" + so);
                                    System.out.println("Cadastrado com sucesso!");
                                }
                            } else {
                                System.out.println("Seu token não é válido!");
                            }
                        } catch (Exception e) {
                            logError("Erro durante o processo de cadastro", e);
                        }
                        log("Email e senha válidas " + email);
                    } else {
                        System.out.println("Dados inválidos, faça o cadastro novamente!!");
                    }
                } catch (Exception e) {
                    logError("Erro durante a verificação de senha e email", e);
                }
            } while (!todasValidacoes);

        } else if (escolha == 1) {
            // Login
            Boolean todasValidacoesLogin = false;
            String validarEmail;
            String validarSenha;

            do {
                try {

                    validarEmail = JOptionPane.showInputDialog(
                            null,
                            "Digite seu e-mail:",
                            "Yotte Trindade",
                            JOptionPane.PLAIN_MESSAGE
                    );
                    App.setLogUserName(validarEmail);
                    validarSenha = JOptionPane.showInputDialog(
                            null,
                            "Digite sua Senha:",
                            "Yotte Trindade",
                            JOptionPane.PLAIN_MESSAGE
                    );

                    System.out.println(validarEmail);

                    usuario.setEmail(validarEmail);
                    usuario.setSenha(validarSenha);
                    System.out.println(usuarioDao.isUsuarioExistente(usuario));
                    if (usuarioDao.isUsuarioExistente(usuario)) {
                        todasValidacoesLogin = true;

                        System.out.println("Id usuario: " + usuarioDao.buscarIdUsuario(usuario));
                        System.out.println(usuarioDao.buscarFkTipoUsuario(usuario));

                        System.out.println("ou");
                        logado = true;
                        if (usuarioDao.buscarFkTipoUsuario(usuario).equals(2)) {
                            Scanner scanneremail = new Scanner(System.in);
                            Scanner scanner01 = new Scanner(System.in);
                            ;
                            System.out.println("O que deseja fazer?\n" +
                                    "                - 0 (Ver usuario especifico)\n" +
                                    "                - 1 (Lista de funcionarios)\n"
                                   );
                            Integer opcao2 = scanner01.nextInt();
                            if (opcao2 == 0) {
                                System.out.println("Digite o email do funcionario que voce quer ver:");
                                String email = scanneremail.nextLine();
                                System.out.println("Deseja ver os dados coletados de quantas horas atras:");
                                Integer tempo = scanner01.nextInt();

                                System.out.println(admDao.buscarFuncEmail(email, tempo));
                            } else if (opcao2 == 1) {
                                System.out.println(admDao.buscarListFunc(usuario));
                            }
                        }
                        log("Login bem-sucedido para o usuário: " + usuario.getEmail());
                        if (usuarioDao.isUsuarioExistente(usuario)){
                            maquina01.buscarIdMaquina(usuarioDao.buscarIdUsuario(usuario));
                        }

                    } else {
                        System.out.println("Email ou senha incorretas. Tente novamente!");
                    }
                } catch (Exception e) {
                    logError("Erro durante o processo de login", e);
                }
            } while (!todasValidacoesLogin);


            System.out.println("Login realizado com sucesso! \uD83D\uDE04");

            // Restante do código para capturar dados de memória
        } else if (escolha == 2) {
            System.out.println("Saindo da aplicação.");
        } else {
            System.out.println("Opção inválida.");
        }

        // Fecha os recursos necessários, como Scanners
        leitor.close();
        leitorTexto.close();




        if (logado.equals(true)) {
            if (usuarioDao.buscarFkTipoUsuario(novoUsuario).equals(3) || usuarioDao.buscarFkTipoUsuario(usuario).equals(3)) {
                // Obtenha os dados da API Looca
                Memoria memoria = looca.getMemoria();

                Processador cpu = looca.getProcessador();

                DiscoGrupo grupoDeDiscos = looca.getGrupoDeDiscos();
                List<Disco> discos = grupoDeDiscos.getDiscos();

                JanelaGrupo grupodDeJanelas = looca.getGrupoDeJanelas();
                List<Janela> janelas = grupodDeJanelas.getJanelas();

                ProcessoGrupo grupoDeProcessos = looca.getGrupoDeProcessos();
                List<Processo> processos = grupoDeProcessos.getProcessos();

                ModelMemoria novaCapturaRam = new ModelMemoria();
                ModelCpu novaCapturaCpu = new ModelCpu();
                ModelDisco novaCapturaDisco = new ModelDisco();
                ModelJanela novaCapturaJanela = new ModelJanela();
                ModelProcesso novaCapturaProcesso = new ModelProcesso();


                // IF para captura fixa, acontece apenas 1 vez.
                if (!maquina01.isComponenteSalvo(maquina01.getIdMaquina())) {
                    novaCapturaRam.setRamTotal(memoria.getTotal());
                    novaCapturaCpu.setNumCPUsFisicas(cpu.getNumeroCpusFisicas());
                    novaCapturaCpu.setNumCPUsLogicas(cpu.getNumeroCpusLogicas());

                    for (Disco disco : discos) {
                        novaCapturaDisco.setTotalDisco(disco.getTamanho());
                    }

                    maquina01.capturarDadosFixo(novaCapturaRam, novaCapturaCpu, novaCapturaDisco);

                } else {
                    maquina01.buscarDadosFixosDosComponentes();
                }
                ;

                // Scheduler de 10 segundos para capturas dinâmicas
                scheduler.scheduleAtFixedRate(() -> {
                    try {
                        // Crie uma nova instância da sua classe com os dados capturados
                        novaCapturaRam.setMemoriaUso(memoria.getEmUso());
                        novaCapturaRam.setDataCaptura(new Date());
                        novaCapturaRam.setDesligada(false);

                        novaCapturaCpu.setUsoCpu(cpu.getUso());
                        novaCapturaCpu.setFreq(cpu.getFrequencia());
                        novaCapturaCpu.setDataCaptura(new Date());
                        novaCapturaCpu.setDesligada(false);

                        for (Disco disco : discos) {
                            novaCapturaDisco.setBytesEscrita(disco.getBytesDeEscritas());
                            novaCapturaDisco.setDataCaptura(new Date());
                            novaCapturaDisco.setBytesLeitura(disco.getBytesDeLeitura());
                            novaCapturaDisco.setEscritas(disco.getEscritas());
                            novaCapturaDisco.setLeituras(disco.getLeituras());
                            novaCapturaDisco.setTamanhoFila(disco.getTamanhoAtualDaFila());
                            novaCapturaDisco.setDesligada(false);
                        }

                        int tamanho = Math.max(windowsInfo.size(), Math.max(janelas.size(), processos.size()));

                        for (int i = 0; i < tamanho; i++) {
                            Janela janela = (i < janelas.size()) ? janelas.get(i) : null;
                            Processo processo = (i < processos.size()) ? processos.get(i) : null;
                            ActiveWindowDetector.WindowInfo ws = (i < windowsInfo.size()) ? windowsInfo.get(i) : new ActiveWindowDetector.WindowInfo("", 0, false, null);
                            ActiveWindowDetector activeWindowDetector = new ActiveWindowDetector();

                            long pidJanela = (janela != null && janela.getPid() != null) ? janela.getPid() : (long) ws.getProcessId();

                            // Verificar se o PID da janela corresponde ao PID do ws
                            if (ws != null && ws.getProcessId() != 0 && pidJanela != 0 && pidJanela != ws.getProcessId()) {
                                System.out.println("Aviso: PID da janela não corresponde ao PID do ws.");
                            } else {
                                novaCapturaJanela.setPid(pidJanela);
                                novaCapturaJanela.setTitulo((janela != null && janela.getTitulo() != null) ? janela.getTitulo() : ((ws != null) ? ws.getWindowName() : null));
                                novaCapturaJanela.setComando((janela != null && janela.getComando() != null) ? janela.getComando() : "");
//                            novaCapturaJanela.setVisivel(activeWindowDetector.isPidInForeground(novaCapturaJanela.getPid()));
                                novaCapturaJanela.setDataCaptura(new Date());

                                novaCapturaProcesso.setPid((processo != null) ? processo.getPid() : null);
                                novaCapturaProcesso.setUsoCpu((processo != null && processo.getUsoCpu() != null) ? processo.getUsoCpu() : ((ws != null && ws.getPerformanceInfo() != null) ? ws.getPerformanceInfo().getCpuUsage() : 0.0));
                                novaCapturaProcesso.setUsoMemoria((processo != null && processo.getUsoMemoria() != null) ? processo.getUsoMemoria() : ((ws != null && ws.getPerformanceInfo() != null) ? ws.getPerformanceInfo().getMemoryUsage() : 0.0));
                                novaCapturaProcesso.setBytesUtilizados((processo != null) ? processo.getBytesUtilizados() : null);

//                            if (novaCapturaJanela.getPid() != 0 && (novaCapturaJanela.getTitulo() != null || ws.getWindowName() != null)) {
//                                maquina01.capturarJanelasProcessos(novaCapturaJanela, novaCapturaProcesso);
//                            }
                            }
                        }


                        maquina01.capturarDadosDinamico(novaCapturaRam, novaCapturaCpu, novaCapturaDisco);

                        // Imprima uma mensagem de sucesso no console
                        System.out.println("Dados de memória capturados e salvos com sucesso!");
                        log("Dados de memória capturados e salvos com sucesso" + memoria);
                    } catch (Exception e) {
                        // Imprima quaisquer erros no console
                        e.printStackTrace();
                        System.err.println("Erro ao capturar e salvar dados de memória.");
                        logError("Erro ao capturar e salvar dados de memória", e);
                    }
                }, 0, 10, TimeUnit.SECONDS);
            }

            // Fechando os Scanners pra não derreter a memória (ou a RAM sei lá)
            leitor.close();
            leitorTexto.close();
        }
    }
}

