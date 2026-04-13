package com.codewithmosh.collections;

import java.util.*;

/**
 * ================================================================
 *  QueueDemo — Interview-Ready Java Queue Cheat-Sheet
 * ================================================================
 *
 *  Topics covered:
 *   1.  Queue interface — offer/poll/peek vs add/remove/element
 *   2.  ArrayDeque as Queue (FIFO)
 *   3.  ArrayDeque as Stack (LIFO) — preferred over java.util.Stack
 *   4.  Deque — double-ended operations
 *   5.  PriorityQueue — Min-Heap (natural order)
 *   6.  PriorityQueue — Max-Heap (custom Comparator)
 *   7.  PriorityQueue with custom objects (Comparable / Comparator)
 *   8.  Interview patterns — BFS with Queue, task scheduling
 */
public class QueueDemo {

    // =============================================================
    //  MAIN entry point
    // =============================================================
    public static void show() {
        System.out.println("\n--- 1. Queue Interface (safe vs throwing methods) ---");
        queueInterfaceDemo();

        System.out.println("\n--- 2. ArrayDeque as FIFO Queue ---");
        fifoDemo();

        System.out.println("\n--- 3. ArrayDeque as Stack (LIFO) ---");
        stackDemo();

        System.out.println("\n--- 4. Deque — double-ended operations ---");
        dequeDemo();

        System.out.println("\n--- 5. PriorityQueue — Min-Heap ---");
        minHeapDemo();

        System.out.println("\n--- 6. PriorityQueue — Max-Heap ---");
        maxHeapDemo();

        System.out.println("\n--- 7. PriorityQueue with custom objects ---");
        customPriorityQueueDemo();

        System.out.println("\n--- 8. BFS simulation with Queue ---");
        bfsSimulationDemo();
    }

    // =============================================================
    //  1. Queue Interface — safe (non-throwing) vs throwing methods
    // =============================================================
    private static void queueInterfaceDemo() {
        // RULE: Prefer safe methods (offer/poll/peek) over throwing methods
        // in production code — they return null/false instead of throwing.

        Queue<String> queue = new ArrayDeque<>();

        // Throwing versions (throw exception on failure)
        queue.add("A");     // throws IllegalStateException if capacity exceeded
        queue.add("B");
        queue.add("C");

        String head = queue.element(); // throws NoSuchElementException if empty
        System.out.println("element() (peek, no remove): " + head); // A

        String removed = queue.remove(); // throws NoSuchElementException if empty
        System.out.println("remove() (removes head): " + removed); // A
        System.out.println("queue after remove: " + queue);

        // Safe (non-throwing) versions
        Queue<String> safe = new ArrayDeque<>();
        boolean offered = safe.offer("X"); // returns false if capacity exceeded
        String polled = safe.poll();       // returns null if empty
        String peeked = safe.peek();       // returns null if empty

        System.out.println("offer: " + offered); // true
        System.out.println("poll:  " + polled);  // X
        System.out.println("peek:  " + peeked);  // null (queue is now empty)
    }

    // =============================================================
    //  2. ArrayDeque as FIFO Queue — faster than LinkedList
    // =============================================================
    private static void fifoDemo() {
        // ArrayDeque: resizable circular array, no null elements allowed
        // Faster than LinkedList (no node allocation)
        Queue<String> queue = new ArrayDeque<>();

        // Enqueue (tail)
        queue.offer("Task-1");
        queue.offer("Task-2");
        queue.offer("Task-3");
        System.out.println("Queue: " + queue);

        // Process in FIFO order
        while (!queue.isEmpty()) {
            System.out.println("Processing: " + queue.poll()); // head first
        }
    }

    // =============================================================
    //  3. ArrayDeque as Stack (LIFO) — preferred over java.util.Stack
    //     java.util.Stack is a legacy class (extends Vector, synchronized)
    // =============================================================
    private static void stackDemo() {
        Deque<Integer> stack = new ArrayDeque<>();

        // push = addFirst (top of stack)
        stack.push(10);
        stack.push(20);
        stack.push(30);
        System.out.println("Stack after pushes: " + stack); // [30, 20, 10]

        // peek — look at top without removing
        System.out.println("peek: " + stack.peek()); // 30

        // pop = removeFirst (LIFO)
        System.out.println("pop: " + stack.pop()); // 30
        System.out.println("pop: " + stack.pop()); // 20
        System.out.println("Stack after pops: " + stack); // [10]

        // Practical: balanced parentheses checker
        System.out.println("Balanced '(({}))': " + isBalanced("(({}))")); // true
        System.out.println("Balanced '([)]':   " + isBalanced("([)]"));   // false
    }

    private static boolean isBalanced(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } else {
                if (stack.isEmpty()) return false;
                char top = stack.pop();
                if ((c == ')' && top != '(') ||
                    (c == ']' && top != '[') ||
                    (c == '}' && top != '{')) return false;
            }
        }
        return stack.isEmpty();
    }

    // =============================================================
    //  4. Deque — double-ended queue
    //     addFirst/offerFirst/peekFirst/pollFirst (head end)
    //     addLast/offerLast/peekLast/pollLast   (tail end)
    // =============================================================
    private static void dequeDemo() {
        Deque<String> deque = new ArrayDeque<>();

        deque.offerFirst("B"); // head
        deque.offerFirst("A"); // head (now: A B)
        deque.offerLast("C");  // tail (now: A B C)
        deque.offerLast("D");  // tail (now: A B C D)
        System.out.println("Deque: " + deque); // [A, B, C, D]

        System.out.println("peekFirst: " + deque.peekFirst()); // A
        System.out.println("peekLast:  " + deque.peekLast());  // D

        System.out.println("pollFirst: " + deque.pollFirst()); // A
        System.out.println("pollLast:  " + deque.pollLast());  // D
        System.out.println("Deque after polls: " + deque);     // [B, C]

        // Sliding window maximum (classic deque problem)
        System.out.println("Sliding window max k=3: "
            + Arrays.toString(slidingWindowMax(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3)));
        // [3, 3, 5, 5, 6, 7]
    }

    /** Return the maximum of each sliding window of size k. */
    private static int[] slidingWindowMax(int[] nums, int k) {
        int n = nums.length;
        int[] result = new int[n - k + 1];
        Deque<Integer> dq = new ArrayDeque<>(); // stores indices

        for (int i = 0; i < n; i++) {
            // Remove indices outside window
            while (!dq.isEmpty() && dq.peekFirst() < i - k + 1) dq.pollFirst();
            // Remove smaller elements (they'll never be the max)
            while (!dq.isEmpty() && nums[dq.peekLast()] < nums[i]) dq.pollLast();
            dq.offerLast(i);
            if (i >= k - 1) result[i - k + 1] = nums[dq.peekFirst()];
        }
        return result;
    }

    // =============================================================
    //  5. PriorityQueue — Min-Heap (natural ordering)
    // =============================================================
    private static void minHeapDemo() {
        // PriorityQueue is a Min-Heap by default
        // poll() always returns the SMALLEST element: O(log n)
        // peek() returns smallest without removing: O(1)
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        minHeap.offer(5);
        minHeap.offer(1);
        minHeap.offer(3);
        minHeap.offer(2);
        minHeap.offer(4);

        System.out.println("MinHeap peek (min): " + minHeap.peek()); // 1

        // Drain in ascending order
        System.out.print("MinHeap poll order: ");
        while (!minHeap.isEmpty()) System.out.print(minHeap.poll() + " "); // 1 2 3 4 5
        System.out.println();

        // Practical: Kth smallest element
        int[] arr = {7, 10, 4, 3, 20, 15};
        int k = 3;
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int n : arr) pq.offer(n);
        int kthSmallest = 0;
        for (int i = 0; i < k; i++) kthSmallest = pq.poll();
        System.out.println(k + "rd smallest in " + Arrays.toString(arr) + ": " + kthSmallest); // 7
    }

    // =============================================================
    //  6. PriorityQueue — Max-Heap (reversed Comparator)
    // =============================================================
    private static void maxHeapDemo() {
        // Reverse natural order → Max-Heap
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        maxHeap.offer(5);
        maxHeap.offer(1);
        maxHeap.offer(3);
        maxHeap.offer(2);
        maxHeap.offer(4);

        System.out.println("MaxHeap peek (max): " + maxHeap.peek()); // 5

        System.out.print("MaxHeap poll order: ");
        while (!maxHeap.isEmpty()) System.out.print(maxHeap.poll() + " "); // 5 4 3 2 1
        System.out.println();

        // Practical: Kth largest element using Min-Heap of size k
        int[] arr = {3, 2, 1, 5, 6, 4};
        int k = 2;
        PriorityQueue<Integer> minK = new PriorityQueue<>();
        for (int n : arr) {
            minK.offer(n);
            if (minK.size() > k) minK.poll(); // keep only k largest
        }
        System.out.println(k + "nd largest in " + Arrays.toString(arr) + ": " + minK.peek()); // 5
    }

    // =============================================================
    //  7. PriorityQueue with custom objects
    // =============================================================
    private static void customPriorityQueueDemo() {
        // Sort customers by name (compareTo / Comparable)
        PriorityQueue<Customer> byName = new PriorityQueue<>();
        byName.offer(new Customer("Zara",  "z@mail.com"));
        byName.offer(new Customer("Alice", "a@mail.com"));
        byName.offer(new Customer("Bob",   "b@mail.com"));

        System.out.print("By name (natural): ");
        while (!byName.isEmpty()) System.out.print(byName.poll() + " "); // Alice Bob Zara
        System.out.println();

        // Sort customers by email (Comparator)
        PriorityQueue<Customer> byEmail = new PriorityQueue<>(new EmailComparator());
        byEmail.offer(new Customer("Zara",  "z@mail.com"));
        byEmail.offer(new Customer("Alice", "c@mail.com"));
        byEmail.offer(new Customer("Bob",   "a@mail.com"));

        System.out.print("By email (EmailComparator): ");
        while (!byEmail.isEmpty()) System.out.print(byEmail.poll() + " "); // Bob Alice Zara
        System.out.println();
    }

    // =============================================================
    //  8. BFS simulation — Queue is the canonical BFS data structure
    // =============================================================
    private static void bfsSimulationDemo() {
        // Binary tree node (inline for demo)
        class Node {
            int val; Node left, right;
            Node(int v) { val = v; }
            Node(int v, Node l, Node r) { val = v; left = l; right = r; }
        }

        // Build a small tree:
        //       1
        //      / \
        //     2   3
        //    / \
        //   4   5
        Node root = new Node(1,
            new Node(2, new Node(4), new Node(5)),
            new Node(3));

        // Level-order traversal using Queue
        Queue<Node> bfsQueue = new ArrayDeque<>();
        bfsQueue.offer(root);
        List<List<Integer>> levels = new ArrayList<>();

        while (!bfsQueue.isEmpty()) {
            int size = bfsQueue.size(); // nodes at current level
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                Node node = bfsQueue.poll();
                level.add(node.val);
                if (node.left  != null) bfsQueue.offer(node.left);
                if (node.right != null) bfsQueue.offer(node.right);
            }
            levels.add(level);
        }
        System.out.println("BFS level-order: " + levels); // [[1], [2, 3], [4, 5]]
    }
}
