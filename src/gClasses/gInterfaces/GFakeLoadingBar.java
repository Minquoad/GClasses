package gClasses.gInterfaces;

public class GFakeLoadingBar extends GLoadingBar {

	private Incrementor incrementor = null;
	
	private float speed = 1f;

	public void start() {
		if (incrementor == null) {
			incrementor = new Incrementor();
		}
	}

	public void restart() {
		end();
		start();
	}

	public void end() {
		if (incrementor != null) {
			incrementor.end();
		}
		incrementor = null;
	}

	private class Incrementor extends Thread {

		private boolean stopped = false;

		public Incrementor() {
			Incrementor.this.start();
		}

		public void run() {

			long startTime = System.currentTimeMillis();

			while (!stopped) {

				long passedTime = System.currentTimeMillis() - startTime;

				float progression = -1f / (((float) passedTime*speed) / 8000f + 1f) + 1f;
				//float progression = (float) (-Math.exp(-((double) passedTime * (double)speed) / 8000d) + 1d);

				GFakeLoadingBar.this.setProgression(progression);

				new Thread(() -> GFakeLoadingBar.this.repaint()).start();
				;

				try {
					Thread.sleep(17);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		public void end() {
			stopped = true;
		}

	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

}
