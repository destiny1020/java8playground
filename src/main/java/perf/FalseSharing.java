package perf;

public class FalseSharing extends Thread {
	private static class DataHolder {
		private volatile long l1 = 0;
		private volatile long l2 = 0;
		private volatile long l3 = 0;
		private volatile long l4 = 0;
//		private long l1 = 0;
//		private long l2 = 0;
//		private long l3 = 0;
//		private long l4 = 0;
	}

	private static final DataHolder dh = new DataHolder();
	private static long nLoops;

	public FalseSharing(Runnable r) {
		super(r);
	}

	public static void main(String[] args) throws Exception {
		nLoops = 1000000000;
		FalseSharing[] tests = new FalseSharing[1];
		tests[0] = new FalseSharing(() -> {
			long temp1 = dh.l1;
			long temp2 = dh.l1;
			long temp3 = dh.l1;
			long temp4 = dh.l1;
			for (long i = 0; i < nLoops; i++) {
				temp1 += i;
				temp2 += i;
				temp3 += i;
				temp4 += i;
//				dh.l2 += i;
//				dh.l3 += i;
//				dh.l4 += i;
			}
			dh.l1 = temp1;
			dh.l2 = temp2;
			dh.l3 = temp3;
			dh.l4 = temp4;
			System.out.println(dh.l1);
			System.out.println(dh.l2);
			System.out.println(dh.l3);
			System.out.println(dh.l4);
		});
//		tests[1] = new FalseSharing(() -> {
//			long temp = dh.l2;
//			for (long i = 0; i < nLoops; i++) {
//				temp += i;
//			}
//			dh.l2 = temp;
//			System.out.println(dh.l2);
//		});
//		tests[2] = new FalseSharing(() -> {
//			long temp = dh.l3;
//			for (long i = 0; i < nLoops; i++) {
//				temp += i;
//			}
//			dh.l3 = temp;
//			System.out.println(dh.l3);
//		});
//		tests[3] = new FalseSharing(() -> {
//			long temp = dh.l4;
//			for (long i = 0; i < nLoops; i++) {
//				temp += i;
//			}
//			dh.l4 = temp;
//			System.out.println(dh.l4);
//		});
		long then = System.currentTimeMillis();
		for (FalseSharing ct : tests) {
			ct.start();
		}

		for (FalseSharing ct : tests) {
			ct.join();
		}
		long now = System.currentTimeMillis();
		// 1 threads: Duration: 28026 ms, no volitile: Duration: 1616 ms, volatile with temp: 1884 ms
		// 2 threads: Duration: 65603 ms
		// 4 threads: Duration: 58209 ms, no volatile: Duration: 1368 ms, volatile with temp: 915 ms
		System.out.println("Duration: " + (now - then) + " ms");
	}
}
