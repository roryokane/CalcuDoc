# CalcuDoc

A free clone of [Soulver](http://www.acqualia.com/soulver/) built on the [Frink programming language](http://futureboy.us/frinkdocs/).

This program is a calculator that shows all your calculations in a text document. Its main advantage over traditional calculators is that you don’t have to explicitly ask for the answer – it is calculated as you type. What’s more, calculations are performed using the powerful [Frink](http://futureboy.us/frinkdocs/) calculation and programming language, which is free to include as a library in other programs. Frink allows calculations to easily keep track of units and prevent you from making conversion errors, comes with built-in constants, and even contains programming constructs like loops and defining your own functions for advanced users.

## Compiling and running

This Java program was written using the free [IntelliJ IDEA](http://www.jetbrains.com/idea/) IDE. It compiles and runs successfully from within the IDE.

I also exported this project as an Eclipse project, so it will theoretically work in Eclipse too.

It uses [Gradle](http://www.gradle.org/) as its build system. The included `gradlew` wrapper will install Gradle if necessary to build the program.

The Frink language is included as a JAR file library in the libs folder and needs no special installation.

## Program features

The main window is fully functional. The calculator supports every feature of the Frink programming language. And it updates the results live as you type.

Note that calculations happen in a separate thread from the UI thread, so you can type as fast as you want without having to wait for the results to appear between keystrokes.

None of the menu items are currently functional. For example, I did not implement saving and loading of files; you must copy and paste calculations into a text file yourself.

The calculator interface does not account for results that take up more than one line (error messages). These cause later results to be out of alignment with their calculation lines.
