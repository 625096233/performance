package com.cmf.ec.performance.statistics;

/**
 * ��ʱ��,ÿ��һ��ʱ����������Щ����
 * 
 * @author zhengw
 * 
 */
public class Timer extends Thread {
	private long[] intervals;
	private Caller caller;

	public Timer(Caller caller, long... intervals) {
		this.caller = caller;
		this.intervals = intervals;
	}

	@Override
	public void run() {
		long startTime = System.currentTimeMillis();
		int totalCall = intervals.length;
		int i = 0;
		while (totalCall > i) {
			if ((System.currentTimeMillis() - startTime) >= intervals[i]) {
				final int j = i;
				Thread call = new Thread() {
					public void run() {
						caller.call(j + 1, intervals[j]);
					};
				};
				call.setDaemon(true);
				call.start();
				i++;
				startTime = System.currentTimeMillis();

			}
		}
	}

	public static interface Caller {
		public void call(int i, long interval);
	}

	public static void main(String[] args) {
		Timer timer = new Timer(new Caller() {

			@Override
			public void call(int i, long interval) {
				System.out.println("��" + i + "�δ���ʱ��" + System.currentTimeMillis());
			}
		}, new long[] { 10, 20, 30 });

		System.out.println("��ʼʱ��" + System.currentTimeMillis());

		timer.start();
	}
}
