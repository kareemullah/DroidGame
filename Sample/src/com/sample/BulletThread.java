package com.sample;


public class BulletThread {
    @SuppressWarnings("unused")
	private GameView view;
    int left=120;
    int top;
    private boolean running = false;
   
    public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}
	
    public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public BulletThread(GameView view) {
          this.view = view;
    }

    public void setRunning(boolean run) {
          running = run;
    }
    
    public boolean getRunning() {
        return running;
    }

}
