//Ting Shen & Emory Yates
//4-6-2015

package CA05;
import java.util.ArrayList;
public class Trader {
	// Name of the trader
	String name;
	// Cash left in the trader's hand
	double cashInHand;
	// Stocks owned by the trader
	ArrayList<Order> possession;
	ArrayList<Order> ordersPlaced;

	public Trader(String name, double cashInHand) {		super();
		this.name = name;
		this.cashInHand = cashInHand;
		this.possession = new ArrayList<Order>();
		this.ordersPlaced = new ArrayList<Order>();
	}

	public void buyFromBank(Market m, String symbol, int volume)
			throws StockMarketExpection {
		Stock sellStock = m.getStockForSymbol(symbol);
		double price = sellStock.getPrice();
		double total = price * volume;
		if (total > this.cashInHand) {
			throw new StockMarketExpection("Cannot place order for stock: " 
					+ sellStock.getSymbol() + " since there is not enough money. Trader: "
					+ this.name);
		}
		else { 
			this.cashInHand = this.cashInHand - total;
			Order order = new BuyOrder(sellStock.getSymbol(), volume, sellStock.getPrice(), this);
			this.possession.add(order);
		}		
	}

	public void placeNewOrder(Market m, String symbol, int volume,
			double price, OrderType orderType) throws StockMarketExpection {
		Stock stock = m.getStockForSymbol(symbol);
		double stockPrice = stock.getPrice();
		double total = stockPrice * volume;
		if (containsStock(symbol, ordersPlaced) != -1) {
			throw new StockMarketExpection("Cannot place two orders for the same stock.");
		}
		else { 	
		Order order = null;
		switch (orderType) {
		case BUY:
			if (total > this.cashInHand) {
				throw new StockMarketExpection("Cannot place order for stock: " 
						+ stock.getSymbol() + " since there is not enough money. Trader: "
						+ this.name);
			}
			order = new BuyOrder(symbol, volume, price, this);
			this.ordersPlaced.add(order);
			m.addOrder(order);
			break;
			
		case SELL:
			order = new SellOrder(symbol, volume, price, this);
			if (containsStock(symbol, possession) == -1)
				throw new StockMarketExpection("Cannot sell a stock you do not own, " + this.name);
			Order bought = possession.get(containsStock(symbol, possession));
			if (bought.getSize() < volume) {
				throw new StockMarketExpection("Cannot sell more stock than you have.");
			}
			this.ordersPlaced.add(order);
			m.addOrder(order);
			break;
			
		default:
			break;
		}
		}
	}

	public void placeNewMarketOrder(Market myMarket, String symbol, int volume,
			double price, OrderType orderType) throws StockMarketExpection {
		Stock stock = myMarket.getStockForSymbol(symbol);
		double stockPrice = stock.getPrice();
		double total = stockPrice * volume;
		if (total > this.cashInHand) {
			throw new StockMarketExpection("Cannot place order for stock: " 
					+ stock.getSymbol() + " since there is not enough money. Trader: "
					+ this.name);
		}
		else if (containsStock(symbol, ordersPlaced) != -1) {
			throw new StockMarketExpection("Cannot place two orders for the same stock.");
		}
		else { 	
		Order order = null;
		switch (orderType) {
		case BUY:
			order = new BuyOrder(symbol, volume, true, this);
			this.ordersPlaced.add(order);
			myMarket.addOrder(order);
			break; 
		case SELL:
			order = new SellOrder(symbol, volume, true, this);
			if (containsStock(symbol, possession) == -1)
				throw new StockMarketExpection("Cannot sell a stock you do not own.");
			Order bought = possession.get(containsStock(symbol, possession));
			if (bought.getSize() < volume) {
				throw new StockMarketExpection("Cannot sell more stock than you have.");
			}
			this.ordersPlaced.add(order);
			myMarket.addOrder(order);
			break;
		default:
		}
		}		
		// Similar to the other method, except the order is a market order
	}

	public void tradePerformed(Order o, double matchPrice)
			throws StockMarketExpection {
		// Notification received that a trade has been made, the parameters are
		// the order corresponding to the trade, and the match price calculated
		// in the order book. Note than an order can sell some of the stocks he
		// bought, etc. Or add more stocks of a kind to his position. Handle
		// these situations.
		
		//The total money spent or earned on Order o.
		double totalPrice = 0;
			
		//This will update the amount of stocks the Trader has if they bought or sold a stock they already own.
		if (containsStock(o.getStockSymbol(), possession) != -1){
			int foundOrder = containsStock(o.getStockSymbol(), possession);
			Order order = possession.get(foundOrder);
			int totalStocks = order.getSize();
			switch (o.getType()) {
			case BUY:
			totalStocks = totalStocks + o.getSize();
			totalPrice = matchPrice * o.getSize();
			cashInHand = cashInHand - totalPrice;
			break;
			case SELL:
			totalStocks = totalStocks - o.getSize();
			totalPrice = matchPrice * o.getSize();
			cashInHand = cashInHand + totalPrice;
			break;
			}
			if(totalStocks == 0) {
				possession.remove(foundOrder);
			}
			else {
			order.setSize(totalStocks);
			possession.set(foundOrder, order);
			}
			int location = containsStock(o.getStockSymbol(), ordersPlaced);
			ordersPlaced.remove(location);
		}
		//This will add the order to the Trader's list of stocks and decrease the cash they have upon completion of a trade.
		else {
			possession.add(o);
			int location = containsStock(o.getStockSymbol(), ordersPlaced);
			ordersPlaced.remove(location);
			totalPrice = matchPrice * o.getSize();
			cashInHand = cashInHand - totalPrice;
		}
		// Update the trader's orderPlaced, position, and cashInHand members
		// based on the notification.
	}
	
	//Checks the position or ordersPlaced lists for the appropriate stock and returns the position if found, -1 if not.
	public int containsStock(String symbol, ArrayList<Order> list){
		int contains = -1;
		for(int i = 0; i < list.size(); i++) {
			Order order = list.get(i);
			if (order.getStockSymbol() == symbol)
				contains = i;
		}
		return contains;
	}

	public void printTrader() {
		System.out.println("Trader Name: " + name);
		System.out.println("=====================");
		System.out.println("Cash: " + cashInHand);
		System.out.println("Stocks Owned: ");
		for (Order o : possession) {
			o.printStockNameInOrder();
		}
		System.out.println("Stocks Desired: ");
		for (Order o : ordersPlaced) {
			o.printOrder();
		}
		System.out.println("+++++++++++++++++++++");
		System.out.println("+++++++++++++++++++++");
	}
}
