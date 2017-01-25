package planning;

public interface Component {
	public int produce(int demand, int lotSize);
	public void addQuantity(int quantity);
	public Component getAlternative();
}
