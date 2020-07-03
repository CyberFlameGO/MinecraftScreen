package dev.whip.screenshare.video;

public class ScreenThread extends Thread{
    private boolean isRunning = true;

    private final ScreenManager manager;

    public ScreenThread(ScreenManager manager){
        this.manager = manager;
    }

    @Override
    public void run() {
        int rednerCount = 0;
        int encodeCount = 0;
        while (this.isRunning) {
            long targetTime = System.currentTimeMillis() + (1000 / manager.getFPS());
            manager.render();
            rednerCount++;

            while (System.currentTimeMillis() < targetTime){
                manager.encode();
                encodeCount++;
            }
            /*
            System.out.println("Encode Count" + encodeCount);
            System.out.println("Render Count" + rednerCount);

             */
        }



        /*
        long lastTime = System.nanoTime();
        double ns = 1000000000 / (double) manager.getFPS();
        double delta = 0;
        long timer = System.currentTimeMillis();

        while (isRunning){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while(delta >= 1) {
                manager.encode();
                delta--;
            }
            manager.render();

            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
            }
        }

         */
    }

    public synchronized void setRunning(boolean running) {
        isRunning = running;
    }
}
