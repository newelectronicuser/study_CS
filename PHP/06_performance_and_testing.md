# Performance and Testing

Ensuring that your code is both fast and correct is essential for high-scale applications.

## 1. OpCache
PHP is an interpreted language. Every time a script runs, the engine must parse and compile it.
- **How it works**: OpCache stores the precompiled script bytecode in shared memory. This eliminates the need for PHP to load and parse scripts on each request.
- **Why?**: It can increase performance by 3x or more.

## 2. PHP-FPM (FastCGI Process Manager)
The standard for modern PHP deployments.
- **Process Management**:
    - **Master Process**: Manages the workers, reloads configuration, and opens/closes listening sockets.
    - **Worker Processes**: Actually execute the PHP code.
- **Tuning**: You can configure the number of worker processes to scale with your server's CPU and RAM.

## 3. Profiling and Benchmarking
- **Xdebug**: A powerful tool for debugging and profiling PHP scripts. It provides stack traces and can generate execution logs (Cachegrind files).
- **Blackfire**: A premium profiling tool that gives deep insights into function call times, memory usage, and SQL queries.
- **Micro-benchmarking**: Using `microtime(true)` to measure specific blocks of code.

## 4. Unit Testing with PHPUnit
The de-facto standard for testing in the PHP ecosystem.
- **Assertions**: Methods to check if the code behaves as expected (e.g., `assertEquals`, `assertTrue`).
- **Mocking**: Creating "fake" objects to isolate the code being tested (e.g., using Mockery or PHPUnit's internal mocker).
```php
public function testAddition() {
    $this->assertEquals(4, 2 + 2);
}
```

## 5. Static Analysis
Tools that find bugs without actually running the code.
- **PHPStan**: Focuses on finding bugs and type errors.
- **Psalm**: A static analysis tool for finding errors in PHP applications.
- **PHP_CodeSniffer**: Ensures your code follows specific coding standards (like PSR-12).

> [!TIP]
> **Performance is often about I/O**: 
> Most bottlesnecks in web applications are caused by slow database queries or external API calls, not the PHP engine itself. Always profile your full request cycle before optimizing tight loops.
