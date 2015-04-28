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
				int outerLoopIncrementer = 0;
				//creates an arraylist of all the available prices
				while (outerLoopIncrementer < sales.size()) {
					if (!prices.contains(sales.get(outerLoopIncrementer).getPrice())) {
						prices.add(sales.get(outerLoopIncrementer).getPrice());
					}
					outerLoopIncrementer++;
				}
				//updates the arrayList to include values found only in purchase
				outerLoopIncrementer = purchases.size() - 1;
				while (outerLoopIncrementer > 0) {
					if (!prices.contains(purchases.get(outerLoopIncrementer).getPrice())) {
						prices.add(purchases.get(outerLoopIncrementer).getPrice());
					}
					outerLoopIncrementer--;
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
				outerLoopIncrementer = 0;
				while (outerLoopIncrementer < purchases.size()) {
					if (purchases.get(outerLoopIncrementer).getPrice() == 0.0) {
						purchasesArray[purchasesArray.length - 1] += purchases.get(outerLoopIncrementer).getSize();
						purchases.remove(outerLoopIncrementer);
					}
					else 
						outerLoopIncrementer = purchases.size();
				}
				outerLoopIncrementer = 0;
				while (outerLoopIncrementer < sales.size()) {
					if (sales.get(outerLoopIncrementer).getPrice() == 0.0) {
						salesArray[0] += sales.get(outerLoopIncrementer).getSize();
						sales.remove(outerLoopIncrementer);
					}
					else
						outerLoopIncrementer = sales.size();
				}
				//Populates the data representation at all middle prices.
				outerLoopIncrementer = 0;
				int outerLoopDecrementer = purchases.size() - 1;
				int innerLoopIncrementer = 0;
				int innerLoopDecrementer = purchases.size() - 1;
				while (sales.size() > 0 || outerLoopDecrementer >= 0) {
					//Sales and purchases both have entries for the given price
					if (outerLoopDecrementer >= 0 && sales.size() > 0) {
					if (sales.get(0).getPrice() == purchases.get(outerLoopDecrementer).getPrice()) {
						priceArray[outerLoopIncrementer + 1] = sales.get(0).getPrice();
						innerLoopIncrementer = 0;
						innerLoopDecrementer = purchases.size() - 1;
						while (innerLoopDecrementer > 0) {
							if (purchases.get(innerLoopDecrementer).getPrice() == priceArray[outerLoopIncrementer + 1]) {
								purchasesArray[outerLoopIncrementer + 1] += purchases.get(innerLoopDecrementer).getSize();
								purchases.remove(innerLoopDecrementer);
								innerLoopDecrementer--;
							}
							else 
								innerLoopDecrementer = 0;
						}
						while (innerLoopIncrementer < sales.size()) {
							if (sales.get(innerLoopIncrementer).getPrice() == priceArray[outerLoopIncrementer + 1]) {
								salesArray[outerLoopIncrementer + 1] += sales.get(innerLoopIncrementer).getSize();
								sales.remove(innerLoopIncrementer);
							}
							else
								innerLoopIncrementer = sales.size();
						}
						outerLoopDecrementer--;
						outerLoopIncrementer++;
					}
					//Sales has a lower entry than purchases' next entry
					else if (sales.get(0).getPrice() < purchases.get(outerLoopDecrementer).getPrice()) {
						priceArray[outerLoopIncrementer + 1] = sales.get(0).getPrice();
						innerLoopIncrementer = 0;
						while (innerLoopIncrementer < sales.size()) {
							if (sales.get(innerLoopIncrementer).getPrice() == priceArray[outerLoopIncrementer + 1]) {
								salesArray[outerLoopIncrementer + 1] += sales.get(innerLoopIncrementer).getSize();
								sales.remove(innerLoopIncrementer);
							}
							else
								innerLoopIncrementer = sales.size();
						}
						outerLoopIncrementer++;
					}
					
					//Purchases has a lower entry than sales' next entry
					else if (sales.get(0).getPrice() > purchases.get(outerLoopDecrementer).getPrice()) {
						priceArray[outerLoopIncrementer + 1] = purchases.get(outerLoopDecrementer).getPrice();
						innerLoopDecrementer = purchases.size() - 1;
						while (innerLoopDecrementer > 0) {
							if (purchases.get(innerLoopDecrementer).getPrice() == priceArray[outerLoopIncrementer + 1]) {
								purchasesArray[outerLoopIncrementer + 1] += purchases.get(innerLoopDecrementer).getSize();
								purchases.remove(innerLoopDecrementer);
								innerLoopDecrementer--;
							}
							else 
								innerLoopDecrementer = 0;
						}
						outerLoopDecrementer--;
						outerLoopIncrementer++;
					}
					}
					else if(outerLoopDecrementer < 0) {
						priceArray[outerLoopIncrementer + 1] = sales.get(0).getPrice();
						innerLoopIncrementer = 0;
						while (innerLoopIncrementer < sales.size()) {
							if (sales.get(innerLoopIncrementer).getPrice() == priceArray[outerLoopIncrementer + 1]) {
								salesArray[outerLoopIncrementer + 1] += sales.get(innerLoopIncrementer).getSize();
								sales.remove(innerLoopIncrementer);
							}
							else
								innerLoopIncrementer = sales.size();
						}
						outerLoopIncrementer++;
					}
					else if(sales.size() < 1) {
						priceArray[outerLoopIncrementer + 1] = purchases.get(outerLoopDecrementer).getPrice();
						innerLoopDecrementer = purchases.size() - 1;
						while (innerLoopDecrementer >= 0) {
							if (purchases.get(innerLoopDecrementer).getPrice() == priceArray[outerLoopIncrementer + 1]) {
								purchasesArray[outerLoopIncrementer + 1] += purchases.get(innerLoopDecrementer).getSize();
								purchases.remove(innerLoopDecrementer);
								innerLoopDecrementer--;
							}
							else 
								innerLoopDecrementer = -1;
						}
						outerLoopDecrementer--;
						outerLoopIncrementer++;
					}
				}
				//Accumulates total sell and buy offers per price range
				outerLoopIncrementer = 1;
				while (outerLoopIncrementer < salesArray.length) {
					salesArray[outerLoopIncrementer] += salesArray[outerLoopIncrementer - 1];
					outerLoopIncrementer++;
				}
				outerLoopDecrementer = purchasesArray.length - 2;
				while (outerLoopDecrementer >= 0) {
					purchasesArray[outerLoopDecrementer] += purchasesArray[outerLoopDecrementer + 1];
					outerLoopDecrementer--;
				}
				//Finds the match price
				int newdifference = 0;
				int olddifference = Integer.MAX_VALUE;
				outerLoopIncrementer = 0;
				int locate = 0;
				while (outerLoopIncrementer < priceArray.length) {
					newdifference = Math.abs(purchasesArray[outerLoopIncrementer] - salesArray[outerLoopIncrementer]);
					if (newdifference < olddifference){
						olddifference = newdifference;
						locate = outerLoopIncrementer;
					}
					else if (newdifference >= olddifference) {
						outerLoopIncrementer = priceArray.length;
					}
					outerLoopIncrementer++;
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
