package ex1;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class RoofControlAgent extends Agent {
    @Override
    protected void setup() {
        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    if (msg.getContent().equals("CLOSE_ROOF")) {
                        closeRoof();
                        System.out.println(getLocalName() + ": Am primit notificarea de la WeatherMonitorAgent. Inchid acoperisul.");
                    } else if (msg.getContent().equals("OPEN_ROOF")) {
                        openRoof();
                        System.out.println(getLocalName() + ": Am primit notificarea de la WeatherMonitorAgent. Deschid acoperisul.");
                    }
                } else {
                    block();
                }
            }
        });
    }

    private void closeRoof() {
        System.out.println(getLocalName() + ": Acoperisul este inchis.");
    }

    private void openRoof() {
        System.out.println(getLocalName() + "Acoperisul este deschis.");
    }
}
