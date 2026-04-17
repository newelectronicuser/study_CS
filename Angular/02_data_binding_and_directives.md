# Data Binding and Directives

Directives are classes that add new behavior to elements in the HTML.

## 1. Data Binding Types
- **Interpolation**: `{{ expression }}`. Used to display dynamic data in the template.
- **Property Binding**: `[property]="expression"`. Used to set properties of DOM elements or component inputs.
- **Event Binding**: `(event)="handler()"`. Used to listen for and respond to user actions.
- **Two-way Binding**: `[(ngModel)]="property"`. Combination of property and event binding to keep UI and model in sync.

## 2. Types of Directives
### Components
Directives with a template. This is the most common type.

### Structural Directives
Change the DOM layout by adding or removing elements. Uses the `*` syntax.
- **`*ngIf`**: Conditionally includes a template based on a truthy value.
- **`*ngFor`**: Iterates over a list and stamps out a template for each item.
- **`*ngSwitch`**: Selects one of many templates based on a switch expression.

### Attribute Directives
Change the appearance or behavior of an existing element.
- **`ngClass`**: Adds and removes a set of CSS classes.
- **`ngStyle`**: Adds and removes a set of HTML styles.
- **`ngModel`**: Adds two-way data binding to an HTML form element.

## 3. Custom Directives
You can create your own directives using the `@Directive` decorator to encapsulate reusable behavior.
```typescript
@Directive({
  selector: '[appHighlight]',
  standalone: true
})
export class HighlightDirective {
  constructor(private el: ElementRef) {
    this.el.nativeElement.style.backgroundColor = 'yellow';
  }
}
```

## 4. `*ngIf` vs `hidden` property
- **`*ngIf`**: Completely removes the element from the DOM. Better for performance if the element is large or has many deep children.
- **`[hidden]`**: Only sets the CSS `display: none`. Better for performance if the element needs to be toggled frequently.

> [!TIP]
> **Control Flow Syntax (Angular 17+)**: Angular 17 introduced a new built-in control flow syntax (`@if`, `@for`, `@switch`) which is more performant and doesn't require importing `CommonModule`.
