# Routing and Navigation

The Angular Router enables navigation from one view to another as users perform application tasks.

## 1. Route Configuration
Routes are an array of objects that map a URL path to a component.
```typescript
const routes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: 'user/:id', component: UserDetailComponent },
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: '**', component: PageNotFoundComponent } // Wildcard route
];
```

## 2. Lazy Loading
Loading application features only when they are needed. This significantly reduces the initial bundle size.
```typescript
{
  path: 'admin',
  loadChildren: () => import('./admin/admin.module').then(m => m.AdminModule)
}
// For standalone components:
{
  path: 'admin',
  loadComponent: () => import('./admin/admin.component').then(m => m.AdminComponent)
}
```

## 3. Route Guards
Guards decide if a user can navigate to or away from a route.
- **`CanActivate`**: Checks if a user has access to a route.
- **`CanActivateChild`**: Checks if a user has access to child routes.
- **`CanDeactivate`**: Checks if a user can leave a route (e.g., unsaved changes).
- **`CanMatch`**: Decides if a route can be matched (often used for lazy-loaded features).
- **`Resolve`**: Pre-fetches data before a route is activated.

## 4. Programmatic Navigation
Move to a different route using the `Router` service.
```typescript
constructor(private router: Router) {}
navigateToHome() {
  this.router.navigate(['/home']);
}
```

## 5. RouterLink
The directive to turn an HTML element into a navigation link.
```html
<a routerLink="/user/1" routerLinkActive="active-link">User Profile</a>
```

> [!TIP]
> **Functional Guards (Angular 15+)**: 
> You can now write route guards as simple functions instead of classes, making them much more concise and easier to test.
