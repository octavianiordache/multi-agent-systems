package ex1;
import jade.core.Agent;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Random;

public class HumidityMonitorAgent extends Agent {
    private static final int CHECK_INTERVAL = 10000; // interval de verificare (10 secunde)
    private static final double HUMIDITY_THRESHOLD = 30.0; // prag de umiditate

    @Override
    protected void setup() {
        addBehaviour(new TickerBehaviour(this, CHECK_INTERVAL) {
            protected void onTick() {
                double currentHumidity = getHumidity(); // metoda pentru a obtine umiditatea curenta
                if (currentHumidity < HUMIDITY_THRESHOLD) {
                    // notifica agentii de control al aspersoarelor
                    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                    String agentName = getLocalName().endsWith("1") ? "sprinklerControlAgent1" : "sprinklerControlAgent2";
                    msg.addReceiver(new AID(agentName, AID.ISLOCALNAME));
                    msg.setContent("START_SPRINKLERS");
                    send(msg);
                    System.out.println(getLocalName()+": Am notificat agentul pentru aspersor " + agentName + ".");
                }
            }
        });
    }
    private double getHumidity() {
        Random random = new Random();
        return 25 + random.nextInt(11); //va genera un numar random intre 25 si 35
    }
}
