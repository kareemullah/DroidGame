package com.sample;

import java.util.Random;

public class TargetThread {
    @SuppressWarnings("unused")
	private GameView view;
    int left=120;
    Random random = null;
    int top;
    private boolean running = false;
    int collapseCount=0;
    
    public TargetThread(GameView view)
    {
    	this.view=view;
    }
   
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

    public void setRunning(boolean run) {
          running = run;
    }
    
    public boolean getRunning() {
        return running;
    }

//	@Override
//	public void run() {
//		// TODO Auto-generated method stub
//		random = new Random();
//		setTop(random.nextInt(view.getHeight()));
//		
//	}

}
