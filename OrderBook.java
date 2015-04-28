//Ting Shen & Emory Yates
//4-6-2015

package CA05;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderBook {
	Market m;
	HashMap<String, ArrayList<Order>> buyOrders;
	HashMap<String, ArrayList<Order>> sellOrders;

	public OrderBook(Market m) {
		this.m = m;
		buyOrders = new HashMap<String, ArrayList<Order>>();
		sellOrders = new HashMap<String, ArrayList<Order>>();
	}

	public void addToOrderBook(Order order) {
		OrderType type = order.getType();
		ArrayList<Order> orders = new ArrayList<Order>();
		switch (type) {
		case BUY:
			if (buyOrders.containsKey(order.getStockSymbol())) {
				orders = buyOrders.get(order.getStockSymbol());
				buyOrders.remove(order.getStockSymbol());
			}
			orders.add(order);
			buyOrders.put(order.getStockSymbol(), orders);
			break;
		case SELL:
			if (sellOrders.containsKey(order.getStockSymbol())) {
				orders = sellOrders.get(order.getStockSymbol());
				sellOrders.remove(order.getStockSymbol());
			}
			orders.add(order);
			sellOrders.put(order.getStockSymbol(), orders);
			break;
		default:
	}	
	}

	//Sorts buy orders from greatest to least price, and sell orders from least to greatest
	public ArrayList<Order> sortOrders(ArrayList<Order> oldList, boolean buy) {
		ArrayList<Order> newList = new ArrayList<Order>();
		double searchFor = 0.0;
		double newSearchFor = 0.0;
		if (buy) {
			while (oldList.size() > 0) {
				for (int inc = 0; inc < oldList.size(); inc++) {
					double checker = oldList.get(inc).getPrice();
					if (checker != searchFor && checker > newSearchFor)
						newSearchFor = checker;
					else if (checker == searchFor) {
						newList.add(oldList.get(inc));
						oldList.remove(inc);
						inc--;
					}
				}
				searchFor = newSearchFor;
				newSearchFor = 0.0;
			}
			return newList;
		}
		else {
			newSearchFor = Double.MAX_VALUE;
			while (oldList.size() > 0) {
				for (int inc = 0; inc < oldList.size(); inc++) {
					double checker = oldList.get(inc).getPrice();
					if (checker != searchFor && checker < newSearchFor)
						newSearchFor = checker;
					else if (checker == searchFor) {
						newList.add(oldList.get(inc));
						oldList.remove(inc);
						inc--;
					}
				}
				searchFor = newSearchFor;
				newSearchFor = Double.MAX_VALUE;
			}
			return newList;
		}
	}
	
	public void trade() {
		for (String key : buyOrders.keySet()) {
			if (sellOrders.containsKey(key)) {
				ArrayList<Order> purchases = new ArrayList<Order>(buyOrders.get(key));
				ArrayList<Order> sales = new ArrayList<Order>(sellOrders.get(key));
				
				purchases = sortOrders(purchases, true);
				sales = sortOrders(sales, false);
				ArrayList<Double> prices = new ArrayList<Double>();
				int inc = 0;
				//creates an arraylist of all the available prices
				while (inc < sales.size()) {
					if (!prices.contains(sales.get(inc).getPrice())) {
						prices.add(sales.get(inc).getPrice());
					}
					inc++;
				}
				//updates the arrayList to include values found only in purchase
				inc = purchases.size() - 1;
				while (inc > 0) {
					if (!prices.contains(purchases.get(inc).getPrice())) {
						prices.add(purchases.get(inc).getPrice());
					}
					inc--;
				}
				//Creates the data representation
				double[] priceArray = new double[prices.size() + 1];
				int[] salesArray = new int[prices.size() + 1];
				int[] purchasesArray = new int[prices.size() + 1];
				priceArray[0] = 0.0;
				priceArray[prices.size()] = Double.MAX_VALUE;
				purchasesArray[prices.size()] = 0;
				salesArray[0] = 0;
				//Populates the data representation at the Market buy and sell prieces
				inc = 0;
				while (inc < purchases.size()) {
					if (purchases.get(inc).getPrice() == 0.0) {
						purchasesArray[purchasesArray.length - 1] += purchases.get(inc).getSize();
						purchases.remove(inc);
					}
					else 
						inc = purchases.size();
				}
				inc = 0;
				while (inc < sales.size()) {
					if (sales.get(inc).getPrice() == 0.0) {
						salesArray[0] += sales.get(inc).getSize();
						sales.remove(inc);
					}
					else
						inc = sales.size();
				}
				//Populates the data representation at all middle prices.
				inc = 0;
				int dec = purchases.size() - 1;
				int incrementer2 = 0;
				int decrementer2 = purchases.size() - 1;
				while (sales.size() > 0 || dec >= 0) {
					//Sales and purchases both have entries for the given price
					if (dec >= 0 && sales.size() > 0) {
					if (sales.get(0).getPrice() == purchases.get(dec).getPrice()) {
						priceArray[inc + 1] = sales.get(0).getPrice();
						incrementer2 = 0;
						decrementer2 = purchases.size() - 1;
						while (decrementer2 > 0) {
							if (purchases.get(decrementer2).getPrice() == priceArray[inc + 1]) {
								purchasesArray[inc + 1] += purchases.get(decrementer2).getSize();
								purchases.remove(decrementer2);
								decrementer2--;
							}
							else 
								decrementer2 = 0;
						}
						while (incrementer2 < sales.size()) {
							if (sales.get(incrementer2).getPrice() == priceArray[inc + 1]) {
								salesArray[inc + 1] += sales.get(incrementer2).getSize();
								sales.remove(incrementer2);
							}
							else
								incrementer2 = sales.size();
						}
						dec--;
						inc++;
					}
					//Sales has a lower entry than purchases' next entry
					else if (sales.get(0).getPrice() < purchases.get(dec).getPrice()) {
						priceArray[inc + 1] = sales.get(0).getPrice();
						incrementer2 = 0;
						while (incrementer2 < sales.size()) {
							if (sales.get(incrementer2).getPrice() == priceArray[inc + 1]) {
								salesArray[inc + 1] += sales.get(incrementer2).getSize();
								sales.remove(incrementer2);
							}
							else
								incrementer2 = sales.size();
						}
						inc++;
					}
					
					//Purchases has a lower entry than sales' next entry
					else if (sales.get(0).getPrice() > purchases.get(dec).getPrice()) {
						priceArray[inc + 1] = purchases.get(dec).getPrice();
						decrementer2 = purchases.size() - 1;
						while (decrementer2 > 0) {
							if (purchases.get(decrementer2).getPrice() == priceArray[inc + 1]) {
								purchasesArray[inc + 1] += purchases.get(decrementer2).getSize();
								purchases.remove(decrementer2);
								decrementer2--;
							}
							else 
								decrementer2 = 0;
						}
						dec--;
						inc++;
					}
					}
					else if(dec < 0) {
						priceArray[inc + 1] = sales.get(0).getPrice();
						incrementer2 = 0;
						while (incrementer2 < sales.size()) {
							if (sales.get(incrementer2).getPrice() == priceArray[inc + 1]) {
								salesArray[inc + 1] += sales.get(incrementer2).getSize();
								sales.remove(incrementer2);
							}
							else
								incrementer2 = sales.size();
						}
						inc++;
					}
					else if(sales.size() < 1) {
						priceArray[inc + 1] = purchases.get(dec).getPrice();
						decrementer2 = purchases.size() - 1;
						while (decrementer2 >= 0) {
							if (purchases.get(decrementer2).getPrice() == priceArray[inc + 1]) {
								purchasesArray[inc + 1] += purchases.get(decrementer2).getSize();
								purchases.remove(decrementer2);
								decrementer2--;
							}
							else 
								decrementer2 = -1;
						}
						dec--;
						inc++;
					}
				}
				//Accumulates total sell and buy offers per price range
				inc = 1;
				while (inc < salesArray.length) {
					salesArray[inc] += salesArray[inc - 1];
					inc++;
				}
				dec = purchasesArray.length - 2;
				while (dec >= 0) {
					purchasesArray[dec] += purchasesArray[dec + 1];
					dec--;
				}
				//Finds the match price
				int newdifference = 0;
				int olddifference = Integer.MAX_VALUE;
				inc = 0;
				int locate = 0;
				while (inc < priceArray.length) {
					newdifference = Math.abs(purchasesArray[inc] - salesArray[inc]);
					if (newdifference < olddifference){
						olddifference = newdifference;
						locate = inc;
					}
					else if (newdifference >= olddifference) {
						inc = priceArray.length;
					}
					inc++;
				}
				//Updates the stock price on the market
				double matchPrice = priceArray[locate];
				try {
					m.updateStockPrice(key, matchPrice);
				} catch (StockMarketExpection e) {
					e.printStackTrace();
				}
				//Notifies traders and removes the completed orders from the respective lists.
				purchases = new ArrayList<Order>(buyOrders.get(key));
				sales =  new ArrayList<Order>(sellOrders.get(key));
				
				for(int i = purchases.size() - 1; i > -1; i--) {
					Order order = purchases.get(i);
					if (order.getPrice() >= matchPrice) {
					Trader notifiable = order.getTrader();
				try {
					notifiable.tradePerformed(order, matchPrice);
					purchases.remove(i); }
				catch (StockMarketExpection e) {
					e.printStackTrace();
				}
				}
				}	
				for(int i = sales.size() - 1; i > -1; i--) {
					Order order = sales.get(i);
					if (order.getPrice() <= matchPrice) {
					Trader notifiable = order.getTrader();
					try {
						notifiable.tradePerformed(order, matchPrice);
						sales.remove(i); }
					catch (StockMarketExpection e) {
						e.printStackTrace();
					}
					}
				}
				sellOrders.put(key, sales);
				buyOrders.put(key, purchases);				

				
				
				
			}
		}
	}

}
