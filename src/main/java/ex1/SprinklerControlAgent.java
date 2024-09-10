package ex1;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;

public class SprinklerControlAgent extends Agent {
    private static final long SPRINKLER_DURATION = 10000; // durata de functionare a aspersoarelor (10 secunde)

    @Override
    protected void setup() {
        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    if (msg.getContent().equals("START_SPRINKLERS")) {
                        startSprinklers();
                        System.out.println(getLocalName() + ": Am primit notificarea de la HumidityMonitorAgent. Pornesc aspersorul.");

                        // adaugam un comportament pentru a opri aspersoarele dupa o perioada
                        addBehaviour(new WakerBehaviour(myAgent, SPRINKLER_DURATION) {
                            protected void onWake() {
                                stopSprinklers();
                                System.out.println(getLocalName() + ": Am oprit aspersorul.");
                            }
                        });
                    }
                } else {
                    block();
                }
            }
        });
    }

    private void startSprinklers() {
        // implementare pornire aspersoare
        System.out.println(getLocalName() + ": Aspersorul este pornit.");
    }

    private void stopSprinklers() {
        // implementare oprire aspersoare
        System.out.println(getLocalName()+ ": Aspersorul este oprit.");
    }
}
