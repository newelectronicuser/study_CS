# Dependency Injection and Services

Dependency Injection (DI) is a design pattern in which a class requests dependencies from external sources rather than creating them itself.

## 1. Services in Angular
Services are singleton objects that encapsulate business logic, data sharing, and external API communication.
```typescript
@Injectable({
  providedIn: 'root' // Makes it a singleton available throughout the app
})
export class DataService {
  constructor(private http: HttpClient) {}
}
```

## 2. Hierarchical Dependency Injection
Angular’s DI system is hierarchical. You can provide a service at different levels:
- **`root`**: One instance for the entire application.
- **`NgModule`**: One instance for everyone in that module.
- **`Component`**: One instance for that component and all its children.

## 3. Injecting a Service
Use the `constructor` or the `inject()` function (modern).
```typescript
// Constructor-based
constructor(private dataService: DataService) {}

// Function-based (Modern)
private dataService = inject(DataService);
```

## 4. Providers
Providers tell the DI system how to create the dependency.
- **`useClass`**: The default. Uses a specific class.
- **`useValue`**: Uses a static value (e.g., a config object).
- **`useFactory`**: Uses a function to create the dependency.
- **`useExisting`**: Alias for an existing provider.

## 5. Injection Tokens
Used when you want to inject something that isn't a class (like a string or an interface).
```typescript
export const API_URL = new InjectionToken<string>('apiUrl');
// Injecting it
constructor(@Inject(API_URL) private apiUrl: string) {}
```

## 6. Tree-Shakable Providers
Using `providedIn: 'root'` inside the `@Injectable` decorator makes the service "tree-shakable". If the service is never used in the app, it won't be included in the final JavaScript bundle.

> [!IMPORTANT]
> **Host, Self, SkipSelf, Optional**:
> These are resolution modifiers that tell Angular WHERE to look for a dependency.
> - `@Optional()`: Don't throw an error if not found.
> - `@SkipSelf()`: Look for the provider in the parent, not the current level.
