# Forms and Performance

## 1. Forms in Angular
Angular provides two different approaches to handling user input through forms:

### Template-driven Forms
- **Concept**: Most logic is in the template (HTML).
- **Asynchronous**: Data binding and validation are asynchronous.
- **Easy**: Best for simple forms (e.g., login, feedback).
- **Tool**: Uses `FormsModule`.

### Reactive (Model-driven) Forms
- **Concept**: Most logic is in the component class.
- **Synchronous**: Access to data models and validation is synchronous.
- **Powerful**: Best for complex forms, dynamic fields, and deep testing.
- **Tool**: Uses `ReactiveFormsModule` (`FormGroup`, `FormControl`, `FormArray`).

## 2. Forms Validation
Both approaches support built-in validators (`required`, `minLength`, `email`) and custom validators (functions that return a validation error or null).

## 3. Performance and Change Detection
Change detection is the process by which Angular syncs the state of the component with the UI.

### Change Detection Strategies
- **`Default`**: Angular checks the entire component tree for every event (like a click or timer).
- **`OnPush`**: Angular only checks the component if:
    1. One of its `@Input()` references changes.
    2. An event originates from the component or its children.
    3. You manually trigger it via `ChangeDetectorRef`.

## 4. Angular Signals (Modern)
Introduced in Angular 16/17, **Signals** are a new way to handle reactivity. They allow Angular to track exactly which parts of the UI need to change, without relying on Zone.js and heavy change detection trees.
```typescript
const count = signal(0);
count.set(1);
count.update(n => n + 1);
const doubleCount = computed(() => count() * 2);
```

## 5. Zone.js and NgZone
Zone.js is a library that patches browser APIs to notify Angular when an asynchronous event happens. `NgZone` allows you to run code "outside" of Angular's zone to prevent unnecessary change detection cycles (improving performance for high-frequency events like `mousemove`).

> [!IMPORTANT]
> **Signals vs. RxJS**: 
> Signals are for **state management** and UI reactivity. RxJS is for **asynchronous event streams** (like HTTP requests). They are complementary, not mutually exclusive.
