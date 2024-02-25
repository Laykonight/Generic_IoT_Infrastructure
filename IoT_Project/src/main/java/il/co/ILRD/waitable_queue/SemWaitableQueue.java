package il.co.ILRD.waitable_queue;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.Semaphore;

public class SemWaitableQueue<E> {
    private final Semaphore numOfElementsInQ;
    private PriorityQueue<E> queue;
    private final Object qAccess = new Object();

    public SemWaitableQueue(Comparator<E> comparator, int capacity) {
        this.queue = new PriorityQueue<>(capacity, comparator);
        this.numOfElementsInQ = new Semaphore(0);
    }

    public SemWaitableQueue(int capacity) {
        this(null, capacity);
    }

    public boolean enqueue(E element) {
        boolean isAdded;
        synchronized (this.qAccess) {
            isAdded = this.queue.add(element);
        }

        if (isAdded) {
            this.numOfElementsInQ.release();
        }

        return isAdded;
    }

    public E dequeue() {
        try {
            this.numOfElementsInQ.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);  //TODO fault tolerance
        }

        synchronized (this.qAccess) {
            return this.queue.poll();
        }

    }

    public boolean remove(E element) {
        boolean isRemoved;
        synchronized (this.qAccess) {
            isRemoved = this.queue.remove(element);
        }
        if (isRemoved){
            try {
                this.numOfElementsInQ.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);  //TODO fault tolerance
            }
        }

        return isRemoved;
    }

    public int size() {
        synchronized (this.qAccess) {
            return this.queue.size();
        }
    }

    public E peek() {
        synchronized (this.qAccess) {
            return this.queue.peek();
        }
    }

    public boolean isEmpty() {
        synchronized (this.qAccess) {
            return this.queue.isEmpty();
        }
    }
}
