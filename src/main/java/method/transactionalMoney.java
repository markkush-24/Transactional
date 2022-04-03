package method;

import static method.DB.transferMoney;

public class transactionalMoney {
    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 2; i++) {
                transferMoney(1, 2, 10);
            }
        });
        Thread thread2 = new Thread(() -> {

            for (int i = 0; i < 2; i++) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                transferMoney(2, 1, 10);
            }
        });
        Thread thread3 = new Thread(() -> {
            for (int i = 0; i < 2; i++) {
                transferMoney(1, 2, 10);
            }
        });
        Thread thread4 = new Thread(() -> {
            for (int i = 0; i < 2; i++) {
                transferMoney(2, 1, 10);
            }
        });


//        Thread thread1 = new Thread(new MyThread1());
//        Thread thread2 = new Thread(new MyThread2());
//        Thread thread3 = new Thread(new MyThread1());
//        Thread thread4 = new Thread(new MyThread2());

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        try {
            thread1.join();//after finish main
            thread2.join();
            thread3.join();
            thread4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class MyThread1 implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 2; i++) {
            transferMoney(2, 1, 10);
        }
    }
}

class MyThread2 implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 2; i++) {
            transferMoney(1, 2, 10);
        }
    }
}