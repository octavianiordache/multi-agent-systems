package ex1;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class Main {
    public static void main(String[] args) {
        Runtime rt = Runtime.instance();
        Profile p = new ProfileImpl();
        AgentContainer ac = rt.createMainContainer(p);
        
        try {
            // cream si pornim agentii HumidityMonitor
            AgentController humidityMonitor1 = ac.createNewAgent("humidityMonitorAgent1", HumidityMonitorAgent.class.getName(), null);
            AgentController humidityMonitor2 = ac.createNewAgent("humidityMonitorAgent2", HumidityMonitorAgent.class.getName(), null);
            
            // cream si pornim agentii SprinklerControl
            AgentController sprinklerControl1 = ac.createNewAgent("sprinklerControlAgent1", SprinklerControlAgent.class.getName(), null);
            AgentController sprinklerControl2 = ac.createNewAgent("sprinklerControlAgent2", SprinklerControlAgent.class.getName(), null);
            
            /// cream si pornim agentii WeatherMonitor
            AgentController weatherMonitor = ac.createNewAgent("weatherMonitorAgent", WeatherMonitorAgent.class.getName(), null);
            
            // cream si pornim agentii RoofControl
            AgentController roofControl1 = ac.createNewAgent("roofControlAgent1", RoofControlAgent.class.getName(), null);
            AgentController roofControl2 = ac.createNewAgent("roofControlAgent2", RoofControlAgent.class.getName(), null);
            
            // pornim toti agentii
            humidityMonitor1.start();
            humidityMonitor2.start();
            
            sprinklerControl1.start();
            sprinklerControl2.start();
            
            weatherMonitor.start();
            
            roofControl1.start();
            roofControl2.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
