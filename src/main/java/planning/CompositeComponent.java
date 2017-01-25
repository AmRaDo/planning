package planning;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class CompositeComponent extends BasicComponent {

	List<Entry<Component, Integer>> dependents = new ArrayList<>();

	public CompositeComponent(String name) {
		super(name);
	}

	public void add(Component component, int quantity) {
		dependents.add(new AbstractMap.SimpleEntry<>(component, quantity));
	}

	@Override
	public int produce(int demand, int lotSize) {
		if (demand < availableQuantity) {
			availableQuantity -= demand;
			return demand;
		}

		if (supplier != null) {
			getFromSuppiler(demand - availableQuantity);
			availableQuantity = 0;
			return demand;
		}

		int required = demand - availableQuantity;
		List<Entry<Component, Integer>> producedQuantities = new ArrayList<>();
		for (Entry<Component, Integer> entry : dependents) {
			int produced = entry.getKey().produce(required * entry.getValue(), entry.getValue());
			if (produced < required * entry.getValue() && entry.getKey().getAlternative() != null) {
				int altProduced = entry.getKey().getAlternative().produce(required * entry.getValue(),
						entry.getValue());
				if (altProduced > produced) {
					entry.getKey().addQuantity(produced);
					producedQuantities.add(new AbstractMap.SimpleEntry<>(entry.getKey().getAlternative(), altProduced));
				} else {
					entry.getKey().getAlternative().addQuantity(altProduced);
					producedQuantities.add(new AbstractMap.SimpleEntry<>(entry.getKey(), produced));
				}
			} else {
				producedQuantities.add(new AbstractMap.SimpleEntry<>(entry.getKey(), produced));
			}
		}
		int minLot = Integer.MAX_VALUE;
		for (int i = 0; i < dependents.size(); i++) {
			int producedQty = producedQuantities.get(i).getValue();
			int lotSze = dependents.get(i).getValue();
			int lot = producedQty / lotSze;
			if (lot < minLot) {
				minLot = lot;
			}
		}
		if (minLot == Integer.MAX_VALUE) {
			minLot = 0;
		}
		if (minLot == required) {
			availableQuantity = 0;
			return demand;
		} else {
			int qnty = minLot + availableQuantity;
			int numLots = qnty / lotSize;
			if (numLots < 1) {
				// give back all the producedGoods
				for (Entry<Component, Integer> entry : producedQuantities) {
					entry.getKey().addQuantity(entry.getValue());
				}
				return 0;
			} else {
				int lotToReturn = qnty % lotSize;
				for (int i = 0; i < dependents.size(); i++) {
					int lotSze = dependents.get(i).getValue();
					producedQuantities.get(i).getKey().addQuantity(lotToReturn * lotSze);
				}
				availableQuantity = 0;
				return numLots * lotSize;
			}

		}
	}
}
