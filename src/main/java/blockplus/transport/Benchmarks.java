
package blockplus.transport;

import blockplus.transport.messages.Messages;

public class Benchmarks {

    private static final int GAMES = BlockplusServer.GAMES;
    private final static Messages MESSAGES = new Messages();

    public static void main(final String[] args) throws Exception {
        final VirtualClient[] virtualClients = new VirtualClient[GAMES];
        for (int i = 1; i <= GAMES; ++i) {
            final VirtualClient virtualClient = newGame(i);
            virtualClients[i - 1] = virtualClient;
        }
        Thread.sleep(1000);
        final long t0 = System.currentTimeMillis();
        System.out.println(t0);
        for (int i = 1; i <= GAMES; ++i) {
            startGame(virtualClients[i - 1]);
        }
        System.out.println(t0);
    }

    private static VirtualClient newGame(final int i) throws Exception {
        final String[] args = new String[] { "" + i };
        final VirtualClient virtualClient = BlockplusServer.runVC(args);
        virtualClient.send(MESSAGES.newPauseResumeGame(true));
        Thread.sleep(1000);
        for (int n = 1; n < 4; ++n) {
            BlockplusServer.runVC(args);
        }
        return virtualClient;
    }

    private static void startGame(final VirtualClient virtualClient) throws Exception {
        virtualClient.send(MESSAGES.newPauseResumeGame(false));
    }

}