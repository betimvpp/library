package com.livraria;

import com.livraria.database.DatabaseConnection;
import com.livraria.database.DatabaseInitializer;
import com.livraria.model.*;
import com.livraria.model.enums.Role;
import com.livraria.service.*;
import com.livraria.ui.BibliotecaConsole;

import java.time.LocalDate;

/**
 * Ponto de entrada da aplicação Livraria.
 * Responsável por inicializar o banco, garantir dados de demonstração
 * e delegar a interação com o usuário para BibliotecaConsole.
 */
public class Main {

        public static void main(String[] args) {
                DatabaseInitializer.inicializar();

                UsuarioService usuarioService = new UsuarioService();
                LivrariaService livrariaService = new LivrariaService();
                AutorService autorService = new AutorService();
                LivroService livroService = new LivroService();
                LivroPorLivrariaService lplService = new LivroPorLivrariaService();
                EmprestimoService emprestimoService = new EmprestimoService();
                VendaService vendaService = new VendaService();

                Livraria livraria = garantirDadosIniciais(
                                usuarioService, livrariaService, autorService, livroService, lplService);

                new BibliotecaConsole(
                                livraria, usuarioService, autorService,
                                livroService, lplService, emprestimoService, vendaService).iniciar();

                DatabaseConnection.closeConnection();
        }

        /**
         * Verifica se já existem dados no banco; se não, cria o conjunto de
         * demonstração.
         */
        private static Livraria garantirDadosIniciais(
                        UsuarioService usuarioService,
                        LivrariaService livrariaService,
                        AutorService autorService,
                        LivroService livroService,
                        LivroPorLivrariaService lplService) {

                // Se já há acervo, a livraria com id=1 já existe
                if (!lplService.listarPorLivraria(1).isEmpty()) {
                        return livrariaService.buscarPorId(1).orElseGet(() -> criarDados(
                                        usuarioService, livrariaService, autorService, livroService, lplService));
                }
                return criarDados(usuarioService, livrariaService, autorService, livroService, lplService);
        }

        private static Livraria criarDados(
                        UsuarioService usuarioService,
                        LivrariaService livrariaService,
                        AutorService autorService,
                        LivroService livroService,
                        LivroPorLivrariaService lplService) {

                System.out.println("\nCriando acervo inicial de demonstração...");

                Usuario admin = obterOuCriarAdmin(usuarioService);
                Livraria livraria = obterOuCriarLivraria(livrariaService, admin.getId());

                Autor robertMartin = obterOuCriarAutor(autorService,
                                "Robert C. Martin", "11111111111", LocalDate.of(1952, 12, 5),
                                "uncle.bob@cleancode.com");
                Autor martinFowler = obterOuCriarAutor(autorService,
                                "Martin Fowler", "22222222222", LocalDate.of(1963, 12, 18), "martin@martinfowler.com");

                adicionarLivroAcervo(livroService, lplService, robertMartin.getId(), livraria.getId(),
                                "Clean Code", LocalDate.of(2008, 8, 1), 5);
                adicionarLivroAcervo(livroService, lplService, robertMartin.getId(), livraria.getId(),
                                "The Clean Coder", LocalDate.of(2011, 5, 13), 3);
                adicionarLivroAcervo(livroService, lplService, martinFowler.getId(), livraria.getId(),
                                "Refactoring", LocalDate.of(1999, 7, 8), 4);
                adicionarLivroAcervo(livroService, lplService, martinFowler.getId(), livraria.getId(),
                                "Patterns of Enterprise Application Architecture", LocalDate.of(2002, 11, 15), 2);

                System.out.println("Acervo criado com sucesso!\n");
                return livraria;
        }

        private static Usuario obterOuCriarAdmin(UsuarioService svc) {
                try {
                        return svc.cadastrarUsuario("00000000000", "Administrador",
                                        LocalDate.of(1980, 1, 1), Role.ADMIN, "admin@livraria.com");
                } catch (Exception e) {
                        return svc.buscarPorCpf("00000000000").orElseThrow();
                }
        }

        private static Livraria obterOuCriarLivraria(LivrariaService svc, int idAdmin) {
                try {
                        return svc.criarLivraria(idAdmin, "Livraria Central", "Sao Paulo");
                } catch (Exception e) {
                        return svc.buscarPorId(1).orElseThrow();
                }
        }

        private static Autor obterOuCriarAutor(AutorService svc,
                        String nome, String cpf, LocalDate nasc, String email) {
                try {
                        return svc.cadastrarAutor(nome, cpf, nasc, email);
                } catch (Exception e) {
                        return svc.buscarPorCpf(cpf).orElseThrow();
                }
        }

        private static void adicionarLivroAcervo(LivroService livroSvc, LivroPorLivrariaService lplSvc,
                        int idAutor, int idLivraria, String titulo, LocalDate lancamento, int estoque) {
                try {
                        Livro livro = livroSvc.publicarLivro(idAutor, titulo, lancamento);
                        lplSvc.adicionarAcervo(idLivraria, livro.getId(), estoque);
                } catch (Exception ignored) {
                        // ja existia
                }
        }
}