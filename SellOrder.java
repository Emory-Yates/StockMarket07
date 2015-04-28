//Ting Shen & Emory Yates
//4-6-2015

package CA05;


public class SellOrder extends Order {
	public SellOrder(String stockSymbol, int size, double price, Trader trader) {
		// TODO:
		// Create a new sell order
		super.stockSymbol = stockSymbol;
		super.size = size;
		super.price = price;
		super.trader = trader;
		super.type = OrderType.SELL;
		super.orderNumber = super.getNextOrderNumber();
	}

	public SellOrder(String stockSymbol, int size, boolean isMarketOrder,
			Trader trader) throws StockMarketExpection {
		// TODO:
		if (isMarketOrder) {
		super.stockSymbol = stockSymbol;
		super.size = size;
		super.price = 0.0;
		super.trader = trader;
		super.type = OrderType.SELL;
		super.orderNumber = super.getNextOrderNumber();
		}
		else throw new StockMarketExpection("Error, invalid price.");
		// Create a new sell order which is a market order
		// Set the price to 0.0, Set isMarketOrder attribute to true
		//
		// If this is called with isMarketOrder == false, throw an exception
		// that an order has been placed without a valid price.
	}

	public void printOrder() {
		System.out.println("Stock: " + stockSymbol + " $" + price + " x "
				+ size + " (Sell)");
	}
}
