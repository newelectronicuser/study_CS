# Architecture and Components

Angular is a platform and framework for building single-page client applications using HTML and TypeScript.

## 1. Core Architecture
- **Modules (`NgModule`)**: Traditional way to organize an application into cohesive blocks of functionality.
- **Components**: The basic building block of an Angular UI. Every component has:
    - A **Template** (HTML).
    - **Styles** (CSS).
    - **Logic** (TypeScript class).
    - **Metadata** (Defined in the `@Component` decorator).
- **Standalone Components (Modern)**: Introduced in Angular 14+, these allow you to build components without needing an `NgModule`. They are now the recommended way to build Angular apps.

## 2. Component Decorator Metadata
- `selector`: The CSS selector that identifies this component in a template.
- `templateUrl` / `template`: The HTML for the component.
- `styleUrls` / `styles`: The CSS for the component.
- `standalone`: boolean (true for standalone components).
- `imports`: Used in standalone components to declare dependencies.

## 3. Component Communication
- **`@Input()`**: Allows data to flow from parent to child.
- **`@Output()` with `EventEmitter`**: Allows data to flow from child to parent via events.
- **`ViewChild`**: Allows a parent component to access properties and methods of a child component or DOM element.
- **`ContentChild`**: Allows a component to access content projected into it via `<ng-content>`.

## 4. Templates and Data Binding
- **Interpolation**: `{{ value }}` (Data to UI).
- **Property Binding**: `[property]="value"` (Data to UI).
- **Event Binding**: `(event)="handler()"` (UI to Data).
- **Two-way Binding**: `[(ngModel)]="value"` (Syncs Data and UI).

## 5. Shadow DOM and View Encapsulation
Angular provides three types of View Encapsulation to control how styles are scoped:
- **`Emulated` (Default)**: Styles are scoped to the component using unique attributes (e.g., `_ngcontent-c1`).
- **`ShadowDom`**: Uses the browser's native Shadow DOM.
- **`None`**: Styles are global.

> [!IMPORTANT]
> **Standalone Components** are a major shift in Angular. They simplify the learning curve by removing the complexity of `NgModules`. Always mention them in an interview.
