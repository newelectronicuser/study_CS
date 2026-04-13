package com.codewithmosh.collections;

import java.util.*;

/**
 * ================================================================
 *  MapDemo — Interview-Ready Java Map Cheat-Sheet
 * ================================================================
 *
 *  Topics covered:
 *   1.  HashMap — CRUD, all access methods
 *   2.  Iteration — keySet / values / entrySet (preferred)
 *   3.  putIfAbsent / getOrDefault / replace / remove(k,v)
 *   4.  merge() — upsert with BiFunction
 *   5.  computeIfAbsent / computeIfPresent / compute
 *   6.  forEach() — lambda iteration
 *   7.  LinkedHashMap — insertion-order guarantee
 *   8.  TreeMap — sorted by key (NavigableMap operations)
 *   9.  Map.of() / Map.copyOf() — immutable maps (Java 9+)
 *  10.  Frequency counter pattern (word count)
 *  11.  Grouping pattern (index by first char)
 */
public class MapDemo {

    // =============================================================
    //  MAIN entry point
    // =============================================================
    public static void show() {
        System.out.println("\n--- 1. HashMap CRUD ---");
        hashMapCrud();

        System.out.println("\n--- 2. Iteration Patterns ---");
        iterationPatterns();

        System.out.println("\n--- 3. Conditional Operations ---");
        conditionalOperations();

        System.out.println("\n--- 4. merge() ---");
        mergeDemo();

        System.out.println("\n--- 5. compute() variants ---");
        computeDemo();

        System.out.println("\n--- 6. LinkedHashMap ---");
        linkedHashMapDemo();

        System.out.println("\n--- 7. TreeMap (NavigableMap) ---");
        treeMapDemo();

        System.out.println("\n--- 8. Immutable Maps ---");
        immutableMapDemo();

        System.out.println("\n--- 9. Frequency Counter (word count) ---");
        frequencyCounterDemo();

        System.out.println("\n--- 10. Grouping Pattern ---");
        groupingDemo();
    }

    // =============================================================
    //  1. HashMap CRUD
    // =============================================================
    private static void hashMapCrud() {
        // Key must be unique; overwriting a key returns the old value
        Map<String, Customer> map = new HashMap<>();
        var c1 = new Customer("Alice", "alice@mail.com");
        var c2 = new Customer("Bob",   "bob@mail.com");
        var c3 = new Customer("Carol", "carol@mail.com");

        map.put(c1.getEmail(), c1); // insert
        map.put(c2.getEmail(), c2);
        map.put(c3.getEmail(), c3);

        System.out.println("size: " + map.size()); // 3

        // get — returns null if key absent
        Customer found = map.get("alice@mail.com");
        System.out.println("get: " + found);

        // containsKey / containsValue
        System.out.println("containsKey:   " + map.containsKey("bob@mail.com"));
        System.out.println("containsValue: " + map.containsValue(c3));

        // replace — only updates if key already exists
        map.replace("alice@mail.com", new Customer("Alice 2.0", "alice@mail.com"));
        System.out.println("After replace: " + map.get("alice@mail.com"));

        // remove by key
        map.remove("carol@mail.com");
        System.out.println("After remove: " + map.keySet());

        // remove(key, value) — conditional: removes only if value matches
        boolean removed = map.remove("bob@mail.com", c2);
        System.out.println("remove(k,v): " + removed + " | size: " + map.size());
    }

    // =============================================================
    //  2. Iteration Patterns
    // =============================================================
    private static void iterationPatterns() {
        Map<String, Integer> scores = new HashMap<>();
        scores.put("Alice", 95);
        scores.put("Bob",   80);
        scores.put("Carol", 88);

        // a) keySet() — iterate keys
        System.out.println("Keys:");
        for (String key : scores.keySet()) System.out.println("  " + key);

        // b) values() — iterate values
        System.out.println("Values:");
        for (int v : scores.values()) System.out.println("  " + v);

        // c) entrySet() — PREFERRED (single pass, access both k and v)
        System.out.println("Entries:");
        for (Map.Entry<String, Integer> e : scores.entrySet())
            System.out.println("  " + e.getKey() + " → " + e.getValue());

        // d) forEach() — lambda, most concise
        System.out.println("forEach:");
        scores.forEach((k, v) -> System.out.println("  " + k + ": " + v));
    }

    // =============================================================
    //  3. Conditional Operations
    // =============================================================
    private static void conditionalOperations() {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);

        // getOrDefault — safe read, avoids null check
        int val = map.getOrDefault("missing", 0);
        System.out.println("getOrDefault(\"missing\"): " + val); // 0

        // putIfAbsent — inserts only if key is not already mapped
        map.putIfAbsent("a", 99); // "a" already exists → no-op
        map.putIfAbsent("b", 2);  // "b" is new → inserted
        System.out.println("after putIfAbsent: " + map); // {a=1, b=2}

        // replaceAll — apply a function to every value
        map.replaceAll((k, v) -> v * 10);
        System.out.println("after replaceAll (*10): " + map); // {a=10, b=20}
    }

    // =============================================================
    //  4. merge() — atomic upsert
    //     merge(key, value, (existingVal, newVal) -> merged)
    //     • if key absent  → inserts value
    //     • if key present → applies the BiFunction
    //     • if BiFunction returns null → removes the key
    // =============================================================
    private static void mergeDemo() {
        Map<String, Integer> wordCount = new HashMap<>();

        // Classic word-count with merge
        for (String word : List.of("apple", "banana", "apple", "cherry", "banana", "apple")) {
            wordCount.merge(word, 1, Integer::sum);
        }
        System.out.println("Word count (merge): " + wordCount);
        // {apple=3, cherry=1, banana=2}

        // merge to build CSV strings per key
        Map<String, String> grouped = new HashMap<>();
        for (String item : List.of("a:1", "b:2", "a:3", "b:4")) {
            String[] parts = item.split(":");
            grouped.merge(parts[0], parts[1], (existing, val) -> existing + "," + val);
        }
        System.out.println("Grouped (merge): " + grouped); // {a=1,3, b=2,4}
    }

    // =============================================================
    //  5. compute() variants
    // =============================================================
    private static void computeDemo() {
        Map<String, List<Integer>> index = new HashMap<>();

        // computeIfAbsent — lazy-init the list; then add to it in one line
        index.computeIfAbsent("evens", k -> new ArrayList<>()).add(2);
        index.computeIfAbsent("evens", k -> new ArrayList<>()).add(4);
        index.computeIfAbsent("odds",  k -> new ArrayList<>()).add(1);
        System.out.println("computeIfAbsent: " + index); // {evens=[2,4], odds=[1]}

        // computeIfPresent — update only if key exists
        Map<String, Integer> scores = new HashMap<>(Map.of("Alice", 80, "Bob", 70));
        scores.computeIfPresent("Alice", (k, v) -> v + 10); // Alice: 80 → 90
        scores.computeIfPresent("Carol", (k, v) -> v + 10); // Carol absent → no-op
        System.out.println("computeIfPresent: " + scores);

        // compute — always invoked (key can be absent or present)
        Map<String, Integer> counter = new HashMap<>();
        counter.compute("a", (k, v) -> (v == null) ? 1 : v + 1); // absent → 1
        counter.compute("a", (k, v) -> (v == null) ? 1 : v + 1); // present → 2
        System.out.println("compute: " + counter); // {a=2}
    }

    // =============================================================
    //  6. LinkedHashMap — preserves insertion order
    // =============================================================
    private static void linkedHashMapDemo() {
        // Same O(1) performance as HashMap but maintains iteration order
        Map<String, Integer> linked = new LinkedHashMap<>();
        linked.put("banana", 2);
        linked.put("apple",  1);
        linked.put("cherry", 3);

        // Iteration always in insertion order
        System.out.println("LinkedHashMap: " + linked); // {banana=2, apple=1, cherry=3}

        // LRU Cache using access-order LinkedHashMap
        Map<Integer, String> lruCache = new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, String> eldest) {
                return size() > 3; // evict when capacity > 3
            }
        };
        lruCache.put(1, "one");
        lruCache.put(2, "two");
        lruCache.put(3, "three");
        lruCache.get(1);           // access key 1 → moves it to end (most recent)
        lruCache.put(4, "four");   // evicts eldest (key 2)
        System.out.println("LRU cache: " + lruCache); // {3=three, 1=one, 4=four}
    }

    // =============================================================
    //  7. TreeMap — sorted by key (natural or custom Comparator)
    // =============================================================
    private static void treeMapDemo() {
        // TreeMap: Red-Black Tree → O(log n) for get/put/remove
        TreeMap<String, Integer> tm = new TreeMap<>();
        tm.put("banana", 2);
        tm.put("apple",  1);
        tm.put("cherry", 3);
        tm.put("date",   4);

        System.out.println("TreeMap (sorted): " + tm); // {apple, banana, cherry, date}

        // NavigableMap operations
        System.out.println("firstKey: " + tm.firstKey());       // apple
        System.out.println("lastKey:  " + tm.lastKey());        // date
        System.out.println("floorKey(\"avocado\"): " + tm.floorKey("avocado"));   // apple
        System.out.println("ceilingKey(\"blueberry\"): " + tm.ceilingKey("blueberry")); // cherry

        // headMap / tailMap — range views
        System.out.println("headMap(\"cherry\"): " + tm.headMap("cherry"));  // keys < cherry
        System.out.println("tailMap(\"banana\"): " + tm.tailMap("banana"));  // keys >= banana

        // Reverse order
        System.out.println("descendingMap: " + tm.descendingMap());

        // Custom key ordering (sort by string length, then alphabetically)
        TreeMap<String, Integer> custom = new TreeMap<>(
            Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder())
        );
        custom.put("fig", 1); custom.put("kiwi", 2); custom.put("apple", 3); custom.put("pea", 4);
        System.out.println("Custom TreeMap: " + custom.keySet()); // [fig, pea, kiwi, apple]
    }

    // =============================================================
    //  8. Immutable Maps (Java 9+)
    // =============================================================
    private static void immutableMapDemo() {
        // Map.of() — up to 10 key-value pairs; no nulls; no duplicates
        Map<String, Integer> fixed = Map.of(
            "Alice", 90,
            "Bob",   85,
            "Carol", 92
        );
        System.out.println("Map.of: " + fixed);
        // fixed.put("Dave", 80); // → UnsupportedOperationException

        // Map.ofEntries() — more than 10 entries
        Map<String, Integer> large = Map.ofEntries(
            Map.entry("A", 1),
            Map.entry("B", 2),
            Map.entry("C", 3)
        );
        System.out.println("Map.ofEntries: " + large);

        // Map.copyOf() — immutable copy
        Map<String, Integer> mutable = new HashMap<>(Map.of("x", 1, "y", 2));
        Map<String, Integer> copy = Map.copyOf(mutable);
        mutable.put("z", 3); // does NOT affect copy
        System.out.println("Map.copyOf: " + copy); // {x=1, y=2}
    }

    // =============================================================
    //  9. Frequency Counter — classic interview pattern
    // =============================================================
    private static void frequencyCounterDemo() {
        String text = "the quick brown fox jumps over the lazy dog the fox";
        Map<String, Integer> freq = new HashMap<>();

        for (String word : text.split(" ")) {
            freq.merge(word, 1, Integer::sum); // same as: freq.put(word, freq.getOrDefault(word,0)+1)
        }
        System.out.println("Word frequencies: " + freq);

        // Top word
        String topWord = Collections.max(freq.entrySet(),
            Map.Entry.comparingByValue()).getKey();
        System.out.println("Most frequent word: " + topWord + " (" + freq.get(topWord) + ")");

        // Character frequency
        Map<Character, Integer> charFreq = new HashMap<>();
        for (char c : "interview".toCharArray()) {
            charFreq.merge(c, 1, Integer::sum);
        }
        System.out.println("Char freq: " + charFreq);
    }

    // =============================================================
    //  10. Grouping Pattern — index by property
    // =============================================================
    private static void groupingDemo() {
        List<Customer> customers = List.of(
            new Customer("Alice", "alice@gmail.com"),
            new Customer("Bob",   "bob@yahoo.com"),
            new Customer("Carol", "carol@gmail.com"),
            new Customer("Dave",  "dave@yahoo.com")
        );

        // Group customers by email domain
        Map<String, List<Customer>> byDomain = new HashMap<>();
        for (Customer c : customers) {
            String domain = c.getEmail().split("@")[1];
            byDomain.computeIfAbsent(domain, k -> new ArrayList<>()).add(c);
        }
        byDomain.forEach((domain, list) ->
            System.out.println(domain + " → " + list));
    }
}
