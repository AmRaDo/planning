package planning;

public class BasicComponent implements Component {

	private final String name;
	protected String depot;
	protected int availableQuantity = 0;
	protected String supplier;
	
	protected Component alternative;

	public BasicComponent(String name) {
		this.name = name;
	}

	public String getDepot() {
		return depot;
	}

	public void setDepot(String depot) {
		this.depot = depot;
	}

	public int getAvailableQuantity() {
		return availableQuantity;
	}

	@Override
	public void addQuantity(int newQuantity) {
		this.availableQuantity += newQuantity;
	}

	public String getName() {
		return name;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	
	@Override
	public Component getAlternative() {
		return alternative;
	}
	public void setAlternative(Component alternative) {
		this.alternative = alternative;
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
		int numLots = availableQuantity /lotSize;
		if(numLots > 0) {
			availableQuantity -= numLots * lotSize;
			return numLots * lotSize;
		}
		return 0;
	}

	protected int getFromSuppiler(int quantity) {
		System.out.printf("Getting %d of %s from Supplier %s\n", quantity, name, supplier);
		return quantity;
	}
}
