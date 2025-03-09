# youtube-dojo - a "Dojo" for YouTube-related "katas"


## Important

This is currently broken - you can build the model but not much else.

## Introduction

The biggest challenge for new developers is learning how to use a specific

tool or framework. In the past this may have taken months but with AI
a new developer can quickly implement most functionality - and then learn
the framework by figuring out why the code doesn't work. :-)

(Insert meme about old developer taking 2 hours for a task, developer using
AI finishing the code in one hour but then spending 4 hours figuring out why
it doesn't work!)

The biggest challenge for experienced developers is determining which
tool or framework is best for your specific problem. This is usually already
determined - continue using what you've been using - but sometimes we're
forced to change our framework. E.g., many of us have used the "struts"
framework but it's been deprecated for many years due to the number of
security weaknesses it had. Projects that had been using struts needed
to migrate to a new framework.

At other times we're working on a green field project and can choose
whatever fits best - on both a technical level and "required skills for
developers" level.

## Dojos and Katas

After many years exploring different frameworks I feel that the best
approach is a dojo/kata approach. The **dojo** is the minimal framework for
an interesting and nontrivial problem. E.g., a YouTube client that
provides functionality beyond what's provided by the standard client.

The **kata** is a specific implementation.

The **dojo** can and should implement a complete test suite built on top
of the interfaces. This lets you perform accurate A/B testing for things
like performance, maintability, number of additional dependencies, etc.

## Test-Driven Development

This is very similar to Test-Driven Development (TDD). The main criticism
of TDD is that people don't understand how you can write tests before
you write any code.

The answer is you can't. The key is to only implement enough functionality
for you to write your tests.

That usually means you need the actual "model" but should use interfaces
for all other levels. These interfaces should provide minimal default
implementations, e.g., every `getX()` returns a constant (1, "value"),
etc. Every `setX(T value)` should immediately return.

That's enough to start writing tests. You then work your way though the
interfaces and implement them piece-by-piece so you'll always get immediate
notification of a change broke previously working tests.

(Sidenote - you'll want to 'ignore' all tests until you've started to
implement the related functionality. Nobody could fine regressions if
there are hundreds or thousands of failing tests.)

