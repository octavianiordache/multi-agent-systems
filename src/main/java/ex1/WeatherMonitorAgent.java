package ex1;

import java.util.Random;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class WeatherMonitorAgent extends Agent {
    private static final int CHECK_INTERVAL = 15000; // interval de verificare (15 secunde)

    @Override
    protected void setup() {
        addBehaviour(new TickerBehaviour(this, CHECK_INTERVAL) {
            protected void onTick() {
                boolean isRaining = checkRain(); // metoda pentru a verifica daca ploua
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                String agentName1 = getLocalName().endsWith("1") ? "roofControlAgent1" : "roofControlAgent2";
                String agentName2 = getLocalName().endsWith("1") ? "roofControlAgent2" : "roofControlAgent1";
                msg.addReceiver(new AID(agentName1, AID.ISLOCALNAME));
                msg.addReceiver(new AID(agentName2, AID.ISLOCALNAME));
                
                if (isRaining) {
                    msg.setContent("CLOSE_ROOF");
                    System.out.println(getLocalName() + ": Am notificat agentii pentru acoperisuri sa inchida acoperisurile.");
                } else {
                    msg.setContent("OPEN_ROOF");
                    System.out.println(getLocalName() + ": Am notificat agentii pentru acoperisuri sa deschida acoperisurile.");
                }
                send(msg);
            }
        });
    }

    private boolean checkRain() {
        Random random = new Random();
        return random.nextBoolean();
    }
}
