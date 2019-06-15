package com.lisowski;


import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class Main extends  Thread {
        @Override
        public void run() {
            waitForConnection();
        }
        // Waiting for connection from devices
        private void waitForConnection() {
            // retrieve the local Bluetooth device object
            LocalDevice local = null;
            StreamConnectionNotifier streamConnectionNotifier;
            StreamConnection streamConnection = null;

            // setup the server to listen for connection
            try {
                local = LocalDevice.getLocalDevice();
                local.setDiscoverable(DiscoveryAgent.GIAC);
                UUID uuid = new UUID(79087355); // "04b6c6fb-0000-1000-8000-00805f9b34fb"
                String url = "btspp://localhost:" + uuid.toString() + ";name=Controller";
                streamConnectionNotifier = (StreamConnectionNotifier) Connector.open(url);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            // waiting for connection
            while(true) {
                try {
                    System.out.println("czekam na poczaczenie");
                    streamConnection = streamConnectionNotifier.acceptAndOpen();

                    Thread processThread = new Thread(new ProcessConnection(streamConnection));
                    processThread.start();
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }

    public static void main(String[] args) {
        new Main().start();
    }
}