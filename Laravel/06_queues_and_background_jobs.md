# Queues and Background Jobs

Laravel's queue services provide a unified API across a variety of different queue backends, such as Amazon SQS, Redis, or even a relational database.

## 1. Why use Queues?
Queues allow you to defer the processing of time-consuming tasks, such as sending an email or processing an uploaded image, until a later time. Moving these tasks to the background speeds up the response time for the user.

## 2. Jobs
Jobs represent a single unit of work.
```bash
php artisan make:job ProcessPodcast
```
The `handle` method in the job class contains the logic to be executed when the job is processed by the queue.

## 3. Workers
A queue worker is a long-lived process that polls the queue and executes jobs as they become available.
```bash
php artisan queue:work
```

## 4. Events and Listeners
Events provide a simple observer implementation, allowing you to subscribe and listen for various events that occur in your application.
- **Event**: Something that happened (e.g., `OrderPlaced`).
- **Listener**: Something that should happen in response to an event (e.g., `SendOrderEmail`).
- Events can be queued simply by implementing the `ShouldQueue` interface on the listener.

## 5. Task Scheduling
In the past, you may have authored a Cron configuration entry for every task you needed to schedule on your server. Laravel's command scheduler allows you to fluently and expressively define your command schedule within Laravel itself.
- Only one Cron entry is needed on your server that calls the Laravel scheduler every minute.

## 6. Notifications
Laravel provides support for sending notifications across a variety of delivery channels, including email, SMS, and Slack.

> [!TIP]
> **Horizon**: 
> If you are using Redis for your queues, you should use **Laravel Horizon**. It provides a beautiful dashboard and code-driven configuration for your Redis queues and queue workers, allowing you to monitor metrics like job throughput, runtime, and job failures.
