package gro

/**
 * @description:
 * @author ：dukelewis
 * @date: 2024/7/1
 * @Copyright ： https://github.com/DukeLewis
 */
class Main {
    private Timer timer;
    private List<Double> sampleData = Arrays.asList(0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0);
    private int index = 0;

    Main() {
        timer = new Timer();
    }

    void start() {
        timer.schedule(new TimerTask() {
            @Override
            void run() {
                if (index < sampleData.size()) {
                    double data = sampleData.get(index++);
                    ChartData.updateData(data);
                    System.out.println("Simulated data sent: " + data);
                } else {
                    timer.cancel(); // 停止定时器
                    System.out.println("All sample data has been sent.");
                }
            }
        }, 0, 1000); // 每秒发送一次数据
    }
    static void main(String[] args) {
        new Main().start();
    }
}
