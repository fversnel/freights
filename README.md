# freights

Compiles and executes finite state machines.

The library is built entirely out of pure functions, decisions about
any kind of execution model are up to the user.

freights is inspired by Akka's FSM DSL but instead of coupling the
state machine logic with the Actor model, freights only
provides the logic part.
The execution model, be it `atoms`, `agents`, or some kind of actor system,
 can be chosen by the user. 
 A basic agent execution model is provided in the library.

The FSM is designed to be a pure function consisting of:

```
state -> message -> state
```

In other words, each message will update the state of the FSM 
to a new state.

## Creating an FSM

To create a finite state machine you have to do the following:

```clojure
(def example-fsm
  (fsm
   {:initial-state [:idle 0]
    :dispatch-fn (fn [state message] 
                   [(first state) message])}

   :idle
   ([[_ switch-count] message]
    :switch [:in-progress (inc switch-count)]
    :stay [:idle switch-count])

   :in-progress
   ([[_ switch-count] message]
    :switch [:idle (inc switch-count)]
    :stay [:in-progress switch-count])))
```

Let's disect this FSM definition:

1. The `initial-state` provides a vector of two elements.
The first element (`:idle`) being the identifier for the state the FSM will start in. 
The second element (`0`) is the data that the FSM holds.
It is very common to setup an FSM like this but freights does not
force you to do it this way. 
The `initial-state` can be any arbitrary clojure value.

2. The `dispatch-fn` works similar to clojure's multimethods except that it
expects a vector of exactly two elements as a return value.
    1. The first element of the vector is the identifier for the state that the
FSM should dispatch on (e.g. `:idle`, `:in-progress`).
    2. The second element is the identifier for the `message` that was dispatched
to the FSM (e.g. `:switch`, `:stay`).

3. Now each state and the messages that it accepts are described.
A state id gets the current state as its first argument and the message
as its second argument. 
The body of any message handler (e.g. `:switch [:idle (inc switch-count)]`)
can be arbitrary clojure code but it should always return a new state or leave the existing state unchanged. 
*If no handler for a particular message has been provided the FSM does nothing.*

## Invoking the FSM

To invoke the FSM we just created we can call it without arguments:

```clojure
(example-fsm)

=> [:idle 0]
```

Which will simply give us the initial state, that we can then use to store somewhere
for our execution model.

We can send a message to our FSM by invoking it with the initial state and
the message `:switch`:

```clojure
(example-fsm (example-fsm) :switch)
=> [:in-progress 1]
```

Now we get back the new state `[:in-progress 1]`, which we can feed
back into to the `example-fsm` again with another message.

## How it works internally

freights compiles your FSM into what is called a `dispatch-map`.
The `dispatch-map` is then used by the `fsm->fn` function to
process state transitions.

`TODO` write more about the internals


## License

Copyright © 2019 Frank Versnel

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
