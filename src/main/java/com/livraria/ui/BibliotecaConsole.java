package com.livraria.ui;

import com.livraria.model.*;
import com.livraria.model.enums.Role;
import com.livraria.service.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Responsável pela interação com o usuário via console.
 * Separa a lógica de apresentação das camadas de serviço e repositório.
 */
public class BibliotecaConsole {

    private final Scanner scanner;
    private final Livraria livraria;

    private final UsuarioService usuarioService;
    private final AutorService autorService;
    private final LivroService livroService;
    private final LivroPorLivrariaService lplService;
    private final EmprestimoService emprestimoService;
    private final VendaService vendaService;

    public BibliotecaConsole(
            Livraria livraria,
            UsuarioService usuarioService,
            AutorService autorService,
            LivroService livroService,
            LivroPorLivrariaService lplService,
            EmprestimoService emprestimoService,
            VendaService vendaService) {
        this.scanner = new Scanner(System.in);
        this.livraria = livraria;
        this.usuarioService = usuarioService;
        this.autorService = autorService;
        this.livroService = livroService;
        this.lplService = lplService;
        this.emprestimoService = emprestimoService;
        this.vendaService = vendaService;
    }

    public void iniciar() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║  Bem-vindo à " + livraria.getNome().trim() + "!");
        System.out.println("╚════════════════════════════════════════════╝");

        boolean rodando = true;
        while (rodando) {
            exibirMenu();
            String opcao = scanner.nextLine().trim();

            switch (opcao) {
                case "1" -> listarLivrosDisponiveis();
                case "2" -> realizarEmprestimo();
                case "3" -> devolverLivro();
                case "4" -> cadastrarLivro();
                case "5" -> buscarPorTitulo();
                case "6" -> buscarPorAutor();
                case "7" -> listarClientes();
                case "8" -> cadastrarCliente();
                case "9" -> historicoEmprestimos();
                case "10" -> comprarLivro();
                case "0" -> {
                    System.out.println("\nAté logo! Boas leituras! 📚");
                    rodando = false;
                }
                default -> System.out.println("Opção inválida. Tente novamente.");
            }
        }

        scanner.close();
    }

    // ─── MENU ─────────────────────────────────────────────────────────────────

    private void exibirMenu() {
        System.out.println("\n╔══════════════════════════════════╗");
        System.out.println("║         MENU PRINCIPAL           ║");
        System.out.println("╠══════════════════════════════════╣");
        System.out.println("║  1. Livros disponíveis           ║");
        System.out.println("║  2. Realizar empréstimo          ║");
        System.out.println("║  3. Devolver livro               ║");
        System.out.println("║  4. Cadastrar novo livro         ║");
        System.out.println("║  5. Buscar por título            ║");
        System.out.println("║  6. Buscar por autor             ║");
        System.out.println("║  7. Listar clientes              ║");
        System.out.println("║  8. Cadastrar cliente            ║");
        System.out.println("║  9. Histórico de um cliente      ║");
        System.out.println("║ 10. Comprar livro                ║");
        System.out.println("║  0. Sair                         ║");
        System.out.println("╚══════════════════════════════════╝");
        System.out.print("Opção: ");
    }

    // ─── OPÇÕES ───────────────────────────────────────────────────────────────

    private void listarLivrosDisponiveis() {
        List<LivroPorLivraria> disponiveis = lplService.listarDisponiveis(livraria.getId());

        if (disponiveis.isEmpty()) {
            System.out.println("\nNenhum livro disponível no momento.");
            return;
        }

        System.out.println("\n─────────────────────────────────────────────────────────────");
        System.out.printf("%-5s %-38s %-22s %-5s%n", "ID", "Título", "Autor", "Disp.");
        System.out.println("─────────────────────────────────────────────────────────────");

        for (LivroPorLivraria lpl : disponiveis) {
            livroService.buscarPorId(lpl.getIdLivro()).ifPresent(livro -> {
                String nomeAutor = autorService.buscarPorId(livro.getIdAutor())
                        .map(Autor::getNomeCompleto).orElse("?");
                System.out.printf("%-5d %-38s %-22s %-5d%n",
                        lpl.getId(), truncar(livro.getTitulo(), 38),
                        truncar(nomeAutor, 22), lpl.getEstoqueDisponivel());
            });
        }
        System.out.println("─────────────────────────────────────────────────────────────");
    }

    private void realizarEmprestimo() {
        listarLivrosDisponiveis();

        System.out.print("\nID do exemplar para empréstimo (0 = cancelar): ");
        int idLpl = lerInt();
        if (idLpl == 0)
            return;

        Usuario consumidor = identificarOuCadastrarConsumidor();
        if (consumidor == null)
            return;

        try {
            Emprestimo emp = emprestimoService.realizarEmprestimo(idLpl, consumidor.getId());
            String titulo = lplService.buscarPorId(idLpl)
                    .flatMap(lpl -> livroService.buscarPorId(lpl.getIdLivro()))
                    .map(Livro::getTitulo).orElse("?");

            System.out.println("\n✅ Empréstimo realizado com sucesso!");
            System.out.println("   Livro       : " + titulo);
            System.out.println("   Cliente      : " + consumidor.getNomeCompleto());
            System.out.println("   Data         : " + emp.getDataEmprestimo());
            System.out.println("   Devolução    : " + emp.getDataEmprestimo().plusDays(14));
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void devolverLivro() {
        System.out.print("ID do empréstimo a devolver: ");
        int idEmp = lerInt();

        try {
            Emprestimo devolvido = emprestimoService.registrarDevolucao(idEmp);
            System.out.println("✅ Devolução registrada em " + devolvido.getDataDevolucao());
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void cadastrarLivro() {
        System.out.println("\n─── Cadastrar novo livro ───");

        List<Autor> autores = autorService.listarTodos();
        if (autores.isEmpty()) {
            System.out.println("Nenhum autor cadastrado. Cadastre um autor primeiro.");
            return;
        }

        System.out.println("Autores disponíveis:");
        autores.forEach(a -> System.out.printf("  [%d] %s%n", a.getId(), a.getNomeCompleto()));

        System.out.print("ID do autor: ");
        int idAutor = lerInt();

        System.out.print("Título: ");
        String titulo = scanner.nextLine().trim();

        System.out.print("Data de lançamento (YYYY-MM-DD): ");
        LocalDate data = lerData();
        if (data == null)
            return;

        System.out.print("Estoque inicial para esta livraria: ");
        int estoque = lerInt();
        if (estoque <= 0) {
            System.out.println("Estoque deve ser positivo.");
            return;
        }

        try {
            Livro livro = livroService.publicarLivro(idAutor, titulo, data);
            lplService.adicionarAcervo(livraria.getId(), livro.getId(), estoque);
            System.out.println("✅ Livro \"" + livro.getTitulo() + "\" cadastrado com " + estoque + " exemplares.");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void buscarPorTitulo() {
        System.out.print("Buscar por título: ");
        String termo = scanner.nextLine().trim();

        List<Livro> resultado = livroService.buscarPorTitulo(termo);
        exibirListaDeLivros(resultado);
    }

    private void buscarPorAutor() {
        System.out.print("Buscar por nome do autor: ");
        String termo = scanner.nextLine().trim();

        List<Livro> resultado = livroService.buscarPorNomeAutor(termo);
        exibirListaDeLivros(resultado);
    }

    private void listarClientes() {
        List<Usuario> todos = usuarioService.listarTodos();
        List<Usuario> consumidores = todos.stream()
                .filter(u -> u.getRole() == Role.CONSUMIDOR)
                .toList();

        if (consumidores.isEmpty()) {
            System.out.println("\nNenhum cliente cadastrado.");
            return;
        }

        System.out.println("\n─────────────────────────────────────────────────");
        System.out.printf("%-5s %-30s %-20s%n", "ID", "Nome", "Email");
        System.out.println("─────────────────────────────────────────────────");
        consumidores.forEach(c -> System.out.printf("%-5d %-30s %-20s%n",
                c.getId(), c.getNomeCompleto(),
                c.getEmail() != null ? c.getEmail() : "—"));
        System.out.println("─────────────────────────────────────────────────");
    }

    private void cadastrarCliente() {
        System.out.println("\n─── Cadastrar novo cliente ───");

        System.out.print("Nome completo: ");
        String nome = scanner.nextLine().trim();

        System.out.print("CPF (com ou sem pontuação): ");
        String cpf = scanner.nextLine().trim();

        System.out.print("Data de nascimento (YYYY-MM-DD): ");
        LocalDate dataNasc = lerData();
        if (dataNasc == null)
            return;

        System.out.print("Email (opcional, Enter para pular): ");
        String email = scanner.nextLine().trim();
        if (email.isBlank())
            email = null;

        try {
            Usuario novo = usuarioService.cadastrarUsuario(cpf, nome, dataNasc, Role.CONSUMIDOR, email);
            System.out.println("✅ Cliente \"" + novo.getNomeCompleto() + "\" cadastrado com ID " + novo.getId());
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void historicoEmprestimos() {
        listarClientes();

        System.out.print("\nID do cliente: ");
        int idConsumidor = lerInt();

        List<Emprestimo> historico = emprestimoService.listarPorConsumidor(idConsumidor);

        if (historico.isEmpty()) {
            System.out.println("Nenhum empréstimo encontrado para este cliente.");
            return;
        }

        System.out.println("\n─────────────────────────────────────────────────────────────");
        System.out.printf("%-5s %-35s %-13s %-13s%n", "ID", "Livro", "Emprestado", "Devolução");
        System.out.println("─────────────────────────────────────────────────────────────");

        for (Emprestimo emp : historico) {
            String titulo = lplService.buscarPorId(emp.getIdLivroPorLivraria())
                    .flatMap(lpl -> livroService.buscarPorId(lpl.getIdLivro()))
                    .map(Livro::getTitulo).orElse("?");

            System.out.printf("%-5d %-35s %-13s %-13s%n",
                    emp.getId(), truncar(titulo, 35),
                    emp.getDataEmprestimo(),
                    emp.getDataDevolucao() != null ? emp.getDataDevolucao().toString() : "pendente");
        }
        System.out.println("─────────────────────────────────────────────────────────────");
    }

    private void comprarLivro() {
        listarLivrosDisponiveis();

        System.out.print("\nID do exemplar para compra (0 = cancelar): ");
        int idLpl = lerInt();
        if (idLpl == 0)
            return;

        Usuario consumidor = identificarOuCadastrarConsumidor();
        if (consumidor == null)
            return;

        try {
            Venda venda = vendaService.realizarVenda(idLpl, consumidor.getId());
            String titulo = lplService.buscarPorId(idLpl)
                    .flatMap(lpl -> livroService.buscarPorId(lpl.getIdLivro()))
                    .map(Livro::getTitulo).orElse("?");

            System.out.println("\n🛒 Compra realizada com sucesso!");
            System.out.println("   Livro   : " + titulo);
            System.out.println("   Cliente : " + consumidor.getNomeCompleto());
            System.out.println("   Data    : " + venda.getDataVenda());
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    // ─── HELPERS ──────────────────────────────────────────────────────────────

    private void exibirListaDeLivros(List<Livro> livros) {
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado.");
            return;
        }

        System.out.println("\n─────────────────────────────────────────────────────────────");
        System.out.printf("%-5s %-38s %-22s %-12s%n", "ID", "Título", "Autor", "Lançamento");
        System.out.println("─────────────────────────────────────────────────────────────");

        for (Livro livro : livros) {
            String nomeAutor = autorService.buscarPorId(livro.getIdAutor())
                    .map(Autor::getNomeCompleto).orElse("?");
            System.out.printf("%-5d %-38s %-22s %-12s%n",
                    livro.getId(), truncar(livro.getTitulo(), 38),
                    truncar(nomeAutor, 22), livro.getDataLancamento());
        }
        System.out.println("─────────────────────────────────────────────────────────────");
    }

    private Usuario identificarOuCadastrarConsumidor() {
        System.out.print("Seu CPF (com ou sem pontuação): ");
        String cpf = scanner.nextLine().trim();

        Optional<Usuario> opt = usuarioService.buscarPorCpf(cpf);

        if (opt.isPresent()) {
            Usuario u = opt.get();
            if (u.getRole() != Role.CONSUMIDOR) {
                System.out.println("Este CPF pertence a um Administrador.");
                return null;
            }
            System.out.println("Bem-vindo de volta, " + u.getNomeCompleto() + "!");
            return u;
        }

        // Cadastro rápido
        System.out.println("CPF não encontrado. Vamos fazer seu cadastro!");
        System.out.print("Nome completo: ");
        String nome = scanner.nextLine().trim();

        System.out.print("Data de nascimento (YYYY-MM-DD): ");
        LocalDate dataNasc = lerData();
        if (dataNasc == null)
            return null;

        System.out.print("Email (opcional, Enter para pular): ");
        String email = scanner.nextLine().trim();

        try {
            return usuarioService.cadastrarUsuario(cpf, nome, dataNasc, Role.CONSUMIDOR,
                    email.isBlank() ? null : email);
        } catch (Exception e) {
            System.out.println("Erro no cadastro: " + e.getMessage());
            return null;
        }
    }

    private int lerInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private LocalDate lerData() {
        try {
            return LocalDate.parse(scanner.nextLine().trim());
        } catch (DateTimeParseException e) {
            System.out.println("Data inválida. Use o formato YYYY-MM-DD.");
            return null;
        }
    }

    private String truncar(String texto, int max) {
        if (texto == null)
            return "";
        return texto.length() <= max ? texto : texto.substring(0, max - 3) + "...";
    }
}
