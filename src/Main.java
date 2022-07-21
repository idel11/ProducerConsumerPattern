import java.util.LinkedList;
import java.util.Queue;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Buffer buffer = new Buffer(2);
        Thread producerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    buffer.produce();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread consumerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    buffer.consume();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        producerThread.start();
        consumerThread.start();

        producerThread.join();
        consumerThread.join();


    }

    static class Buffer {

        private Queue<Integer> queue;
        private int size;

        public Buffer(int size) {
            this.queue = new LinkedList<>();
            this.size = size;
        }

        public void produce() throws InterruptedException {
            int value = 0;
            while (true) {
                synchronized (this) {
                    while (queue.size() >= size){
                        wait();
                    }
                    queue.add(value);
                    System.out.println("Produced product with ID: " + value);
                    value++;
                    notify();
                    Thread.sleep(1000);
                }
            }
        }

        public void consume() throws InterruptedException {
            while (true){
                synchronized (this){
                    while (queue.size() == 0){
                        wait();
                    }
                    Integer value = queue.poll();
                    System.out.println("Product id: " + value + " is consumed");
                    notify();
                    Thread.sleep(1000);
                }
            }
        }
    }
}
