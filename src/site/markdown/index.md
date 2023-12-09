# Tuples

This library focuses on the concept of a Tuple as an ordered List of N elements. The core turnertech.tuples.Tuple class supports any number of elements, but is not typed! It is however able to handle nested Tuples in an elegant manner. The Tuple class is immutable, and only provides minimal functionality. Further functions for performing operations on Tuple instances can be found in the turnertech.tuples.Tuples class.

Additionally, a few Typed Tuple classes are provided.

## Assumptions

- A ```n-tuple``` does not "```extend```" a ```n+1-tuple```. In Java this means I cannot cast a ```Tuple3``` to a ```Tuple2```. This would not be correct for Tuples as a Tuple is immutable. Casting as in this example would be equivelant to removing an element, and not casting.

## Examples

Typed tuples, usefull for fixed data, return types or similar.

```java
Tuple2 a = new Tuple2(1, 2);
Tuple2 b = Tuple.from(1, new File(""));

int firstOfA = a.getElement0(); // 1
File secondOfB = b.getElement1(); // File instance
```

Raw tuples, usefull for something (probably).

```java
Tuple a = new Tuple2(1, 2);
Tuple b = Tuple.from(new Tuple0(), 1, Tuple.from(2, 6), new Tuple0());

int firstOfA = a.getElement0(); // 1
String bString = b.toString(); // ((), 1, (2, 6), ())
Object b.get(3); // 6
```