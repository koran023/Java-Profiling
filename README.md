Java-Profiling
==============

Code with some major performance issues for the profiling/performance lab

## Background

In this lab we'll use VisualVM - https://visualvm.java.net/, a Java profiling tool, to analyze performance issues in a Java program that creates and processes large numbers of randomly generated trees.

This lab will be all "in-class"; there won't be anything to turn in, but these concepts and tools are important. The lab write-up leads you to the answer to a significant degree, so you could just race through and, "figure out who dunnit", and not really learn a lot. I'd encourage you instead to *stop at each of the questions below and put some significant thought into them*. You'll learn a lot more and get a lot more out of the experience if you try to solve a lot of these problems yourself instead of just jumping ahead to my "answer". You might also discover some cool different way to think about the problem that I hadn't thought of.

## Install the visualvm plug-in for Eclipse

The command line has a version of the profiler already installed-- but it's configured for Java 1.7 (which doesn't help us much).  Included in this repository are all the files necessary for Eclipse to install a plug-in, so start by cloning the lab on your local machine.

Now go through the following steps:

1. Clone this repository
2. Unzip the profiler plug-in

1. Start Eclipse
2. Select the Menu option `Help`
3. Select the Menu item `Install New Software...`
4. Click the Button `Add...`
5. Click the button `Local...`
6. Navigate to your lab subdirectory and select the profiler folder you unzipped.
7. Click `OK`
7. Provide a Name in the dialog box (something like 'VisualVM Profiler')
8. Click `OK`
9. Select `Uncategorized`
10. Click `Next>`
11. Select Visual VM Launcher Feature
12. Select `Next>`
13. Accept the license agreement and click `Finish`
14. The software is unsigned... that's fine
15. Restart Eclipse


## Get the code

You will use the code in your repository. You don't need to commit anything back since we're not actually turning stuff in, but you might find it helpful if you want to be able to track the changes you make during the day, or come back to this later.

## An overview of the code

The ideas behind this code and the lab come from a real-world performance problem some students and Nic ran into while doing some genetic programming research a few years ago, but He stripped things down to the bits of the code that matter for the lab. Look around a little, probably starting with `Main.java`. The key thing that happens here is that we construct a large number of randomly generated trees that represent simple arithmetic expressions. Leaves are small integers, and non-leaves (internal nodes) are operators (one of +, -, *, or /). After we construct those trees, we compute the size of them all, printing timing and memory usage information at important points in the process.

Some semi-sophisticated techniques have been employed in an effort to increase flexibility (not really the issue here) and improve performance (which is what we care about). There are two places, for example, where caching has been introduced to try to avoid duplication of computation and memory. One is in `RandomTreeFactory.doBuildRandomTree()`, which is the core of the algorithm that generates random trees. Here, after we've constructed a random (sub)tree, we look in the cache to see if we've already generated that tree. If we have, then we return the _old_ copy (the one we found in the cache) instead of the newly generated one. This ensures that if we end up generating a fragment like "5 + 3" several dozen times, there will actually only be one copy of it in memory, and every use of it will point to that copy.

The second use of caching is in `Tree.acceptVisitor()`. Parts of this are fairly tricky because we're using the visitor pattern - http://en.wikipedia.org/wiki/Visitor_pattern here. The visitor pattern is a really beautiful design pattern, but also one of the more complex ones when you first encounter it. All you really need to understand for this lab, though, is that when we call a method like `Tree.size()`, we'll check to see if we've ever called `size()` on this tree before, and if we have we'll just return that result rather than recursively traverse the tree again.

## Does time to construct the trees seem reasonable?

Run the program, remembering to hit enter so the program actually keeps going. Look at and probably scribble down on some scratch paper the timing and memory results. Now cut the number of trees _N_ that get generated in half (this is controlled in `Main`, or you can use a command line argument) and run it again. Play with a few different values of _N_ and see what you get.

:bangbang: *You'd typically expect the time to construct _N_ trees to be roughly linear in _N_. Is that true in this case?*

## Why does construction take so long?

Our preliminary data suggests that construction is indeed taking longer than makes sense. We could just bang around in the code, hoping to stumble across something that helps, or we can take a data-driven approach.

:bangbang: *Before you collect data, look over the code in `RandomTreeFactory` (and things it calls elsewhere) and sketch out a list of two or three possible places you think might be bottlenecks.*

## Start up !VisualVM and connect it to our process

We have a nice program in the lab called `jvisualvm` (https://visualvm.java.net/). Open up a terminal and start up `jvisualvm`. You can't do much from the initial GUI until you've connected to a running Java process. Once you've done that, though, you can do all kinds of cool things. So let's hook things up:

   * Start the tree program again but _don't_ hit return yet; we want the program to pause while we connect !VisualVM.
   * Once the program's running, go back to !VisualVM, and you should see a new `treeshuffle.asgraph.Main` entry in the "Applications" section on the LHS of the main window.
   * Double click that entry.
   * Choose the "Profiler" tab up at the top, and click the "CPU" button to start profiling CPU usage.
   * Go back to Eclipse (or wherever you ran your code from) and hit return so your program starts.
   * Go back to !VisualVM and watch as the profiling information rolls in.

You actually have to do all this every time you (re)start your program. !VisualVM connects to a specific process, and you get a new process whenever you restart the program.

:bangbang: *What method or methods seem to be taking up all the CPU time? Do they make sense given that you're primarily constructing a large number of new trees? Do the relative importance of different methods change over time? Do those changes make sense?*

The program will probably run much more slowly with profiling activated, so feel free to kill it if you're getting bored. Make a point of jotting down the results for the top few methods, though, so you can compare these results to results you'll get after you make some changes to the code.

## Why all these calls to equals()?

Lots of calls to `doBuildRandomTree()` make sense, but what's up with all these `equals()` calls? The one odd bit in `RandomTreeFactory` is the caching; could that be related to the problem?

:bangbang: *Turn off caching in `doBuildRandomTree()` and run the program again (either with or without the profiler). Does that seem to help?*

One might be tempted to just decide that the caching sucks here and remove it, but that's actually not a good choice.

:bangbang: *Spend a few moments trying to figure out why caching leads to a ton of `equals()` calls in particular. How are lookups done in `HashMaps`?*

`doBuildRandomTree()` doesn't seem to directly call `equals()`, which suggests that maybe there are indirect calls coming from somewhere. It turns out that when we look something up in a `HashMap` we first compute the `hashCode()` to identify the bucket to look in, and then do linear search in that bucket. This means that if we have a poor `hashCode()` definition (one with lots of collisions) then we end up with _long_ (and increasingly long) linear searches in the buckets as we add more and more trees to the cache in `RandomTreeFactory`. That's not good.

## Try to improve Node.hashCode()

The `hashCode()` in `Leaf` is pretty reasonable, so the problem is likely in `Node.hashCode()`.

:bangbang: *Look at `Node.hashCode()`. How might we end up with collisions from this? (Can you come up with two simple trees with the same hash code?)*

:bangbang: *Improve `Node.hashCode()`. I'd use the algorithm in Bloch's _Effective Java_ which is also summarized in this Wikipedia entry - http://en.wikipedia.org/wiki/Java_hashCode(). If you think you have something that improves things, first just run it normally, and if that looks promising run it through !VisualVM. Be sure to jot down some notes on the !VisualVM results. How do they compare to your earlier results?*

Now we have a _lot_ fewer calls to _equals()_ and things run much better. It looks like computing our more complex `hashCode()`, however, is now a significant consumer of resources. The problem is that we end up re-computing hash codes over and over. Maybe we could cache this as well?

:bangbang: *Implement a simple cache for `Node.hashCode()`. Create a field (I called mine `hash`) and initialize it to some known value (I used 0). In the `hashCode()` method, check and see if your field has the initial value. If it does, then compute the hash code, assign the result to your field, and return it. If it _doesn't_ have the initialized value, it's presumably because we've already computed and cached this hash code, so just return the value of your field.*

:bangbang: *Run !VisualVM on system with the new `Node.hashCode()` and record your results. Have we improved things?*

## Does the cache in !RandomTreeFactory help?

Now that we've resolved a major bottleneck caused by the hash codes, we'll turn out attention to the two big caching mechanisms and see if they actually help much.  We go to a fair amount of trouble with all the caching in `RandomTreeFactory`, for example, and it would be nice to know whether it was worth it.

:bangbang: *Turn off the caching in `RandomTreeFactory` and run things without !VisualVM. Will it even run?*

Let's try to figure out what's going on here. Turn the caching back on, and re-run your program and connect it to !VisualVM. This time, instead of choosing the "Profiler" tab, choose the "Monitor" tab. One of the plots there shows the heap usage over time.

:bangbang: *How does the heap usage change over time? We normally expect the memory usage to grow roughly linearly as we create more and more trees. What's happening here? Why?*

:bangbang: *Try turning the caching off and watch what the monitor in !VisualVM tells you. You might increase the maximum amount of heap allocated to your Java process to see what effect that has.*

## Does the cache in Tree help?

In a similar fashion, we should see if the caching in `Tree` helps.

:bangbang: *Turn it off and collect data both from the command line output and from the !VisualVM profiler. Does this cache help?*

## What did we learn from all this?

All in all, what did we get from this? What was really important to the performance of the system?

-- Main.NicMcPhee - 29 Nov 2011<br>
-- Main.VincentBorchardt - 05 Nov 2012<br>
-- Main.JohnMcCall - 06 Nov 2013
