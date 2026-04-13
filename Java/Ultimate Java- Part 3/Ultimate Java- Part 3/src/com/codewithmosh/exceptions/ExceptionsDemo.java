package com.codewithmosh.exceptions;

import java.io.*;
import java.sql.SQLException;

/**
 * ================================================================
 *  ExceptionsDemo — Interview-Ready Java Exception Handling Sheet
 * ================================================================
 *
 *  Topics covered:
 *   1.  Exception hierarchy — Throwable / Error / Exception / RuntimeException
 *   2.  try-catch (single, specific-to-general order)
 *   3.  Multi-catch (Java 7+)
 *   4.  finally block — always executes
 *   5.  try-with-resources — AutoCloseable (Java 7+)
 *   6.  throw keyword — explicitly raise exceptions
 *   7.  throws clause — declare checked exceptions in signature
 *   8.  Custom checked exception    — AccountException (wraps cause)
 *   9.  Custom unchecked exception  — InsufficientFundsException hierarchy
 *  10.  Exception chaining (wrapping) — getCause()
 *  11.  Re-throwing — catch, log, re-throw
 *  12.  Nested try-catch
 *  13.  Common runtime exceptions reference
 *  14.  Best practices summary
 */
public class ExceptionsDemo {

    // =============================================================
    //  MAIN entry point
    // =============================================================
    public static void show() {
        System.out.println("\n--- 1. Exception Hierarchy (see comments) ---");
        hierarchyDemo();

        System.out.println("\n--- 2. Basic try-catch ---");
        basicTryCatch();

        System.out.println("\n--- 3. Specific → General ordering ---");
        specificToGeneralDemo();

        System.out.println("\n--- 4. Multi-catch (Java 7+) ---");
        multiCatchDemo();

        System.out.println("\n--- 5. finally block ---");
        finallyDemo();

        System.out.println("\n--- 6. try-with-resources ---");
        tryWithResourcesDemo();

        System.out.println("\n--- 7. throw keyword ---");
        throwDemo();

        System.out.println("\n--- 8. throws clause (checked) ---");
        throwsClauseDemo();

        System.out.println("\n--- 9. Custom exceptions (Account / InsufficientFunds) ---");
        customExceptionDemo();

        System.out.println("\n--- 10. Exception chaining (wrapping) ---");
        exceptionChainingDemo();

        System.out.println("\n--- 11. Re-throwing ---");
        rethrowingDemo();

        System.out.println("\n--- 12. Nested try-catch ---");
        nestedTryCatchDemo();

        System.out.println("\n--- 13. Common Runtime Exceptions ---");
        commonRuntimeExceptions();
    }

    // =============================================================
    //  1. Exception Hierarchy (informational)
    //
    //  Throwable
    //  ├── Error               (JVM-level, don't catch: OOM, StackOverflow)
    //  └── Exception
    //      ├── RuntimeException  (UNCHECKED — not enforced by compiler)
    //      │   ├── NullPointerException
    //      │   ├── IllegalArgumentException
    //      │   ├── ArrayIndexOutOfBoundsException
    //      │   └── ArithmeticException
    //      └── (all others)    (CHECKED — compiler forces you to handle)
    //          ├── IOException
    //          ├── SQLException
    //          └── AccountException  ← our custom checked exception
    // =============================================================
    private static void hierarchyDemo() {
        System.out.println("See class hierarchy comment above.");
        System.out.println("AccountException extends Exception       → CHECKED");
        System.out.println("InsufficientFundsException extends Exception → CHECKED");
        // Checked = must be caught or declared with 'throws'
        // Unchecked = extends RuntimeException; no compile-time enforcement
    }

    // =============================================================
    //  2. Basic try-catch
    // =============================================================
    private static void basicTryCatch() {
        try {
            int result = 10 / 0; // throws ArithmeticException
            System.out.println("Result: " + result);
        } catch (ArithmeticException e) {
            System.out.println("Caught ArithmeticException: " + e.getMessage()); // / by zero
        }

        // e.getMessage(), e.toString(), e.printStackTrace() are key interview methods
        try {
            String s = null;
            s.length(); // throws NullPointerException
        } catch (NullPointerException e) {
            System.out.println("getMessage:  " + e.getMessage());
            System.out.println("getClass:    " + e.getClass().getSimpleName());
            // e.printStackTrace() would print the full stack trace
        }
    }

    // =============================================================
    //  3. Specific → General ordering
    //     Always catch more specific exceptions BEFORE general ones.
    //     Compiler enforces this for checked exceptions.
    // =============================================================
    private static void specificToGeneralDemo() {
        Object[] arr = {42, "hello", null};

        for (Object obj : arr) {
            try {
                String s = (String) obj;  // may throw ClassCastException
                System.out.println(s.length()); // may throw NullPointerException
            } catch (ClassCastException e) {
                System.out.println("CCE — not a String: " + e.getMessage());
            } catch (NullPointerException e) {
                System.out.println("NPE — null element");
            } catch (Exception e) {
                // General fallback — catches anything missed above
                System.out.println("Unexpected: " + e);
            }
        }
    }

    // =============================================================
    //  4. Multi-catch (Java 7+)
    //     Same handling for multiple exception types — no duplication.
    //     The variable 'e' is effectively final in multi-catch.
    // =============================================================
    private static void multiCatchDemo() {
        try {
            risky(1);
        } catch (ArithmeticException | ArrayIndexOutOfBoundsException e) {
            // Both handled identically — DRY principle
            System.out.println("Multi-catch: " + e.getClass().getSimpleName()
                + " — " + e.getMessage());
        }

        try {
            risky(2);
        } catch (ArithmeticException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Multi-catch: " + e.getMessage());
        }
    }

    private static void risky(int choice) {
        if (choice == 1) throw new ArithmeticException("divide by zero");
        int[] arr = new int[3];
        int x = arr[10]; // ArrayIndexOutOfBoundsException
    }

    // =============================================================
    //  5. finally block
    //     ALWAYS runs — even if catch rethrows, or return is used.
    //     Exception: System.exit() and JVM crash skip finally.
    // =============================================================
    private static void finallyDemo() {
        System.out.println("Case 1: exception caught");
        try {
            int x = 1 / 0;
        } catch (ArithmeticException e) {
            System.out.println("  catch: " + e.getMessage());
        } finally {
            System.out.println("  finally: always runs (cleanup here)");
        }

        System.out.println("Case 2: no exception");
        try {
            int x = 10 / 2;
            System.out.println("  result: " + x);
        } catch (ArithmeticException e) {
            System.out.println("  catch (won't run)");
        } finally {
            System.out.println("  finally: still runs");
        }

        System.out.println("Case 3: return inside try");
        System.out.println("  return value: " + finallyWithReturn());
    }

    private static String finallyWithReturn() {
        try {
            return "from-try";     // finally still runs before this is returned
        } finally {
            System.out.println("  finally: runs before return");
            // If you return here, it OVERRIDES the try's return value
        }
    }

    // =============================================================
    //  6. try-with-resources (Java 7+)
    //     Resource must implement AutoCloseable (or Closeable).
    //     close() is called automatically, even if an exception occurs.
    //     Eliminates verbose finally { try { r.close() } catch{} } pattern.
    // =============================================================
    private static void tryWithResourcesDemo() {
        // Reading a file — StringReader used here to avoid needing a real file
        try (var reader = new StringReader("Hello, try-with-resources!")) {
            char[] buf = new char[27];
            reader.read(buf);
            System.out.println("Read: " + new String(buf));
        } catch (IOException e) {
            System.out.println("IO error: " + e.getMessage());
        }
        // reader.close() was called automatically at the end of the try block

        // Multiple resources — closed in REVERSE order of declaration
        try (var r1 = new StringReader("r1");
             var r2 = new StringReader("r2")) {
            System.out.println("Both resources open — r2 closed first, then r1");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Custom AutoCloseable
        try (var db = new FakeDbConnection("localhost")) {
            db.query("SELECT 1");
        } catch (Exception e) {
            System.out.println("DB error: " + e.getMessage());
        }
        // FakeDbConnection.close() called automatically
    }

    /** Simulates a resource that implements AutoCloseable. */
    static class FakeDbConnection implements AutoCloseable {
        private final String host;
        FakeDbConnection(String host) {
            this.host = host;
            System.out.println("DB connected to " + host);
        }
        void query(String sql) { System.out.println("Querying: " + sql); }
        @Override
        public void close() {
            System.out.println("DB connection to " + host + " closed.");
        }
    }

    // =============================================================
    //  7. throw keyword — explicitly raise an exception
    //     Use IllegalArgumentException for invalid inputs (unchecked).
    //     Use domain exceptions for business rule violations.
    // =============================================================
    private static void throwDemo() {
        try {
            deposit(-50);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        try {
            findUser(-1);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }

    private static void deposit(float amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Deposit amount must be positive, got: " + amount);
        System.out.println("Deposited: " + amount);
    }

    private static void findUser(int id) {
        if (id <= 0)
            throw new IllegalArgumentException("User ID must be positive");
        System.out.println("Found user: " + id);
    }

    // =============================================================
    //  8. throws clause — declares checked exceptions in the signature
    //     Caller MUST handle it (catch or re-declare with throws).
    // =============================================================
    private static void throwsClauseDemo() {
        // Must handle AccountException because withdraw() declares 'throws AccountException'
        var account = new Account();
        try {
            account.withdraw(200); // balance is 0 → throws AccountException
        } catch (AccountException e) {
            System.out.println("Caught AccountException: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getMessage());
        }

        // Successful deposit
        try {
            account.deposit(500);
            System.out.println("Deposited OK");
        } catch (IllegalArgumentException e) {
            System.out.println("Bad deposit: " + e.getMessage());
        }
    }

    // =============================================================
    //  9. Custom Exceptions
    //     AccountException    — checked (extends Exception), wraps the cause
    //     InsufficientFundsException — checked (extends Exception)
    // =============================================================
    private static void customExceptionDemo() {
        var account = new Account();
        try {
            account.withdraw(100); // balance=0 → AccountException wrapping InsufficientFunds
        } catch (AccountException e) {
            System.out.println("AccountException: " + e.getMessage());
            // getCause() retrieves the wrapped InsufficientFundsException
            Throwable cause = e.getCause();
            System.out.println("Root cause: " + cause.getMessage());
        }

        // InsufficientFundsException with custom message
        try {
            throw new InsufficientFundsException("You need $500 but only have $0");
        } catch (InsufficientFundsException e) {
            System.out.println("Custom message: " + e.getMessage());
        }
    }

    // =============================================================
    //  10. Exception Chaining (Wrapping)
    //      Low-level exceptions (e.g., SQLException) are wrapped in
    //      domain exceptions (e.g., AccountException) to maintain
    //      the cause chain while hiding implementation details.
    // =============================================================
    private static void exceptionChainingDemo() {
        try {
            loadUserFromDb(42);
        } catch (AccountException e) {
            System.out.println("Domain error: " + e.getMessage());
            // Walk the cause chain
            Throwable cause = e.getCause();
            while (cause != null) {
                System.out.println("  caused by: " + cause.getClass().getSimpleName()
                    + " — " + cause.getMessage());
                cause = cause.getCause();
            }
        }
    }

    /** Simulates a service method that wraps a DB exception. */
    private static void loadUserFromDb(int id) throws AccountException {
        try {
            // Simulate a DB error
            throw new SQLException("Connection refused to db:5432");
        } catch (SQLException e) {
            // Wrap the low-level SQLException in a domain exception
            // Preserves the original cause for debugging
            throw new AccountException(e);
        }
    }

    // =============================================================
    //  11. Re-throwing
    //      Pattern: catch → log → rethrow the SAME or a WRAPPED exception.
    //      NEVER swallow exceptions silently (empty catch block).
    // =============================================================
    private static void rethrowingDemo() {
        // a) Rethrow as-is (checked)
        try {
            processFile();
        } catch (AccountException e) {
            System.out.println("Caller handled rethrown AccountException: " + e.getCause().getMessage());
        }

        // b) Wrap and rethrow (convert checked → unchecked)
        try {
            convertAndRethrow();
        } catch (RuntimeException e) {
            System.out.println("Caught wrapped runtime: " + e.getMessage());
            System.out.println("Original cause: " + e.getCause().getMessage());
        }
    }

    private static void processFile() throws AccountException {
        var account = new Account();
        try {
            account.withdraw(999);
        } catch (AccountException e) {
            System.out.println("[log] withdraw failed — rethrowing");
            throw e; // rethrow the same exception
        }
    }

    private static void convertAndRethrow() {
        try {
            var account = new Account();
            account.withdraw(500);
        } catch (AccountException e) {
            // Wrap checked exception in unchecked so callers don't have to declare it
            throw new RuntimeException("Account operation failed", e);
        }
    }

    // =============================================================
    //  12. Nested try-catch
    //      Use when inner failures should be handled independently,
    //      and the outer operation can continue.
    // =============================================================
    private static void nestedTryCatchDemo() {
        try {
            System.out.println("Outer try: starting");

            try {
                int[] arr = new int[3];
                arr[10] = 1; // ArrayIndexOutOfBoundsException
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Inner catch: array error — " + e.getMessage());
                // Inner exception is handled; outer try continues
            }

            System.out.println("Outer try: continues after inner catch");

            int x = 10 / 0; // ArithmeticException propagates to outer catch

        } catch (ArithmeticException e) {
            System.out.println("Outer catch: " + e.getMessage());
        } finally {
            System.out.println("Outer finally: always runs");
        }
    }

    // =============================================================
    //  13. Common Runtime Exceptions Reference
    // =============================================================
    private static void commonRuntimeExceptions() {
        // NullPointerException
        try { String s = null; s.length(); }
        catch (NullPointerException e)          { System.out.println("NPE: " + e.getClass().getSimpleName()); }

        // ArrayIndexOutOfBoundsException
        try { int[] a = new int[2]; int x = a[5]; }
        catch (ArrayIndexOutOfBoundsException e) { System.out.println("AIOOBE: " + e.getMessage()); }

        // ClassCastException
        try { Object o = "hello"; Integer i = (Integer) o; }
        catch (ClassCastException e)            { System.out.println("CCE: " + e.getMessage()); }

        // NumberFormatException (extends IllegalArgumentException)
        try { int n = Integer.parseInt("abc"); }
        catch (NumberFormatException e)         { System.out.println("NFE: " + e.getMessage()); }

        // ArithmeticException
        try { int r = 5 / 0; }
        catch (ArithmeticException e)           { System.out.println("AE: " + e.getMessage()); }

        // IllegalArgumentException
        try { deposit(-1); }
        catch (IllegalArgumentException e)      { System.out.println("IAE: " + e.getMessage()); }

        // IllegalStateException — object is in wrong state
        try { throw new IllegalStateException("cannot call send() before connect()"); }
        catch (IllegalStateException e)         { System.out.println("ISE: " + e.getMessage()); }

        // StackOverflowError (Error — don't catch in production)
        try { recurse(); }
        catch (StackOverflowError e)            { System.out.println("SOE: stack overflow caught"); }
    }

    private static void recurse() { recurse(); }
}
