package CA05;


public class Client {
	public static void main(String[] args) {
		//cannot actually make this variable private, as it was declared in method
		Market firstMarket = new Market("NASDAQ");
		IPO.enterNewStock(firstMarket, "SBUX", "Starbucks Corp.", 92.86);
		IPO.enterNewStock(firstMarket, "TWTR", "Twitter Inc.", 47.88);
		IPO.enterNewStock(firstMarket, "VSLR", "Vivint Solar", 16.44);
		IPO.enterNewStock(firstMarket, "GILD", "Gilead Sciences", 93.33);

		firstMarket.printStocks();

		Market secondMarket = new Market("Nikkei");
		IPO.enterNewStock(secondMarket, "BABA", "Alibaba", 84.88);
		IPO.enterNewStock(secondMarket, "BDU", "Baidu", 253.66);

		secondMarket.printStocks();

		// Create 10 traders
		Trader neda = new Trader("Neda", 200000.00);
		Trader scott = new Trader("Scott", 100000.00);
		Trader luke = new Trader("Luke", 100000.00);
		Trader thomas = new Trader("Thomas", 100000.00);
		Trader sritika = new Trader("Sritika", 100000.00);
		Trader meg = new Trader("Meg", 100000.00);
		Trader jen = new Trader("Jen", 100000.00);
		Trader emory = new Trader("Emory", 100000.00);
		Trader justin = new Trader("Justin", 100000.00);
		Trader zach = new Trader("Zach", 100000.00);
		Trader matt = new Trader("Matt", 100000.00);
		Trader angela = new Trader("Angela", 100000.00);
		Trader hamza = new Trader("Hamza", 100000.00);
		Trader ethan = new Trader("Ethan", 100000.00);

		// Two traders with market orders
		Trader trader1 = new Trader("T1", 300000.00);
		Trader trader2 = new Trader("T2", 300000.00);

		try {
			neda.buyFromBank(firstMarket, "SBUX", 1600);
			scott.buyFromBank(firstMarket, "SBUX", 300);
			luke.buyFromBank(firstMarket, "SBUX", 300);
			thomas.buyFromBank(firstMarket, "SBUX", 300);
			sritika.buyFromBank(firstMarket, "SBUX", 600);
			meg.buyFromBank(firstMarket, "SBUX", 700);
			jen.buyFromBank(firstMarket, "SBUX", 500);
			trader1.buyFromBank(firstMarket, "SBUX", 1500);
			// Exception
			emory.buyFromBank(firstMarket, "SBUX", 5000);
		} catch (StockMarketExpection e) {
			e.printStackTrace();
		}
		neda.printTrader();
		scott.printTrader();
		luke.printTrader();
		thomas.printTrader();
		sritika.printTrader();
		meg.printTrader();
		jen.printTrader();
		emory.printTrader();
		trader1.printTrader();

		// Place sell orders
		try {
			neda.placeNewOrder(firstMarket, "SBUX", 100, 97.0, OrderType.SELL);
			scott.placeNewOrder(firstMarket, "SBUX", 300, 97.5, OrderType.SELL);
			luke.placeNewOrder(firstMarket, "SBUX", 300, 98.0, OrderType.SELL);
			thomas.placeNewOrder(firstMarket, "SBUX", 300, 98.5, OrderType.SELL);
			sritika.placeNewOrder(firstMarket, "SBUX", 500, 99.0, OrderType.SELL);
			meg.placeNewOrder(firstMarket, "SBUX", 700, 99.5, OrderType.SELL);
			jen.placeNewOrder(firstMarket, "SBUX", 500, 100.0, OrderType.SELL);
			trader1.placeNewMarketOrder(firstMarket, "SBUX", 1500, 0, OrderType.SELL);

		} catch (StockMarketExpection e) {
			e.printStackTrace();
		}

		System.out.println("Printing after the sell orders are placed");
		System.out.println("&^&^&^&^&^&^&^&^&^&^&^&^&^&^&^&^&^&^&^&^");
		neda.printTrader();
		scott.printTrader();
		luke.printTrader();
		thomas.printTrader();
		sritika.printTrader();
		meg.printTrader();
		jen.printTrader();
		trader1.printTrader();

		// Place buy orders
		try {
			emory.placeNewOrder(firstMarket, "SBUX", 200, 101.0, OrderType.BUY);
			justin.placeNewOrder(firstMarket, "SBUX", 300, 100.5, OrderType.BUY);
			zach.placeNewOrder(firstMarket, "SBUX", 400, 100.0, OrderType.BUY);
			matt.placeNewOrder(firstMarket, "SBUX", 500, 99.5, OrderType.BUY);
			angela.placeNewOrder(firstMarket, "SBUX", 900, 99.0, OrderType.BUY);
			hamza.placeNewOrder(firstMarket, "SBUX", 1000, 98.5, OrderType.BUY);
			ethan.placeNewOrder(firstMarket, "SBUX", 900, 98.0, OrderType.BUY);
			trader2.placeNewMarketOrder(firstMarket, "SBUX", 700, 0, OrderType.BUY);
		} catch (StockMarketExpection e) {
			e.printStackTrace();
		}

		System.out.println("Printing after the buy orders are placed");
		System.out.println("&^&^&^&^&^&^&^&^&^&^&^&^&^&^&^&^&^&^&^&^");
		emory.printTrader();
		justin.printTrader();
		zach.printTrader();
		matt.printTrader();
		angela.printTrader();
		hamza.printTrader();
		ethan.printTrader();
		trader2.printTrader();

		firstMarket.triggerTrade();

		System.out.println("Printing after the tradings are done");
		System.out.println("&^&^&^&^&^&^&^&^&^&^&^&^&^&^&^&^&^&^&^&^");
		neda.printTrader();
		scott.printTrader();
		luke.printTrader();
		thomas.printTrader();
		sritika.printTrader();
		meg.printTrader();
		jen.printTrader();
		emory.printTrader();
		justin.printTrader();
		zach.printTrader();
		matt.printTrader();
		angela.printTrader();
		hamza.printTrader();
		ethan.printTrader();
		trader1.printTrader();
		trader2.printTrader();

		firstMarket.printHistoryFor("SBUX");

	}
}
