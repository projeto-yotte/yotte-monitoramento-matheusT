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
        return dateStr + "-" + logUserName + ".txt";
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

        // for (ActiveWindowDetector.WindowInfo ws : windowsInfo) {
        //     if (ws != null) {
        //         System.out.println("Window Name: " + ws.getWindowName());
        //         System.out.println("PID: " + ws.getProcessId());
        //         System.out.println("Is in Foreground: " + ws.isInForeground());
        //     }
        // }

        // ===============================================

        System.out.println("""
                        \033[1;31m
                        :::   :::  ::::::::  ::::::::::: ::::::::::: ::::::::::
                        :+:   :+: :+:    :+:     :+:         :+:     :+:
                         +:+ +:+  +:+    +:+     +:+         +:+     +:+
                          +#++:   +#+    +:+     +#+         +#+     +#++:++#
                           +#+    +#+    +#+     +#+         +#+     +#+
                           #+#    #+#    #+#     #+#         #+#     #+#
                           ###     ########      ###         ###     ##########
                \033[0m
                \033[1;34m
                Já tem cadastro?
                - 0 (Caso não)
                - 1 (Caso sim)
                - 2 (Para sair da aplicação)
                \033[0m
                """);
        Integer opcao = leitor.nextInt();

        if (opcao == 0) {
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
                    System.out.println("\033[1;31mFazer cadastro...\033[0m");

                    System.out.println("\033[1;34mDigite seu nome:\033[0m");
                    nome = leitorTexto.nextLine();

                    System.out.println("\033[1;34mDigite seu email:\033[0m");
                    email = leitorTexto.nextLine();
                    App.setLogUserName(email);

                    System.out.println("\033[1;34mDigite sua senha:\033[0m");
                    senha = leitorTexto.nextLine();

                    System.out.println("\033[1;34mDigite seu área de atuação:\033[0m");
                    area = leitorTexto.nextLine();

                    System.out.println("\033[1;34mDigite seu cargo:\033[0m");
                    cargo = leitorTexto.nextLine();

                    System.out.println("\033[1;34mDigite sua empresa:\033[0m");
                    empresa = leitorTexto.nextLine();

                    System.out.println("\033[1;34mDigite seu IP:\033[0m");
                    ip = leitorTexto.nextLine();

                    System.out.println("\033[1;34mDigite o modelo do notebook:\033[0m");
                    modelo = leitorTexto.nextLine();

                    System.out.println("\033[1;34mDigite qual SO você utiliza:\033[0m");
                    so = leitorTexto.nextLine();

                    System.out.println("\033[1;34mDigite seu token de acesso:\033[0m");
                    matricula = leitorTexto.nextLine();

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
                                    JdbcTemplate conMySQL = con.getConexaoDoBanco();
                                    JdbcTemplate conSql = con.getConexaoDoBanco();

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
                                    maquinaDao.salvarMaquina(novaMaquina, usuarioDao.buscarIdUsuario(novoUsuario), usuarioDao.buscarIdToken(matricula));
                                    maquina01.buscarIdMaquina(usuarioDao.buscarIdUsuario(novoUsuario));

                                    logado = true;
                                    log("Cadastro bem-sucedido para o usuário " + nome);
                                    log("Área de atuação" + area);
                                    log("Sistema Operacional" + so);
                                    System.out.println("\033[1;31mCadastrado com sucesso!\033[0m");
                                }
                            } else {
                                System.out.println("\033[1;35mSeu token não é válido!\033[0m");
                            }
                        } catch (Exception e) {
                            logError("\033[1;35mErro durante o processo de cadastro\033[0m", e);
                        }
                        log("\033[1;35mEmail e senha válidas " + email);
                    } else {
                        System.out.println("\033[1;35mDados inválidos, faça o cadastro novamente!!\033[0m");
                    }
                } catch (Exception e) {
                    logError("\033[1;35mErro durante a verificação de senha e email\033[0m", e);
                }
            } while (!todasValidacoes);

        } else if (opcao == 1) {
            // Login
            Boolean todasValidacoesLogin = false;
            String validarEmail;
            String validarSenha;

            do {
                try {

                    System.out.println("\033[1;34mDigite seu email:\033[0m");
                    validarEmail = leitorTexto.nextLine();
                    App.setLogUserName(validarEmail);
                    System.out.println("\033[1;34mDigite sua senha:\033[0m");
                    validarSenha = leitorTexto.nextLine();

                    usuario.setEmail(validarEmail);
                    usuario.setSenha(validarSenha);
                    System.out.println(usuarioDao.isUsuarioExistente(usuario));
                    if (usuarioDao.isUsuarioExistente(usuario)) {
                        todasValidacoesLogin = true;

                        System.out.println("\033[1;31mId usuario:\033[0m" + usuarioDao.buscarIdUsuario(usuario));
                        System.out.println(usuarioDao.buscarFkTipoUsuario(usuario));

                        System.out.println("ou");
                        logado = true;
                        if (usuarioDao.buscarFkTipoUsuario(usuario).equals(2)) {
                            Scanner scanneremail = new Scanner(System.in);
                            Scanner scanner01 = new Scanner(System.in);
                            ;
                            System.out.println("\033[1;34mO que deseja fazer?\n" +
                                    "                - 0 (Ver usuario especifico)\n" +
                                    "                - 1 (Lista de funcionarios)\033[0m\n"
                            );
                            Integer opcao2 = scanner01.nextInt();
                            if (opcao2 == 0) {
                                System.out.println("\033[1;34mDigite o email do funcionario que voce quer ver:\033[0m");
                                String email = scanneremail.nextLine();
                                System.out.println("\033[1;34mDeseja ver os dados coletados de quantas horas atras:\033[0m");
                                Integer tempo = scanner01.nextInt();

                                System.out.println(admDao.buscarFuncEmail(email, tempo));
                            } else if (opcao2 == 1) {
                                System.out.println(admDao.buscarListFunc(usuario));
                            }
                        }
                        log("\033[1;31mLogin bem-sucedido para o usuário: \033[0m" + usuario.getEmail());
                        if (usuarioDao.isUsuarioExistente(usuario)) {
                            maquina01.buscarIdMaquina(usuarioDao.buscarIdUsuario(usuario));
                        }

                    } else {
                        System.out.println("\033[1;35mEmail ou senha incorretas. Tente novamente!\033[0m");
                    }
                } catch (Exception e) {
                    logError("\033[1;35mErro durante o processo de login\033[0m", e);
                }
            } while (!todasValidacoesLogin);


            System.out.println("\033[1;31mLogin realizado com sucesso! \033[0m \uD83D\uDE04");

            // Restante do código para capturar dados de memória
        } else if (opcao == 2) {
            System.out.println("\033[1;31mSaindo da aplicação.\033[0m");
        } else {
            System.out.println("\033[1;35mOpção inválida.\033[0m");
        }

        // Fecha os recursos necessários, como Scanners
        leitor.close();
        leitorTexto.close();


        if (logado.equals(true)) {

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
                            novaCapturaJanela.setVisivel(activeWindowDetector.isPidInForeground(novaCapturaJanela.getPid()));
                            novaCapturaJanela.setDataCaptura(new Date());

                            novaCapturaProcesso.setPid((processo != null) ? processo.getPid() : null);
                            novaCapturaProcesso.setUsoCpu((processo != null && processo.getUsoCpu() != null) ? processo.getUsoCpu() : ((ws != null && ws.getPerformanceInfo() != null) ? ws.getPerformanceInfo().getCpuUsage() : 0.0));
                            novaCapturaProcesso.setUsoMemoria((processo != null && processo.getUsoMemoria() != null) ? processo.getUsoMemoria() : ((ws != null && ws.getPerformanceInfo() != null) ? ws.getPerformanceInfo().getMemoryUsage() : 0.0));
                            novaCapturaProcesso.setBytesUtilizados((processo != null) ? processo.getBytesUtilizados() : null);

                            if (novaCapturaJanela.getPid() != 0 && (novaCapturaJanela.getTitulo() != null || ws.getWindowName() != null)) {
                                maquina01.capturarJanelasProcessos(novaCapturaJanela, novaCapturaProcesso);
                            }
                        }
                    }


                    maquina01.capturarDadosDinamico(novaCapturaRam, novaCapturaCpu, novaCapturaDisco);

                    // Imprima uma mensagem de sucesso no console
                    System.out.println("\033[1;91mDados de memória capturados e salvos com sucesso!\033[0m");
                    log("Dados de memória capturados e salvos com sucesso" + memoria);
                } catch (Exception e) {
                    // Imprima quaisquer erros no console
                    e.printStackTrace();
                    System.err.println("\033[1;35mErro ao capturar e salvar dados de memória.\033[0m");
                    logError("Erro ao capturar e salvar dados de memória", e);
                }
            }, 0, 10, TimeUnit.SECONDS);
        }

        // Fechando os Scanners pra não derreter a memória (ou a RAM sei lá)
        leitor.close();
        leitorTexto.close();
    }
}


