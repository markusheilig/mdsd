import com.company.petrinet.PetriNet;

public class GeneratedApp {
	public static void main(String[] args) {
		PetriNet myPetriNet = PetriNet.create("Wikipedia Beispiel")
			.addPlace("s1")
				.initWithTokens(8)
				  .withIngoingTransition("t")
				  .andCost(2)
			  .and()
				  .withOutgoingTransition("t2")
				  .andCost(1)
			.addPlace("s2")
				.initWithTokens(4)
				  .withIngoingTransition("t3")
				  .andCost(-45)
			.end();
		
		myPetriNet.validate();
	}
}