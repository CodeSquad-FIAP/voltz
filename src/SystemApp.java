import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.nio.file.*;
import java.io.BufferedWriter;

public class SystemApp {
    public static void main(String[] args) {
        try {
            Market market = new Market();
            market.updatePrices();
            market.showPrices();
            User user1 = new User("Scrooge McDuck", 1, "scrooge@ducktales.com", "vault123");
            User user2 = new User("Donald Duck", 3, "donald@ducktales.com", "quackquack");
            User user3 = new User("Daisy Duck", 4, "daisy@ducktales.com", "flower123");
            User user4 = new User("Carol", 5, "carol@ducktales.com", "password");

            List<User> userList = List.of(user1, user2, user3, user4);
            Company company1 = new Company("DuckCorp", 1, "CNPJ-001");
            Company company2 = new Company("TreasureInc", 2, "CNPJ-002");
            Company company3 = new Company("Duck Enterprises", 3, "CNPJ-010");
            Company company4 = new Company("Duck Investments", 4, "CNPJ-011");

            List<Company> companyList = List.of(company1, company2, company3, company4);
            Wallet wallet = new Wallet();
            CryptoAsset btc = new CryptoAsset("Bitcoin", "BTC", 2.0, market.getPrice("BTC"));
            CryptoAsset eth = new CryptoAsset("Ethereum", "ETH", 5.0, market.getPrice("ETH"));
            CryptoAsset ada = new CryptoAsset("Cardano", "ADA", 1000, market.getPrice("ADA"));

            wallet.addAsset(btc);
            wallet.addAsset(eth);
            
            company1.allocateAsset(btc);
            company2.allocateAsset(eth, 1.0);
            company3.allocateAsset(ada);

            List<Transaction> transactions = new ArrayList<>();
            transactions.add(new Transaction(btc, 2.0, "BUY"));
            transactions.add(new Transaction(eth, 5.0, "BUY"));

            List<UserCompanyRelation> relations = new ArrayList<>();
            relations.add(new UserCompanyRelation(user1, company1, 50000.00, LocalDate.of(2025, 5, 1)));
            relations.add(new UserCompanyRelation(user1, company2, 35000.00, LocalDate.of(2025, 5, 2)));
            relations.add(new UserCompanyRelation(user2, company1, 20000.00, LocalDate.of(2025, 5, 3)));
            relations.add(new UserCompanyRelation(user3, company2, 25000.00, LocalDate.of(2025, 5, 4)));

            Map<User, List<Company>> userCompanies = new HashMap<>();
            userCompanies.put(user2, Arrays.asList(company3, company4));
            userCompanies.put(user3, Collections.singletonList(company4));

            Report report = new Report();
            System.out.println();
            report.generateWalletReport(wallet);
            System.out.println();
            report.generateTransactionReport(transactions);
            System.out.println();
            report.generateCompanyReport(companyList);
            System.out.println();

            System.out.println("=== USER-COMPANY RELATIONS ===");
            for (UserCompanyRelation rel : relations) {
                rel.showRelation();
            }

            System.out.println("\n=== TESTE HASHMAP USANDO USER E COMPANY ===");
            for (var entry : userCompanies.entrySet()) {
                System.out.printf("%s -> %s%n",
                        entry.getKey().getName(),
                        entry.getValue().stream().map(Company::getName).collect(Collectors.joining(", "))
                );
            }

            Path file = Paths.get("data.txt");
            try (BufferedWriter writer = Files.newBufferedWriter(file)) {
                for (User u : userList) {
                    writer.write(String.join(";", "USER", String.valueOf(u.getId()), u.getEmail(), u.getName()));
                    writer.newLine();
                }
                for (var e : userCompanies.entrySet()) {
                    for (Company c : e.getValue()) {
                        writer.write(String.join(";", "REL", String.valueOf(e.getKey().getId()), String.valueOf(c.getId())));
                        writer.newLine();
                    }
                }
            }

            List<String> lines = Files.readAllLines(file);
            lines.add("USER;5;Carol;carol@ducktales.com");
            Files.write(file, lines, StandardOpenOption.TRUNCATE_EXISTING);

            
            System.out.println("-- Lista de Usuários --");
            for (User u : userList) {
                u.showInfo();
            }

            
            System.out.println("\n-- Lista de Empresas --");
            for (Company c : companyList) {
                c.showInfo();
            }

          
            System.out.println("\n-- Ativos alocados na primeira empresa --");
            company1.showAllocatedAssets();

            System.out.println("\n=== FIM DO TESTE ARRAYLIST ===");

        } catch (Exception e) {
            System.err.println("❌ Ocorreu um erro ao executar o sistema: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
