# Eloquent ORM and Database Management

Laravel's Eloquent ORM provides a beautiful, simple ActiveRecord implementation for working with your database.

## 1. Defining Models
Each database table has a corresponding "Model" that is used to interact with that table.
```php
class User extends Model { /* ... */ }
```

## 2. Relationships
Eloquent makes it easy to manage relationships between tables.
- **One to One**: `return $this->hasOne(Profile::class);` / `belongsTo`
- **One to Many**: `return $this->hasMany(Comment::class);` / `belongsTo`
- **Many to Many**: `return $this->belongsToMany(Role::class);` (Uses a pivot table).
- **Polymorphic**: Allows a model to belong to more than one other model on a single association.

## 3. Query Builder vs. Eloquent
- **Query Builder**: More performant for complex/large queries. Closer to raw SQL. (`DB::table('users')->get()`)
- **Eloquent**: Much more expressive. Supports relationships, events, and attribute casting. (`User::all()`)

## 4. Migrations
Version control for your database. Allows you to define your table structure in PHP code.
```php
Schema::create('flights', function (Blueprint $table) {
    $table->id();
    $table->string('name');
    $table->timestamps();
});
```

## 5. Eager Loading vs. Lazy Loading
- **Lazy Loading (Default)**: Relationships are loaded only when they are accessed.
- **Eager Loading**: Relationships are loaded in the initial query using `with()`.
    - **Why?**: Prevents the **N+1 query problem**, where $N$ additional queries are executed to load a relationship for each result in a list.
```php
// Eager Loading
$books = Book::with('author')->get();
```

## 6. Accessors and Mutators
- **Accessors**: Format a model attribute when you retrieve it (`getNameAttribute`).
- **Mutators**: Format a model attribute when you set it (`setNameAttribute`).

> [!IMPORTANT]
> **Eloquent Observers**: 
> A clean way to group "events" for a model (e.g., `creating`, `updating`, `deleting`). Instead of adding logic to your controller, you can handle it in a dedicated Observer class.
