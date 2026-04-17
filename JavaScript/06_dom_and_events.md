# DOM and Events

The Document Object Model (DOM) is an interface that allows JavaScript to interact with HTML.

## 1. Event Propagation (Flow)
When an event occurs on an element, it doesn't just happen there; it travels through the DOM tree.

1.  **Capturing Phase**: The event travels down from the `window` to the target element.
2.  **Target Phase**: The event reaches the target element.
3.  **Bubbling Phase**: The event travels back up from the target element to the `window`.

**Note**: By default, event listeners are added in the **Bubbling** phase. To use the capturing phase, set the third argument of `addEventListener` to `true`.

## 2. Event Delegation
A pattern where you attach a single event listener to a parent element instead of attaching multiple listeners to child elements.

### Why?
- **Performance**: Fewer event listeners = less memory usage.
- **Dynamic Content**: Automatically handles events for child elements added to the DOM later.

```javascript
document.getElementById('parent').addEventListener('click', (event) => {
  if (event.target.tagName === 'LI') {
    console.log('List item clicked!');
  }
});
```

## 3. Debouncing and Throttling
Techniques used to control how many times a function is executed over time (e.g., for scroll, resize, or keyup events).

- **Debouncing**: Waits for a specific period of inactivity before executing the function. (e.g., search autocomplete).
- **Throttling**: Ensures the function is executed at most once every X milliseconds. (e.g., scroll event tracking).

## 4. DOM Performance
- **Repaint**: Updating visibility (color, visibility).
- **Reflow**: Updating the geometry/layout (width, height, position). Reflow is much more expensive.
- **Batching**: Use `requestAnimationFrame` for smooth animations and to batch DOM updates.

## 5. Event Objects
- `event.target`: The element that *triggered* the event.
- `event.currentTarget`: The element the *event listener* is attached to.
- `event.preventDefault()`: Stops the default browser behavior (e.g., form submission).
- `event.stopPropagation()`: Stops the event from bubbling up (or capturing down).

> [!IMPORTANT]
> **Capture vs Bubble**:
> Most interviewers will ask which one comes first. **Capturing always happens before Bubbling.**
