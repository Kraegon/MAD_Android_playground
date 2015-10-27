package comfycorp.hueapp;

/**
 * Created by Kraegon on 27/10/2015.
 */
public class BridgeMiddleMan {
    private static BridgeMiddleMan ourInstance = new BridgeMiddleMan();

    public static BridgeMiddleMan getInstance() {
        return ourInstance;
    }

    private BridgeMiddleMan() {
    }
}
