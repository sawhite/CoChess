CoChess
=======

This project 'CoChess' (a deliberate pun on the word 'coaches') forms the beginnings of a program/project that coaches its user into playing better chess.

The project combines three pieces of earlier work:
- The stockfish chess engine, probably the strongest open source chess engine in the world

When using the program, it assumes you have stockfish installed on your machine, as it
calls out to it as an external process.

On MacOS, you can install stockfish using the command

    brew install stockfish

Program Design
--------------

One of the ideas is to separate the presentation from the core chess engine logic. I wanted to have a reasonably nice
looking display of a chess board and was able to get this up and running fairly quickly by adapting some code that I
wrote using my chart component back in 2010. The chess board display would be better written as a custom component - 
and this would be a future refactoring exercise - but the immediate advantage of using the chart is that it gives me 
the ability to easily animate the moves on the board (and also provide shadows for the pieces).


