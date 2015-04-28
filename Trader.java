
//4-6-2015

package CA05;

import java.util.ArrayList;

public class Trader {
	// Name of the trader
	String name;
	// Cash left in the trader's hand
	double cashInHand;
	// Stocks owned by the trader
	ArrayList<Order> position;
	// Orders placed by the trader
	ArrayList<Order> ordersPlaced;

	public Trader(String name, double cashInHand) {
		super();
		this.name = name;
		this.cashInHand = cashInHand;
		this.position = new ArrayList<Order>();
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
			this.position.add(order);
		}		
		// Buy stock straight from the bank
		// Need not place the stock in the order list
		// Add it straight to the user's position
		// If the stock's price is larger than the cash possessed, then an
		// exception is thrown
		// Adjust cash possessed since the trader spent money to purchase a
		// stock.
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
			if (containsStock(symbol, position) == -1)
				throw new StockMarketExpection("Cannot sell a stock you do not own, " + this.name);
			Order bought = position.get(containsStock(symbol, position));
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
		
		// Place a new order and add to the orderlist
		// Also enter the order into the orderbook of the market.
		// Note that no trade has been made yet. The order is in suspension
		// until a trade is triggered.
		//
		// If the stock's price is larger than the cash possessed, then an
		// exception is thrown
		// A trader cannot place two orders for the same stock, throw an
		// exception if there are multiple orders for the same stock.
		// Also a person cannot place a sell order for a stock that he does not
		// own. Or he cannot sell more stocks than he possesses. Throw an
		// exception in these cases.

	}

	public void placeNewMarketOrder(Market m, String symbol, int volume,
			double price, OrderType orderType) throws StockMarketExpection {
		Stock stock = m.getStockForSymbol(symbol);
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
			m.addOrder(order);
			break; 
		case SELL:
			order = new SellOrder(symbol, volume, true, this);
			if (containsStock(symbol, position) == -1)
				throw new StockMarketExpection("Cannot sell a stock you do not own.");
			Order bought = position.get(containsStock(symbol, position));
			if (bought.getSize() < volume) {
				throw new StockMarketExpection("Cannot sell more stock than you have.");
			}
			this.ordersPlaced.add(order);
			m.addOrder(order);
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
		if (containsStock(o.getStockSymbol(), position) != -1){
			int i = containsStock(o.getStockSymbol(), position);
			Order order = position.get(i);
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
				position.remove(i);
			}
			else {
			order.setSize(totalStocks);
			position.set(i, order);
			}
			int location = containsStock(o.getStockSymbol(), ordersPlaced);
			ordersPlaced.remove(location);
		}
		//This will add the order to the Trader's list of stocks and decrease the cash they have upon completion of a trade.
		else {
			position.add(o);
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
		for (Order o : position) {
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
