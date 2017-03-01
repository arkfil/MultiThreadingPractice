public class TrafficController {

    private final static int MIN_TURN = 5;
    private final static int LEFT = 0;
    private final static int RIGHT = 1;

    private int onBridge = 0;
    private boolean waitingLeft = false;
    private boolean waitingRight = false;
    private int direction = LEFT;
    private int turnCount = 0;
    private boolean changingDirection = false;


    public synchronized void enterLeft() {
	if ((!waitingRight) && onBridge == 0) {
	    direction = RIGHT;
	    turnCount = 0;
	} else {
	    while ((turnCount >= MIN_TURN && waitingRight) || 
		   direction == LEFT || changingDirection) {
		changingDirection = turnCount >= MIN_TURN;
		waitingLeft = true;
		try {wait();} catch (InterruptedException e) {}
		waitingLeft = false;
	    }
	}
	onBridge++;
	turnCount++;
    }

    public synchronized void enterRight() {
	if ((!waitingLeft) && onBridge == 0) {
	    direction = LEFT;
	    turnCount = 0;
	} else {
	    while ((turnCount >= MIN_TURN && waitingLeft) || 
		   direction == RIGHT || changingDirection) {
		changingDirection = turnCount >= MIN_TURN;
		waitingRight = true;
		try {wait();} catch (InterruptedException e) {}
		waitingRight = false;
	    }
	}
	onBridge++;
	turnCount++;
    }

     public synchronized void leaveLeft() {
	 onBridge--;
	 if (onBridge==0 && waitingLeft) {
	     turnCount = 0;
	     direction = RIGHT;
	     changingDirection = false;
	     notifyAll();
	 }
    }

    public synchronized void leaveRight() {
	 onBridge--;
	 if (onBridge==0 && waitingRight) {
	     turnCount = 0;
	     direction = LEFT;
	     changingDirection = false;
	     notifyAll();
	 }

    }

}