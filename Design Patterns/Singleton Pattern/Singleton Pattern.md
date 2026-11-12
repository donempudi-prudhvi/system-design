# Singleton Design Pattern
- Creational design pattern
- Guarantees that a class has only single instance in the application
- Provides a global access point to the application for the instance

## When a class is called singleton
- One private constructor - so that no one call initialise the instance using `new` keyword
- Static method that hands out the shared instance

## Top use cases of singleton pattern. 
- Loggers
- Connection pool managers
- Thread pool managers
- Configuration holders
- Caches
- Registries

## Implementations in Java

**1. Eager initialization**
```
    public class Config{
        private static final Config instance = new Config();
        private Config(){}
        public static Config getInstance(){
            return instance;
        }
    }
```
**Pros**:
- Thread Safe 

**Cons**:
- Instance is created when the Config class is first initialized
- if the class gets touched for some other reason before you need the instance.

**How does it work?**
- JVM loads the class along with static memebers on class Load
- Static key word enables to have only one copy of the member 
- Final keyword restricts changing value to the member 

`Thread safe is the ability of a piece of code, class, method to be safely used in a multi threaded applications at the same time. This prevents data corruption.`

**2. Lazy initialization**
```
    public class Config{
        private static Config instance;
        private Config(){}
        public static Config getInstance(){
            if(instance == null){
                instance =  new Config();
            }
            return instance;
        }
    }
```
**Cons**:
- This is broken under concurrency - non thread safe.

**How does it work?**:
- If two threads try to get the instance, both of them will create a new instance. 

**3. Synchronized method**
```
public class Config{
        private static Config instance;
        private Config(){}
        public static synchronized Config getInstance(){
            if(instance == null){
                instance = new Config();
            }
            return instance;
        }
    }

```
**Pros:**
- Thread safe due to synchronised keyword. 
- Each thread wait for curent thread execution 

**Cons:**
- A thread have to wait untill the current thread completes it's operation even though an instance exists for the same class
- Adds up locking costs on application, results in slow performance at scale

**4. Double checked locking:**
````
public class Config{
        private static volatile Config instance;
        private Config(){}
        public static Config getInstance(){
            if(instance == null){
                synchronized(Config.class){
                    if(instance == null){
                        instance = new Config();
                    }
                }
            }
            return instance;
        }
    }

````
*volatile keyword prevents a thread from seeing partially constructed object*

**Hows does it works?**
- The first check avoids locking, if the instance exists
- The synchronized block does the locking, and the second check prevents duplicate creation.
- Without volatile, the risk is another thread seeing a partially constructed object because of instruction reordering

**Pros:**
- Prevents duplicate object creation, thread safe

**5. Static Holder Idiom - Lazy**

```
public class Config{
    private Config(){}
    public static Config getInstance(){
        return Holder.instance;
    }
    private static class Holder{
        private static final Config instance = new Config();
    }
}

```

**Pros:**
- Thread safe
- Lazy load

**How does it work?:**
- The holder class is not loaded until the getinstane method is call
- JVM guarantees thread safety with static class execution

**6.Enum Singleton**
```
public enum Config{
    INSTANCE;
    private final Properties prop;
    Config(){
        props = new Properties();
    }
    public String get(String key){ return prop.getProperty(key); }
}

```
**Pros:**
- Thread safe, serialization safe immune to reflection attacks.

**Cons:**
- Can't extend another class
- The enum is initialized on first use of the class

**How does it work?**
- Instance; is shorthand for: `public static final Config Instance = new Config();`
- To use it we can call `String value = Config.Instance.get("db.url");`

## How to break a singleton? 
- **Reflection** : `setAccessible(true)` on a private constructor can create a second instance. if the instance already exists, We can throw an error in ths constructor.
- **Serialization** : Deserializing creates a new object. We can fix it by implementing `readResolve()` to return existing instance.
    - A serializable singleton breaks when you deserialize it, because Java creates a new object from the byte stream without calling your constructor.
    ```
    public class Config implements Serializable {
        private static final Config INSTANCE = new Config();

        private Config() {}

        public static Config getInstance() { return INSTANCE; }
    }

    
    Config a = Config.getInstance();

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    new ObjectOutputStream(bos).writeObject(a);

    Config b = (Config) new ObjectInputStream(
        new ByteArrayInputStream(bos.toByteArray())).readObject();

    System.out.println(a == b);   // false -> two instances, singleton broken
    ```
    - **Below is the fix**
    ```
    public class Config implements Serializable {
    private static final Config INSTANCE = new Config();

    private Config() {}

    public static Config getInstance() { return INSTANCE; }

    private Object readResolve() {
        return INSTANCE;
    }
    }

    ```

    **How it works**
    - `readObject()` deserializes the bytes and builds a new Config object.
    - Before returning it, Java checks whether the class defines a method named readResolve() with exactly this signature.
    - If it does, Java calls it and returns its result instead of the newly built object.
    - The freshly created object is discarded and becomes garbage.
    
    **Signature rules**
    - Name must be readResolve, with no parameters.
    - Return type must be Object.
    - It can be private, protected, or public. private works for this class only; subclasses won’t inherit it, while protected or public would be inherited.

- **Cloning**: If the class implements `Cloneable`, `clone()` can produce a copy. Override it to throw an exception or return the same instance.
- **Multiple class loaders:** Each class loader can have its own “singleton,” so the guarantee is per class loader, not per JVM.

## Criticisms and trade-offs
- **Hidden dependencies:** Code calling Config.getInstance() hides what it depends on, unlike constructor injection.
- **Hard to test:** Global state persists between tests and is difficult to mock or replace.
- **Violates single responsibility:** The class manages its own lifecycle as well as its business logic.
- **Concurrency risk:** Mutable shared state inside a singleton needs its own thread-safety handling. The pattern only guarantees one instance, not safe use of it.

`In modern codebases (Spring, for example), a “singleton” is usually a container-managed scope: the framework creates one instance and injects it where needed. You get the single-instance behavior without the static access point, which avoids most of the testing and coupling problems above.`