# DEEP COPY

The Class makes deep copy of object, using reflection. The important remarks are:
- It doesn't process objects without default constructor
- It doesn't pay attention to Map, records and arrays also
- Ideally it should be a Singleton

All of this are points to improve and could be refactored later on. But I guess
it's out of scope for this task.
