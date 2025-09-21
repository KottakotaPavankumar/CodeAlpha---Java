package Project;

import java.util.*;
import java.io.*;

// Model classes
class Stock {
    String symbol;
    String name;
    double price;

    Stock(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }

    public String toString() {
        return symbol + " (" + name + ") - $" + price;
    }
}

class Market {
    List<Stock> stocks = new ArrayList<>();

    public Market() {
        stocks.add(new Stock("AAPL", "Apple Inc.", 188.2));
        stocks.add(new Stock("GOOG", "Alphabet Inc.", 2758.3));
        stocks.add(new Stock("TSLA", "Tesla Inc.", 729.4));
        stocks.add(new Stock("AMZN", "Amazon.com, Inc.", 3342.8));
    }

    Stock getStockBySymbol(String symbol) {
        for (Stock s : stocks) {
            if (s.symbol.equalsIgnoreCase(symbol)) return s;
        }
        return null;
    }

    void display() {
        System.out.println("=== Market Data ===");
        for (Stock s : stocks) {
            System.out.println(s);
        }
    }
}

class Transaction {
    String type; // "BUY" or "SELL"
    Stock stock;
    int quantity;
    double totalAmount;

    Transaction(String type, Stock stock, int quantity) {
        this.type = type;
        this.stock = stock;
        this.quantity = quantity;
        this.totalAmount = quantity * stock.price;
    }

    public String toString() {
        return type + " " + quantity + " x " + stock.symbol + " @ $" + stock.price + " = $" + totalAmount;
    }
}

@SuppressWarnings("Serial")
class Portfolio implements Serializable {
    Map<String, Integer> holdings = new HashMap<>();
    List<Transaction> history = new ArrayList<>();
    double balance;

    Portfolio(double startingBalance) {
        this.balance = startingBalance;
    }

    void buy(Stock stock, int quantity) {
        double cost = stock.price * quantity;
        if (cost > balance) {
            System.out.println("Insufficient funds!");
            return;
        }
        balance -= cost;
        holdings.put(stock.symbol, holdings.getOrDefault(stock.symbol, 0) + quantity);
        history.add(new Transaction("BUY", stock, quantity));
        System.out.println("Bought " + quantity + " Shares of " + stock.symbol);
    }

    void sell(Stock stock, int quantity) {
        int owned = holdings.getOrDefault(stock.symbol, 0);
        if (quantity > owned) {
            System.out.println("Not enough shares to sell!");
            return;
        }
        balance += stock.price * quantity;
        holdings.put(stock.symbol, owned - quantity);
        history.add(new Transaction("SELL", stock, quantity));
        System.out.println("Sold " + quantity + " Shares of " + stock.symbol);
    }

    void summary(Market market) {
        System.out.println("\n=== Portfolio Summary ===");
        System.out.println("Balance: $" + balance);
        System.out.println("Holdings:");
        for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
            if (entry.getValue() == 0) continue;
            Stock s = market.getStockBySymbol(entry.getKey());
            System.out.println(entry.getKey() + " - " + entry.getValue() + " Shares @ $" + s.price + " Each");
        }
        System.out.println("Transaction History:");
        for (Transaction t : history) {
            System.out.println(t);
        }
        System.out.println();
    }
}

// Optionally persist the portfolio
class PortfolioIO {
    static void save(Portfolio p, String filename) throws Exception {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
        out.writeObject(p);
        out.close();
    }

    static Portfolio load(String filename) throws Exception {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
        Portfolio p = (Portfolio) in.readObject();
        in.close();
        return p;
    }
}

// Main application
public class Stock_Trading_Platform {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Market market = new Market();

        Portfolio portfolio;
        String filename = "portfolio.dat";
        // Try to load existing portfolio
        try {
            portfolio = PortfolioIO.load(filename);
            System.out.println("Portfolio loaded from file.");
        } catch (Exception e) {
            portfolio = new Portfolio(10000.0); // Starting balance
            System.out.println("New portfolio started with $10,000.");
        }

        while (true) {
            System.out.println("\nChoose an option: ");
            System.out.println("1. Show Market Data");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. Portfolio Summary");
            System.out.println("5. Save & Exit");
            int choice = sc.nextInt();
            if (choice == 1) {
                market.display();
            } else if (choice == 2) {
                market.display();
                System.out.print("Enter stock symbol to buy: ");
                String symbol = sc.next();
                Stock stock = market.getStockBySymbol(symbol);
                if (stock == null) {
                    System.out.println("Invalid Symbol.");
                    continue;
                }
                System.out.print("Enter quantity to buy: ");
                int qty = sc.nextInt();
                portfolio.buy(stock, qty);
            } else if (choice == 3) {
                System.out.print("Enter stock symbol to sell: ");
                String symbol = sc.next();
                Stock stock = market.getStockBySymbol(symbol);
                if (stock == null) {
                    System.out.println("Invalid Symbol.");
                    continue;
                }
                System.out.print("Enter quantity to sell: ");
                int qty = sc.nextInt();
                portfolio.sell(stock, qty);
            } else if (choice == 4) {
                portfolio.summary(market);
            } else if (choice == 5) {
                try {
                    PortfolioIO.save(portfolio, filename);
                    System.out.println("Portfolio saved. Exiting.");
                } catch (Exception e) {
                    System.out.println("Unable to save portfolio.");
                }
                break;
            }
        }
        sc.close();
    }
}
