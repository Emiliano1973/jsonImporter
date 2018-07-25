package ie.demo.demost.entities.enums;

public enum State {

    STARTED("STARTED"), FINISHED("FINISHED");
    private final String value;

    State(String value) {
        this.value = value;
    }

    public static State getStateFromString(String state) {
        if (state == null)
            throw new NullPointerException("State string cannot be null!");
        State stateReturn = null;
        state = state.toUpperCase();
        switch (state) {
            case "STARTED": {
                stateReturn = STARTED;
                break;
            }
            case "FINISHED": {
                stateReturn = FINISHED;
                break;
            }
            default: {
                throw new IllegalArgumentException("Error, the value :" + state + " cannot be converted");
            }
        }

        return stateReturn;
    }

    public String getValue() {
        return value;
    }
}
