//Ting Shen & Emory Yates
//4-6-2015

package CA05;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarketHistory implements IObserver {
	private ISubject subject;
	Market m;
	Map<String, List<Double>> history;

	public MarketHistory(Market m) {
		super();
		this.m = m;
		history = new HashMap<String, List<Double>>();
	}

	@Override
	public void setSubject(ISubject priceSetter) {
		this.subject = priceSetter;
	}

	public void startHistoryWithPrice(String symbol, Double newPrice)
			throws StockMarketExpection {
		if (!history.containsKey(symbol)) {
			List<Double> priceList = new ArrayList<Double>();
			priceList.add(newPrice);
			history.put(symbol, priceList);
		}
	}

	@Override
	public void update() {
		Stock updatedStock = (Stock) subject.getUpdate();
		if (m.getStockForSymbol(updatedStock.getSymbol()) == null) {
			return;
		}
		if (history.containsKey(updatedStock.getSymbol())) {
			List<Double> priceList = history.get(updatedStock.getSymbol());
			priceList.add(updatedStock.getPrice());
			history.put(updatedStock.getSymbol(), priceList);
		} else {
			// New entry to history
			List<Double> priceList = new ArrayList<Double>();
			priceList.add(updatedStock.getPrice());
			history.put(updatedStock.getSymbol(), priceList);
		}
	}

	public void updatePriceHistory(String symbol, Double newPrice) {
		List<Double> prices = history.get(symbol);
		prices.add(newPrice);
		history.remove(symbol);
		history.put(symbol, prices);
		
	}
	
	public ArrayList<Double> getPriceFor(String symbol) {
		if (history.containsKey(symbol)) {
			return (ArrayList<Double>) history.get(symbol);
		} else {
			return new ArrayList<Double>();
		}
	}
}
