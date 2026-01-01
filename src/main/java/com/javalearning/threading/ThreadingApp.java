package com.javalearning.threading;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * THREADING AND MULTITHREADING APPLICATION
 *
 * Topics covered:
 * - Threading (#69)
 * - Multithreading (#70)
 * - Anonymous classes (#63)
 * - TimerTasks (#64)
 *
 * REAL-WORLD USE CASE:
 * Concurrent programming for parallel processing
 * Similar to Web Workers in browsers or async operations in Node.js
 * Frequency: Common in backend services, data processing, UI responsiveness
 *
 * TYPESCRIPT COMPARISON:
 * TypeScript/JavaScript:
 * - Single-threaded with event loop
 * - async/await for asynchronous operations
 * - Web Workers for true parallelism
 * - setTimeout/setInterval for scheduled tasks
 *
 * Java:
 * - Multi-threaded by default
 * - Thread class for parallel execution
 * - ExecutorService for thread pools
 * - Timer/TimerTask for scheduled tasks
 */
public class ThreadingApp {

    /**
     * THREADING BASICS (#69)
     */
    public static void demonstrateBasicThreading() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║         BASIC THREADING DEMONSTRATION            ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        /**
         * THREADING (#69)
         *
         * Threads allow concurrent execution of code
         * Like running multiple tasks at the same time
         *
         * TWO WAYS TO CREATE THREADS IN JAVA:
         *
         * 1. Extend Thread class
         * 2. Implement Runnable interface (recommended)
         *
         * TypeScript comparison:
         * TypeScript (async/await):
         * async function task() { ... }
         * await task();
         *
         * TypeScript (Web Worker):
         * const worker = new Worker('worker.js');
         * worker.postMessage(data);
         *
         * Java:
         * Thread thread = new Thread(() -> { ... });
         * thread.start();
         */

        System.out.println("\n🧵 Creating and starting threads...\n");

        // Method 1: Using Runnable interface with lambda (Java 8+)
        Thread thread1 = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                System.out.println("Thread 1: Count " + i);
                try {
                    Thread.sleep(500); // Sleep for 500ms
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Method 2: Using Runnable interface with anonymous class
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 5; i++) {
                    System.out.println("  Thread 2: Count " + i);
                    try {
                        Thread.sleep(700);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // Start both threads
        thread1.start();
        thread2.start();

        // Wait for threads to complete
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n✓ Both threads completed");

        /* KEY THREAD METHODS:
         *
         * - start(): Begin thread execution
         * - run(): Code to execute (don't call directly!)
         * - sleep(ms): Pause execution
         * - join(): Wait for thread to finish
         * - interrupt(): Stop thread
         * - isAlive(): Check if thread is running
         *
         * IMPORTANT:
         * - Call start(), not run()
         * - start() creates new thread
         * - run() executes in current thread
         */

        /* REAL-WORLD USES:
         * - Background tasks (file uploads, data processing)
         * - UI responsiveness (don't block main thread)
         * - Server request handling (one thread per request)
         * - Batch processing
         * - Real-time data updates
         *
         * Frequency: Very common in backend development
         */
    }

    /**
     * ANONYMOUS CLASSES (#63)
     */
    public static void demonstrateAnonymousClasses() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║      ANONYMOUS CLASSES DEMONSTRATION             ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        /**
         * ANONYMOUS CLASSES (#63)
         *
         * Class without a name, defined inline
         * Useful for one-time implementations
         *
         * TypeScript comparison:
         * TypeScript (object literal):
         * const handler = {
         *   onClick: () => { console.log('Clicked'); }
         * };
         *
         * TypeScript (inline class):
         * const worker = new class {
         *   doWork() { console.log('Working'); }
         * };
         *
         * Java anonymous class:
         * Runnable task = new Runnable() {
         *   public void run() { System.out.println("Running"); }
         * };
         */

        System.out.println("\n🕵️ Creating anonymous class instances...\n");

        // Anonymous class implementing Runnable
        Runnable task1 = new Runnable() {
            @Override
            public void run() {
                System.out.println("Task 1: Anonymous class implementation");
                System.out.println("  Processing data...");
            }
        };

        // Same with lambda (shorter syntax) - Java 8+
        Runnable task2 = () -> {
            System.out.println("Task 2: Lambda expression");
            System.out.println("  Processing data...");
        };

        // Execute tasks
        task1.run();
        task2.run();

        // Anonymous class extending Thread
        Thread customThread = new Thread() {
            @Override
            public void run() {
                System.out.println("\nCustom thread: Anonymous class extending Thread");
                System.out.println("  Doing custom work...");
            }
        };

        customThread.start();

        try {
            customThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /* WHEN TO USE ANONYMOUS CLASSES:
         *
         * Use when:
         * - One-time implementation
         * - Short, simple implementation
         * - Event handlers
         * - Callbacks
         *
         * Don't use when:
         * - Implementation is complex
         * - Need to reuse the class
         * - Multiple methods needed
         *
         * MODERN ALTERNATIVE:
         * Lambda expressions (Java 8+) are preferred for functional interfaces
         *
         * Anonymous class:
         * button.addActionListener(new ActionListener() {
         *   public void actionPerformed(ActionEvent e) { ... }
         * });
         *
         * Lambda:
         * button.addActionListener(e -> { ... });
         */

        /* REAL-WORLD USES:
         * - Event listeners (UI frameworks)
         * - Callbacks (async operations)
         * - One-time tasks
         * - Thread creation
         * - Comparators for sorting
         *
         * Frequency: Common, but lambdas are preferred now
         */
    }

    /**
     * TIMER TASKS (#64)
     */
    public static void demonstrateTimerTasks() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║         TIMER TASKS DEMONSTRATION                ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        /**
         * TIMER TASKS (#64)
         *
         * Schedule tasks to run after delay or repeatedly
         * Like setTimeout/setInterval in JavaScript
         *
         * TypeScript/JavaScript:
         * setTimeout(() => { console.log('Once'); }, 1000);
         * setInterval(() => { console.log('Repeat'); }, 1000);
         *
         * Java:
         * Timer timer = new Timer();
         * timer.schedule(task, delay);
         * timer.scheduleAtFixedRate(task, delay, period);
         */

        System.out.println("\n⏲️ Scheduling timer tasks...\n");

        Timer timer = new Timer();

        // One-time task (like setTimeout)
        TimerTask oneTimeTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("⏰ One-time task executed (after 1 second)");
            }
        };

        timer.schedule(oneTimeTask, 1000); // Execute after 1 second

        // Repeating task (like setInterval)
        final int[] counter = {0};
        TimerTask repeatingTask = new TimerTask() {
            @Override
            public void run() {
                counter[0]++;
                System.out.println("🔄 Repeating task #" + counter[0] +
                                 " (every 500ms)");

                if (counter[0] >= 5) {
                    System.out.println("✓ Stopping repeating task");
                    this.cancel();
                    timer.cancel();
                }
            }
        };

        timer.scheduleAtFixedRate(repeatingTask, 2000, 500);
        // Start after 2 seconds, repeat every 500ms

        // Wait for tasks to complete
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /* TIMER VS SCHEDULEDEXECUTORSERVICE:
         *
         * Timer (older, simpler):
         * - Single thread
         * - Simple scheduling
         * - Good for simple tasks
         *
         * ScheduledExecutorService (modern, recommended):
         * - Thread pool
         * - Better performance
         * - More features
         * - Preferred for production
         */

        /* REAL-WORLD USES:
         * - Scheduled data refresh
         * - Periodic health checks
         * - Session cleanup
         * - Cache invalidation
         * - Auto-save functionality
         * - Heartbeat signals
         * - Reminder notifications
         *
         * In Angular:
         * - Similar to RxJS interval()
         * - Observable.interval(1000)
         * - setInterval(() => {}, 1000)
         *
         * Frequency: Common in backend services
         */
    }

    /**
     * MULTITHREADING (#70)
     */
    public static void demonstrateMultithreading() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║        MULTITHREADING DEMONSTRATION              ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        /**
         * MULTITHREADING (#70)
         *
         * Running multiple threads simultaneously
         * Better performance for CPU-intensive or I/O operations
         *
         * THREAD POOL (ExecutorService):
         * Manages a pool of threads for efficient execution
         * Better than creating threads manually
         *
         * TypeScript comparison:
         * JavaScript is single-threaded, but can use:
         * - Promise.all() for concurrent async operations
         * - Web Workers for true parallelism
         *
         * const promises = [task1(), task2(), task3()];
         * await Promise.all(promises);
         *
         * Java:
         * ExecutorService executor = Executors.newFixedThreadPool(3);
         * executor.submit(task1);
         * executor.submit(task2);
         * executor.submit(task3);
         */

        System.out.println("\n🧶 Creating thread pool with 3 threads...\n");

        // Create thread pool with 3 threads
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Submit multiple tasks
        for (int i = 1; i <= 5; i++) {
            final int taskId = i;

            executor.submit(() -> {
                String threadName = Thread.currentThread().getName();
                System.out.println("Task " + taskId + " started on " + threadName);

                try {
                    // Simulate work
                    Thread.sleep(1000);

                    System.out.println("Task " + taskId + " completed on " + threadName);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        // Shutdown executor
        executor.shutdown();

        // Wait for all tasks to complete
        while (!executor.isTerminated()) {
            // Wait
        }

        System.out.println("\n✓ All tasks completed");

        /* THREAD POOL TYPES:
         *
         * newFixedThreadPool(n):
         * - Fixed number of threads
         * - Reuses threads
         * - Good for known workload
         *
         * newCachedThreadPool():
         * - Creates threads as needed
         * - Reuses idle threads
         * - Good for many short tasks
         *
         * newSingleThreadExecutor():
         * - Single thread
         * - Tasks execute sequentially
         * - Good for ordered execution
         *
         * newScheduledThreadPool(n):
         * - Scheduled/periodic tasks
         * - Like Timer but better
         */

        /* THREAD SAFETY WARNING:
         *
         * When multiple threads access shared data, use:
         * - synchronized keyword
         * - Locks (ReentrantLock)
         * - Atomic classes (AtomicInteger)
         * - Concurrent collections (ConcurrentHashMap)
         *
         * Example:
         * synchronized (object) {
         *   // Only one thread at a time
         *   sharedData++;
         * }
         */

        /* REAL-WORLD USES:
         * - Web servers (handle multiple requests)
         * - Batch processing (process files in parallel)
         * - Data processing (process records concurrently)
         * - Image processing (process pixels in parallel)
         * - API calls (make multiple API calls concurrently)
         * - Background tasks (email sending, notifications)
         *
         * Frequency: Very common in enterprise applications
         */
    }

    /**
     * PRACTICAL EXAMPLE: Data Processing
     */
    public static void demonstrateDataProcessing() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║     PRACTICAL: PARALLEL DATA PROCESSING          ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        System.out.println("\n📊 Processing 10,000 records with multithreading...\n");

        final int TOTAL_RECORDS = 10000;
        final int THREAD_COUNT = 4;
        final int RECORDS_PER_THREAD = TOTAL_RECORDS / THREAD_COUNT;

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        long startTime = System.currentTimeMillis();

        // Process data in parallel
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            final int startRecord = i * RECORDS_PER_THREAD;
            final int endRecord = startRecord + RECORDS_PER_THREAD;

            executor.submit(() -> {
                System.out.println("Thread " + threadId +
                                 " processing records " + startRecord + " to " + endRecord);

                int processed = 0;
                for (int record = startRecord; record < endRecord; record++) {
                    // Simulate data processing
                    Math.sqrt(record); // Some calculation
                    processed++;
                }

                System.out.println("Thread " + threadId + " completed: " +
                                 processed + " records processed");
            });
        }

        executor.shutdown();

        while (!executor.isTerminated()) {
            // Wait
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("\n✓ All processing completed in " + duration + "ms");
        System.out.println("✓ Using " + THREAD_COUNT + " threads for parallel processing");

        /* BENEFITS OF MULTITHREADING:
         *
         * 1. Better CPU utilization
         * 2. Faster processing of large datasets
         * 3. Responsive applications (UI doesn't freeze)
         * 4. Concurrent request handling
         *
         * WHEN TO USE:
         * - CPU-intensive operations
         * - I/O operations (file, network)
         * - Independent tasks
         * - Large datasets
         *
         * WHEN NOT TO USE:
         * - Sequential dependencies
         * - Overhead > benefit
         * - Simple, fast operations
         */
    }

    /**
     * Main method
     */
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║    THREADING & CONCURRENCY EXAMPLES              ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        demonstrateBasicThreading();
        demonstrateAnonymousClasses();
        demonstrateTimerTasks();
        demonstrateMultithreading();
        demonstrateDataProcessing();

        System.out.println("\n\n╔══════════════════════════════════════════════════╗");
        System.out.println("║    ALL CONCEPTS DEMONSTRATED SUCCESSFULLY!       ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        System.out.println("\nConcepts covered:");
        System.out.println("✓ Threading (#69) - Concurrent execution");
        System.out.println("✓ Multithreading (#70) - Thread pools & parallel processing");
        System.out.println("✓ Anonymous Classes (#63) - Inline implementations");
        System.out.println("✓ TimerTasks (#64) - Scheduled tasks");
    }
}
