package planning;

public class Example1 {

	public static void main(String[] args) {
		CompositeComponent part = new CompositeComponent("P");
		CompositeComponent c1 = new CompositeComponent("C1");
		
		CompositeComponent c2 = new CompositeComponent("C2");
		CompositeComponent c3 = new CompositeComponent("C3");
		CompositeComponent c4 = new CompositeComponent("C4");
		c4.addQuantity(20);
		c3.setSupplier("S1");
		part.add(c1, 1);
		part.add(c2, 1);
		c1.add(c3, 1);
		c1.add(c4, 1);
		c2.add(c4, 1);
		System.out.println(part.produce(20, 1));
	}

}
